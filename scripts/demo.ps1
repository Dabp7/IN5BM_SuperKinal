param(
    [Parameter(Mandatory=$true)][string]$MySqlBin,
    [string]$Maven = 'mvn',
    [string]$Ffmpeg = '',
    [int]$Port = 33318,
    [switch]$CaptureOnly
)
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path -Parent $PSScriptRoot
$taskData = Join-Path $taskRoot ('work\mysql-demo-' + [guid]::NewGuid().ToString('N'))
$taskVariables = @('SUPERKINAL_DB_URL','SUPERKINAL_DB_USER','SUPERKINAL_DB_PASSWORD','SUPERKINAL_RUN_INTEGRATION','MYSQL_PWD')
$taskOldEnvironment = @{}
foreach ($taskName in $taskVariables) { $taskOldEnvironment[$taskName] = [Environment]::GetEnvironmentVariable($taskName) }
$taskProcess = $null
function Test-TaskDemoReady {
    $ErrorActionPreference = 'SilentlyContinue'
    & (Join-Path $MySqlBin 'mysqladmin.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot --connect-timeout=2 ping 2>$null | Out-Null
    return $LASTEXITCODE -eq 0
}
if ($Port -eq 3306) { throw 'La demo nunca utiliza el puerto habitual 3306.' }
if (Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue) { throw 'El puerto de demo está ocupado.' }
Push-Location $taskRoot
try {
    New-Item -ItemType Directory -Path $taskData -Force | Out-Null
    & (Join-Path $MySqlBin 'mysqld.exe') --no-defaults --initialize-insecure "--basedir=$(Split-Path -Parent $MySqlBin)" "--datadir=$taskData"
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo inicializar el servidor de demo.' }
    $taskArguments = @('--no-defaults',"--basedir=`"$(Split-Path -Parent $MySqlBin)`"", "--datadir=`"$taskData`"", "--port=$Port", '--bind-address=127.0.0.1', '--mysqlx=OFF', '--console')
    $taskProcess = Start-Process -FilePath (Join-Path $MySqlBin 'mysqld.exe') -ArgumentList $taskArguments -WindowStyle Hidden -PassThru -RedirectStandardError (Join-Path $taskData 'server.log')
    $env:MYSQL_PWD = ''
    $taskReady = $false
    for ($taskAttempt=0; $taskAttempt -lt 90; $taskAttempt++) { if (Test-TaskDemoReady) { $taskReady=$true; break }; Start-Sleep -Milliseconds 500 }
    if (-not $taskReady) { throw 'El servidor de demo no arrancó.' }
    $taskSql = (Get-Content database/schema.sql -Raw -Encoding UTF8).Replace('DBKinalExpress2020527','superkinal_demo')
    $taskSql | & (Join-Path $MySqlBin 'mysql.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot --default-character-set=utf8mb4
    if ($LASTEXITCODE -ne 0) { throw 'Falló la instalación del esquema de demo.' }
    & (Join-Path $MySqlBin 'mysql.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot --execute="ALTER USER 'root'@'localhost' IDENTIFIED BY 'demo-only'"
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo configurar la clave del servidor aislado.' }
    $env:SUPERKINAL_DB_URL = "jdbc:mysql://127.0.0.1:$Port/superkinal_demo?sslMode=DISABLED&allowPublicKeyRetrieval=true&serverTimezone=America/Guatemala"
    $env:SUPERKINAL_DB_USER = 'root'; $env:SUPERKINAL_DB_PASSWORD = 'demo-only'; $env:MYSQL_PWD='demo-only'; $env:SUPERKINAL_RUN_INTEGRATION=''
    & $Maven -B '-DskipTests' package
    if ($LASTEXITCODE -ne 0) { throw 'Falló el build de demo.' }
    java -cp 'target/superkinal-2.0.0.jar;target/lib/*' org.diegobercian.demo.DemoData
    if ($LASTEXITCODE -ne 0) { throw 'Falló el seed ficticio.' }
    java -cp 'target/superkinal-2.0.0.jar;target/lib/*' org.diegobercian.demo.DemoCapture docs/demo
    if ($LASTEXITCODE -ne 0) { throw 'Falló la captura de pantallas.' }
    if ($Ffmpeg) {
        $taskFrames=Join-Path $taskData 'screens.txt'
        $taskDemoPath=(Join-Path $taskRoot 'docs/demo').Replace('\','/')
        @("file '$taskDemoPath/clientes.png'",'duration 3',"file '$taskDemoPath/productos.png'",'duration 3',"file '$taskDemoPath/facturas.png'",'duration 3',"file '$taskDemoPath/facturas.png'") | Set-Content -LiteralPath $taskFrames -Encoding ASCII
        & $Ffmpeg -y -loglevel error -f concat -safe 0 -i $taskFrames -vf 'scale=1356:760:force_original_aspect_ratio=decrease,pad=1356:760:(ow-iw)/2:(oh-ih)/2' -r 12 -t 9 -c:v libvpx -deadline realtime -cpu-used 8 -threads 2 -b:v 900k -pix_fmt yuv420p docs/demo/recorrido.webm
        if ($LASTEXITCODE -ne 0) { throw 'Falló la generación del video de capturas.' }
    }
    if (-not $CaptureOnly) { java -jar target/superkinal-2.0.0.jar; if($LASTEXITCODE -ne 0){throw 'La app terminó con un error.'} }
    Write-Output "Demo finalizada. Datos aislados conservados en $taskData; no se tocó la base personal."
} finally {
    if ($taskProcess -and -not $taskProcess.HasExited) {
        $taskSavedPreference=$ErrorActionPreference; $ErrorActionPreference='SilentlyContinue'
        & (Join-Path $MySqlBin 'mysqladmin.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot shutdown 2>$null | Out-Null
        $ErrorActionPreference=$taskSavedPreference
        if (-not $taskProcess.WaitForExit(10000)) { $taskProcess.Kill() }
    }
    foreach ($taskName in $taskVariables) { [Environment]::SetEnvironmentVariable($taskName,$taskOldEnvironment[$taskName]) }
    Pop-Location
}

param(
    [Parameter(Mandatory=$true)][string]$MySqlBin,
    [string]$Maven = 'mvn',
    [int]$Port = 33317
)
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path -Parent $PSScriptRoot
$taskData = Join-Path $taskRoot ('work\mysql-test-' + [guid]::NewGuid().ToString('N'))
$taskProcess = $null
$taskVariables = @('SUPERKINAL_DB_URL','SUPERKINAL_DB_USER','SUPERKINAL_DB_PASSWORD','SUPERKINAL_RUN_INTEGRATION','MYSQL_PWD')
$taskOldEnvironment = @{}
function Test-TaskMySqlReady {
    # Windows PowerShell turns native stderr into terminating errors with Stop.
    # A refused connection is expected while this private server is starting.
    $ErrorActionPreference = 'SilentlyContinue'
    & (Join-Path $MySqlBin 'mysqladmin.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot --connect-timeout=2 ping 2>$null | Out-Null
    return $LASTEXITCODE -eq 0
}
foreach ($taskName in $taskVariables) { $taskOldEnvironment[$taskName] = [Environment]::GetEnvironmentVariable($taskName) }
if ($Port -eq 3306) { throw 'Use un puerto diferente al servidor habitual.' }
if (Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue) { throw 'El puerto ya está ocupado.' }
Push-Location $taskRoot
try {
    New-Item -ItemType Directory -Path $taskData -Force | Out-Null
    & (Join-Path $MySqlBin 'mysqld.exe') --no-defaults --initialize-insecure "--basedir=$(Split-Path -Parent $MySqlBin)" "--datadir=$taskData"
    if ($LASTEXITCODE -ne 0) { throw 'Falló la inicialización de MySQL de prueba.' }
    $taskArguments = @('--no-defaults',"--basedir=`"$(Split-Path -Parent $MySqlBin)`"", "--datadir=`"$taskData`"", "--port=$Port", '--bind-address=127.0.0.1', '--mysqlx=OFF', '--console')
    $taskProcess = Start-Process -FilePath (Join-Path $MySqlBin 'mysqld.exe') -ArgumentList $taskArguments -WindowStyle Hidden -PassThru -RedirectStandardError (Join-Path $taskData 'server.log')
    $env:MYSQL_PWD = ''
    $taskReady = $false
    for ($taskAttempt=0; $taskAttempt -lt 60; $taskAttempt++) {
        if (Test-TaskMySqlReady) { $taskReady=$true; break }
        Start-Sleep -Milliseconds 500
    }
    if (-not $taskReady) { throw 'El servidor de prueba no arrancó.' }
    $taskSql = (Get-Content database/schema.sql -Raw).Replace('DBKinalExpress2020527','superkinal_audit_test')
    $taskSql | & (Join-Path $MySqlBin 'mysql.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot --default-character-set=utf8mb4
    if ($LASTEXITCODE -ne 0) { throw 'Falló la instalación del esquema.' }
    & (Join-Path $MySqlBin 'mysql.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot --execute="ALTER USER 'root'@'localhost' IDENTIFIED BY 'test-only'"
    if ($LASTEXITCODE -ne 0) { throw 'Falló la configuración del usuario del servidor aislado.' }
    $env:SUPERKINAL_DB_URL = "jdbc:mysql://127.0.0.1:$Port/superkinal_audit_test?sslMode=DISABLED&allowPublicKeyRetrieval=true&serverTimezone=America/Guatemala"
    $env:SUPERKINAL_DB_USER = 'root'
    # Windows PowerShell removes an env var when assigned ''; Java would see null.
    $env:SUPERKINAL_DB_PASSWORD = 'test-only'
    $env:MYSQL_PWD = 'test-only'
    $env:SUPERKINAL_RUN_INTEGRATION = '1'
    & $Maven -B clean verify
    if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas Java.' }
    python tests/test_migration.py --mysql (Join-Path $MySqlBin 'mysql.exe') --port $Port
    if ($LASTEXITCODE -ne 0) { throw 'Falló la prueba de migración.' }
    java -jar target/superkinal-2.0.0.jar --smoke-test
    if ($LASTEXITCODE -ne 0) { throw 'Falló el arranque del artefacto.' }
} finally {
    if ($taskProcess -and -not $taskProcess.HasExited) {
        & (Join-Path $MySqlBin 'mysqladmin.exe') --no-defaults '--host=127.0.0.1' "-P$Port" -uroot shutdown 2>$null | Out-Null
        if (-not $taskProcess.WaitForExit(10000)) { $taskProcess.Kill() }
    }
    foreach ($taskName in $taskVariables) { [Environment]::SetEnvironmentVariable($taskName,$taskOldEnvironment[$taskName]) }
    Pop-Location
}

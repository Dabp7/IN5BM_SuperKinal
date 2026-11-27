# Verificación ejecutada — 27 de noviembre de 2026

Entorno: Windows 10 x64, JDK 8u202 con JavaFX ya instalado, Maven de NetBeans 25, MySQL Community 8.0.46. El README recomienda un JDK 8 FX actualizado para nuevas instalaciones. Todas las bases usadas estuvieron en servidores privados de loopback y puertos 33317/33318; no se utilizó el servicio ni la base personal.

## Batería completa

Comando:

```powershell
.\scripts\test-isolated.ps1 -MySqlBin 'C:\Program Files\MySQL\MySQL Server 8.0\bin' -Maven 'C:\Program Files\NetBeans-25\netbeans\java\maven\bin\mvn.cmd'
```

Con JAVA_HOME apuntando al JDK 8 FX, `mvn -B clean verify` terminó con **24 pruebas, cero fallos, cero errores y cero omitidas**: 15 de integración, 5 de validación y 4 de vistas. Incluyen el nuevo seed sintético, login PBKDF2, inventario y totales, concurrencia, cuatro reportes PDF, 18 FXML y recuperación del formulario ante errores.

La medición de 30 páginas con 10 002 productos observó 444 ms en esta ejecución. Es un dato de ese equipo y dataset, no una promesa de rendimiento.

La prueba de migración ejecutada sobre la instalación aislada rechazó usuarios duplicados y fechas inválidas antes de alterar datos, conservó filas, saldos y precios históricos y corrigió totales. El JAR empaquetado arrancó con `--smoke-test` y devolvió `SuperKinal smoke test OK`.

## Demo y capturas

`scripts/demo.ps1 -CaptureOnly` compiló el JAR, creó un esquema nuevo `superkinal_demo`, cargó tres clientes, tres productos y dos facturas, y renderizó los FXML reales desde esa base. Se inspeccionaron visualmente `clientes.png`, `productos.png` y `facturas.png`. Las cifras visibles coinciden con el seed: stock 28/37/19 y facturas Q122.50/Q25.20.

Se generó `docs/demo/recorrido.webm` con FFmpeg 9.0.2. Validación de metadatos: VP8, 1356 × 760, 12 fps y 9.0 segundos. Decodificar el video completo con `ffmpeg -v error -i docs/demo/recorrido.webm -f null -` terminó con código cero. El video es una secuencia de capturas reales, no una grabación de interacción manual.

Los servidores MySQL privados se apagaron al finalizar cada script. Sus directorios permanecen bajo work y están excluidos de Git. No se valida una impresora física ni emisión fiscal. No se repitió una revisión manual de todas las combinaciones de interfaz.

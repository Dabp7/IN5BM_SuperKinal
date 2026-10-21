# SuperKinal

Aplicación de escritorio JavaFX para clientes, proveedores, compras, productos, empleados y facturas. Proyecto académico IN5BM, actualizado a partir de la auditoría de 44 hallazgos.

## Requisitos

- JDK 8 que incluya JavaFX. Se recomienda una distribución Zulu FX 8 actualizada.
- Maven 3.9 o posterior.
- MySQL 8.0.16 o posterior (se utilizan restricciones CHECK de MySQL 8).
- Python 3 y cliente MySQL únicamente para la prueba automatizada de migración.

Abrir `pom.xml` como proyecto Maven en el IDE. El build ya no depende de NetBeans 8.2, rutas de una computadora particular ni archivos JAR guardados en el repositorio.

## Instalar una base nueva

Ejecutar `database/schema.sql` con el cliente MySQL sobre una base vacía. No contiene DROP DATABASE, credenciales, usuarios de prueba ni ejemplos destructivos. El archivo original `src/org/diegobercian/database/SuperKinal2020527.sql` contiene la misma instalación segura para conservar su ubicación conocida.

El esquema usa `DBKinalExpress2020527`. Para otro nombre, ajustar ese nombre en el script y en la URL de conexión. El script no debe ejecutarse encima de una instalación existente.

## Actualizar una instalación anterior

1. Detener la aplicación y crear un respaldo que incluya datos, rutinas y triggers. Ensayar primero en una copia.
2. Ejecutar **una sola vez**, sin la opción `--force`, `database/migrations/V002__auditoria.sql`. Valida los datos antes de alterar las tablas. Si encuentra nulos, negativos, usuarios duplicados o fechas inválidas, corregirlos y reintentar. Las fechas deben ser `AAAA-MM-DD`.
3. Configurar la conexión y ejecutar el conversor de contraseñas:

```powershell
java -cp "target/superkinal-2.0.0.jar;target/lib/*" org.diegobercian.service.AuthService
```

En Linux/macOS se usa `:` en lugar de `;`. El conversor transforma las claves anteriores a PBKDF2, conserva la clave de acceso y omite hashes ya migrados. El login rechaza texto plano. Los usuarios nuevos deben usar contraseñas de 12 a 128 caracteres.

La migración conserva los saldos de inventario existentes y registra un saldo inicial en el libro de movimientos. Recalcula los totales a partir de los detalles, pero no puede reconstruir precios históricos alterados ni recuperar filas borradas por versiones anteriores. Esos casos requieren revisar respaldos y conciliar existencias. El DDL de MySQL hace commits implícitos: ante una interrupción durante cambios de estructura, restaurar el respaldo y repetir en una copia; no continuar sobre una migración parcial.

## Conexión

Configurar estas variables de entorno antes de iniciar (también se aceptan propiedades Java `-D` con los mismos nombres):

```powershell
$env:SUPERKINAL_DB_USER = 'usuario_aplicacion'
$env:SUPERKINAL_DB_PASSWORD = 'su_clave'
$env:SUPERKINAL_DB_URL = 'jdbc:mysql://localhost:3306/DBKinalExpress2020527?sslMode=REQUIRED&connectTimeout=5000&socketTimeout=15000&serverTimezone=America/Guatemala'
```

La contraseña no tiene valor predeterminado. Para desarrollo local aislado puede usarse `sslMode=DISABLED`; para un servidor remoto usar TLS y los certificados correspondientes. Usar un usuario de aplicación con permisos limitados al esquema. Si se utilizó la contraseña que antes estaba en el código, reemplazarla en el servidor: quitarla del archivo no la elimina del historial de Git.

## Compilar y ejecutar

```powershell
mvn clean verify
java -jar target/superkinal-2.0.0.jar
```

Conservar `target/lib/` junto al JAR. Maven fija las dependencias, compila las cuatro plantillas JRXML desde sus fuentes y ejecuta las pruebas unitarias. El adaptador `build.xml` permite `ant jar`, `ant test` y `ant run` cuando Maven y Ant están instalados. La aplicación muestra errores de conexión y permite reintentar sin perder el formulario.

## Reglas de operación

- Los códigos se introducen manualmente y son únicos. Cantidades y códigos numéricos deben ser positivos; importes no negativos y con hasta dos decimales.
- Cada compra aumenta el saldo y cada venta lo descuenta de forma atómica. Cambiar cantidades o productos aplica la diferencia. Borrar una línea revierte su movimiento; no se permite revertir una compra si dejaría existencias negativas.
- Los precios de venta conservan los márgenes originales: costo × 1.40, × 1.35 y × 1.25. El costo vigente es el de la última línea de compra ingresada o editada. Borrar una compra conserva los precios vigentes.
- Cada detalle de venta fija el precio del producto al crearse. Editar su cantidad o moverlo de factura conserva ese precio; cambiar de producto toma el precio del nuevo producto. Actualizar el catálogo no modifica ventas anteriores.
- El total de cada documento se actualiza mediante diferencias dentro de la misma transacción. No se elimina un catálogo o documento con registros relacionados. El historial de movimientos también protege productos usados anteriormente.
- Una compra puede contener productos de distintos proveedores; el proveedor se conserva en el catálogo de productos. No se atribuye un único proveedor a toda la compra.
- Los listados se consultan en páginas de 100 registros. Carga, guardado, login y reportes se ejecutan fuera del hilo de la interfaz. Los selectores conservan mapas por identificador para seleccionar sin consultas adicionales.
- Clientes, productos, proveedores y facturas tienen reportes. En los otros módulos el botón explica que no hay un reporte disponible; durante edición sirve para cancelar.
- El reporte de factura conserva el desglose de IVA del proyecto original (12% incluido) y calcula el total con decimales. Este proyecto académico no implementa facturación electrónica ni emisión fiscal.

## Pruebas completas en Windows

Con `JAVA_HOME` apuntando a JDK 8 con JavaFX:

```powershell
.\scripts\test-isolated.ps1 -MySqlBin 'C:\Program Files\MySQL\MySQL Server 8.0\bin'
```

El script crea un directorio nuevo bajo `work/`, arranca MySQL en un puerto separado y lo apaga al finalizar. Rechaza el puerto habitual y los puertos ocupados. No usa la base de trabajo. Ejecuta compilación, pruebas de SQL, formularios, login, concurrencia, cuatro reportes, 10 000 productos, migración y arranque del JAR.

`mvn verify` sin el entorno de integración ejecuta las pruebas unitarias y omite deliberadamente las pruebas con base y escritorio. GitHub Actions ejecuta la batería completa con MySQL y una pantalla virtual. Los resultados y reportes de prueba quedan en `target/` y se adjuntan al workflow.

Consultar `docs/CORRECCIONES.md` para la cobertura de la auditoría y los límites de verificación.

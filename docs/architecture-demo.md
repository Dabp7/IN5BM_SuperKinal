# Decisiones y ejemplo de corrección

La interfaz conserva los FXML del proyecto académico. Los controladores describen campos y procedimientos; `CrudController` comparte estados de alta, edición, carga y error. `CrudRepository` enlaza valores por parámetros y cada llamada obtiene y cierra su propia conexión. El trabajo de base y reportes se ejecuta con `Async`, fuera del hilo JavaFX, y el resultado se aplica en ese hilo al completar la operación.

Los cambios de existencias y totales se calculan dentro de la transacción de MySQL, con triggers y restricciones que rechazan stock insuficiente. El precio de la venta es un snapshot: cambiar el catálogo no altera una factura anterior. La demo añade compras antes de ventas y usa los mismos triggers, por lo que el stock mostrado proviene de movimientos reales, no de un número pintado en pantalla.

El seed solo admite los nombres `superkinal_demo` y `superkinal_audit_test` y rechaza un catálogo de clientes no vacío. El script crea un servidor separado, nunca reutiliza el servicio habitual y no borra una base existente. La identidad demo se almacena con el mismo PBKDF2 que usa el programa.

## Antes y después: arranque reproducible en Windows

Antes, `scripts/test-isolated.ps1` usaba ErrorActionPreference=Stop e intentaba mysqladmin antes de que MySQL aceptara conexiones. Windows PowerShell convertía el stderr nativo en un error terminante: la prueba terminaba durante una espera esperada. Además, asignar una cadena vacía a SUPERKINAL_DB_PASSWORD eliminaba esa variable del entorno, por lo que Java recibía null y rechazaba la conexión.

Ahora, la consulta de disponibilidad tiene un ámbito local que trata una conexión rechazada como 'aún no listo'. Tras instalar el esquema, el usuario del servidor de prueba recibe una clave de laboratorio explícita, pasada en el entorno. La prueba completa puede alcanzar instalación, SQL concurrente, formularios, reportes, migración y arranque del JAR. Se mantiene Stop para los errores que sí deben detener la ejecución.

La batería de integración prueba el seed: tres clientes, stock 28/37, totales 122.50/25.20, login con hash y rechazo a una segunda carga sobre datos existentes. No se hacen afirmaciones sobre emisión fiscal, rendimiento productivo o seguridad empresarial.

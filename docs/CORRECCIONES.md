# Cobertura de correcciones

Rama de trabajo: `refactor/optimizacion`. Base de la revisión: los 44 hallazgos de `IN5BM_SuperKinal_Auditoria`, entregados el 4 de octubre de 2026. La auditoría original se conserva fuera de este repositorio.

## Datos y transacciones

- **H01–H03:** cada módulo utiliza su procedimiento de eliminación correspondiente. Los borrados inexistentes producen error; la lista se vuelve a consultar después de una operación confirmada.
- **H04:** el alta de cliente respeta el orden código, NIT, nombres, apellidos, dirección, teléfono y correo.
- **H05–H08:** parámetros SQL prefijados y columnas calificadas; una búsqueda devuelve solamente el identificador solicitado.
- **H09–H10, H14–H15:** totales no nulos, diferencias atómicas por línea y corrección de origen/destino al mover detalles. El último detalle eliminado deja cero.
- **H11:** los cambios del catálogo o las compras no reescriben precios históricos. La actualización de cantidad conserva el precio capturado.
- **H12–H13:** movimientos de inventario persistentes, actualización condicional de saldo y reversión dentro de la misma transacción. Dos ventas simultáneas no pueden vender más del saldo disponible. Un fallo revierte existencias, detalles y totales.
- **H16:** todas las relaciones anteriores con borrado en cascada ahora usan RESTRICT. Se conservan las referencias históricas; un registro usado no puede eliminarse.
- **H17:** política explícita de último costo ingresado/editado y aplicación directa de los márgenes. Se retira la función que elegía una compra arbitraria.
- **H32:** restricciones NOT NULL y CHECK; usuario único; fechas DATE; importes no negativos; cantidades y códigos numéricos positivos. La política de códigos y proveedores está documentada.
- **H43:** instalación vacía separada de la migración. Ningún script de instalación reinicia la base ni ejecuta ejemplos. La migración valida antes de modificar, conserva filas y saldos e inicia el libro de inventario.

## Formularios, navegación y acceso

- **H21–H23:** identificador Proveedor unificado, columnas Contacto/Razón Social corregidas y navegación de Clientes/Compras reparada.
- **H24:** fábrica de conexión sin secretos incrustados; configuración externa, TLS configurable y errores visibles con reintento.
- **H25–H26:** contraseñas PBKDF2-HMAC-SHA256 con sal aleatoria de 16 bytes y 600 000 iteraciones mediante JCA. Login sin texto plano. Registro atómico protegido por índice único y conversor explícito de claves antiguas.
- **H27–H28:** validación compartida de requeridos, longitud, números, fechas, relaciones, correo y teléfono. Selección vacía protegida; el formulario conserva sus entradas y modo ante fallos. La interfaz aplica cambios después de confirmar la escritura.
- **H29–H30:** repositorio y servicios separados, tareas en segundo plano y cierre de conexiones/statements/resultados con try-with-resources. Tablas paginadas a 100 filas y catálogos por identificador; el refresco permite volver a consultar sin reabrir el módulo.
- **H31:** modelos monetarios con BigDecimal, fechas con LocalDate y parámetros JDBC tipados. El reporte usa decimales para importes e IVA.
- **H44:** selectores de encabezado/filler corregidos, foco visible y hover sin cambios de tamaño. Seleccionar filas con teclado actualiza los campos. El contenedor principal permite desplazamiento cuando la pantalla es pequeña.

## Reportes y entrega

- **H18:** selección obligatoria y parámetro `numeroFactura` correcto. Factura con partidas, totales completos y resumen calculado con BigDecimal.
- **H19–H20:** imágenes por classpath, dependencias completas declaradas y cuatro JRXML compilados en cada build.
- **H33–H40:** los ocho módulos sin plantilla muestran un mensaje claro al usar Reportes. Durante la edición sigue siendo una acción de cancelar.
- **H41:** clase principal declarada en Maven; adaptador Ant portable. Se retiran la configuración antigua de NetBeans, los JAR de dependencias copiados y los artefactos compilados del control de versiones.
- **H42:** README de instalación/migración/ejecución, pruebas automatizadas y workflow de GitHub Actions con MySQL y JavaFX.

## Error adicional encontrado

`sp_actualizarEmpleados` filtraba por cargo, ignoraba el código del empleado, no actualizaba su cargo y reasignaba el turno a sí mismo. Ahora modifica solamente el empleado solicitado; la regresión comprueba que otro empleado con el mismo cargo permanece intacto.

## Alcance de validación

Las pruebas incluyen los doce contratos CRUD, integridad monetaria, inventario, borrados restringidos, concurrencia de ventas y registro, contraseñas, generación real de los cuatro reportes, carga de las 18 vistas y recuperación del formulario ante un alta duplicada. La prueba de volumen carga 10 000 productos y consulta páginas repetidas; mide el tiempo observado, sin presentarlo como garantía universal de rendimiento.

La migración se prueba desde las tablas originales con filas existentes, totales erróneos, usuarios duplicados y fechas inválidas. Conserva los saldos existentes como apertura: no inventa una recuperación de históricos que ya fueron alterados o eliminados.

No se valida una impresora física ni se certifica accesibilidad con lectores de pantalla. Las pruebas JavaFX ejercitan carga, selección, navegación corregida, guardado y error con base real; no equivalen a una revisión manual de todas las combinaciones de pantalla, datos y dispositivos. La instancia de prueba es independiente de la base del usuario.


## Segunda pasada: ventanas y formularios adaptables

- Se sustituyeron las 18 vistas de posiciones fijas por contenedores adaptables: formularios de una, dos o tres columnas, acciones que se redistribuyen y tablas con desplazamiento interno.
- Cambiar de módulo conserva las dimensiones elegidas por el usuario. La ventana inicial se limita al área útil del monitor; las pantallas estrechas desplazan el contenido verticalmente sin exigir un escritorio grande.
- Los identificadores no aparecen al agregar; se generan al confirmar la escritura. Durante edición y consulta están protegidos. Los campos calculados permanecen de solo lectura y no son requisitos de alta.
- V003 activa AUTO_INCREMENT en los once módulos numéricos y agrega una secuencia transaccional para códigos de producto. Conserva los códigos anteriores. El libro de inventario registra el identificador real del detalle una vez asignado, evitando referencias cero durante un alta automática.
- Fechas con calendario, nombres de campos legibles, campos opcionales identificados, foco y errores visibles. Después de guardar se muestra el código y se localiza el registro en su página.
- Las pruebas cubren las 18 vistas en cuatro resoluciones, altura y ancho de los campos, alta de productos sin código, selección del registro guardado, protección de identificadores y persistencia del tamaño de ventana. Las capturas se adjuntan a GitHub Actions.

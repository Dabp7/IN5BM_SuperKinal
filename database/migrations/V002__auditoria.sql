-- Backup first. Stop the application. MySQL DDL commits implicitly; this migration is one-time.
USE DBKinalExpress2020527;
DROP PROCEDURE IF EXISTS sp_preflight_auditoria;
DELIMITER $$
CREATE PROCEDURE sp_preflight_auditoria() BEGIN
IF EXISTS(SELECT 1 FROM Usuarios WHERE idUsuario IS NULL OR ingresoUsuario IS NULL OR ingresoClave IS NULL OR NOT (CHAR_LENGTH(TRIM(ingresoUsuario)) > 0) OR NOT (CHAR_LENGTH(TRIM(ingresoClave)) > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en Usuarios antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM Clientes WHERE codigoCliente IS NULL OR NITClientes IS NULL OR nombresCliente IS NULL OR apellidosCliente IS NULL OR direccionCliente IS NULL OR telefonoCliente IS NULL OR correoCliente IS NULL OR NOT (codigoCliente > 0) OR NOT (CHAR_LENGTH(TRIM(NITClientes)) > 0) OR NOT (CHAR_LENGTH(TRIM(nombresCliente)) > 0) OR NOT (CHAR_LENGTH(TRIM(apellidosCliente)) > 0) OR NOT (CHAR_LENGTH(TRIM(direccionCliente)) > 0) OR NOT (CHAR_LENGTH(TRIM(telefonoCliente)) > 0) OR NOT (CHAR_LENGTH(TRIM(correoCliente)) > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en Clientes antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM Proveedores WHERE codigoProveedor IS NULL OR NITProveedor IS NULL OR nombresProveedor IS NULL OR apellidosProveedor IS NULL OR direccionProveedor IS NULL OR razonSocial IS NULL OR contactoPrincipal IS NULL OR paginaWeb IS NULL OR NOT (codigoProveedor > 0) OR NOT (CHAR_LENGTH(TRIM(NITProveedor)) > 0) OR NOT (CHAR_LENGTH(TRIM(nombresProveedor)) > 0) OR NOT (CHAR_LENGTH(TRIM(apellidosProveedor)) > 0) OR NOT (CHAR_LENGTH(TRIM(direccionProveedor)) > 0) OR NOT (CHAR_LENGTH(TRIM(razonSocial)) > 0) OR NOT (CHAR_LENGTH(TRIM(contactoPrincipal)) > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en Proveedores antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM Compras WHERE numeroDocumento IS NULL OR fechaDocumento IS NULL OR descripcion IS NULL OR NOT (numeroDocumento > 0) OR NOT (CHAR_LENGTH(TRIM(descripcion)) > 0) OR NOT (totalDocumento >= 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en Compras antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM CargoEmpleado WHERE codigoCargoEmpleado IS NULL OR nombreCargo IS NULL OR descripcionCargo IS NULL OR NOT (codigoCargoEmpleado > 0) OR NOT (CHAR_LENGTH(TRIM(nombreCargo)) > 0) OR NOT (CHAR_LENGTH(TRIM(descripcionCargo)) > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en CargoEmpleado antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM TipoProducto WHERE codigoTipoProducto IS NULL OR descripcion IS NULL OR NOT (codigoTipoProducto > 0) OR NOT (CHAR_LENGTH(TRIM(descripcion)) > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en TipoProducto antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM Productos WHERE codigoProducto IS NULL OR descripcionProducto IS NULL OR precioUnitario IS NULL OR precioDocena IS NULL OR precioMayor IS NULL OR imagenProducto IS NULL OR codigoTipoProducto IS NULL OR codigoProveedor IS NULL OR NOT (CHAR_LENGTH(TRIM(codigoProducto)) > 0) OR NOT (CHAR_LENGTH(TRIM(descripcionProducto)) > 0) OR NOT (precioUnitario >= 0) OR NOT (precioDocena >= 0) OR NOT (precioMayor >= 0) OR NOT (existencia >= 0) OR NOT (codigoTipoProducto > 0) OR NOT (codigoProveedor > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en Productos antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM Empleados WHERE codigoEmpleado IS NULL OR nombresEmpleado IS NULL OR apellidosEmpleado IS NULL OR sueldo IS NULL OR direccion IS NULL OR turno IS NULL OR codigoCargoEmpleado IS NULL OR NOT (codigoEmpleado > 0) OR NOT (CHAR_LENGTH(TRIM(nombresEmpleado)) > 0) OR NOT (CHAR_LENGTH(TRIM(apellidosEmpleado)) > 0) OR NOT (sueldo >= 0) OR NOT (CHAR_LENGTH(TRIM(direccion)) > 0) OR NOT (CHAR_LENGTH(TRIM(turno)) > 0) OR NOT (codigoCargoEmpleado > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en Empleados antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM EmailProveedor WHERE codigoEmailProveedor IS NULL OR emailProveedor IS NULL OR descripcion IS NULL OR codigoProveedor IS NULL OR NOT (codigoEmailProveedor > 0) OR NOT (CHAR_LENGTH(TRIM(emailProveedor)) > 0) OR NOT (CHAR_LENGTH(TRIM(descripcion)) > 0) OR NOT (codigoProveedor > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en EmailProveedor antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM TelefonoProveedor WHERE codigoTelefonoProveedor IS NULL OR numeroPrincipal IS NULL OR numeroSecundario IS NULL OR observaciones IS NULL OR codigoProveedor IS NULL OR NOT (codigoTelefonoProveedor > 0) OR NOT (CHAR_LENGTH(TRIM(numeroPrincipal)) > 0) OR NOT (codigoProveedor > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en TelefonoProveedor antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM DetalleCompra WHERE codigoDetalleCompra IS NULL OR costoUnitario IS NULL OR cantidad IS NULL OR codigoProducto IS NULL OR numeroDocumento IS NULL OR NOT (codigoDetalleCompra > 0) OR NOT (costoUnitario >= 0) OR NOT (cantidad > 0) OR NOT (CHAR_LENGTH(TRIM(codigoProducto)) > 0) OR NOT (numeroDocumento > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en DetalleCompra antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM Factura WHERE numeroFactura IS NULL OR estado IS NULL OR fechaFactura IS NULL OR codigoCliente IS NULL OR codigoEmpleado IS NULL OR NOT (numeroFactura > 0) OR NOT (CHAR_LENGTH(TRIM(estado)) > 0) OR NOT (totalFactura >= 0) OR NOT (CHAR_LENGTH(TRIM(fechaFactura)) > 0) OR NOT (codigoCliente > 0) OR NOT (codigoEmpleado > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en Factura antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM DetalleFactura WHERE codigoDetalleFactura IS NULL OR precioUnitario IS NULL OR cantidad IS NULL OR numeroFactura IS NULL OR codigoProducto IS NULL OR NOT (codigoDetalleFactura > 0) OR NOT (precioUnitario >= 0) OR NOT (cantidad > 0) OR NOT (numeroFactura > 0) OR NOT (CHAR_LENGTH(TRIM(codigoProducto)) > 0)) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir datos inválidos en DetalleFactura antes de migrar'; END IF;
IF EXISTS(SELECT 1 FROM Factura WHERE fechaFactura NOT REGEXP '^[1-9][0-9]{3}-[0-9]{2}-[0-9]{2}$' OR SUBSTRING(fechaFactura,6,2) NOT BETWEEN '01' AND '12' OR SUBSTRING(fechaFactura,9,2) NOT BETWEEN '01' AND '31' OR CAST(SUBSTRING(fechaFactura,9,2) AS UNSIGNED) > DAY(LAST_DAY(CONCAT(LEFT(fechaFactura,7),'-01')))) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Corregir fechas de factura: formato AAAA-MM-DD válido'; END IF;
IF EXISTS(SELECT ingresoUsuario FROM Usuarios GROUP BY ingresoUsuario HAVING COUNT(*)>1) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Resolver usuarios duplicados antes de migrar'; END IF;
END $$
DELIMITER ;
CALL sp_preflight_auditoria();
DROP PROCEDURE sp_preflight_auditoria;
DROP TRIGGER IF EXISTS tr_insertarPreciosDetalleFactura_Before_Insert;
DROP TRIGGER IF EXISTS tr_actualizarPreciosDetalleFactura_after_update;
DROP TRIGGER IF EXISTS tr_insertarPreciosProductos_after_Insert;
DROP TRIGGER IF EXISTS tr_actualizarPreciosProductos_after_update;
DROP TRIGGER IF EXISTS tr_eliminarPreciosProductos_after_delete;
DROP TRIGGER IF EXISTS tr_insertarTotalCompra_after_Insert;
DROP TRIGGER IF EXISTS tr_actualizarTotalCompra_after_update;
DROP TRIGGER IF EXISTS tr_eliminarTotalCompra_after_delete;
DROP TRIGGER IF EXISTS tr_insertarTotalFactura_after_Insert;
DROP TRIGGER IF EXISTS tr_actualizarTotalFactura_after_update;
DROP TRIGGER IF EXISTS tr_eliminarTotalFactura_after_delete;
DROP FUNCTION IF EXISTS fn_TraerPrecioUnitario;
DROP FUNCTION IF EXISTS fn_TotalCompra;
DROP FUNCTION IF EXISTS fn_TotalFactura;
DROP PROCEDURE IF EXISTS sp_agregarClientes;
DROP PROCEDURE IF EXISTS sp_listarClientes;
DROP PROCEDURE IF EXISTS sp_buscarClientes;
DROP PROCEDURE IF EXISTS sp_actualizarClientes;
DROP PROCEDURE IF EXISTS sp_eliminarClientes;
DROP PROCEDURE IF EXISTS sp_agregarProveedores;
DROP PROCEDURE IF EXISTS sp_listarProveedores;
DROP PROCEDURE IF EXISTS sp_buscarProveedores;
DROP PROCEDURE IF EXISTS sp_actualizarProveedores;
DROP PROCEDURE IF EXISTS sp_eliminarProveedores;
DROP PROCEDURE IF EXISTS sp_agregarCargoEmpleado;
DROP PROCEDURE IF EXISTS sp_listarCargoEmpleado;
DROP PROCEDURE IF EXISTS sp_buscarCargoEmpleado;
DROP PROCEDURE IF EXISTS sp_actualizarCargoEmpleado;
DROP PROCEDURE IF EXISTS sp_eliminarCargoEmpleado;
DROP PROCEDURE IF EXISTS sp_agregarTipoProducto;
DROP PROCEDURE IF EXISTS sp_listarTipoProducto;
DROP PROCEDURE IF EXISTS sp_buscarTipoProducto;
DROP PROCEDURE IF EXISTS sp_actualizarTipoProducto;
DROP PROCEDURE IF EXISTS sp_eliminarTipoProducto;
DROP PROCEDURE IF EXISTS sp_agregarCompras;
DROP PROCEDURE IF EXISTS sp_listarCompras;
DROP PROCEDURE IF EXISTS sp_buscarCompras;
DROP PROCEDURE IF EXISTS sp_actualizarCompras;
DROP PROCEDURE IF EXISTS sp_eliminarCompras;
DROP PROCEDURE IF EXISTS sp_actualizarComprasTotal;
DROP PROCEDURE IF EXISTS sp_agregarProductos;
DROP PROCEDURE IF EXISTS sp_listarProductos;
DROP PROCEDURE IF EXISTS sp_buscarProductos;
DROP PROCEDURE IF EXISTS sp_actualizarProductos;
DROP PROCEDURE IF EXISTS sp_eliminarProductos;
DROP PROCEDURE IF EXISTS sp_actualizarPreciosProductos;
DROP PROCEDURE IF EXISTS sp_agregarEmpleados;
DROP PROCEDURE IF EXISTS sp_listarEmpleados;
DROP PROCEDURE IF EXISTS sp_buscarEmpleados;
DROP PROCEDURE IF EXISTS sp_actualizarEmpleados;
DROP PROCEDURE IF EXISTS sp_eliminarEmpleados;
DROP PROCEDURE IF EXISTS sp_agregarEmailProveedor;
DROP PROCEDURE IF EXISTS sp_listarEmailProveedor;
DROP PROCEDURE IF EXISTS sp_buscarEmailProveedor;
DROP PROCEDURE IF EXISTS sp_actualizarEmailProveedor;
DROP PROCEDURE IF EXISTS sp_eliminarEmailProveedor;
DROP PROCEDURE IF EXISTS sp_agregarTelefonoProveedor;
DROP PROCEDURE IF EXISTS sp_listarTelefonoProveedor;
DROP PROCEDURE IF EXISTS sp_buscarTelefonoProveedor;
DROP PROCEDURE IF EXISTS sp_actualizarTelefonoProveedor;
DROP PROCEDURE IF EXISTS sp_eliminarTelefonoProveedor;
DROP PROCEDURE IF EXISTS sp_agregarFactura;
DROP PROCEDURE IF EXISTS sp_listarFacturas;
DROP PROCEDURE IF EXISTS sp_buscarFacturas;
DROP PROCEDURE IF EXISTS sp_actualizarFacturas;
DROP PROCEDURE IF EXISTS sp_eliminarFacturas;
DROP PROCEDURE IF EXISTS sp_actualizarFacturaTotal;
DROP PROCEDURE IF EXISTS sp_agregarDetalleFactura;
DROP PROCEDURE IF EXISTS sp_listarDetalleFactura;
DROP PROCEDURE IF EXISTS sp_buscarDetalleFactura;
DROP PROCEDURE IF EXISTS sp_ActualizarDetalleFactura;
DROP PROCEDURE IF EXISTS sp_eliminarDetalleFactura;
DROP PROCEDURE IF EXISTS sp_agregarDetalleCompra;
DROP PROCEDURE IF EXISTS sp_listarDetalleCompra;
DROP PROCEDURE IF EXISTS sp_buscarDetalleCompra;
DROP PROCEDURE IF EXISTS sp_eliminarDetalleCompra;
DROP PROCEDURE IF EXISTS sp_actualizarDetalleCompra;
DROP PROCEDURE IF EXISTS sp_actualizarPrecioDetalleFactura;
ALTER TABLE Productos DROP FOREIGN KEY FK_Productos_TipoProducto;
ALTER TABLE Productos DROP FOREIGN KEY FK_Productos_Proveedor;
ALTER TABLE Empleados DROP FOREIGN KEY FK_Empleados_CargoEmpleado;
ALTER TABLE EmailProveedor DROP FOREIGN KEY FK_EmailProveedor_Proveedores;
ALTER TABLE TelefonoProveedor DROP FOREIGN KEY FK_TelefonoProveedor_Proveedores;
ALTER TABLE DetalleCompra DROP FOREIGN KEY FK_DetalleCompra_Productos;
ALTER TABLE DetalleCompra DROP FOREIGN KEY FK_DetalleCompra_Compras;
ALTER TABLE Factura DROP FOREIGN KEY FK_Factura_Clientes;
ALTER TABLE Factura DROP FOREIGN KEY FK_Factura_Empleados;
ALTER TABLE DetalleFactura DROP FOREIGN KEY FK_DetalleFactura_Factura;
ALTER TABLE DetalleFactura DROP FOREIGN KEY FK_DetalleFactura;
ALTER TABLE Usuarios
MODIFY COLUMN idUsuario int NOT NULL AUTO_INCREMENT,
MODIFY COLUMN ingresoUsuario varchar(60) NOT NULL,
MODIFY COLUMN ingresoClave varchar(255) NOT NULL,
ADD CONSTRAINT uq_usuario UNIQUE (ingresoUsuario),
ADD CONSTRAINT ck_Usuarios_0 CHECK (CHAR_LENGTH(TRIM(ingresoUsuario)) > 0),
ADD CONSTRAINT ck_Usuarios_1 CHECK (CHAR_LENGTH(TRIM(ingresoClave)) > 0);
ALTER TABLE Clientes
MODIFY COLUMN codigoCliente int NOT NULL,
MODIFY COLUMN NITClientes varchar(10) NOT NULL,
MODIFY COLUMN nombresCliente varchar(50) NOT NULL,
MODIFY COLUMN apellidosCliente varchar(50) NOT NULL,
MODIFY COLUMN direccionCliente varchar(150) NOT NULL,
MODIFY COLUMN telefonoCliente varchar(8) NOT NULL,
MODIFY COLUMN correoCliente varchar(45) NOT NULL,
ADD CONSTRAINT ck_Clientes_0 CHECK (codigoCliente > 0),
ADD CONSTRAINT ck_Clientes_1 CHECK (CHAR_LENGTH(TRIM(NITClientes)) > 0),
ADD CONSTRAINT ck_Clientes_2 CHECK (CHAR_LENGTH(TRIM(nombresCliente)) > 0),
ADD CONSTRAINT ck_Clientes_3 CHECK (CHAR_LENGTH(TRIM(apellidosCliente)) > 0),
ADD CONSTRAINT ck_Clientes_4 CHECK (CHAR_LENGTH(TRIM(direccionCliente)) > 0),
ADD CONSTRAINT ck_Clientes_5 CHECK (CHAR_LENGTH(TRIM(telefonoCliente)) > 0),
ADD CONSTRAINT ck_Clientes_6 CHECK (CHAR_LENGTH(TRIM(correoCliente)) > 0);
ALTER TABLE Proveedores
MODIFY COLUMN codigoProveedor int NOT NULL,
MODIFY COLUMN NITProveedor varchar(10) NOT NULL,
MODIFY COLUMN nombresProveedor varchar(60) NOT NULL,
MODIFY COLUMN apellidosProveedor varchar(60) NOT NULL,
MODIFY COLUMN direccionProveedor varchar(150) NOT NULL,
MODIFY COLUMN razonSocial varchar(60) NOT NULL,
MODIFY COLUMN contactoPrincipal varchar(100) NOT NULL,
MODIFY COLUMN paginaWeb varchar(50) NOT NULL,
ADD CONSTRAINT ck_Proveedores_0 CHECK (codigoProveedor > 0),
ADD CONSTRAINT ck_Proveedores_1 CHECK (CHAR_LENGTH(TRIM(NITProveedor)) > 0),
ADD CONSTRAINT ck_Proveedores_2 CHECK (CHAR_LENGTH(TRIM(nombresProveedor)) > 0),
ADD CONSTRAINT ck_Proveedores_3 CHECK (CHAR_LENGTH(TRIM(apellidosProveedor)) > 0),
ADD CONSTRAINT ck_Proveedores_4 CHECK (CHAR_LENGTH(TRIM(direccionProveedor)) > 0),
ADD CONSTRAINT ck_Proveedores_5 CHECK (CHAR_LENGTH(TRIM(razonSocial)) > 0),
ADD CONSTRAINT ck_Proveedores_6 CHECK (CHAR_LENGTH(TRIM(contactoPrincipal)) > 0);
UPDATE Compras h SET totalDocumento = (SELECT COALESCE(SUM(d.costoUnitario*d.cantidad),0) FROM DetalleCompra d WHERE d.numeroDocumento=h.numeroDocumento);
ALTER TABLE Compras
MODIFY COLUMN numeroDocumento int NOT NULL,
MODIFY COLUMN fechaDocumento date NOT NULL,
MODIFY COLUMN descripcion varchar(60) NOT NULL,
MODIFY COLUMN totalDocumento decimal(10,2) NOT NULL DEFAULT 0,
ADD CONSTRAINT ck_Compras_0 CHECK (numeroDocumento > 0),
ADD CONSTRAINT ck_Compras_1 CHECK (CHAR_LENGTH(TRIM(descripcion)) > 0),
ADD CONSTRAINT ck_Compras_2 CHECK (totalDocumento >= 0);
ALTER TABLE CargoEmpleado
MODIFY COLUMN codigoCargoEmpleado int NOT NULL,
MODIFY COLUMN nombreCargo varchar(45) NOT NULL,
MODIFY COLUMN descripcionCargo varchar(45) NOT NULL,
ADD CONSTRAINT ck_CargoEmpleado_0 CHECK (codigoCargoEmpleado > 0),
ADD CONSTRAINT ck_CargoEmpleado_1 CHECK (CHAR_LENGTH(TRIM(nombreCargo)) > 0),
ADD CONSTRAINT ck_CargoEmpleado_2 CHECK (CHAR_LENGTH(TRIM(descripcionCargo)) > 0);
ALTER TABLE TipoProducto
MODIFY COLUMN codigoTipoProducto int NOT NULL,
MODIFY COLUMN descripcion varchar(45) NOT NULL,
ADD CONSTRAINT ck_TipoProducto_0 CHECK (codigoTipoProducto > 0),
ADD CONSTRAINT ck_TipoProducto_1 CHECK (CHAR_LENGTH(TRIM(descripcion)) > 0);
UPDATE Productos SET existencia=COALESCE(existencia,0);
ALTER TABLE Productos
MODIFY COLUMN codigoProducto varchar(15) NOT NULL,
MODIFY COLUMN descripcionProducto varchar(45) NOT NULL,
MODIFY COLUMN precioUnitario decimal(10,2) NOT NULL DEFAULT 0,
MODIFY COLUMN precioDocena decimal(10,2) NOT NULL DEFAULT 0,
MODIFY COLUMN precioMayor decimal(10,2) NOT NULL DEFAULT 0,
MODIFY COLUMN imagenProducto varchar(45) NOT NULL,
MODIFY COLUMN existencia int NOT NULL DEFAULT 0,
MODIFY COLUMN codigoTipoProducto int NOT NULL,
MODIFY COLUMN codigoProveedor int NOT NULL,
ADD CONSTRAINT ck_Productos_0 CHECK (CHAR_LENGTH(TRIM(codigoProducto)) > 0),
ADD CONSTRAINT ck_Productos_1 CHECK (CHAR_LENGTH(TRIM(descripcionProducto)) > 0),
ADD CONSTRAINT ck_Productos_2 CHECK (precioUnitario >= 0),
ADD CONSTRAINT ck_Productos_3 CHECK (precioDocena >= 0),
ADD CONSTRAINT ck_Productos_4 CHECK (precioMayor >= 0),
ADD CONSTRAINT ck_Productos_5 CHECK (existencia >= 0),
ADD CONSTRAINT ck_Productos_6 CHECK (codigoTipoProducto > 0),
ADD CONSTRAINT ck_Productos_7 CHECK (codigoProveedor > 0);
ALTER TABLE Empleados
MODIFY COLUMN codigoEmpleado int NOT NULL,
MODIFY COLUMN nombresEmpleado varchar(50) NOT NULL,
MODIFY COLUMN apellidosEmpleado varchar(50) NOT NULL,
MODIFY COLUMN sueldo decimal(10,2) NOT NULL,
MODIFY COLUMN direccion varchar(150) NOT NULL,
MODIFY COLUMN turno varchar(15) NOT NULL,
MODIFY COLUMN codigoCargoEmpleado int NOT NULL,
ADD CONSTRAINT ck_Empleados_0 CHECK (codigoEmpleado > 0),
ADD CONSTRAINT ck_Empleados_1 CHECK (CHAR_LENGTH(TRIM(nombresEmpleado)) > 0),
ADD CONSTRAINT ck_Empleados_2 CHECK (CHAR_LENGTH(TRIM(apellidosEmpleado)) > 0),
ADD CONSTRAINT ck_Empleados_3 CHECK (sueldo >= 0),
ADD CONSTRAINT ck_Empleados_4 CHECK (CHAR_LENGTH(TRIM(direccion)) > 0),
ADD CONSTRAINT ck_Empleados_5 CHECK (CHAR_LENGTH(TRIM(turno)) > 0),
ADD CONSTRAINT ck_Empleados_6 CHECK (codigoCargoEmpleado > 0);
ALTER TABLE EmailProveedor
MODIFY COLUMN codigoEmailProveedor int NOT NULL,
MODIFY COLUMN emailProveedor varchar(50) NOT NULL,
MODIFY COLUMN descripcion varchar(100) NOT NULL,
MODIFY COLUMN codigoProveedor int NOT NULL,
ADD CONSTRAINT ck_EmailProveedor_0 CHECK (codigoEmailProveedor > 0),
ADD CONSTRAINT ck_EmailProveedor_1 CHECK (CHAR_LENGTH(TRIM(emailProveedor)) > 0),
ADD CONSTRAINT ck_EmailProveedor_2 CHECK (CHAR_LENGTH(TRIM(descripcion)) > 0),
ADD CONSTRAINT ck_EmailProveedor_3 CHECK (codigoProveedor > 0);
ALTER TABLE TelefonoProveedor
MODIFY COLUMN codigoTelefonoProveedor int NOT NULL,
MODIFY COLUMN numeroPrincipal varchar(8) NOT NULL,
MODIFY COLUMN numeroSecundario varchar(8) NOT NULL,
MODIFY COLUMN observaciones varchar(45) NOT NULL,
MODIFY COLUMN codigoProveedor int NOT NULL,
ADD CONSTRAINT ck_TelefonoProveedor_0 CHECK (codigoTelefonoProveedor > 0),
ADD CONSTRAINT ck_TelefonoProveedor_1 CHECK (CHAR_LENGTH(TRIM(numeroPrincipal)) > 0),
ADD CONSTRAINT ck_TelefonoProveedor_2 CHECK (codigoProveedor > 0);
ALTER TABLE DetalleCompra
MODIFY COLUMN codigoDetalleCompra int NOT NULL,
MODIFY COLUMN costoUnitario decimal(10,2) NOT NULL,
MODIFY COLUMN cantidad int NOT NULL,
MODIFY COLUMN codigoProducto varchar(15) NOT NULL,
MODIFY COLUMN numeroDocumento int NOT NULL,
ADD CONSTRAINT ck_DetalleCompra_0 CHECK (codigoDetalleCompra > 0),
ADD CONSTRAINT ck_DetalleCompra_1 CHECK (costoUnitario >= 0),
ADD CONSTRAINT ck_DetalleCompra_2 CHECK (cantidad > 0),
ADD CONSTRAINT ck_DetalleCompra_3 CHECK (CHAR_LENGTH(TRIM(codigoProducto)) > 0),
ADD CONSTRAINT ck_DetalleCompra_4 CHECK (numeroDocumento > 0);
UPDATE Factura h SET totalFactura = (SELECT COALESCE(SUM(d.precioUnitario*d.cantidad),0) FROM DetalleFactura d WHERE d.numeroFactura=h.numeroFactura);
ALTER TABLE Factura
MODIFY COLUMN numeroFactura int NOT NULL,
MODIFY COLUMN estado varchar(50) NOT NULL,
MODIFY COLUMN totalFactura decimal(10,2) NOT NULL DEFAULT 0,
MODIFY COLUMN fechaFactura date NOT NULL,
MODIFY COLUMN codigoCliente int NOT NULL,
MODIFY COLUMN codigoEmpleado int NOT NULL,
ADD CONSTRAINT ck_Factura_0 CHECK (numeroFactura > 0),
ADD CONSTRAINT ck_Factura_1 CHECK (CHAR_LENGTH(TRIM(estado)) > 0),
ADD CONSTRAINT ck_Factura_2 CHECK (totalFactura >= 0),
ADD CONSTRAINT ck_Factura_3 CHECK (CHAR_LENGTH(TRIM(fechaFactura)) > 0),
ADD CONSTRAINT ck_Factura_4 CHECK (codigoCliente > 0),
ADD CONSTRAINT ck_Factura_5 CHECK (codigoEmpleado > 0);
ALTER TABLE DetalleFactura
MODIFY COLUMN codigoDetalleFactura int NOT NULL,
MODIFY COLUMN precioUnitario decimal(10,2) NOT NULL DEFAULT 0,
MODIFY COLUMN cantidad int NOT NULL,
MODIFY COLUMN numeroFactura int NOT NULL,
MODIFY COLUMN codigoProducto varchar(15) NOT NULL,
ADD CONSTRAINT ck_DetalleFactura_0 CHECK (codigoDetalleFactura > 0),
ADD CONSTRAINT ck_DetalleFactura_1 CHECK (precioUnitario >= 0),
ADD CONSTRAINT ck_DetalleFactura_2 CHECK (cantidad > 0),
ADD CONSTRAINT ck_DetalleFactura_3 CHECK (numeroFactura > 0),
ADD CONSTRAINT ck_DetalleFactura_4 CHECK (CHAR_LENGTH(TRIM(codigoProducto)) > 0);
ALTER TABLE Productos ADD CONSTRAINT FK_Productos_TipoProducto FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto) ON DELETE RESTRICT;
ALTER TABLE Productos ADD CONSTRAINT FK_Productos_Proveedor FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE RESTRICT;
ALTER TABLE Empleados ADD CONSTRAINT FK_Empleados_CargoEmpleado FOREIGN KEY (codigoCargoEmpleado) REFERENCES CargoEmpleado(codigoCargoEmpleado) ON DELETE RESTRICT;
ALTER TABLE EmailProveedor ADD CONSTRAINT FK_EmailProveedor_Proveedores FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE RESTRICT;
ALTER TABLE TelefonoProveedor ADD CONSTRAINT FK_TelefonoProveedor_Proveedores FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE RESTRICT;
ALTER TABLE DetalleCompra ADD CONSTRAINT FK_DetalleCompra_Productos FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) ON DELETE RESTRICT;
ALTER TABLE DetalleCompra ADD CONSTRAINT FK_DetalleCompra_Compras FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento) ON DELETE RESTRICT;
ALTER TABLE Factura ADD CONSTRAINT FK_Factura_Clientes FOREIGN KEY (codigoCliente) REFERENCES Clientes(codigoCliente) ON DELETE RESTRICT;
ALTER TABLE Factura ADD CONSTRAINT FK_Factura_Empleados FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado) ON DELETE RESTRICT;
ALTER TABLE DetalleFactura ADD CONSTRAINT FK_DetalleFactura_Factura FOREIGN KEY (numeroFactura) REFERENCES Factura(numeroFactura) ON DELETE RESTRICT;
ALTER TABLE DetalleFactura ADD CONSTRAINT FK_DetalleFactura FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) ON DELETE RESTRICT;
CREATE TABLE MovimientosInventario (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    codigoProducto VARCHAR(15) NOT NULL,
    variacion INT NOT NULL,
    origen VARCHAR(30) NOT NULL,
    referencia INT NOT NULL,
    creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mov_producto FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) ON DELETE RESTRICT,
    INDEX ix_mov_producto_fecha (codigoProducto, creado)
) ENGINE=InnoDB;
INSERT INTO MovimientosInventario(codigoProducto,variacion,origen,referencia) SELECT codigoProducto,existencia,'saldo:migracion',0 FROM Productos;
DELIMITER $$
CREATE PROCEDURE sp_agregarClientes(IN p_codigoCliente int, IN p_NITClientes varchar(10), IN p_nombresCliente varchar(50), IN p_apellidosCliente varchar(50), IN p_direccionCliente varchar(150), IN p_telefonoCliente varchar(8), IN p_correoCliente varchar(45))
BEGIN
		insert into Clientes(codigoCliente, NITClientes, nombresCliente, apellidosCliente, direccionCliente, telefonoCliente, correoCliente)
		values (p_codigoCliente, p_NITClientes, p_nombresCliente, p_apellidosCliente, p_direccionCliente, p_telefonoCliente, p_correoCliente);
	END $$

CREATE PROCEDURE sp_listarClientes()
BEGIN
    SELECT Clientes.codigoCliente, Clientes.NITClientes, Clientes.nombresCliente, Clientes.apellidosCliente, Clientes.direccionCliente, Clientes.telefonoCliente, Clientes.correoCliente FROM Clientes ORDER BY Clientes.codigoCliente;
END $$

CREATE PROCEDURE sp_buscarClientes(IN p_codigoCliente int)
BEGIN
		SELECT Clientes.codigoCliente, Clientes.NITClientes, Clientes.nombresCliente, Clientes.apellidosCliente, Clientes.direccionCliente, Clientes.telefonoCliente, Clientes.correoCliente FROM Clientes where Clientes.codigoCliente = p_codigoCliente;
	END $$

CREATE PROCEDURE sp_actualizarClientes(IN p_codigoCliente int, IN p_NITClientes varchar(10), IN p_nombresCliente varchar(50), IN p_apellidosCliente varchar(50), IN p_direccionCliente varchar(150), IN p_telefonoCliente varchar(8), IN p_correoCliente varchar(45))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Clientes WHERE codigoCliente = p_codigoCliente FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update Clientes
    set
		Clientes.NITClientes = p_NITClientes,
        Clientes.nombresCliente = p_nombresCliente,
        Clientes.apellidosCliente = p_apellidosCliente,
        Clientes.direccionCliente = p_direccionCliente,
        Clientes.telefonoCliente = p_telefonoCliente,
        Clientes.correoCliente = p_correoCliente
	where
		Clientes.codigoCliente = p_codigoCliente;
END $$

CREATE PROCEDURE sp_eliminarClientes(IN p_codigoCliente int)
BEGIN
		delete from Clientes where Clientes.codigoCliente = p_codigoCliente;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarProveedores(IN p_codigoProveedor int, IN p_NITProveedor varchar(10), IN p_nombresProveedor varchar(60), IN p_apellidosProveedor varchar(60), IN p_direccionProveedor varchar(150), IN p_razonSocial varchar(60), IN p_contactoPrincipal varchar(100), IN p_paginaWeb varchar(50))
BEGIN
		insert into Proveedores(codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb)
		values (p_codigoProveedor, p_NITProveedor, p_nombresProveedor, p_apellidosProveedor, p_direccionProveedor, p_razonSocial, p_contactoPrincipal, p_paginaWeb);
	END $$

CREATE PROCEDURE sp_listarProveedores()
BEGIN
    SELECT Proveedores.codigoProveedor, Proveedores.NITProveedor, Proveedores.nombresProveedor, Proveedores.apellidosProveedor, Proveedores.direccionProveedor, Proveedores.razonSocial, Proveedores.contactoPrincipal, Proveedores.paginaWeb FROM Proveedores ORDER BY Proveedores.codigoProveedor;
END $$

CREATE PROCEDURE sp_buscarProveedores(IN p_codigoProveedor int)
BEGIN
		SELECT Proveedores.codigoProveedor, Proveedores.NITProveedor, Proveedores.nombresProveedor, Proveedores.apellidosProveedor, Proveedores.direccionProveedor, Proveedores.razonSocial, Proveedores.contactoPrincipal, Proveedores.paginaWeb FROM Proveedores where Proveedores.codigoProveedor = p_codigoProveedor;
	END $$

CREATE PROCEDURE sp_actualizarProveedores(IN p_codigoProveedor int, IN p_NITProveedor varchar(10), IN p_nombresProveedor varchar(60), IN p_apellidosProveedor varchar(60), IN p_direccionProveedor varchar(150), IN p_razonSocial varchar(60), IN p_contactoPrincipal varchar(100), IN p_paginaWeb varchar(50))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Proveedores WHERE codigoProveedor = p_codigoProveedor FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update Proveedores
    set
		Proveedores.NITProveedor = p_NITProveedor,
        Proveedores.nombresProveedor = p_nombresProveedor,
        Proveedores.apellidosProveedor = p_apellidosProveedor,
        Proveedores.direccionProveedor = p_direccionProveedor,
        Proveedores.razonSocial = p_razonSocial,
        Proveedores.contactoPrincipal = p_contactoPrincipal,
        Proveedores.paginaWeb = p_paginaWeb
	where
		Proveedores.codigoProveedor = p_codigoProveedor;
END $$

CREATE PROCEDURE sp_eliminarProveedores(IN p_codigoProveedor int)
BEGIN
		delete from Proveedores where Proveedores.codigoProveedor = p_codigoProveedor;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarCargoEmpleado(IN p_codigoCargoEmpleado int, IN p_nombreCargo varchar(45), IN p_descripcionCargo varchar(45))
BEGIN
		insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
		values (p_codigoCargoEmpleado, p_nombreCargo, p_descripcionCargo);
	END $$

CREATE PROCEDURE sp_listarCargoEmpleado()
BEGIN
    SELECT CargoEmpleado.codigoCargoEmpleado, CargoEmpleado.nombreCargo, CargoEmpleado.descripcionCargo FROM CargoEmpleado ORDER BY CargoEmpleado.codigoCargoEmpleado;
END $$

CREATE PROCEDURE sp_buscarCargoEmpleado(IN p_codigoCargoEmpleado int)
BEGIN
		SELECT CargoEmpleado.codigoCargoEmpleado, CargoEmpleado.nombreCargo, CargoEmpleado.descripcionCargo FROM CargoEmpleado where CargoEmpleado.codigoCargoEmpleado = p_codigoCargoEmpleado;
	END $$

CREATE PROCEDURE sp_actualizarCargoEmpleado(IN p_codigoCargoEmpleado int, IN p_nombreCargo varchar(45), IN p_descripcionCargo varchar(45))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM CargoEmpleado WHERE codigoCargoEmpleado = p_codigoCargoEmpleado FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update CargoEmpleado
    set
		CargoEmpleado.nombreCargo = p_nombreCargo,
        CargoEmpleado.descripcionCargo = p_descripcionCargo
	where
		CargoEmpleado.codigoCargoEmpleado = p_codigoCargoEmpleado;
END $$

CREATE PROCEDURE sp_eliminarCargoEmpleado(IN p_codigoCargoEmpleado int)
BEGIN
		delete from CargoEmpleado where CargoEmpleado.codigoCargoEmpleado = p_codigoCargoEmpleado;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarTipoProducto(IN p_codigoTipoProducto int, IN p_descripcion varchar(45))
BEGIN
		insert into TipoProducto(codigoTipoProducto, descripcion)
		values (p_codigoTipoProducto, p_descripcion);
	END $$

CREATE PROCEDURE sp_listarTipoProducto()
BEGIN
    SELECT TipoProducto.codigoTipoProducto, TipoProducto.descripcion FROM TipoProducto ORDER BY TipoProducto.codigoTipoProducto;
END $$

CREATE PROCEDURE sp_buscarTipoProducto(IN p_codigoTipoProducto int)
BEGIN
		SELECT TipoProducto.codigoTipoProducto, TipoProducto.descripcion FROM TipoProducto where TipoProducto.codigoTipoProducto = p_codigoTipoProducto;
	END $$

CREATE PROCEDURE sp_actualizarTipoProducto(IN p_codigoTipoProducto int, IN p_descripcion varchar(45))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM TipoProducto WHERE codigoTipoProducto = p_codigoTipoProducto FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update TipoProducto
    set
		TipoProducto.descripcion = p_descripcion
	where
		TipoProducto.codigoTipoProducto = p_codigoTipoProducto;
END $$

CREATE PROCEDURE sp_eliminarTipoProducto(IN p_codigoTipoProducto int)
BEGIN
		delete from TipoProducto where TipoProducto.codigoTipoProducto = p_codigoTipoProducto;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarCompras(IN p_numeroDocumento int, IN p_fechaDocumento date, IN p_descripcion varchar(60))
BEGIN
	insert into Compras(numeroDocumento, fechaDocumento,descripcion)
    values (p_numeroDocumento, p_fechaDocumento,p_descripcion);
END $$

CREATE PROCEDURE sp_listarCompras()
BEGIN
    SELECT Compras.numeroDocumento, Compras.fechaDocumento, Compras.descripcion, Compras.totalDocumento FROM Compras ORDER BY Compras.numeroDocumento;
END $$

CREATE PROCEDURE sp_buscarCompras(IN p_numeroDocumento int)
BEGIN
	SELECT Compras.numeroDocumento, Compras.fechaDocumento, Compras.descripcion, Compras.totalDocumento FROM Compras where Compras.numeroDocumento = p_numeroDocumento;
END $$

CREATE PROCEDURE sp_actualizarCompras(IN p_numeroDocumento int, IN p_fechaDocumento date, IN p_descripcion varchar(60))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Compras WHERE numeroDocumento = p_numeroDocumento FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update Compras
    set
		Compras.fechaDocumento = p_fechaDocumento,
		Compras.descripcion = p_descripcion
	where
		Compras.numeroDocumento = p_numeroDocumento;
END $$

CREATE PROCEDURE sp_eliminarCompras(IN p_numeroDocumento int)
BEGIN
	delete from Compras where Compras.numeroDocumento = p_numeroDocumento;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_actualizarComprasTotal(IN p_numDoc int, IN p_total decimal(10,2))
BEGIN
	update Compras
	set
		Compras.totalDocumento=p_total
    where
		Compras.numeroDocumento=p_numDoc;
END $$

CREATE PROCEDURE sp_agregarProductos(IN p_codigoProducto varchar(15), IN p_descripcionProducto varchar(45), IN p_imagenProducto varchar(45), IN p_codigoTipoProducto int(11), IN p_codigoProveedor int)
BEGIN
	insert into Productos(codigoProducto,descripcionProducto,imagenProducto,codigoTipoProducto,codigoProveedor)
    values (p_codigoProducto,p_descripcionProducto,p_imagenProducto,p_codigoTipoProducto,p_codigoProveedor);
END $$

CREATE PROCEDURE sp_listarProductos()
BEGIN
    SELECT Productos.codigoProducto, Productos.descripcionProducto, Productos.precioUnitario, Productos.precioDocena, Productos.precioMayor, Productos.imagenProducto, Productos.existencia, Productos.codigoTipoProducto, Productos.codigoProveedor FROM Productos ORDER BY Productos.codigoProducto;
END $$

CREATE PROCEDURE sp_buscarProductos(IN p_codigoProducto varchar(15))
BEGIN
	SELECT Productos.codigoProducto, Productos.descripcionProducto, Productos.precioUnitario, Productos.precioDocena, Productos.precioMayor, Productos.imagenProducto, Productos.existencia, Productos.codigoTipoProducto, Productos.codigoProveedor FROM Productos where Productos.codigoProducto = p_codigoProducto;
END $$

CREATE PROCEDURE sp_actualizarProductos(IN p_codigoProducto varchar(15), IN p_descripcionProducto varchar(45), IN p_imagenProducto varchar(45), IN p_codigoTipoProducto int(11), IN p_codigoProveedor int)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Productos WHERE codigoProducto = p_codigoProducto FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update Productos
    set
		Productos.descripcionProducto = p_descripcionProducto,
        Productos.imagenProducto = p_imagenProducto,
        Productos.codigoTipoProducto = p_codigoTipoProducto,
        Productos.codigoProveedor = p_codigoProveedor
	where
		Productos.codigoProducto = p_codigoProducto;
END $$

CREATE PROCEDURE sp_eliminarProductos(IN p_codigoProducto varchar(45))
BEGIN
	delete from Productos where Productos.codigoProducto = p_codigoProducto;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarEmpleados(IN p_codigoEmpleado int, IN p_nombresEmpleado varchar(50), IN p_apellidosEmpleado varchar(50), IN p_sueldo decimal(10,2), IN p_direccion varchar(150), IN p_turno varchar(15), IN p_codigoCargoEmpleado int)
BEGIN
	insert into Empleados(codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado)
    values (p_codigoEmpleado, p_nombresEmpleado, p_apellidosEmpleado, p_sueldo, p_direccion, p_turno, p_codigoCargoEmpleado);
END $$

CREATE PROCEDURE sp_listarEmpleados()
BEGIN
    SELECT Empleados.codigoEmpleado, Empleados.nombresEmpleado, Empleados.apellidosEmpleado, Empleados.sueldo, Empleados.direccion, Empleados.turno, Empleados.codigoCargoEmpleado FROM Empleados ORDER BY Empleados.codigoEmpleado;
END $$

CREATE PROCEDURE sp_buscarEmpleados(IN p_codigoEmpleado int)
BEGIN
	SELECT Empleados.codigoEmpleado, Empleados.nombresEmpleado, Empleados.apellidosEmpleado, Empleados.sueldo, Empleados.direccion, Empleados.turno, Empleados.codigoCargoEmpleado FROM Empleados where Empleados.codigoEmpleado = p_codigoEmpleado;
END $$

CREATE PROCEDURE sp_actualizarEmpleados(IN p_codigoEmpleado int, IN p_nombresEmpleado varchar(50), IN p_apellidosEmpleado varchar(50), IN p_sueldo decimal(10,2), IN p_direccion varchar(150), IN p_turno varchar(15), IN p_codigoCargoEmpleado int)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Empleados WHERE codigoEmpleado = p_codigoEmpleado FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

 UPDATE Empleados SET nombresEmpleado=p_nombresEmpleado, apellidosEmpleado=p_apellidosEmpleado, sueldo=p_sueldo, direccion=p_direccion, turno=p_turno, codigoCargoEmpleado=p_codigoCargoEmpleado WHERE codigoEmpleado=p_codigoEmpleado;
END $$

CREATE PROCEDURE sp_eliminarEmpleados(IN p_codigoEmpleado int)
BEGIN
	delete from Empleados where Empleados.codigoEmpleado = p_codigoEmpleado;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarEmailProveedor(IN p_codigoEmailProveedor int, IN p_emailProveedor varchar(50), IN p_descripcion varchar(100), IN p_codigoProveedor int)
BEGIN
	insert into EmailProveedor(codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor)
    values(p_codigoEmailProveedor, p_emailProveedor, p_descripcion, p_codigoProveedor);
END $$

CREATE PROCEDURE sp_listarEmailProveedor()
BEGIN
    SELECT EmailProveedor.codigoEmailProveedor, EmailProveedor.emailProveedor, EmailProveedor.descripcion, EmailProveedor.codigoProveedor FROM EmailProveedor ORDER BY EmailProveedor.codigoEmailProveedor;
END $$

CREATE PROCEDURE sp_buscarEmailProveedor(IN p_codigoEmailProveedor int)
BEGIN
	SELECT EmailProveedor.codigoEmailProveedor, EmailProveedor.emailProveedor, EmailProveedor.descripcion, EmailProveedor.codigoProveedor FROM EmailProveedor where EmailProveedor.codigoEmailProveedor = p_codigoEmailProveedor;
END $$

CREATE PROCEDURE sp_actualizarEmailProveedor(IN p_codigoEmailProveedor int, IN p_emailProveedor varchar(50), IN p_descripcion varchar(100), IN p_codigoProveedor int)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM EmailProveedor WHERE codigoEmailProveedor = p_codigoEmailProveedor FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update EmailProveedor
	set
		EmailProveedor.emailProveedor = p_emailProveedor,
        EmailProveedor.descripcion = p_descripcion,
        EmailProveedor.codigoProveedor = p_codigoProveedor
	where
		EmailProveedor.codigoEmailProveedor = p_codigoEmailProveedor;
END $$

CREATE PROCEDURE sp_eliminarEmailProveedor(IN p_codigoEmailProveedor int)
BEGIN
	delete from EmailProveedor where EmailProveedor.codigoEmailProveedor = p_codigoEmailProveedor;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarTelefonoProveedor(IN p_codigoTelefonoProveedor int, IN p_numeroPrincipal varchar(8), IN p_numeroSecundario varchar(8), IN p_observaciones varchar(45), IN p_codigoProveedor int)
BEGIN
	insert into TelefonoProveedor(codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
    values (p_codigoTelefonoProveedor, p_numeroPrincipal, p_numeroSecundario, p_observaciones, p_codigoProveedor);
END $$

CREATE PROCEDURE sp_listarTelefonoProveedor()
BEGIN
    SELECT TelefonoProveedor.codigoTelefonoProveedor, TelefonoProveedor.numeroPrincipal, TelefonoProveedor.numeroSecundario, TelefonoProveedor.observaciones, TelefonoProveedor.codigoProveedor FROM TelefonoProveedor ORDER BY TelefonoProveedor.codigoTelefonoProveedor;
END $$

CREATE PROCEDURE sp_buscarTelefonoProveedor(IN p_codigoTelefonoProveedor int)
BEGIN
	SELECT TelefonoProveedor.codigoTelefonoProveedor, TelefonoProveedor.numeroPrincipal, TelefonoProveedor.numeroSecundario, TelefonoProveedor.observaciones, TelefonoProveedor.codigoProveedor FROM TelefonoProveedor where TelefonoProveedor.codigoTelefonoProveedor = p_codigoTelefonoProveedor;
END $$

CREATE PROCEDURE sp_actualizarTelefonoProveedor(IN p_codigoTelefonoProveedor int, IN p_numeroPrincipal varchar(8), IN p_numeroSecundario varchar(8), IN p_observaciones varchar(45), IN p_codigoProveedor int)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM TelefonoProveedor WHERE codigoTelefonoProveedor = p_codigoTelefonoProveedor FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update TelefonoProveedor
    set
		TelefonoProveedor.numeroPrincipal = p_numeroPrincipal,
        TelefonoProveedor.numeroSecundario = p_numeroSecundario,
        TelefonoProveedor.observaciones = p_observaciones,
        TelefonoProveedor.codigoProveedor = p_codigoProveedor
	where
		TelefonoProveedor.codigoTelefonoProveedor = p_codigoTelefonoProveedor;
END $$

CREATE PROCEDURE sp_eliminarTelefonoProveedor(IN p_codigoTelefonoProveedor int)
BEGIN
	delete from TelefonoProveedor where TelefonoProveedor.codigoTelefonoProveedor = p_codigoTelefonoProveedor;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarFactura(IN p_numeroFactura int, IN p_estado varchar(50), IN p_fechaFactura date, IN p_codigoCliente int, IN p_codigoEmpleado int)
BEGIN
	insert into Factura(numeroFactura,estado,fechaFactura,codigoCliente,codigoEmpleado)
    values (p_numeroFactura,p_estado,p_fechaFactura,p_codigoCliente,p_codigoEmpleado);
END $$

CREATE PROCEDURE sp_listarFacturas()
BEGIN
    SELECT Factura.numeroFactura, Factura.estado, Factura.totalFactura, Factura.fechaFactura, Factura.codigoCliente, Factura.codigoEmpleado FROM Factura ORDER BY Factura.numeroFactura;
END $$

CREATE PROCEDURE sp_buscarFacturas(IN p_numeroFactura int)
BEGIN
	SELECT Factura.numeroFactura, Factura.estado, Factura.totalFactura, Factura.fechaFactura, Factura.codigoCliente, Factura.codigoEmpleado FROM Factura where Factura.numeroFactura = p_numeroFactura;
END $$

CREATE PROCEDURE sp_actualizarFacturas(IN p_numeroFactura int, IN p_estado varchar(50), IN p_fechaFactura date, IN p_codigoCliente int, IN p_codigoEmpleado int)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Factura WHERE numeroFactura = p_numeroFactura FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update Factura
    set
		Factura.estado = p_estado,
        Factura.fechaFactura = p_fechaFactura,
        Factura.codigoCliente = p_codigoCliente,
        Factura.codigoEmpleado = p_codigoEmpleado
	where
		Factura.numeroFactura = p_numeroFactura;
END $$

CREATE PROCEDURE sp_eliminarFacturas(IN p_numeroFactura int)
BEGIN
	delete from Factura where Factura.numeroFactura = p_numeroFactura;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_actualizarFacturaTotal(IN p_numFac int, IN p_total decimal(10,2))
BEGIN
	update Factura
	set
		Factura.totalFactura=p_total
    where
		Factura.numeroFactura=p_numFac;
END $$

CREATE PROCEDURE sp_agregarDetalleFactura(IN p_codigoDetalleFactura int, IN p_cantidad int, IN p_numeroFactura int, IN p_codigoProducto varchar(15))
BEGIN
	insert into DetalleFactura(codigoDetalleFactura, cantidad, numeroFactura, codigoProducto)
    values (p_codigoDetalleFactura, p_cantidad, p_numeroFactura, p_codigoProducto);
END $$

CREATE PROCEDURE sp_listarDetalleFactura()
BEGIN
    SELECT DetalleFactura.codigoDetalleFactura, DetalleFactura.precioUnitario, DetalleFactura.cantidad, DetalleFactura.numeroFactura, DetalleFactura.codigoProducto FROM DetalleFactura ORDER BY DetalleFactura.codigoDetalleFactura;
END $$

CREATE PROCEDURE sp_buscarDetalleFactura(IN p_codigoDetalleFactura int)
BEGIN
	SELECT DetalleFactura.codigoDetalleFactura, DetalleFactura.precioUnitario, DetalleFactura.cantidad, DetalleFactura.numeroFactura, DetalleFactura.codigoProducto FROM DetalleFactura where DetalleFactura.codigoDetalleFactura = p_codigoDetalleFactura;
END $$

CREATE PROCEDURE sp_ActualizarDetalleFactura(IN p_codigoDetalleFactura int, IN p_precioUnitario decimal(10,2), IN p_cantidad int, IN p_numeroFactura int, IN p_codigoProducto varchar(15))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM DetalleFactura WHERE codigoDetalleFactura = p_codigoDetalleFactura FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update DetalleFactura
    set
		DetalleFactura.cantidad = p_cantidad,
        DetalleFactura.numeroFactura = p_numeroFactura,
        DetalleFactura.codigoProducto = p_codigoProducto
	where
		DetalleFactura.codigoDetalleFactura = p_codigoDetalleFactura;
END $$

CREATE PROCEDURE sp_eliminarDetalleFactura(IN p_codigoDetalleFactura int)
BEGIN
	delete from DetalleFactura where DetalleFactura.codigoDetalleFactura = p_codigoDetalleFactura;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_agregarDetalleCompra(IN p_codigoDc int, IN p_costo decimal(10,2), IN p_cant int, IN p_codProd varchar(15) , IN p_numDoc int)
BEGIN
	insert into DetalleCompra (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    values(p_codigoDc, p_costo,p_cant, p_codProd, p_numDoc);

END $$

CREATE PROCEDURE sp_listarDetalleCompra()
BEGIN
    SELECT DetalleCompra.codigoDetalleCompra, DetalleCompra.costoUnitario, DetalleCompra.cantidad, DetalleCompra.codigoProducto, DetalleCompra.numeroDocumento FROM DetalleCompra ORDER BY DetalleCompra.codigoDetalleCompra;
END $$

CREATE PROCEDURE sp_buscarDetalleCompra(IN p_codDetCompra int)
BEGIN
	select
    DetalleCompra.codigoDetalleCompra,
    DetalleCompra.costoUnitario,
    DetalleCompra.cantidad,
    DetalleCompra.codigoProducto,
    DetalleCompra.numeroDocumento
    from DetalleCompra
    where DetalleCompra.codigoDetalleCompra=p_codDetCompra;
END $$

CREATE PROCEDURE sp_eliminarDetalleCompra(IN p_codProd int)
BEGIN
	delete from DetalleCompra
    where DetalleCompra.codigoDetalleCompra=p_codProd;

    IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe'; END IF;
END $$

CREATE PROCEDURE sp_actualizarDetalleCompra(IN p_codDetCompra int, IN p_precUnit decimal(10,2), IN p_cant int, IN p_codProd varchar(15) , IN p_numDoc int )
BEGIN
    IF NOT EXISTS (SELECT 1 FROM DetalleCompra WHERE codigoDetalleCompra = p_codDetCompra FOR UPDATE) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya no existe; actualice los datos'; END IF;

	update DetalleCompra
	set
		DetalleCompra.costoUnitario=p_precUnit,
		DetalleCompra.cantidad=p_cant,
		DetalleCompra.codigoProducto=p_codProd,
		DetalleCompra.numeroDocumento=p_numDoc
    where
		DetalleCompra.codigoDetalleCompra=p_codDetCompra;
END $$

CREATE PROCEDURE sp_movimientoInventario(IN p_producto VARCHAR(15), IN p_delta INT, IN p_origen VARCHAR(30), IN p_referencia INT)
BEGIN
    IF p_delta <> 0 THEN
        UPDATE Productos SET existencia = existencia + p_delta
        WHERE codigoProducto = p_producto AND existencia + p_delta >= 0;
        IF ROW_COUNT() <> 1 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existencia insuficiente o producto inexistente';
        END IF;
        INSERT INTO MovimientosInventario(codigoProducto, variacion, origen, referencia)
        VALUES(p_producto, p_delta, p_origen, p_referencia);
    END IF;
END $$

CREATE TRIGGER tr_compra_before_insert BEFORE INSERT ON DetalleCompra FOR EACH ROW
BEGIN
CALL sp_movimientoInventario(NEW.codigoProducto, 1 * NEW.cantidad, 'compra:alta', NEW.codigoDetalleCompra);
END $$

CREATE TRIGGER tr_compra_after_insert AFTER INSERT ON DetalleCompra FOR EACH ROW
BEGIN
UPDATE Compras SET totalDocumento = totalDocumento + NEW.costoUnitario * NEW.cantidad WHERE numeroDocumento = NEW.numeroDocumento;
UPDATE Productos SET precioUnitario = ROUND(NEW.costoUnitario * 1.40, 2), precioDocena = ROUND(NEW.costoUnitario * 1.35, 2), precioMayor = ROUND(NEW.costoUnitario * 1.25, 2) WHERE codigoProducto = NEW.codigoProducto;
END $$

CREATE TRIGGER tr_compra_before_update BEFORE UPDATE ON DetalleCompra FOR EACH ROW
BEGIN
IF NEW.codigoProducto = OLD.codigoProducto THEN
CALL sp_movimientoInventario(NEW.codigoProducto, 1 * (NEW.cantidad - OLD.cantidad), 'compra:edicion', NEW.codigoDetalleCompra);
ELSE
CALL sp_movimientoInventario(OLD.codigoProducto, -1 * OLD.cantidad, 'compra:origen', OLD.codigoDetalleCompra);
CALL sp_movimientoInventario(NEW.codigoProducto, 1 * NEW.cantidad, 'compra:destino', NEW.codigoDetalleCompra);
END IF;
END $$

CREATE TRIGGER tr_compra_after_update AFTER UPDATE ON DetalleCompra FOR EACH ROW
BEGIN
IF OLD.numeroDocumento = NEW.numeroDocumento THEN UPDATE Compras SET totalDocumento = totalDocumento + NEW.costoUnitario * NEW.cantidad - OLD.costoUnitario * OLD.cantidad WHERE numeroDocumento = NEW.numeroDocumento; ELSE UPDATE Compras SET totalDocumento = totalDocumento - OLD.costoUnitario * OLD.cantidad WHERE numeroDocumento = OLD.numeroDocumento; UPDATE Compras SET totalDocumento = totalDocumento + NEW.costoUnitario * NEW.cantidad WHERE numeroDocumento = NEW.numeroDocumento; END IF;
UPDATE Productos SET precioUnitario = ROUND(NEW.costoUnitario * 1.40, 2), precioDocena = ROUND(NEW.costoUnitario * 1.35, 2), precioMayor = ROUND(NEW.costoUnitario * 1.25, 2) WHERE codigoProducto = NEW.codigoProducto;
END $$

CREATE TRIGGER tr_compra_before_delete BEFORE DELETE ON DetalleCompra FOR EACH ROW
BEGIN
CALL sp_movimientoInventario(OLD.codigoProducto, -1 * OLD.cantidad, 'compra:baja', OLD.codigoDetalleCompra);
END $$

CREATE TRIGGER tr_compra_after_delete AFTER DELETE ON DetalleCompra FOR EACH ROW
BEGIN
UPDATE Compras SET totalDocumento = totalDocumento - OLD.costoUnitario * OLD.cantidad WHERE numeroDocumento = OLD.numeroDocumento;
END $$

CREATE TRIGGER tr_venta_before_insert BEFORE INSERT ON DetalleFactura FOR EACH ROW
BEGIN
SET NEW.precioUnitario = (SELECT precioUnitario FROM Productos WHERE codigoProducto = NEW.codigoProducto);
CALL sp_movimientoInventario(NEW.codigoProducto, -1 * NEW.cantidad, 'venta:alta', NEW.codigoDetalleFactura);
END $$

CREATE TRIGGER tr_venta_after_insert AFTER INSERT ON DetalleFactura FOR EACH ROW
BEGIN
UPDATE Factura SET totalFactura = totalFactura + NEW.precioUnitario * NEW.cantidad WHERE numeroFactura = NEW.numeroFactura;
END $$

CREATE TRIGGER tr_venta_before_update BEFORE UPDATE ON DetalleFactura FOR EACH ROW
BEGIN
IF NEW.codigoProducto = OLD.codigoProducto THEN SET NEW.precioUnitario = OLD.precioUnitario; ELSE SET NEW.precioUnitario = (SELECT precioUnitario FROM Productos WHERE codigoProducto = NEW.codigoProducto); END IF;
IF NEW.codigoProducto = OLD.codigoProducto THEN
CALL sp_movimientoInventario(NEW.codigoProducto, -1 * (NEW.cantidad - OLD.cantidad), 'venta:edicion', NEW.codigoDetalleFactura);
ELSE
CALL sp_movimientoInventario(OLD.codigoProducto, 1 * OLD.cantidad, 'venta:origen', OLD.codigoDetalleFactura);
CALL sp_movimientoInventario(NEW.codigoProducto, -1 * NEW.cantidad, 'venta:destino', NEW.codigoDetalleFactura);
END IF;
END $$

CREATE TRIGGER tr_venta_after_update AFTER UPDATE ON DetalleFactura FOR EACH ROW
BEGIN
IF OLD.numeroFactura = NEW.numeroFactura THEN UPDATE Factura SET totalFactura = totalFactura + NEW.precioUnitario * NEW.cantidad - OLD.precioUnitario * OLD.cantidad WHERE numeroFactura = NEW.numeroFactura; ELSE UPDATE Factura SET totalFactura = totalFactura - OLD.precioUnitario * OLD.cantidad WHERE numeroFactura = OLD.numeroFactura; UPDATE Factura SET totalFactura = totalFactura + NEW.precioUnitario * NEW.cantidad WHERE numeroFactura = NEW.numeroFactura; END IF;
END $$

CREATE TRIGGER tr_venta_before_delete BEFORE DELETE ON DetalleFactura FOR EACH ROW
BEGIN
CALL sp_movimientoInventario(OLD.codigoProducto, 1 * OLD.cantidad, 'venta:baja', OLD.codigoDetalleFactura);
END $$

CREATE TRIGGER tr_venta_after_delete AFTER DELETE ON DetalleFactura FOR EACH ROW
BEGIN
UPDATE Factura SET totalFactura = totalFactura - OLD.precioUnitario * OLD.cantidad WHERE numeroFactura = OLD.numeroFactura;
END $$
DELIMITER ;

DROP VIEW IF EXISTS vw_Productos;
CREATE VIEW vw_Productos AS SELECT p.codigoProducto, p.descripcionProducto, t.descripcion AS Descripcion, v.nombresProveedor FROM Productos p JOIN TipoProducto t ON p.codigoTipoProducto=t.codigoTipoProducto JOIN Proveedores v ON p.codigoProveedor=v.codigoProveedor;

-- V003. Aplicar después de V002, una sola vez, con la aplicación detenida y un respaldo.
USE DBKinalExpress2020527;
CREATE TABLE SecuenciasProducto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
) ENGINE=InnoDB;
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
ALTER TABLE Clientes DROP CHECK ck_Clientes_0, MODIFY COLUMN codigoCliente INT NOT NULL AUTO_INCREMENT;
ALTER TABLE Proveedores DROP CHECK ck_Proveedores_0, MODIFY COLUMN codigoProveedor INT NOT NULL AUTO_INCREMENT;
ALTER TABLE Compras DROP CHECK ck_Compras_0, MODIFY COLUMN numeroDocumento INT NOT NULL AUTO_INCREMENT;
ALTER TABLE CargoEmpleado DROP CHECK ck_CargoEmpleado_0, MODIFY COLUMN codigoCargoEmpleado INT NOT NULL AUTO_INCREMENT;
ALTER TABLE TipoProducto DROP CHECK ck_TipoProducto_0, MODIFY COLUMN codigoTipoProducto INT NOT NULL AUTO_INCREMENT;
ALTER TABLE Empleados DROP CHECK ck_Empleados_0, MODIFY COLUMN codigoEmpleado INT NOT NULL AUTO_INCREMENT;
ALTER TABLE EmailProveedor DROP CHECK ck_EmailProveedor_0, MODIFY COLUMN codigoEmailProveedor INT NOT NULL AUTO_INCREMENT;
ALTER TABLE TelefonoProveedor DROP CHECK ck_TelefonoProveedor_0, MODIFY COLUMN codigoTelefonoProveedor INT NOT NULL AUTO_INCREMENT;
ALTER TABLE DetalleCompra DROP CHECK ck_DetalleCompra_0, MODIFY COLUMN codigoDetalleCompra INT NOT NULL AUTO_INCREMENT;
ALTER TABLE Factura DROP CHECK ck_Factura_0, MODIFY COLUMN numeroFactura INT NOT NULL AUTO_INCREMENT;
ALTER TABLE DetalleFactura DROP CHECK ck_DetalleFactura_0, MODIFY COLUMN codigoDetalleFactura INT NOT NULL AUTO_INCREMENT;
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
DELIMITER $$
CREATE PROCEDURE sp_nuevoCodigoProducto(OUT p_codigo VARCHAR(15))
BEGIN
    DECLARE v_id BIGINT;
    REPEAT
        INSERT INTO SecuenciasProducto(id) VALUES (NULL);
        SET v_id = LAST_INSERT_ID();
        IF v_id > 99999999999999 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Se agotaron los códigos de producto'; END IF;
        SET p_codigo = CONCAT('P', LPAD(v_id,14,'0'));
    UNTIL NOT EXISTS(SELECT 1 FROM Productos WHERE codigoProducto=p_codigo) END REPEAT;
END $$
DROP PROCEDURE sp_agregarClientes $$
CREATE PROCEDURE sp_agregarClientes(IN p_codigoCliente int, IN p_NITClientes varchar(10), IN p_nombresCliente varchar(50), IN p_apellidosCliente varchar(50), IN p_direccionCliente varchar(150), IN p_telefonoCliente varchar(8), IN p_correoCliente varchar(45))
BEGIN
		insert into Clientes(codigoCliente, NITClientes, nombresCliente, apellidosCliente, direccionCliente, telefonoCliente, correoCliente)
		values (p_codigoCliente, p_NITClientes, p_nombresCliente, p_apellidosCliente, p_direccionCliente, p_telefonoCliente, p_correoCliente);

    SELECT COALESCE(NULLIF(p_codigoCliente,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarProveedores $$
CREATE PROCEDURE sp_agregarProveedores(IN p_codigoProveedor int, IN p_NITProveedor varchar(10), IN p_nombresProveedor varchar(60), IN p_apellidosProveedor varchar(60), IN p_direccionProveedor varchar(150), IN p_razonSocial varchar(60), IN p_contactoPrincipal varchar(100), IN p_paginaWeb varchar(50))
BEGIN
		insert into Proveedores(codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb)
		values (p_codigoProveedor, p_NITProveedor, p_nombresProveedor, p_apellidosProveedor, p_direccionProveedor, p_razonSocial, p_contactoPrincipal, p_paginaWeb);

    SELECT COALESCE(NULLIF(p_codigoProveedor,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarCargoEmpleado $$
CREATE PROCEDURE sp_agregarCargoEmpleado(IN p_codigoCargoEmpleado int, IN p_nombreCargo varchar(45), IN p_descripcionCargo varchar(45))
BEGIN
		insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
		values (p_codigoCargoEmpleado, p_nombreCargo, p_descripcionCargo);

    SELECT COALESCE(NULLIF(p_codigoCargoEmpleado,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarTipoProducto $$
CREATE PROCEDURE sp_agregarTipoProducto(IN p_codigoTipoProducto int, IN p_descripcion varchar(45))
BEGIN
		insert into TipoProducto(codigoTipoProducto, descripcion)
		values (p_codigoTipoProducto, p_descripcion);

    SELECT COALESCE(NULLIF(p_codigoTipoProducto,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarCompras $$
CREATE PROCEDURE sp_agregarCompras(IN p_numeroDocumento int, IN p_fechaDocumento date, IN p_descripcion varchar(60))
BEGIN
	insert into Compras(numeroDocumento, fechaDocumento,descripcion)
    values (p_numeroDocumento, p_fechaDocumento,p_descripcion);

    SELECT COALESCE(NULLIF(p_numeroDocumento,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarProductos $$
CREATE PROCEDURE sp_agregarProductos(IN p_codigoProducto varchar(15), IN p_descripcionProducto varchar(45), IN p_imagenProducto varchar(45), IN p_codigoTipoProducto int(11), IN p_codigoProveedor int)
BEGIN
    IF p_codigoProducto IS NULL OR TRIM(p_codigoProducto)='' THEN CALL sp_nuevoCodigoProducto(p_codigoProducto); END IF;

	insert into Productos(codigoProducto,descripcionProducto,imagenProducto,codigoTipoProducto,codigoProveedor)
    values (p_codigoProducto,p_descripcionProducto,p_imagenProducto,p_codigoTipoProducto,p_codigoProveedor);

    SELECT p_codigoProducto AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarEmpleados $$
CREATE PROCEDURE sp_agregarEmpleados(IN p_codigoEmpleado int, IN p_nombresEmpleado varchar(50), IN p_apellidosEmpleado varchar(50), IN p_sueldo decimal(10,2), IN p_direccion varchar(150), IN p_turno varchar(15), IN p_codigoCargoEmpleado int)
BEGIN
	insert into Empleados(codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado)
    values (p_codigoEmpleado, p_nombresEmpleado, p_apellidosEmpleado, p_sueldo, p_direccion, p_turno, p_codigoCargoEmpleado);

    SELECT COALESCE(NULLIF(p_codigoEmpleado,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarEmailProveedor $$
CREATE PROCEDURE sp_agregarEmailProveedor(IN p_codigoEmailProveedor int, IN p_emailProveedor varchar(50), IN p_descripcion varchar(100), IN p_codigoProveedor int)
BEGIN
	insert into EmailProveedor(codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor)
    values(p_codigoEmailProveedor, p_emailProveedor, p_descripcion, p_codigoProveedor);

    SELECT COALESCE(NULLIF(p_codigoEmailProveedor,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarTelefonoProveedor $$
CREATE PROCEDURE sp_agregarTelefonoProveedor(IN p_codigoTelefonoProveedor int, IN p_numeroPrincipal varchar(8), IN p_numeroSecundario varchar(8), IN p_observaciones varchar(45), IN p_codigoProveedor int)
BEGIN
	insert into TelefonoProveedor(codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
    values (p_codigoTelefonoProveedor, p_numeroPrincipal, p_numeroSecundario, p_observaciones, p_codigoProveedor);

    SELECT COALESCE(NULLIF(p_codigoTelefonoProveedor,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarFactura $$
CREATE PROCEDURE sp_agregarFactura(IN p_numeroFactura int, IN p_estado varchar(50), IN p_fechaFactura date, IN p_codigoCliente int, IN p_codigoEmpleado int)
BEGIN
	insert into Factura(numeroFactura,estado,fechaFactura,codigoCliente,codigoEmpleado)
    values (p_numeroFactura,p_estado,p_fechaFactura,p_codigoCliente,p_codigoEmpleado);

    SELECT COALESCE(NULLIF(p_numeroFactura,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarDetalleFactura $$
CREATE PROCEDURE sp_agregarDetalleFactura(IN p_codigoDetalleFactura int, IN p_cantidad int, IN p_numeroFactura int, IN p_codigoProducto varchar(15))
BEGIN
	insert into DetalleFactura(codigoDetalleFactura, cantidad, numeroFactura, codigoProducto)
    values (p_codigoDetalleFactura, p_cantidad, p_numeroFactura, p_codigoProducto);

    SELECT COALESCE(NULLIF(p_codigoDetalleFactura,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_agregarDetalleCompra $$
CREATE PROCEDURE sp_agregarDetalleCompra(IN p_codigoDc int, IN p_costo decimal(10,2), IN p_cant int, IN p_codProd varchar(15) , IN p_numDoc int)
BEGIN
	insert into DetalleCompra (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    values(p_codigoDc, p_costo,p_cant, p_codProd, p_numDoc);


    SELECT COALESCE(NULLIF(p_codigoDc,0), LAST_INSERT_ID()) AS codigoGenerado;
END $$
DROP PROCEDURE sp_movimientoInventario $$
CREATE PROCEDURE sp_movimientoInventario(IN p_producto VARCHAR(15), IN p_delta INT, IN p_origen VARCHAR(30), IN p_referencia INT)
BEGIN
    IF p_delta <> 0 THEN
        UPDATE Productos SET existencia = existencia + p_delta
        WHERE codigoProducto = p_producto AND existencia + p_delta >= 0;
        IF ROW_COUNT() <> 1 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existencia insuficiente o producto inexistente';
        END IF;
        IF p_referencia > 0 THEN
        INSERT INTO MovimientosInventario(codigoProducto, variacion, origen, referencia)
        VALUES(p_producto, p_delta, p_origen, p_referencia);
        END IF;
    END IF;
END $$
DROP TRIGGER tr_compra_before_insert $$
CREATE TRIGGER tr_compra_before_insert BEFORE INSERT ON DetalleCompra FOR EACH ROW
BEGIN
CALL sp_movimientoInventario(NEW.codigoProducto, 1 * NEW.cantidad, 'compra:alta', 0);
END $$
DROP TRIGGER tr_compra_after_insert $$
CREATE TRIGGER tr_compra_after_insert AFTER INSERT ON DetalleCompra FOR EACH ROW
BEGIN
INSERT INTO MovimientosInventario(codigoProducto,variacion,origen,referencia) VALUES(NEW.codigoProducto,NEW.cantidad,'compra:alta',NEW.codigoDetalleCompra);

UPDATE Compras SET totalDocumento = totalDocumento + NEW.costoUnitario * NEW.cantidad WHERE numeroDocumento = NEW.numeroDocumento;
UPDATE Productos SET precioUnitario = ROUND(NEW.costoUnitario * 1.40, 2), precioDocena = ROUND(NEW.costoUnitario * 1.35, 2), precioMayor = ROUND(NEW.costoUnitario * 1.25, 2) WHERE codigoProducto = NEW.codigoProducto;
END $$
DROP TRIGGER tr_venta_before_insert $$
CREATE TRIGGER tr_venta_before_insert BEFORE INSERT ON DetalleFactura FOR EACH ROW
BEGIN
SET NEW.precioUnitario = (SELECT precioUnitario FROM Productos WHERE codigoProducto = NEW.codigoProducto);
CALL sp_movimientoInventario(NEW.codigoProducto, -1 * NEW.cantidad, 'venta:alta', 0);
END $$
DROP TRIGGER tr_venta_after_insert $$
CREATE TRIGGER tr_venta_after_insert AFTER INSERT ON DetalleFactura FOR EACH ROW
BEGIN
INSERT INTO MovimientosInventario(codigoProducto,variacion,origen,referencia) VALUES(NEW.codigoProducto,-NEW.cantidad,'venta:alta',NEW.codigoDetalleFactura);

UPDATE Factura SET totalFactura = totalFactura + NEW.precioUnitario * NEW.cantidad WHERE numeroFactura = NEW.numeroFactura;
END $$
CREATE TRIGGER tr_id_Clientes_insert BEFORE INSERT ON Clientes FOR EACH ROW BEGIN IF NEW.codigoCliente < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_Clientes_update BEFORE UPDATE ON Clientes FOR EACH ROW BEGIN IF NEW.codigoCliente <> OLD.codigoCliente THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_Proveedores_insert BEFORE INSERT ON Proveedores FOR EACH ROW BEGIN IF NEW.codigoProveedor < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_Proveedores_update BEFORE UPDATE ON Proveedores FOR EACH ROW BEGIN IF NEW.codigoProveedor <> OLD.codigoProveedor THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_Compras_insert BEFORE INSERT ON Compras FOR EACH ROW BEGIN IF NEW.numeroDocumento < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_Compras_update BEFORE UPDATE ON Compras FOR EACH ROW BEGIN IF NEW.numeroDocumento <> OLD.numeroDocumento THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_CargoEmpleado_insert BEFORE INSERT ON CargoEmpleado FOR EACH ROW BEGIN IF NEW.codigoCargoEmpleado < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_CargoEmpleado_update BEFORE UPDATE ON CargoEmpleado FOR EACH ROW BEGIN IF NEW.codigoCargoEmpleado <> OLD.codigoCargoEmpleado THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_TipoProducto_insert BEFORE INSERT ON TipoProducto FOR EACH ROW BEGIN IF NEW.codigoTipoProducto < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_TipoProducto_update BEFORE UPDATE ON TipoProducto FOR EACH ROW BEGIN IF NEW.codigoTipoProducto <> OLD.codigoTipoProducto THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_Empleados_insert BEFORE INSERT ON Empleados FOR EACH ROW BEGIN IF NEW.codigoEmpleado < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_Empleados_update BEFORE UPDATE ON Empleados FOR EACH ROW BEGIN IF NEW.codigoEmpleado <> OLD.codigoEmpleado THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_EmailProveedor_insert BEFORE INSERT ON EmailProveedor FOR EACH ROW BEGIN IF NEW.codigoEmailProveedor < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_EmailProveedor_update BEFORE UPDATE ON EmailProveedor FOR EACH ROW BEGIN IF NEW.codigoEmailProveedor <> OLD.codigoEmailProveedor THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_TelefonoProveedor_insert BEFORE INSERT ON TelefonoProveedor FOR EACH ROW BEGIN IF NEW.codigoTelefonoProveedor < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_TelefonoProveedor_update BEFORE UPDATE ON TelefonoProveedor FOR EACH ROW BEGIN IF NEW.codigoTelefonoProveedor <> OLD.codigoTelefonoProveedor THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_DetalleCompra_insert BEFORE INSERT ON DetalleCompra FOR EACH ROW BEGIN IF NEW.codigoDetalleCompra < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_DetalleCompra_update BEFORE UPDATE ON DetalleCompra FOR EACH ROW BEGIN IF NEW.codigoDetalleCompra <> OLD.codigoDetalleCompra THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_Factura_insert BEFORE INSERT ON Factura FOR EACH ROW BEGIN IF NEW.numeroFactura < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_Factura_update BEFORE UPDATE ON Factura FOR EACH ROW BEGIN IF NEW.numeroFactura <> OLD.numeroFactura THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
CREATE TRIGGER tr_id_DetalleFactura_insert BEFORE INSERT ON DetalleFactura FOR EACH ROW BEGIN IF NEW.codigoDetalleFactura < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Código inválido'; END IF; END $$
CREATE TRIGGER tr_id_DetalleFactura_update BEFORE UPDATE ON DetalleFactura FOR EACH ROW BEGIN IF NEW.codigoDetalleFactura <> OLD.codigoDetalleFactura THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del registro no se puede modificar'; END IF; END $$
DELIMITER ;

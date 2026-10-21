-- MySQL 8.0.16+. Run only in a new, empty database.

CREATE DATABASE IF NOT EXISTS DBKinalExpress2020527 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE DBKinalExpress2020527;

CREATE TABLE Usuarios (
    idUsuario int NOT NULL AUTO_INCREMENT,
    ingresoUsuario varchar(60) NOT NULL,
    ingresoClave varchar(255) NOT NULL,
    PRIMARY KEY (idUsuario),
    CONSTRAINT uq_usuario UNIQUE (ingresoUsuario),
    CONSTRAINT ck_Usuarios_0 CHECK (CHAR_LENGTH(TRIM(ingresoUsuario)) > 0),
    CONSTRAINT ck_Usuarios_1 CHECK (CHAR_LENGTH(TRIM(ingresoClave)) > 0)
) ENGINE=InnoDB;

CREATE TABLE Clientes (
    codigoCliente int NOT NULL AUTO_INCREMENT,
    NITClientes varchar(10) NOT NULL,
    nombresCliente varchar(50) NOT NULL,
    apellidosCliente varchar(50) NOT NULL,
    direccionCliente varchar(150) NOT NULL,
    telefonoCliente varchar(8) NOT NULL,
    correoCliente varchar(45) NOT NULL,
    PRIMARY KEY (codigoCliente),
    CONSTRAINT ck_Clientes_1 CHECK (CHAR_LENGTH(TRIM(NITClientes)) > 0),
    CONSTRAINT ck_Clientes_2 CHECK (CHAR_LENGTH(TRIM(nombresCliente)) > 0),
    CONSTRAINT ck_Clientes_3 CHECK (CHAR_LENGTH(TRIM(apellidosCliente)) > 0),
    CONSTRAINT ck_Clientes_4 CHECK (CHAR_LENGTH(TRIM(direccionCliente)) > 0),
    CONSTRAINT ck_Clientes_5 CHECK (CHAR_LENGTH(TRIM(telefonoCliente)) > 0),
    CONSTRAINT ck_Clientes_6 CHECK (CHAR_LENGTH(TRIM(correoCliente)) > 0)
) ENGINE=InnoDB;

CREATE TABLE Proveedores (
    codigoProveedor int NOT NULL AUTO_INCREMENT,
    NITProveedor varchar(10) NOT NULL,
    nombresProveedor varchar(60) NOT NULL,
    apellidosProveedor varchar(60) NOT NULL,
    direccionProveedor varchar(150) NOT NULL,
    razonSocial varchar(60) NOT NULL,
    contactoPrincipal varchar(100) NOT NULL,
    paginaWeb varchar(50) NOT NULL,
    PRIMARY KEY (codigoProveedor),
    CONSTRAINT ck_Proveedores_1 CHECK (CHAR_LENGTH(TRIM(NITProveedor)) > 0),
    CONSTRAINT ck_Proveedores_2 CHECK (CHAR_LENGTH(TRIM(nombresProveedor)) > 0),
    CONSTRAINT ck_Proveedores_3 CHECK (CHAR_LENGTH(TRIM(apellidosProveedor)) > 0),
    CONSTRAINT ck_Proveedores_4 CHECK (CHAR_LENGTH(TRIM(direccionProveedor)) > 0),
    CONSTRAINT ck_Proveedores_5 CHECK (CHAR_LENGTH(TRIM(razonSocial)) > 0),
    CONSTRAINT ck_Proveedores_6 CHECK (CHAR_LENGTH(TRIM(contactoPrincipal)) > 0)
) ENGINE=InnoDB;

CREATE TABLE Compras (
    numeroDocumento int NOT NULL AUTO_INCREMENT,
    fechaDocumento date NOT NULL,
    descripcion varchar(60) NOT NULL,
    totalDocumento decimal(10,2) NOT NULL DEFAULT 0,
    PRIMARY KEY (numeroDocumento),
    CONSTRAINT ck_Compras_1 CHECK (CHAR_LENGTH(TRIM(descripcion)) > 0),
    CONSTRAINT ck_Compras_2 CHECK (totalDocumento >= 0)
) ENGINE=InnoDB;

CREATE TABLE CargoEmpleado (
    codigoCargoEmpleado int NOT NULL AUTO_INCREMENT,
    nombreCargo varchar(45) NOT NULL,
    descripcionCargo varchar(45) NOT NULL,
    PRIMARY KEY (codigoCargoEmpleado),
    CONSTRAINT ck_CargoEmpleado_1 CHECK (CHAR_LENGTH(TRIM(nombreCargo)) > 0),
    CONSTRAINT ck_CargoEmpleado_2 CHECK (CHAR_LENGTH(TRIM(descripcionCargo)) > 0)
) ENGINE=InnoDB;

CREATE TABLE TipoProducto (
    codigoTipoProducto int NOT NULL AUTO_INCREMENT,
    descripcion varchar(45) NOT NULL,
    PRIMARY KEY (codigoTipoProducto),
    CONSTRAINT ck_TipoProducto_1 CHECK (CHAR_LENGTH(TRIM(descripcion)) > 0)
) ENGINE=InnoDB;

CREATE TABLE Productos (
    codigoProducto varchar(15) NOT NULL,
    descripcionProducto varchar(45) NOT NULL,
    precioUnitario decimal(10,2) NOT NULL DEFAULT 0,
    precioDocena decimal(10,2) NOT NULL DEFAULT 0,
    precioMayor decimal(10,2) NOT NULL DEFAULT 0,
    imagenProducto varchar(45) NOT NULL,
    existencia int NOT NULL DEFAULT 0,
    codigoTipoProducto int NOT NULL,
    codigoProveedor int NOT NULL,
    PRIMARY KEY (codigoProducto),
    CONSTRAINT FK_Productos_TipoProducto FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto) ON DELETE RESTRICT,
    CONSTRAINT FK_Productos_Proveedor FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE RESTRICT,
    CONSTRAINT ck_Productos_0 CHECK (CHAR_LENGTH(TRIM(codigoProducto)) > 0),
    CONSTRAINT ck_Productos_1 CHECK (CHAR_LENGTH(TRIM(descripcionProducto)) > 0),
    CONSTRAINT ck_Productos_2 CHECK (precioUnitario >= 0),
    CONSTRAINT ck_Productos_3 CHECK (precioDocena >= 0),
    CONSTRAINT ck_Productos_4 CHECK (precioMayor >= 0),
    CONSTRAINT ck_Productos_5 CHECK (existencia >= 0),
    CONSTRAINT ck_Productos_6 CHECK (codigoTipoProducto > 0),
    CONSTRAINT ck_Productos_7 CHECK (codigoProveedor > 0)
) ENGINE=InnoDB;

CREATE TABLE Empleados (
    codigoEmpleado int NOT NULL AUTO_INCREMENT,
    nombresEmpleado varchar(50) NOT NULL,
    apellidosEmpleado varchar(50) NOT NULL,
    sueldo decimal(10,2) NOT NULL,
    direccion varchar(150) NOT NULL,
    turno varchar(15) NOT NULL,
    codigoCargoEmpleado int NOT NULL,
    PRIMARY KEY (codigoEmpleado),
    CONSTRAINT FK_Empleados_CargoEmpleado FOREIGN KEY (codigoCargoEmpleado) REFERENCES CargoEmpleado(codigoCargoEmpleado) ON DELETE RESTRICT,
    CONSTRAINT ck_Empleados_1 CHECK (CHAR_LENGTH(TRIM(nombresEmpleado)) > 0),
    CONSTRAINT ck_Empleados_2 CHECK (CHAR_LENGTH(TRIM(apellidosEmpleado)) > 0),
    CONSTRAINT ck_Empleados_3 CHECK (sueldo >= 0),
    CONSTRAINT ck_Empleados_4 CHECK (CHAR_LENGTH(TRIM(direccion)) > 0),
    CONSTRAINT ck_Empleados_5 CHECK (CHAR_LENGTH(TRIM(turno)) > 0),
    CONSTRAINT ck_Empleados_6 CHECK (codigoCargoEmpleado > 0)
) ENGINE=InnoDB;

CREATE TABLE EmailProveedor (
    codigoEmailProveedor int NOT NULL AUTO_INCREMENT,
    emailProveedor varchar(50) NOT NULL,
    descripcion varchar(100) NOT NULL,
    codigoProveedor int NOT NULL,
    PRIMARY KEY (codigoEmailProveedor),
    CONSTRAINT FK_EmailProveedor_Proveedores FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE RESTRICT,
    CONSTRAINT ck_EmailProveedor_1 CHECK (CHAR_LENGTH(TRIM(emailProveedor)) > 0),
    CONSTRAINT ck_EmailProveedor_2 CHECK (CHAR_LENGTH(TRIM(descripcion)) > 0),
    CONSTRAINT ck_EmailProveedor_3 CHECK (codigoProveedor > 0)
) ENGINE=InnoDB;

CREATE TABLE TelefonoProveedor (
    codigoTelefonoProveedor int NOT NULL AUTO_INCREMENT,
    numeroPrincipal varchar(8) NOT NULL,
    numeroSecundario varchar(8) NOT NULL,
    observaciones varchar(45) NOT NULL,
    codigoProveedor int NOT NULL,
    PRIMARY KEY (codigoTelefonoProveedor),
    CONSTRAINT FK_TelefonoProveedor_Proveedores FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE RESTRICT,
    CONSTRAINT ck_TelefonoProveedor_1 CHECK (CHAR_LENGTH(TRIM(numeroPrincipal)) > 0),
    CONSTRAINT ck_TelefonoProveedor_2 CHECK (codigoProveedor > 0)
) ENGINE=InnoDB;

CREATE TABLE DetalleCompra (
    codigoDetalleCompra int NOT NULL AUTO_INCREMENT,
    costoUnitario decimal(10,2) NOT NULL,
    cantidad int NOT NULL,
    codigoProducto varchar(15) NOT NULL,
    numeroDocumento int NOT NULL,
    PRIMARY KEY (codigoDetalleCompra),
    CONSTRAINT FK_DetalleCompra_Productos FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) ON DELETE RESTRICT,
    CONSTRAINT FK_DetalleCompra_Compras FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento) ON DELETE RESTRICT,
    CONSTRAINT ck_DetalleCompra_1 CHECK (costoUnitario >= 0),
    CONSTRAINT ck_DetalleCompra_2 CHECK (cantidad > 0),
    CONSTRAINT ck_DetalleCompra_3 CHECK (CHAR_LENGTH(TRIM(codigoProducto)) > 0),
    CONSTRAINT ck_DetalleCompra_4 CHECK (numeroDocumento > 0)
) ENGINE=InnoDB;

CREATE TABLE Factura (
    numeroFactura int NOT NULL AUTO_INCREMENT,
    estado varchar(50) NOT NULL,
    totalFactura decimal(10,2) NOT NULL DEFAULT 0,
    fechaFactura date NOT NULL,
    codigoCliente int NOT NULL,
    codigoEmpleado int NOT NULL,
    PRIMARY KEY (numeroFactura),
    CONSTRAINT FK_Factura_Clientes FOREIGN KEY (codigoCliente) REFERENCES Clientes(codigoCliente) ON DELETE RESTRICT,
    CONSTRAINT FK_Factura_Empleados FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado) ON DELETE RESTRICT,
    CONSTRAINT ck_Factura_1 CHECK (CHAR_LENGTH(TRIM(estado)) > 0),
    CONSTRAINT ck_Factura_2 CHECK (totalFactura >= 0),
    CONSTRAINT ck_Factura_3 CHECK (CHAR_LENGTH(TRIM(fechaFactura)) > 0),
    CONSTRAINT ck_Factura_4 CHECK (codigoCliente > 0),
    CONSTRAINT ck_Factura_5 CHECK (codigoEmpleado > 0)
) ENGINE=InnoDB;

CREATE TABLE DetalleFactura (
    codigoDetalleFactura int NOT NULL AUTO_INCREMENT,
    precioUnitario decimal(10,2) NOT NULL DEFAULT 0,
    cantidad int NOT NULL,
    numeroFactura int NOT NULL,
    codigoProducto varchar(15) NOT NULL,
    PRIMARY KEY (codigoDetalleFactura),
    CONSTRAINT FK_DetalleFactura_Factura FOREIGN KEY (numeroFactura) REFERENCES Factura(numeroFactura) ON DELETE RESTRICT,
    CONSTRAINT FK_DetalleFactura FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) ON DELETE RESTRICT,
    CONSTRAINT ck_DetalleFactura_1 CHECK (precioUnitario >= 0),
    CONSTRAINT ck_DetalleFactura_2 CHECK (cantidad > 0),
    CONSTRAINT ck_DetalleFactura_3 CHECK (numeroFactura > 0),
    CONSTRAINT ck_DetalleFactura_4 CHECK (CHAR_LENGTH(TRIM(codigoProducto)) > 0)
) ENGINE=InnoDB;

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

CREATE TABLE SecuenciasProducto (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
) ENGINE=InnoDB;

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
CREATE PROCEDURE sp_agregarClientes(IN p_codigoCliente int, IN p_NITClientes varchar(10), IN p_nombresCliente varchar(50), IN p_apellidosCliente varchar(50), IN p_direccionCliente varchar(150), IN p_telefonoCliente varchar(8), IN p_correoCliente varchar(45))
BEGIN
		insert into Clientes(codigoCliente, NITClientes, nombresCliente, apellidosCliente, direccionCliente, telefonoCliente, correoCliente)
		values (p_codigoCliente, p_NITClientes, p_nombresCliente, p_apellidosCliente, p_direccionCliente, p_telefonoCliente, p_correoCliente);

    SELECT COALESCE(NULLIF(p_codigoCliente,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_codigoProveedor,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_codigoCargoEmpleado,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_codigoTipoProducto,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_numeroDocumento,0), LAST_INSERT_ID()) AS codigoGenerado;
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
    IF p_codigoProducto IS NULL OR TRIM(p_codigoProducto)='' THEN CALL sp_nuevoCodigoProducto(p_codigoProducto); END IF;

	insert into Productos(codigoProducto,descripcionProducto,imagenProducto,codigoTipoProducto,codigoProveedor)
    values (p_codigoProducto,p_descripcionProducto,p_imagenProducto,p_codigoTipoProducto,p_codigoProveedor);

    SELECT p_codigoProducto AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_codigoEmpleado,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_codigoEmailProveedor,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_codigoTelefonoProveedor,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_numeroFactura,0), LAST_INSERT_ID()) AS codigoGenerado;
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

    SELECT COALESCE(NULLIF(p_codigoDetalleFactura,0), LAST_INSERT_ID()) AS codigoGenerado;
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


    SELECT COALESCE(NULLIF(p_codigoDc,0), LAST_INSERT_ID()) AS codigoGenerado;
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
        IF p_referencia > 0 THEN
        INSERT INTO MovimientosInventario(codigoProducto, variacion, origen, referencia)
        VALUES(p_producto, p_delta, p_origen, p_referencia);
        END IF;
    END IF;
END $$

CREATE TRIGGER tr_compra_before_insert BEFORE INSERT ON DetalleCompra FOR EACH ROW
BEGIN
CALL sp_movimientoInventario(NEW.codigoProducto, 1 * NEW.cantidad, 'compra:alta', 0);
END $$

CREATE TRIGGER tr_compra_after_insert AFTER INSERT ON DetalleCompra FOR EACH ROW
BEGIN
INSERT INTO MovimientosInventario(codigoProducto,variacion,origen,referencia) VALUES(NEW.codigoProducto,NEW.cantidad,'compra:alta',NEW.codigoDetalleCompra);

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
CALL sp_movimientoInventario(NEW.codigoProducto, -1 * NEW.cantidad, 'venta:alta', 0);
END $$

CREATE TRIGGER tr_venta_after_insert AFTER INSERT ON DetalleFactura FOR EACH ROW
BEGIN
INSERT INTO MovimientosInventario(codigoProducto,variacion,origen,referencia) VALUES(NEW.codigoProducto,-NEW.cantidad,'venta:alta',NEW.codigoDetalleFactura);

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

CREATE VIEW vw_Productos AS SELECT p.codigoProducto, p.descripcionProducto, t.descripcion AS Descripcion, v.nombresProveedor FROM Productos p JOIN TipoProducto t ON p.codigoTipoProducto=t.codigoTipoProducto JOIN Proveedores v ON p.codigoProveedor=v.codigoProveedor;

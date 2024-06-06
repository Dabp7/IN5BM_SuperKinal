drop database if exists DBKinalExpress2020527; 	

create database DBKinalExpress2020527;

use DBKinalExpress2020527;

set global time_zone = '-6:00';

create table Clientes(
	codigoCliente int not null,
	NITClientes varchar(10),
	nombresCliente varchar(50),
	apellidosCliente varchar(50),
	direccionCliente varchar(150),
	telefonoCliente varchar(8),
	correoCliente varchar(45),
    primary key PK_codigoCliente(codigoCliente)
);

 create table Proveedores(
    codigoProveedor int not null,
    NITProveedor varchar(10),
    nombresProveedor varchar(60),
    apellidosProveedor varchar(60),
    direccionProveedor varchar(150),
    razonSocial varchar(60),
    contactoPrincipal varchar(100),
    paginaWeb varchar(50),
    primary key PK_codigoProveedor(codigoProveedor)
 );
 
 create table Compras(
	numeroDocumento int not null,
    fechaDocumento date,
    descripcion varchar(60),
    totalDocumento decimal(10,2),
    primary key PK_numeroDocumento(numeroDocumento)
 );
 
create table CargoEmpleado(
	codigoCargoEmpleado int not null,
    nombreCargo varchar(45),
    descripcionCargo varchar(45),
    primary key PK_CargoEmpleado(codigoCargoEmpleado)
);
 
 create table TipoProducto(
	codigoTipoProducto int not null,
    descripcion varchar(45),
    primary key PK_codigoTipoProducto(codigoTipoProducto)
 );
 
create table Productos(
	codigoProducto varchar(15) not null,
    descripcionProducto varchar(45),
    precioUnitario decimal(10,2) default 0.0,
    precioDocena decimal(10,2) default 0.0,
    precioMayor decimal(10,2) default 0.0,
    imagenProducto varchar(45),
    existencia int,
    codigoTipoProducto int,
    codigoProveedor int,
    primary key PK_Productos(codigoProducto),
    constraint FK_Productos_TipoProducto foreign key Productos(codigoTipoProducto)
		references TipoProducto(codigoTipoProducto) on delete cascade,
	constraint FK_Productos_Proveedor foreign key Productos(codigoProveedor)
		references Proveedores(codigoProveedor) on delete cascade
);

create table Empleados(
	codigoEmpleado int not null,
    nombresEmpleado varchar(50),
    apellidosEmpleado varchar(50),
    sueldo decimal(10,2),
    direccion varchar(150),
    turno varchar(15),
    codigoCargoEmpleado int,
    primary key PK_Empleados(codigoEmpleado),
    constraint FK_Empleados_CargoEmpleado foreign key Empleados(codigoCargoEmpleado)
		references CargoEmpleado(codigoCargoEmpleado) on delete cascade
);

create table EmailProveedor(
	codigoEmailProveedor int not null,
    emailProveedor varchar(50),
    descripcion varchar(100),
    codigoProveedor int,
    primary key PK_EmailProveedor(codigoEmailProveedor),
    constraint FK_EmailProveedor_Proveedores foreign key EmailProveedor(codigoProveedor) 
		references Proveedores(codigoProveedor) on delete cascade
);

create table TelefonoProveedor(
	codigoTelefonoProveedor int not null,
    numeroPrincipal varchar(8),
    numeroSecundario varchar(8),
    observaciones varchar(45),
    codigoProveedor int,
    primary key PK_TelefonoProveedor(codigoTelefonoProveedor) ,
    constraint FK_TelefonoProveedor_Proveedores foreign key TelefonoProveedor(codigoProveedor)
		references Proveedores(codigoProveedor) on delete cascade
);


create table DetalleCompra(
	codigoDetalleCompra int not null,
    costoUnitario decimal(10,2),
    cantidad int,
    codigoProducto varchar(15),
    numeroDocumento int,
    primary key PK_DetalleCompra(codigoDetalleCompra),
    constraint FK_DetalleCompra_Productos foreign key DetalleCompra(codigoProducto)
		references Productos(codigoProducto) on delete cascade,
	constraint FK_DetalleCompra_Compras foreign key DetalleCompra(numeroDocumento)
		references	Compras(numeroDocumento)on delete cascade
);

create table Factura(
	numeroFactura int not null,
    estado varchar(50),
    totalFactura decimal(10,2),
    fechaFactura varchar(45),
    codigoCliente int,
    codigoEmpleado int,
    primary key PK_Factura(numeroFactura),
    constraint FK_Factura_Clientes foreign key Factura(codigoCliente)
		references Clientes(codigoCliente) on delete cascade,
	constraint FK_Factura_Empleados foreign key Factura(codigoEmpleado)
		references Empleados(codigoEmpleado) on delete cascade
);

create table DetalleFactura(
	codigoDetalleFactura int not null,
    precioUnitario decimal(10,2),
    cantidad int,
    numeroFactura int,
    codigoProducto varchar(15),
    primary key PK_DetalleFactura(codigoDetalleFactura),
    constraint FK_DetalleFactura_Factura foreign key DetalleFactura(numeroFactura)
		references Factura(numeroFactura) on delete cascade,
	constraint FK_DetalleFactura foreign key DetalleFactura(codigoProducto)
		references Productos(codigoProducto)on delete cascade
);
 


-- ----------------------------------------------------------------------------------------------------------------
delimiter $$
	create procedure sp_agregarClientes(in codigoCliente int, in NITClientes varchar(10), in nombresCliente varchar(50), in apellidosCliente varchar(50), in direccionCliente varchar(150), in telefonoCliente varchar(8), in correoCliente varchar(45))
	begin
		insert into Clientes(codigoCliente, NITClientes, nombresCliente, apellidosCliente, direccionCliente, telefonoCliente, correoCliente)
		values (codigoCliente, NITClientes, nombresCliente, apellidosCliente, direccionCliente, telefonoCliente, correoCliente);
	end $$
delimiter ;

call sp_agregarClientes(1,'502452698','Diego','Bercian','12 Calle y 6 Avenida','3441421','dbercian');
call sp_agregarClientes(5,'452021456','Samuel','Pérez','13 Calle y 5 Avenida','2595631','sPerez');


delimiter $$
	create procedure sp_listarClientes()
	begin
		select * from Clientes;
	end $$
delimiter ;


call sp_listarClientes;


delimiter $$
	create procedure sp_buscarClientes(in codigoCliente int)
	begin
		select * from Clientes where codigoCliente = codigoCliente;
	end $$
delimiter ;


call sp_buscarClientes(1);
 
delimiter $$
create procedure sp_actualizarClientes(in codigoCliente int, in NITClientes varchar(10), in nombresCliente varchar(50), in apellidosCliente varchar(50), in direccionCliente varchar(150), telefonoCliente varchar(8), in correoCliente varchar(45))
begin
	update Clientes
    set
		Clientes.NITClientes = NITClientes,
        Clientes.nombresCliente = nombresCliente,
        Clientes.apellidosCliente = apellidosCliente,
        Clientes.direccionCliente = direccionCliente,
        Clientes.telefonoCliente = telefonoCliente,
        Clientes.correoCliente = correoCliente
	where
		Clientes.codigoCliente = codigoCliente;
end $$
delimiter ;


call sp_actualizarClientes(2,'5434534','Orlando','Gomez','11 Calle y 10 Avenida','12345678','ogomez');
    
delimiter $$
	create procedure sp_eliminarClientes(in codigoCliente int)
	begin
		delete from Clientes where Clientes.codigoCliente = codigoCliente;
	end $$
delimiter ;
 
call sp_eliminarClientes(2);


-- -----------------------------------------------------------------------

delimiter $$
	create procedure sp_agregarProveedores(in codigoProveedor int, in NITProveedor varchar(10), in nombresProveedor varchar(60), in apellidosProveedor varchar(60), in direccionProveedor varchar(150), in razonSocial varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
	begin
		insert into Proveedores(codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb)
		values (codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb);
	end $$
delimiter ;

call sp_agregarProveedores(1, '5485', 'David', 'Arevalo', '6 calle 4-76 zona 7', 'Arcos Dorados, C.A.', 'Mateo', 'arcosdorados.com');
call sp_agregarProveedores(2, '5285', 'Adrián', 'Figueroa', '8 calle 4-43 zona 12', 'Carlos Fernández, E. I. R. L.', 'Luis', 'EIRL.com');
call sp_agregarProveedores(3, '5280', 'Gustavo', 'Verdezoto', '5 calle 4-56 zona 2	', 'Importaciones PFV.', 'Byron', 'PFV.com');

delimiter $$
	create procedure sp_listarProveedores()
	begin
		select * from Proveedores;
	end $$
delimiter ;


call sp_listarProveedores;


delimiter $$
	create procedure sp_buscarProveedores(in codigoProveedor int)
	begin
		select * from Proveedores where codigoProveedor = codigoProveedor;
	end $$
delimiter ;


call sp_buscarProveedores(1);
 
delimiter $$
create procedure sp_actualizarProveedores(in codigoProveedor int, in NITProveedor varchar(10), in nombresProveedor varchar(60), in apellidosProveedor varchar(60), in direccionProveedor varchar(150), in razonSocial varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
begin
	update Proveedores
    set
		Proveedores.NITProveedor = NITProveedor,
        Proveedores.nombresProveedor = nombresProveedor,
        Proveedores.apellidosProveedor = apellidosProveedor,
        Proveedores.direccionProveedor = direccionProveedor,
        Proveedores.razonSocial = razonSocial,
        Proveedores.contactoPrincipal = contactoPrincipal,
        Proveedores.paginaWeb = paginaWeb
	where
		Proveedores.codigoProveedor = codigoProveedor;
end $$
delimiter ;


call sp_actualizarProveedores(1,'5434534','Orlando','Gomez','11 Calle y 10 Avenida','12345678', 'Importaciones PFV.','ogomez');
    
    
delimiter $$
	create procedure sp_eliminarProveedores(in codigoProveedor int)
	begin
		delete from Proveedores where Proveedores.codigoProveedor = codigoProveedor;
	end $$
delimiter ;
 
call sp_eliminarProveedores(1);

-- ------------------------------------------------------------------------

delimiter $$
	create procedure sp_agregarCargoEmpleado(in codigoCargoEmpleado int, in nombreCargo varchar(45), in descripcionCargo varchar(45))
	begin
		insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
		values (codigoCargoEmpleado, nombreCargo, descripcionCargo);
	end $$
delimiter ;

call sp_agregarCargoEmpleado(1, 'Caja', 'Encargado de atender');
call sp_agregarCargoEmpleado(2, 'Jefe', 'Supervisa el trabajo de los demas');
call sp_agregarCargoEmpleado(3, 'Limpieza', 'Limpia todo el supermercado');

 
delimiter $$
	create procedure sp_listarCargoEmpleado()
	begin
		select * from CargoEmpleado;
	end $$
delimiter ;


call sp_listarCargoEmpleado;


delimiter $$
	create procedure sp_buscarCargoEmpleado(in codigoCargoEmpleado int)
	begin
		select * from CargoEmpleado where codigoCargoEmpleado = codigoCargoEmpleado;
	end $$
delimiter ;


call sp_buscarCargoEmpleado(1);
 
delimiter $$
create procedure sp_actualizarCargoEmpleado(in codigoCargoEmpleado int, in nombreCargo varchar(45), in descripcionCargo varchar(45))
begin
	update CargoEmpleado
    set
		CargoEmpleado.nombreCargo = nombreCargo,
        CargoEmpleado.descripcionCargo = descripcionCargo
	where
		CargoEmpleado.codigoCargoEmpleado = codigoCargoEmpleado;
end $$
delimiter ;


call sp_actualizarCargoEmpleado(1, 'Atención al cliente', 'Encargado de la interacción');
    
    
delimiter $$
	create procedure sp_eliminarCargoEmpleado(in codigoCargoEmpleado int)
	begin
		delete from CargoEmpleado where CargoEmpleado.codigoCargoEmpleado = codigoCargoEmpleado;
	end $$
delimiter ;
 
call sp_eliminarCargoEmpleado(4);

-- -------------------------------------------------------------------------------------------------------


delimiter $$
	create procedure sp_agregarTipoProducto(in codigoTipoProducto int, in descripcion varchar(45))
	begin
		insert into TipoProducto(codigoTipoProducto, descripcion)
		values (codigoTipoProducto, descripcion);
	end $$
delimiter ;

call sp_agregarTipoProducto(1, 'Carnes');
call sp_agregarTipoProducto(2, 'Bebidas');
call sp_agregarTipoProducto(3, 'Comida rápida');

 
delimiter $$
	create procedure sp_listarTipoProducto()
	begin
		select * from TipoProducto;
	end $$
delimiter ;


call sp_listarTipoProducto;


delimiter $$
	create procedure sp_buscarTipoProducto(in codigoTipoProducto int)
	begin
		select * from TipoProducto where codigoTipoProducto = codigoTipoProducto;
	end $$
delimiter ;


call sp_buscarTipoProducto(1);
 
delimiter $$
create procedure sp_actualizarTipoProducto(in codigoTipoProducto int, in descripcion varchar(45))
begin
	update TipoProducto
    set
		TipoProducto.descripcion = descripcion
	where
		TipoProducto.codigoTipoProducto = codigoTipoProducto;
end $$
delimiter ;


call sp_actualizarTipoProducto(1, 'Jugos');
    
    
delimiter $$
	create procedure sp_eliminarTipoProducto(in codigoTipoProducto int)
	begin
		delete from TipoProducto where TipoProducto.codigoTipoProducto = codigoTipoProducto;
	end $$
delimiter ;
 
call sp_eliminarTipoProducto(3);

-- -----------------------------------------------------------------
delimiter $$
create procedure sp_agregarCompras(in numeroDocumento int,in fechaDocumento date, in descripcion varchar(60))
begin
	insert into Compras(numeroDocumento, fechaDocumento,descripcion)
    values (numeroDocumento, fechaDocumento,descripcion);
end $$
delimiter ;
 
call sp_agregarCompras(1, '2024-03-19', 'Pollo');
call sp_agregarCompras(2, '2023-11-05', 'Fideos');

 
delimiter $$
create procedure sp_listarCompras()
begin 
	select * from Compras; 
end $$
delimiter ;
 
call sp_listarCompras();
 
delimiter $$
create procedure sp_buscarCompras(in numeroDocumento int)
begin
	select * from Compras where Compras.numeroDocumento = numeroDocumento;
end $$
delimiter ;
 
call sp_buscarCompras(1);
 
delimiter $$
create procedure sp_actualizarCompras(in numeroDocumento int,in fechaDocumento date, in descripcion varchar(60))
begin
	update Compras
    set
		Compras.fechaDocumento = fechaDocumento,
		Compras.descripcion = descripcion
	where
		Compras.numeroDocumento = numeroDocumento;
end $$
delimiter ;
 
call sp_actualizarCompras(1, '2024-04-23', 'Helado');
 
delimiter $$
create procedure sp_eliminarCompras(in numeroDocumento int)
begin 
	delete from Compras where Compras.numeroDocumento = numeroDocumento;
end $$
delimiter ;
 
call sp_eliminarCompras(1);

-- insertar total compras
delimiter $$
create procedure sp_actualizarComprasTotal(in numDoc int,in total decimal(10,2))
begin
	update Compras 
	set 
		Compras.totalDocumento=total
    where
		Compras.numeroDocumento=numDoc;
end $$
delimiter ;

-- -------------------------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarProductos(in codigoProducto varchar(15),in descripcionProducto varchar(45), in imagenProducto varchar(45), in codigoTipoProducto int(11), in codigoProveedor int)
begin
	insert into Productos(codigoProducto,descripcionProducto,imagenProducto,codigoTipoProducto,codigoProveedor)
    values (codigoProducto,descripcionProducto,imagenProducto,codigoTipoProducto,codigoProveedor);
end $$
delimiter ;
 
call sp_agregarProductos('abc','Alta Calidad','imagen.PNG',1,2);
call sp_agregarProductos('xyz','Alta Calidad','imagen2.PNG',1,2);
 
delimiter $$
create procedure sp_listarProductos()
begin 
	select * from Productos; 
end $$
delimiter ;
 
call sp_listarProductos();
 
delimiter $$
create procedure sp_buscarProductos(in codigoProducto varchar(15))
begin
	select * from Productos where productos.codigoProducto = codigoProducto;
end $$
delimiter ;
 
call sp_buscarProductos('jjjj');
 
delimiter $$
create procedure sp_actualizarProductos(in codigoProducto varchar(15),in descripcionProducto varchar(45), in imagenProducto varchar(45), in codigoTipoProducto int(11), in codigoProveedor int)
begin
	update productos
    set
		productos.descripcionProducto = descripcionProducto,
        productos.imagenProducto = imagenProducto,
        productos.codigoTipoProducto = codigoTipoProducto,
        productos.codigoProveedor = codigoProveedor
	where
		productos.codigoProducto = codigoProducto;
end $$
delimiter ;
 
call sp_actualizarProductos('abc','Alta','JPG',2,2);
 
delimiter $$
create procedure sp_eliminarProductos(in codigoProducto varchar(45))
begin 
	delete from Productos where productos.codigoProducto = codigoProducto;
end $$
delimiter ;
 
call sp_eliminarProductos('dfg');


delimiter $$
create procedure sp_actualizarPreciosProductos(in codProd varchar(15),in precUnit decimal(10,2),in precDoc decimal(10,5), in precMay decimal(10,2), in exist int)
begin
	update Productos 
	set 
		Productos.precioUnitario=precUnit,
		Productos.precioDocena=precDoc,
        Productos.precioMayor=precMay,
        Productos.existencia=exist
    where
		Productos.codigoProducto=codProd;
end $$
delimiter ;
-- -----------------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarEmpleados(in codigoEmpleado int, in nombresEmpleado varchar(50), in apellidosEmpleado varchar(50), in sueldo decimal(10,2), in direccion varchar(150), in turno varchar(15), in codigoCargoEmpleado int)
begin 
	insert into Empleados(codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado)
    values (codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado);
end $$
delimiter ;

call sp_agregarEmpleados(1,'Pedro','Gomez','10.0','10 Calle y 10 Avenida','M',2);
call sp_agregarEmpleados(2,'Juan','Isaza','10.0','5 Calle y 7 Avenida','V',1);


delimiter $$
create procedure sp_listarEmpleados()
begin 
	select * from Empleados;
end $$
delimiter ;

call sp_listarEmpleados();

delimiter $$
create procedure sp_buscarEmpleados(in codigoEmpleado int)
begin 
	select * from Empleados where Empleados.codigoEmpleado = codigoEmpleado;
end $$
delimiter ;

call sp_buscarEmpleados(1);

delimiter $$
create procedure sp_actualizarEmpleados(in codigoEmpleado int, in nombresEmpleado varchar(50), in apellidosEmpleado varchar(50), in sueldo decimal(10,2), in direccion varchar(150), in turno varchar(15), in codigoCargoEmpleado int)
begin 
	update Empleados
    set	
		Empleados.nombresEmpleado = nombresEmpleado,
		Empleados.apellidosEmpleado = apellidosEmpleado,
        Empleados.sueldo = sueldo,
        Empleados.direccion = direccion,
        Empleados.turno = Empleados.turno
	where
		codigoCargoEmpleado = Empleados.codigoCargoEmpleado;
end $$
delimiter ;

call sp_actualizarEmpleados(1,'Isaac','Sisa','25.0','2 Calle y 8 Avenida','V',2);

delimiter $$
create procedure sp_eliminarEmpleados(in codigoEmpleado int)
begin 
	delete from Empleados where Empleados.codigoEmpleado = codigoEmpleado;
end $$
delimiter ;

call sp_eliminarEmpleados(1);


-- ----------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarEmailProveedor(in codigoEmailProveedor int, in emailProveedor varchar(50), in descripcion varchar(100), in codigoProveedor int)
begin
	insert into EmailProveedor(codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor)
    values(codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor);
end $$
delimiter ;

call sp_agregarEmailProveedor(1,'dbercianc@kinal','Guatemala',3);
call sp_agregarEmailProveedor(2,'iVerdezoto@kinal','Guatemala',2);
call sp_agregarEmailProveedor(3,'mCastro@kinal','Guatemala',2);

delimiter $$
create procedure sp_listarEmailProveedor()
begin
	select * from EmailProveedor;
end $$
delimiter ;

call sp_listarEmailProveedor;

delimiter $$
create procedure sp_buscarEmailProveedor(in codigoEmailProveedor int)
begin
	select*from EmailProveedor where EmailProveedor.codigoEmailProveedor = codigoEmailProveedor;
end $$
delimiter ;

call sp_buscarEmailProveedor(1);

delimiter $$
create procedure sp_actualizarEmailProveedor(in codigoEmailProveedor int, in emailProveedor varchar(50), in descripcion varchar(100), in codigoProveedor int)
begin
	update EmailProveedor
	set
		EmailProveedor.emailProveedor = emailProveedor,
        EmailProveedor.descripcion = descripcion,
        EmailProveedor.codigoProveedor = codigoProveedor
	where
		EmailProveedor.codigoEmailProveedor = codigoEmailProveedor;
end $$
delimiter ;

call sp_actualizarEmailProveedor(1,'iSisa@gmail.com','Kinal',2);

delimiter $$
create procedure sp_eliminarEmailProveedor(in codigoEmailProveedor int)
begin
	delete from EmailProveedor where EmailProveedor.codigoEmailProveedor = codigoEmailProveedor;
end $$
delimiter ;

call sp_eliminarEmailProveedor(1);

-- -------------------------------------------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarTelefonoProveedor(in codigoTelefonoProveedor int, in numeroPrincipal varchar(8), in numeroSecundario varchar(8), in observaciones varchar(45), in codigoProveedor int)
begin
	insert into TelefonoProveedor(codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
    values (codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor);
end $$
delimiter ;

call sp_agregarTelefonoProveedor(1,'12345678','87654321','Numeros de contacto',2);

delimiter $$
create procedure sp_listarTelefonoProveedor()
begin
	select * from TelefonoProveedor;
end $$
delimiter ;

call sp_listarTelefonoProveedor;

delimiter $$
create procedure sp_buscarTelefonoProveedor(in codigoTelefonoProveedor int)
begin
	select * from TelefonoProveedor where TelefonoProveedor.codigoTelefonoProveedor = codigoTelefonoProveedor;
end $$
delimiter ;

call sp_buscarTelefonoProveedor(1);

delimiter $$
create procedure sp_actualizarTelefonoProveedor(in codigoTelefonoProveedor int, in numeroPrincipal varchar(8), in numeroSecundario varchar(8), in observaciones varchar(45), in codigoProveedor int)
begin
	update TelefonoProveedor
    set
		TelefonoProveedor.numeroPrincipal = numeroPrincipal,
        TelefonoProveedor.numeroSecundario = numeroSecundario, 
        TelefonoProveedor.observaciones = observaciones,
        TelefonoProveedor.codigoProveedor = codigoProveedor
	where 
		TelefonoProveedor.codigoTelefonoProveedor = codigoTelefonoProveedor;
end $$
delimiter ;

call sp_actualizarTelefonoProveedor(1,'09876543','12345678','Numeros de referencia',2);

delimiter $$
create procedure sp_eliminarTelefonoProveedor(in codigoTelefonoProveedor int)
begin
	delete from TelefonoProveedor where TelefonoProveedor.codigoTelefonoProveedor = codigoTelefonoProveedor;
end $$
delimiter ;

call sp_eliminarTelefonoProveedor(1);

-- ------------------------------------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarFactura(in numeroFactura int,in estado varchar(50), in fechaFactura varchar(45), in codigoCliente int, in codigoEmpleado int)
begin
	insert into Factura(numeroFactura,estado,fechaFactura,codigoCliente,codigoEmpleado)
    values (numeroFactura,estado,fechaFactura,codigoCliente,codigoEmpleado);
end $$
delimiter ;
 
call sp_agregarFactura(1, 'activo','20-05-2024' ,1,2);
call sp_agregarFactura(2, 'expirado','28-05-2022',1,2);
 
delimiter $$
create procedure sp_listarFacturas()
begin 
	select * from Factura; 
end $$
delimiter ;
 
call sp_listarFacturas();
 
delimiter $$
create procedure sp_buscarFacturas(in numeroFactura int)
begin
	select * from Factura where Factura.numeroFactura = numeroFactura;
end $$
delimiter ;
 
call sp_buscarFacturas(1);
 
delimiter $$
create procedure sp_actualizarFacturas(in numeroFactura int,in estado varchar(50), in fechaFactura varchar(45), in codigoCliente int, in codigoEmpleado int)
begin
	update Factura
    set
		Factura.estado = estado,
        Factura.fechaFactura = fechaFactura,
        Factura.codigoCliente = codigoCliente,
        Factura.codigoEmpleado = codigoEmpleado
	where
		Factura.numeroFactura = numeroFactura;
end $$
delimiter ;
 
call sp_actualizarFacturas(1, 'expirado','04-04-2023', 1,2);

 
delimiter $$
create procedure sp_eliminarFacturas(in numeroFactura int)
begin 
	delete from Factura where Factura.numeroFactura = numeroFactura;
end $$
delimiter ;
 
call sp_eliminarFacturas(2);

delimiter $$
create procedure sp_actualizarFacturaTotal(in numFac int,in total decimal(10,2))
begin
	update Factura 
	set 
		Factura.totalFactura=total
    where
		Factura.numeroFactura=numFac;
end $$
delimiter ;


-- ------------------------------------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarDetalleFactura(in codigoDetalleFactura int,in cantidad int, in numeroFactura int, in codigoProducto varchar(15))
begin
	insert into DetalleFactura(codigoDetalleFactura, cantidad, numeroFactura, codigoProducto)
    values (codigoDetalleFactura, cantidad, numeroFactura, codigoProducto);
end $$
delimiter ;
 
call sp_agregarDetalleFactura(1, 5,1 ,'abc');
call sp_agregarDetalleFactura(2, 5,1 ,'xyz');

 
delimiter $$
create procedure sp_listarDetalleFactura()
begin 
	select * from DetalleFactura; 
end $$
delimiter ;
 
call sp_listarDetalleFactura();
 
delimiter $$
create procedure sp_buscarDetalleFactura(in codigoDetalleFactura int)
begin
	select * from DetalleFactura where DetalleFactura.codigoDetalleFactura = codigoDetalleFactura;
end $$
delimiter ;
 
call sp_buscarDetalleFactura(1);
 
delimiter $$
create procedure sp_ActualizarDetalleFactura(in codigoDetalleFactura int, in precioUnitario decimal(10,2),in cantidad int, in numeroFactura int, in codigoProducto varchar(15))
begin
	update DetalleFactura
    set
		DetalleFactura.precioUnitario = precioUnitario,
		DetalleFactura.cantidad = cantidad,
        DetalleFactura.numeroFactura = numeroFactura,
        DetalleFactura.codigoProducto = codigoProducto
	where
		DetalleFactura.codigoDetalleFactura = codigoDetalleFactura;
end $$
delimiter ;
 
call sp_ActualizarDetalleFactura(1, 10.0, 3, 1, 'abc');

 
delimiter $$
create procedure sp_eliminarDetalleFactura(in codigoDetalleFactura int)
begin 
	delete from DetalleFactura where DetalleFactura.codigoDetalleFactura = codigoDetalleFactura;
end $$
delimiter ;
 
call sp_eliminarDetalleFactura(2);



-- ------------------------------------------------------------------------------------------------------



delimiter $$
create procedure sp_agregarDetalleCompra(in codigoDc int, in costo decimal(10,2), in cant int, in codProd varchar(15) , in numDoc int)
begin
	insert into DetalleCompra (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    values(codigoDc, costo,cant, codProd, numDoc);
    
end $$
delimiter ;

call sp_agregarDetalleCompra(1,1.00, 1, "abc", 2);

-- listar DetalleCompra
delimiter $$
create procedure sp_listarDetalleCompra()
begin
	select
	DetalleCompra.codigoDetalleCompra,
    DetalleCompra.costoUnitario,
    DetalleCompra.cantidad,
    DetalleCompra.codigoProducto,
    DetalleCompra.numeroDocumento
    from DetalleCompra ;
end $$
delimiter ;


-- buscar DetalleCompra
delimiter $$
create procedure sp_buscarDetalleCompra(in codDetCompra int)
begin
	select 
    DetalleCompra.codigoDetalleCompra,
    DetalleCompra.costoUnitario,
    DetalleCompra.cantidad,
    DetalleCompra.codigoProducto,
    DetalleCompra.numeroDocumento
    from DetalleCompra 
    where DetalleCompra.codigoDetalleCompra=codDetCompra;
end $$
delimiter ;

-- eliminar DetalleCompra
delimiter $$
create procedure sp_eliminarDetalleCompra(in codProd varchar(15))
begin
	delete from DetalleCompra 
    where DetalleCompra.codigoDetalleCompra=codProd;
end $$
delimiter ;

-- actualizar DetalleCompra
delimiter $$
create procedure sp_actualizarDetalleCompra(in codDetCompra int, in precUnit decimal(10,2), in cant int, in codProd varchar(15) , in numDoc int )
begin
	update DetalleCompra 
	set 
		DetalleCompra.costoUnitario=precUnit,
		DetalleCompra.cantidad=cant,
		DetalleCompra.codigoProducto=codProd,
		DetalleCompra.numeroDocumento=numDoc
    where
		DetalleCompra.codigoDetalleCompra=codDetCompra;
end $$
delimiter ;

-- ------------------------------------------------------------------

-- traer el precio unitario

delimiter //
create function fn_TraerPrecioUnitario(codProd varchar(15)) returns decimal(10,2)
deterministic
begin
	declare precio decimal(10,2);
	set precio= (select DetalleCompra.costoUnitario from DetalleCompra
    where DetalleCompra.codigoProducto=codProd);
	return precio;
end //

delimiter ;


-- Precios Detalle factura
-- insertar Precios Detalle factura
delimiter //
create trigger tr_insertarPreciosDetalleFactura_Before_Insert
before insert on DetalleFactura
for each row
	begin
        		
        set new.precioUnitario= (select precioUnitario from Productos
		where Productos.codigoProducto=new.codigoProducto);
        
	end //
delimiter ;

-- actualizar DetalleFactura
delimiter $$
create procedure sp_actualizarPrecioDetalleFactura(in codProd varchar(15), in precUnit decimal(10,2) )
begin
	update DetalleFactura 
	set 
		DetalleFactura.precioUnitario=precUnit
    where
		DetalleFactura.codigoProducto=codProd;
end $$
delimiter ;

-- actualizar Precios Detalle factura
delimiter //
create trigger tr_actualizarPreciosDetalleFactura_after_update
after update on Productos
for each row
	begin
		call sp_actualizarPrecioDetalleFactura(new.codigoProducto,
        (select new.precioUnitario from Productos where Productos.codigoProducto=new.codigoProducto));
        
	end //
delimiter ;


-- insertar precios en Productos
delimiter //
create trigger tr_insertarPreciosProductos_after_Insert
after insert on DetalleCompra
for each row
	begin
    call sp_actualizarPreciosProductos(new.codigoProducto, 
									(fn_TraerPrecioUnitario(new.codigoProducto)+(fn_TraerPrecioUnitario(new.codigoProducto)*0.40)),
									(fn_TraerPrecioUnitario(new.codigoProducto)+(fn_TraerPrecioUnitario(new.codigoProducto)*0.35)),
                                    (fn_TraerPrecioUnitario(new.codigoProducto)+(fn_TraerPrecioUnitario(new.codigoProducto)*0.25)),
                                    new.cantidad);
                                    
	end //
delimiter ;

-- actualizar precios en Productos
delimiter //
create trigger tr_actualizarPreciosProductos_after_update
after update on DetalleCompra
for each row
	begin
    call sp_actualizarPreciosProductos(new.codigoProducto, 
									(fn_TraerPrecioUnitario(new.codigoProducto)+(fn_TraerPrecioUnitario(new.codigoProducto)*0.40)),
									(fn_TraerPrecioUnitario(new.codigoProducto)+(fn_TraerPrecioUnitario(new.codigoProducto)*0.35)),
                                    (fn_TraerPrecioUnitario(new.codigoProducto)+(fn_TraerPrecioUnitario(new.codigoProducto)*0.25)),
                                    new.cantidad);
                                    
	end //
delimiter ;

-- eliminar precios en Productos
delimiter //
create trigger tr_eliminarPreciosProductos_after_delete
after delete on DetalleCompra
for each row
	begin
    call sp_actualizarPreciosProductos(old.codigoProducto, 0,0,0,0);
                                    
	end //
delimiter ;


-- insertar total compra
delimiter //
create trigger tr_insertarTotalCompra_after_Insert
after insert on DetalleCompra
for each row
	begin
    declare total decimal(10,2);
    
    set total=((select sum(costoUnitario*cantidad) from DetalleCompra where DetalleCompra.numeroDocumento=new.numeroDocumento));
    
    call sp_actualizarComprasTotal(new.numeroDocumento, total);
                                    
	end //
delimiter ;

-- actualizar total compra
delimiter //
create trigger tr_actualizarTotalCompra_after_update
after update on DetalleCompra
for each row
	begin
    declare total decimal(10,2);
    
    set total=((select sum(new.costoUnitario*new.cantidad) from DetalleCompra where DetalleCompra.numeroDocumento=new.numeroDocumento));
    
    call sp_actualizarComprasTotal(new.numeroDocumento, total);
                                    
	end //
delimiter ;

-- total compra
delimiter //
create function fn_TotalCompra(numDocumento int) returns decimal(10,2)
deterministic
begin
    declare sumatoria decimal(10,2);
    
    set sumatoria = (select sum(cantidad*costoUnitario) from DetalleCompra 
					where numeroDocumento=numDocumento) ;
    return sumatoria;
end //
delimiter ;

-- eliminar total compra
delimiter //
create trigger tr_eliminarTotalCompra_after_delete
after delete on DetalleCompra
for each row
	begin
    declare total decimal(10,2);
    
    set total=fn_TotalCompra(old.numeroDocumento);
    
    call sp_actualizarComprasTotal(old.numeroDocumento, total);
                                    
	end //
delimiter ;


-- insertar total factura
delimiter //
create trigger tr_insertarTotalFactura_after_Insert
after insert on DetalleFactura
for each row
	begin
    declare total decimal(10,2);
    
    set total=((select sum(precioUnitario*cantidad) from DetalleFactura where DetalleFactura.numeroFactura=new.numeroFactura ));
    
    call sp_actualizarFacturaTotal(new.numeroFactura, total);
                                    
	end //
delimiter ;

-- actualizar total factura
delimiter //
create trigger tr_actualizarTotalFactura_after_update
after update on DetalleFactura
for each row
	begin
    declare total decimal(10,2);
    
    set total=((select sum(new.precioUnitario*cantidad) from DetalleFactura where DetalleFactura.numeroFactura=new.numeroFactura ));
    
    call sp_actualizarFacturaTotal(new.numeroFactura, total);
                                    
	end //
delimiter ;


-- total factura
delimiter //
create function fn_TotalFactura(numFact int) returns decimal(10,2)
deterministic
begin
    declare sumatoria decimal(10,2);
    
    set sumatoria = (select sum(precioUnitario*cantidad) from DetalleFactura 
					where numeroFactura=numFact) ;
    return sumatoria;
end //
delimiter ;

-- eliminar total factura
delimiter //
create trigger tr_eliminarTotalFactura_after_delete
after delete on DetalleFactura
for each row
	begin
    declare total decimal(10,2);
    
    set total=fn_TotalFactura(old.numeroFactura);
    
    call sp_actualizarFacturaTotal(old.numeroFactura, total);
                                    
	end //
delimiter ;

-- -------------------------------------

create view vw_Productos as
select Productos.codigoProducto, Productos.descripcionProducto, TipoProducto.Descripcion, Proveedores.nombresProveedor
from Productos
LEFT JOIN TipoProducto ON Productos.codigoTipoProducto = TipoProducto.codigoTipoProducto
LEFT JOIN Proveedores ON Productos.codigoProveedor = Proveedores.codigoProveedor;


select * from vw_Productos;




drop database if exists DBKinalExpress2020527;

create database DBKinalExpress2020527;

use DBKinalExpress2020527;

set global time_zone = '-6:00';

create table Usuarios(
	idUsuario int auto_increment,
    ingresoUsuario varchar(60),
    ingresoClave varchar(50),
    primary key PK_idUsuario(idUsuario)
);

insert into Usuarios(ingresoUsuario, ingresoClave)
values('Dabp','123456');

select * from Usuarios;

delete from Usuarios where idUsuario = 120;

select * from Usuarios where Usuarios.ingresoUsuario = 'Dabp' and Usuarios.ingresoClave = '123456';
select * from Usuarios where Usuarios.ingresoUsuario = 'Torres';


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
	codigoProducto varchar(15) not null unique,
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


call sp_actualizarClientes(0,'5434534','Orlando','Gomez','11 Calle y 10 Avenida','12345678','ogomez');
    
delimiter $$
	create procedure sp_eliminarClientes(in codigoCliente int)
	begin
		delete from Clientes where Clientes.codigoCliente = codigoCliente;
	end $$
delimiter ;
 
call sp_eliminarClientes(0);


-- -----------------------------------------------------------------------

delimiter $$
	create procedure sp_agregarProveedores(in codigoProveedor int, in NITProveedor varchar(10), in nombresProveedor varchar(60), in apellidosProveedor varchar(60), in direccionProveedor varchar(150), in razonSocial varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
	begin
		insert into Proveedores(codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb)
		values (codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb);
	end $$
delimiter ;



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


call sp_actualizarProveedores(0,'5434534','Orlando','Gomez','11 Calle y 10 Avenida','12345678', 'Importaciones PFV.','ogomez');
    
    
delimiter $$
	create procedure sp_eliminarProveedores(in codigoProveedor int)
	begin
		delete from Proveedores where Proveedores.codigoProveedor = codigoProveedor;
	end $$
delimiter ;
 
call sp_eliminarProveedores(0);

-- ------------------------------------------------------------------------

delimiter $$
	create procedure sp_agregarCargoEmpleado(in codigoCargoEmpleado int, in nombreCargo varchar(45), in descripcionCargo varchar(45))
	begin
		insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
		values (codigoCargoEmpleado, nombreCargo, descripcionCargo);
	end $$
delimiter ;


 
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


call sp_buscarCargoEmpleado(0);
 
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


call sp_actualizarCargoEmpleado(0, 'Atención al cliente', 'Encargado de la interacción');
    
    
delimiter $$
	create procedure sp_eliminarCargoEmpleado(in codigoCargoEmpleado int)
	begin
		delete from CargoEmpleado where CargoEmpleado.codigoCargoEmpleado = codigoCargoEmpleado;
	end $$
delimiter ;
 
call sp_eliminarCargoEmpleado(0);

-- -------------------------------------------------------------------------------------------------------


delimiter $$
	create procedure sp_agregarTipoProducto(in codigoTipoProducto int, in descripcion varchar(45))
	begin
		insert into TipoProducto(codigoTipoProducto, descripcion)
		values (codigoTipoProducto, descripcion);
	end $$
delimiter ;


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


call sp_actualizarTipoProducto(0, 'Jugos');
    
    
delimiter $$
	create procedure sp_eliminarTipoProducto(in codigoTipoProducto int)
	begin
		delete from TipoProducto where TipoProducto.codigoTipoProducto = codigoTipoProducto;
	end $$
delimiter ;
 
call sp_eliminarTipoProducto(0);

-- -----------------------------------------------------------------
delimiter $$
create procedure sp_agregarCompras(in numeroDocumento int,in fechaDocumento date, in descripcion varchar(60))
begin
	insert into Compras(numeroDocumento, fechaDocumento,descripcion)
    values (numeroDocumento, fechaDocumento,descripcion);
end $$
delimiter ;

 
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
 
call sp_actualizarCompras(0, '2024-04-23', 'Helado');
 
delimiter $$
create procedure sp_eliminarCompras(in numeroDocumento int)
begin 
	delete from Compras where Compras.numeroDocumento = numeroDocumento;
end $$
delimiter ;
 
call sp_eliminarCompras(0);

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
 
call sp_buscarProductos('a');
 
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
 
call sp_actualizarProductos('a','Alta','JPG',2,2);
 
delimiter $$
create procedure sp_eliminarProductos(in codigoProducto varchar(45))
begin 
	delete from Productos where productos.codigoProducto = codigoProducto;
end $$
delimiter ;
 
call sp_eliminarProductos('a');


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

call sp_actualizarEmpleados(0,'a','a','25.0','2 Calle y 8 Avenida','V',2);

delimiter $$
create procedure sp_eliminarEmpleados(in codigoEmpleado int)
begin 
	delete from Empleados where Empleados.codigoEmpleado = codigoEmpleado;
end $$
delimiter ;

call sp_eliminarEmpleados(0);


-- ----------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarEmailProveedor(in codigoEmailProveedor int, in emailProveedor varchar(50), in descripcion varchar(100), in codigoProveedor int)
begin
	insert into EmailProveedor(codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor)
    values(codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor);
end $$
delimiter ;


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

call sp_actualizarEmailProveedor(0,'j@gmail.com','Kinal',2);

delimiter $$
create procedure sp_eliminarEmailProveedor(in codigoEmailProveedor int)
begin
	delete from EmailProveedor where EmailProveedor.codigoEmailProveedor = codigoEmailProveedor;
end $$
delimiter ;

call sp_eliminarEmailProveedor(0);

-- -------------------------------------------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarTelefonoProveedor(in codigoTelefonoProveedor int, in numeroPrincipal varchar(8), in numeroSecundario varchar(8), in observaciones varchar(45), in codigoProveedor int)
begin
	insert into TelefonoProveedor(codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
    values (codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor);
end $$
delimiter ;


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

call sp_actualizarTelefonoProveedor(0,'09876543','12345678','Numeros de referencia',2);

delimiter $$
create procedure sp_eliminarTelefonoProveedor(in codigoTelefonoProveedor int)
begin
	delete from TelefonoProveedor where TelefonoProveedor.codigoTelefonoProveedor = codigoTelefonoProveedor;
end $$
delimiter ;

call sp_eliminarTelefonoProveedor(0);

-- ------------------------------------------------------------------------------------------------------

delimiter $$
create procedure sp_agregarFactura(in numeroFactura int,in estado varchar(50), in fechaFactura varchar(45), in codigoCliente int, in codigoEmpleado int)
begin
	insert into Factura(numeroFactura,estado,fechaFactura,codigoCliente,codigoEmpleado)
    values (numeroFactura,estado,fechaFactura,codigoCliente,codigoEmpleado);
end $$
delimiter ;
 

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
 
call sp_actualizarFacturas(0, 'expirado','04-04-2023', 1,2);

 
delimiter $$
create procedure sp_eliminarFacturas(in numeroFactura int)
begin 
	delete from Factura where Factura.numeroFactura = numeroFactura;
end $$
delimiter ;
 
call sp_eliminarFacturas(0);

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
 
call sp_ActualizarDetalleFactura(0, 10.0, 3, 1, 'abc');

 
delimiter $$
create procedure sp_eliminarDetalleFactura(in codigoDetalleFactura int)
begin 
	delete from DetalleFactura where DetalleFactura.codigoDetalleFactura = codigoDetalleFactura;
end $$
delimiter ;
 
call sp_eliminarDetalleFactura(0);



-- ------------------------------------------------------------------------------------------------------



delimiter $$
create procedure sp_agregarDetalleCompra(in codigoDc int, in costo decimal(10,2), in cant int, in codProd varchar(15) , in numDoc int)
begin
	insert into DetalleCompra (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    values(codigoDc, costo,cant, codProd, numDoc);
    
end $$
delimiter ;



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
    where DetalleCompra.codigoProducto=codProd limit 1);
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
		where Productos.codigoProducto=new.codigoProducto limit 1);
        
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

select sum(precioUnitario*cantidad) from DetalleFactura 
					where numeroFactura=1;



-- actualizar total factura
delimiter //
create trigger tr_actualizarTotalFactura_after_update
after update on DetalleFactura
for each row
	begin
    declare total decimal(10,2);
    
    set total=((select sum(new.precioUnitario*cantidad) from DetalleFactura where DetalleFactura.numeroFactura=old.numeroFactura ));
    
    call sp_actualizarFacturaTotal(old.numeroFactura, total);
                                    
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

select * from DetalleFactura
	join Factura on DetalleFactura.numeroFactura = Factura.numeroFactura
    join Clientes on Factura.codigoCliente = Clientes.codigoCliente
    join Productos on DetalleFactura.codigoProducto = Productos.codigoProducto;


-- ---------------------------------
CALL sp_agregarClientes(1, '502452698', 'Diego', 'Bercian', '12 Calle y 6 Avenida, Zona 1, Guatemala', '34414211', 'dbercian@gmail.com');
CALL sp_agregarClientes(2, '368295147', 'Maria', 'Lopez', '5 Avenida y 3 Calle, Zona 10, Guatemala', '56789012', 'mlopez@gmail.com');
CALL sp_agregarClientes(3, '492813576', 'Juan', 'Perez', '8 Calle y 2 Avenida, Zona 4, Guatemala', '67890123', 'jperez@gmail.com');
CALL sp_agregarClientes(4, '183746592', 'Ana', 'Garcia', '9 Avenida y 5 Calle, Zona 13, Guatemala', '78901234', 'agarcia@gmail.com');
CALL sp_agregarClientes(5, '927364185', 'Carlos', 'Martinez', '6 Calle y 7 Avenida, Zona 9, Guatemala', '89012345', 'cmartinez@gmail.com');
CALL sp_agregarClientes(6, '615372948', 'Lucia', 'Hernandez', '10 Avenida y 1 Calle, Zona 15, Guatemala', '90123456', 'lhernandez@gmail.com');
CALL sp_agregarClientes(7, '738194652', 'Roberto', 'Rodriguez', '3 Calle y 8 Avenida, Zona 11, Guatemala', '12345678', 'rrodriguez@gmail.com');
CALL sp_agregarClientes(8, '384756291', 'Elena', 'Gomez', '7 Avenida y 12 Calle, Zona 2, Guatemala', '23456789', 'egomez@gmail.com');
CALL sp_agregarClientes(9, '529831746', 'Miguel', 'Fernandez', '4 Calle y 9 Avenida, Zona 12, Guatemala', '34567890', 'mfernandez@gmail.com');
CALL sp_agregarClientes(10, '671493852', 'Sofia', 'Mendez', '2 Avenida y 11 Calle, Zona 6, Guatemala', '45678901', 'smendez@gmail.com');
CALL sp_agregarClientes(11, '948573120', 'Andres', 'Sanchez', '1 Avenida y 10 Calle, Zona 5, Guatemala', '56712345', 'asanchez@gmail.com');
CALL sp_agregarClientes(12, '857392641', 'Patricia', 'Diaz', '4 Avenida y 6 Calle, Zona 7, Guatemala', '67823456', 'pdiaz@gmail.com');
CALL sp_agregarClientes(13, '176485920', 'Francisco', 'Ruiz', '11 Calle y 3 Avenida, Zona 8, Guatemala', '78934567', 'fruiz@gmail.com');
CALL sp_agregarClientes(14, '293874516', 'Valeria', 'Vasquez', '2 Calle y 5 Avenida, Zona 14, Guatemala', '89045678', 'vvasquez@gmail.com');
CALL sp_agregarClientes(15, '385762941', 'Sergio', 'Ortiz', '8 Avenida y 13 Calle, Zona 3, Guatemala', '90156789', 'sortiz@gmail.com');
CALL sp_agregarClientes(16, '497382165', 'Isabel', 'Morales', '5 Calle y 2 Avenida, Zona 18, Guatemala', '12367890', 'imorales@gmail.com');
CALL sp_agregarClientes(17, '516274893', 'Luis', 'Castillo', '3 Avenida y 4 Calle, Zona 21, Guatemala', '23478901', 'lcastillo@gmail.com');
CALL sp_agregarClientes(18, '678293415', 'Monica', 'Rojas', '6 Avenida y 8 Calle, Zona 17, Guatemala', '34589012', 'mrojas@gmail.com');
CALL sp_agregarClientes(19, '729384561', 'Ricardo', 'Navarro', '12 Avenida y 7 Calle, Zona 19, Guatemala', '45690123', 'rnavarro@gmail.com');
CALL sp_agregarClientes(20, '834726195', 'Daniela', 'Gutierrez', '9 Calle y 10 Avenida, Zona 16, Guatemala', '56701234', 'dgutierrez@gmail.com');


CALL sp_agregarProveedores(1, '548512349', 'David', 'Arevalo', '6 calle 4-76 zona 7, Guatemala', 'Arcos Dorados, C.A.', 'Mateo', 'arcosdorados.com');
CALL sp_agregarProveedores(2, '629174853', 'Carlos', 'Ramirez', '8 avenida 5-20 zona 1, Guatemala', 'Distribuidora Ramirez', 'Juan', 'distriramirez.com');
CALL sp_agregarProveedores(3, '183947562', 'Luis', 'Mendoza', '3 calle 2-15 zona 10, Guatemala', 'Mendoza S.A.', 'Ana', 'mendoza.com.gt');
CALL sp_agregarProveedores(4, '295738164', 'Ana', 'Lopez', '7 avenida 9-11 zona 4, Guatemala', 'Proveedores Lopez', 'Sofia', 'proveedoreslopez.com');
CALL sp_agregarProveedores(5, '376495821', 'Maria', 'Gonzalez', '5 calle 6-18 zona 5, Guatemala', 'Gonzalez y Asociados', 'Pedro', 'gonzalezyasociados.com');
CALL sp_agregarProveedores(6, '485293716', 'Jorge', 'Martinez', '4 avenida 3-12 zona 6, Guatemala', 'Martinez Importaciones', 'Luis', 'martinezimport.com');
CALL sp_agregarProveedores(7, '594182374', 'Patricia', 'Hernandez', '9 calle 7-21 zona 2, Guatemala', 'Hernandez Suministros', 'Carlos', 'hernandezsuministros.com');
CALL sp_agregarProveedores(8, '612384795', 'Francisco', 'Jimenez', '2 avenida 4-19 zona 9, Guatemala', 'Jimenez Servicios', 'Miguel', 'jimenezservicios.com');
CALL sp_agregarProveedores(9, '738291456', 'Elena', 'Castillo', '1 calle 8-23 zona 12, Guatemala', 'Castillo y Cía.', 'Lucia', 'castilloycia.com');
CALL sp_agregarProveedores(10, '847593216', 'Roberto', 'Rivas', '10 avenida 5-14 zona 15, Guatemala', 'Rivas Industrial', 'Fernando', 'rivasindustrial.com');
CALL sp_agregarProveedores(11, '921847356', 'Sofia', 'Diaz', '6 avenida 3-22 zona 11, Guatemala', 'Diaz Proveeduría', 'Andrea', 'diazproveeduria.com');
CALL sp_agregarProveedores(12, '183947652', 'Miguel', 'Ortiz', '7 calle 1-34 zona 3, Guatemala', 'Ortiz Equipos', 'Ricardo', 'ortizequipos.com');
CALL sp_agregarProveedores(13, '295183476', 'Isabel', 'Reyes', '8 avenida 9-12 zona 8, Guatemala', 'Reyes Distribuciones', 'Gabriel', 'reyesdistribuciones.com');
CALL sp_agregarProveedores(14, '364819527', 'Gabriel', 'Flores', '5 calle 6-23 zona 14, Guatemala', 'Flores y Flores', 'Maria', 'floresyflores.com');
CALL sp_agregarProveedores(15, '485927316', 'Laura', 'Morales', '3 avenida 7-45 zona 13, Guatemala', 'Morales Comercio', 'Javier', 'moralescomercio.com');
CALL sp_agregarProveedores(16, '592837146', 'Daniel', 'Vargas', '9 calle 2-56 zona 18, Guatemala', 'Vargas Import', 'Roberto', 'vargasimport.com');
CALL sp_agregarProveedores(17, '618293745', 'Monica', 'Navarro', '1 avenida 8-19 zona 16, Guatemala', 'Navarro y Cía.', 'Oscar', 'navarroycia.com');
CALL sp_agregarProveedores(18, '738291465', 'Victor', 'Fernandez', '2 calle 4-10 zona 17, Guatemala', 'Fernandez Servicios', 'Rosa', 'fernandezservicios.com');
CALL sp_agregarProveedores(19, '859374612', 'Lucia', 'Mejia', '8 avenida 5-22 zona 21, Guatemala', 'Mejia y Asociados', 'Enrique', 'mejiayasociados.com');
CALL sp_agregarProveedores(20, '972483156', 'Oscar', 'Salazar', '4 calle 9-30 zona 19, Guatemala', 'Salazar Importaciones', 'Daniela', 'salazarimportaciones.com');

CALL sp_agregarCargoEmpleado(1, 'Caja', 'Encargado de atender');
CALL sp_agregarCargoEmpleado(2, 'Reponedor', 'Reponer productos en estantes');
CALL sp_agregarCargoEmpleado(3, 'Carnicero', 'Cortar y preparar carne');
CALL sp_agregarCargoEmpleado(4, 'Pescadero', 'Vender y preparar pescado');
CALL sp_agregarCargoEmpleado(5, 'Panadero', 'Hornear y vender pan');
CALL sp_agregarCargoEmpleado(6, 'Frutero', 'Organizar frutas y verduras');
CALL sp_agregarCargoEmpleado(7, 'Charcutero', 'Vender embutidos y quesos');
CALL sp_agregarCargoEmpleado(8, 'Cajero', 'Cobrar a los clientes');
CALL sp_agregarCargoEmpleado(9, 'Supervisor', 'Supervisar al personal');
CALL sp_agregarCargoEmpleado(10, 'Jefe de Turno', 'Coordinar el turno de trabajo');
CALL sp_agregarCargoEmpleado(11, 'Limpiador', 'Mantener limpieza del local');
CALL sp_agregarCargoEmpleado(12, 'Encargado de Almacén', 'Gestionar el almacén');
CALL sp_agregarCargoEmpleado(13, 'Seguridad', 'Vigilar el establecimiento');
CALL sp_agregarCargoEmpleado(14, 'Florista', 'Arreglar y vender flores');
CALL sp_agregarCargoEmpleado(15, 'Atención al Cliente', 'Ayudar a los clientes');
CALL sp_agregarCargoEmpleado(16, 'Pastelero', 'Hacer y vender pasteles');
CALL sp_agregarCargoEmpleado(17, 'Encargado de Bebidas', 'Gestionar sección de bebidas');
CALL sp_agregarCargoEmpleado(18, 'Encargado de Lácteos', 'Gestionar sección de lácteos');
CALL sp_agregarCargoEmpleado(19, 'Encargado de Limpieza', 'Gestionar productos de limpieza');
CALL sp_agregarCargoEmpleado(20, 'Encargado de Electrónica', 'Vender productos electrónicos');
CALL sp_agregarCargoEmpleado(21, 'Gerente de Tienda', 'Administrar la tienda');

CALL sp_agregarTipoProducto(1, 'Carnes');
CALL sp_agregarTipoProducto(2, 'Pescados y Mariscos');
CALL sp_agregarTipoProducto(3, 'Frutas');
CALL sp_agregarTipoProducto(4, 'Verduras');
CALL sp_agregarTipoProducto(5, 'Lácteos');
CALL sp_agregarTipoProducto(6, 'Panadería');
CALL sp_agregarTipoProducto(7, 'Pastelería');
CALL sp_agregarTipoProducto(8, 'Bebidas');
CALL sp_agregarTipoProducto(9, 'Embutidos');
CALL sp_agregarTipoProducto(10, 'Quesos');
CALL sp_agregarTipoProducto(11, 'Conservas');
CALL sp_agregarTipoProducto(12, 'Congelados');
CALL sp_agregarTipoProducto(13, 'Desayunos');
CALL sp_agregarTipoProducto(14, 'Cereales');
CALL sp_agregarTipoProducto(15, 'Dulces');
CALL sp_agregarTipoProducto(16, 'Snacks');
CALL sp_agregarTipoProducto(17, 'Higiene Personal');
CALL sp_agregarTipoProducto(18, 'Limpieza del Hogar');
CALL sp_agregarTipoProducto(19, 'Productos para Mascotas');
CALL sp_agregarTipoProducto(20, 'Electrónica');
CALL sp_agregarTipoProducto(21, 'Juguetes');

CALL sp_agregarCompras(1, '2024-03-19', 'Compra de carnes');
CALL sp_agregarCompras(2, '2024-03-20', 'Compra de frutas y verduras');
CALL sp_agregarCompras(3, '2024-03-21', 'Compra de lácteos');
CALL sp_agregarCompras(4, '2024-03-22', 'Compra de pan y pasteles');
CALL sp_agregarCompras(5, '2024-03-23', 'Compra de bebidas');
CALL sp_agregarCompras(6, '2024-03-24', 'Compra de embutidos');
CALL sp_agregarCompras(7, '2024-03-25', 'Compra de quesos');
CALL sp_agregarCompras(8, '2024-03-26', 'Compra de conservas');
CALL sp_agregarCompras(9, '2024-03-27', 'Compra de congelados');
CALL sp_agregarCompras(10, '2024-03-28', 'Compra de cereales');
CALL sp_agregarCompras(11, '2024-03-29', 'Compra de dulces');
CALL sp_agregarCompras(12, '2024-03-30', 'Compra de snacks');
CALL sp_agregarCompras(13, '2024-03-31', 'Compra de higiene personal');
CALL sp_agregarCompras(14, '2024-04-01', 'Compra de productos de limpieza');
CALL sp_agregarCompras(15, '2024-04-02', 'Compra de productos para mascotas');
CALL sp_agregarCompras(16, '2024-04-03', 'Compra de electrónica');
CALL sp_agregarCompras(17, '2024-04-04', 'Compra de juguetes');
CALL sp_agregarCompras(18, '2024-04-05', 'Compra de herramientas');
CALL sp_agregarCompras(19, '2024-04-06', 'Compra de artículos de papelería');
CALL sp_agregarCompras(20, '2024-04-07', 'Compra de artículos de jardinería');
CALL sp_agregarCompras(21, '2024-04-08', 'Compra de utensilios de cocina');


CALL sp_agregarProductos('A1B2C3', 'Carne de res', 'carne_res.jpg', 1, 1);
CALL sp_agregarProductos('D4E5F6', 'Filete de pescado', 'filete_pescado.jpg', 2, 2);
CALL sp_agregarProductos('G7H8I9', 'Manzanas rojas', 'manzanas.jpg', 3, 3);
CALL sp_agregarProductos('J1K2L3', 'Tomates frescos', 'tomates.jpg', 4, 4);
CALL sp_agregarProductos('M4N5O6', 'Leche entera', 'leche.jpg', 5, 5);
CALL sp_agregarProductos('J2K2L3', 'Pan integral', 'pan_integral.jpg', 6, 6);
CALL sp_agregarProductos('S1T2U3', 'Pastel de chocolate', 'pastel_chocolate.jpg', 7, 7);
CALL sp_agregarProductos('V4W5X6', 'Jugo de naranja', 'jugo_naranja.jpg', 8, 8);
CALL sp_agregarProductos('Y7Z8A1', 'Salchichas', 'salchichas.jpg', 9, 9);
CALL sp_agregarProductos('B2C3D4', 'Queso cheddar', 'queso_cheddar.jpg', 10, 10);
CALL sp_agregarProductos('E5F6G7', 'Atún en lata', 'atun_lata.jpg', 11, 11);
CALL sp_agregarProductos('H8I9J1', 'Pizza congelada', 'pizza_congelada.jpg', 12, 12);
CALL sp_agregarProductos('K2L3M4', 'Cereal de avena', 'cereal_avena.jpg', 14, 13);
CALL sp_agregarProductos('N5O6P7', 'Barra de chocolate', 'barra_chocolate.jpg', 15, 14);
CALL sp_agregarProductos('Q8R9S1', 'Papas fritas', 'papas_fritas.jpg', 16, 15);
CALL sp_agregarProductos('T2U3V4', 'Shampoo', 'shampoo.jpg', 17, 16);
CALL sp_agregarProductos('W5X6Y7', 'Detergente', 'detergente.jpg', 18, 17);
CALL sp_agregarProductos('Z8A1B2', 'Croquetas para perro', 'croquetas_perro.jpg', 19, 18);
CALL sp_agregarProductos('C3D4E5', 'Teléfono móvil', 'telefono_movil.jpg', 20, 19);
CALL sp_agregarProductos('F6G7H8', 'Muñeca de juguete', 'muneca.jpg', 21, 20);
CALL sp_agregarProductos('Q7R9S1', 'Martillo', 'martillo.jpg', 18, 5);
CALL sp_agregarProductos('L3M4N5', 'Cuaderno', 'cuaderno.jpg', 19, 10);
CALL sp_agregarProductos('O6P7Q8', 'Tijeras de jardinería', 'tijeras_jardineria.jpg', 20, 15);
CALL sp_agregarProductos('R9S1T2', 'Sartén', 'sarten.jpg', 21, 8);
CALL sp_agregarProductos('U3V4W5', 'Jabón de manos', 'jabon_manos.jpg', 17, 3);
CALL sp_agregarProductos('X6Y7Z8', 'Huevos frescos', 'huevos_frescos.jpg', 1, 2);
CALL sp_agregarProductos('A2B3C4', 'Lechuga fresca', 'lechuga_fresca.jpg', 4, 1);


CALL sp_agregarEmpleados(1, 'Pedro', 'Gomez', '10.50', '10 Calle y 10 Avenida', '8:00-12:00', 1);
CALL sp_agregarEmpleados(2, 'Maria', 'Lopez', '9.75', '15 Avenida y 5 Calle', '13:00-17:00', 2);
CALL sp_agregarEmpleados(3, 'Juan', 'Martinez', '11.25', '20 Calle y 25 Avenida', '8:00-12:00', 3);
CALL sp_agregarEmpleados(4, 'Sofia', 'Rodriguez', '12.00', '30 Avenida y 40 Calle', '9:00-13:00', 4);
CALL sp_agregarEmpleados(5, 'Carlos', 'Hernandez', '10.75', '5 Avenida y 8 Calle', '14:00-18:00', 5);
CALL sp_agregarEmpleados(6, 'Laura', 'Garcia', '9.50', '12 Calle y 15 Avenida', '8:00-12:00', 6);
CALL sp_agregarEmpleados(7, 'Javier', 'Perez', '11.75', '18 Avenida y 25 Calle', '13:00-17:00', 7);
CALL sp_agregarEmpleados(8, 'Ana', 'Sanchez', '10.25', '22 Calle y 30 Avenida', '10:00-14:00', 8);
CALL sp_agregarEmpleados(9, 'Diego', 'Gutierrez', '9.00', '3 Avenida y 6 Calle', '12:00-16:00', 9);
CALL sp_agregarEmpleados(10, 'Marta', 'Morales', '12.50', '8 Calle y 10 Avenida', '9:00-13:00', 10);
CALL sp_agregarEmpleados(11, 'Luis', 'Diaz', '9.25', '14 Avenida y 18 Calle', '14:00-18:00', 11);
CALL sp_agregarEmpleados(12, 'Elena', 'Rojas', '11.00', '25 Calle y 35 Avenida', '8:00-12:00', 12);
CALL sp_agregarEmpleados(13, 'Roberto', 'Castillo', '10.75', '6 Calle y 8 Avenida', '13:00-17:00', 13);
CALL sp_agregarEmpleados(14, 'Monica', 'Navarro', '9.50', '10 Avenida y 12 Calle', '10:00-14:00', 14);
CALL sp_agregarEmpleados(15, 'Jorge', 'Fernandez', '11.25', '18 Avenida y 20 Calle', '12:00-16:00', 15);
CALL sp_agregarEmpleados(16, 'Isabel', 'Ponce', '10.25', '30 Calle y 40 Avenida', '9:00-13:00', 16);
CALL sp_agregarEmpleados(17, 'Ricardo', 'Luna', '9.00', '5 Avenida y 7 Calle', '14:00-18:00', 17);
CALL sp_agregarEmpleados(18, 'Diana', 'Martinez', '12.75', '16 Calle y 22 Avenida', '8:00-12:00', 18);
CALL sp_agregarEmpleados(19, 'Mateo', 'Gomez', '9.25', '20 Avenida y 25 Calle', '13:00-17:00', 19);
CALL sp_agregarEmpleados(20, 'Valeria', 'Perez', '11.50', '35 Calle y 45 Avenida', '10:00-14:00', 20);
CALL sp_agregarEmpleados(21, 'Santiago', 'Hernandez', '10.75', '8 Avenida y 10 Calle', '12:00-16:00', 21);
CALL sp_agregarEmpleados(22, 'Lucia', 'Garcia', '9.50', '12 Calle y 15 Avenida', '9:00-13:00', 21);
CALL sp_agregarEmpleados(23, 'Andres', 'Sanchez', '11.25', '18 Avenida y 20 Calle', '14:00-18:00', 20);
CALL sp_agregarEmpleados(24, 'Camila', 'Rojas', '10.00', '25 Calle y 30 Avenida', '8:00-12:00', 20);
CALL sp_agregarEmpleados(25, 'Gustavo', 'Castillo', '9.75', '6 Calle y 8 Avenida', '13:00-17:00', 16);
CALL sp_agregarEmpleados(26, 'Fernanda', 'Navarro', '12.50', '10 Avenida y 12 Calle', '10:00-14:00', 12);

CALL sp_agregarEmailProveedor(1, 'dbercianc@kinal', 'Guatemala', 1);
CALL sp_agregarEmailProveedor(2, 'mlopez@empresa.com', 'Productos deliciosos', 2);
CALL sp_agregarEmailProveedor(3, 'jmartinez@frutas.com', 'Frutas frescas y de calidad', 3);
CALL sp_agregarEmailProveedor(4, 'srodriguez@productos.com', 'Variedad de lácteos', 4);
CALL sp_agregarEmailProveedor(5, 'chernandez@panaderia.com', 'Pan recién horneado', 5);
CALL sp_agregarEmailProveedor(6, 'lgarcia@pasteleria.com', 'Pasteles artesanales', 6);
CALL sp_agregarEmailProveedor(7, 'jperez@jugo.com', 'Jugos naturales', 7);
CALL sp_agregarEmailProveedor(8, 'asanchez@salchichas.com', 'Salchichas gourmet', 8);
CALL sp_agregarEmailProveedor(9, 'dgutierrez@quesos.com', 'Quesos de todo tipo', 9);
CALL sp_agregarEmailProveedor(10, 'mmorales@atun.com', 'Atún enlatado de calidad', 10);
CALL sp_agregarEmailProveedor(11, 'ldiaz@pizza.com', 'Pizzas listas para hornear', 11);
CALL sp_agregarEmailProveedor(12, 'rojas@cereal.com', 'Cereales saludables', 12);
CALL sp_agregarEmailProveedor(13, 'rcastillo@chocolate.com', 'Chocolates artesanales', 13);
CALL sp_agregarEmailProveedor(14, 'mnavarro@papas.com', 'Papas fritas crujientes', 14);
CALL sp_agregarEmailProveedor(15, 'jfernandez@shampoo.com', 'Shampoo para todo tipo de cabello', 15);
CALL sp_agregarEmailProveedor(16, 'iponce@detergente.com', 'Detergentes eficientes', 16);
CALL sp_agregarEmailProveedor(17, 'rluna@croquetas.com', 'Croquetas para perros y gatos', 17);
CALL sp_agregarEmailProveedor(18, 'dmartinez@telefono.com', 'Teléfonos móviles de última generación', 18);
CALL sp_agregarEmailProveedor(19, 'mgomez@muneca.com', 'Muñecas de juguete para niños', 19);
CALL sp_agregarEmailProveedor(20, 'vperez@martillo.com', 'Martillos resistentes y duraderos', 20);

CALL sp_agregarTelefonoProveedor(1, '23456789', '98765432', 'Teléfono principal y secundario', 1);
CALL sp_agregarTelefonoProveedor(2, '34567890', '87654321', 'Contacto directo y alternativo', 2);
CALL sp_agregarTelefonoProveedor(3, '45678901', '76543210', 'Número principal y backup', 3);
CALL sp_agregarTelefonoProveedor(4, '56789012', '65432109', 'Teléfono principal y reserva', 4);
CALL sp_agregarTelefonoProveedor(5, '67890123', '54321098', 'Línea principal y secundaria', 5);
CALL sp_agregarTelefonoProveedor(6, '78901234', '43210987', 'Número de contacto y backup', 6);
CALL sp_agregarTelefonoProveedor(7, '89012345', '32109876', 'Teléfono principal y auxiliar', 7);
CALL sp_agregarTelefonoProveedor(8, '90123456', '21098765', 'Número principal y secundario', 8);
CALL sp_agregarTelefonoProveedor(9, '01234567', '09876543', 'Línea directa y respaldo', 9);
CALL sp_agregarTelefonoProveedor(10, '98765432', '87654321', 'Contacto principal y alternativo', 10);
CALL sp_agregarTelefonoProveedor(11, '87654321', '76543210', 'Teléfono principal y respaldo', 11);
CALL sp_agregarTelefonoProveedor(12, '76543210', '65432109', 'Número principal y secundario', 12);
CALL sp_agregarTelefonoProveedor(13, '65432109', '54321098', 'Teléfono principal y auxiliar', 13);
CALL sp_agregarTelefonoProveedor(14, '54321098', '43210987', 'Línea principal y secundaria', 14);
CALL sp_agregarTelefonoProveedor(15, '43210987', '32109876', 'Contacto directo y backup', 15);
CALL sp_agregarTelefonoProveedor(16, '32109876', '21098765', 'Número de contacto y reserva', 16);
CALL sp_agregarTelefonoProveedor(17, '21098765', '09876543', 'Teléfono principal y secundario', 17);
CALL sp_agregarTelefonoProveedor(18, '09876543', '98765432', 'Línea directa y respaldo', 18);
CALL sp_agregarTelefonoProveedor(19, '34567890', '87654321', 'Contacto principal y alternativo', 19);
CALL sp_agregarTelefonoProveedor(20, '23456789', '76543210', 'Teléfono principal y respaldo', 20);

CALL sp_agregarFactura('0001', 'Pagada', '2024-05-20', 1, 1);
CALL sp_agregarFactura('0002', 'Pendiente', '2024-05-21', 2, 2);
CALL sp_agregarFactura('0003', 'Pagada', '2024-05-22', 3, 3);
CALL sp_agregarFactura('0004', 'Pendiente', '2024-05-23', 4, 4);
CALL sp_agregarFactura('0005', 'Pagada', '2024-05-24', 5, 5);
CALL sp_agregarFactura('0006', 'Pendiente', '2024-05-25', 6, 6);
CALL sp_agregarFactura('0007', 'Pagada', '2024-05-26', 7, 7);
CALL sp_agregarFactura('0008', 'Pendiente', '2024-05-27', 8, 8);
CALL sp_agregarFactura('0009', 'Pagada', '2024-05-28', 9, 9);
CALL sp_agregarFactura('0010', 'Pendiente', '2024-05-29', 10, 10);
CALL sp_agregarFactura('0011', 'Pagada', '2024-05-30', 11, 11);
CALL sp_agregarFactura('0012', 'Pendiente', '2024-05-31', 12, 12);
CALL sp_agregarFactura('0013', 'Pagada', '2024-06-01', 13, 13);
CALL sp_agregarFactura('0014', 'Pendiente', '2024-06-02', 14, 14);
CALL sp_agregarFactura('0015', 'Pagada', '2024-06-03', 15, 15);
CALL sp_agregarFactura('0016', 'Pendiente', '2024-06-04', 16, 16);
CALL sp_agregarFactura('0017', 'Pagada', '2024-06-05', 17, 17);
CALL sp_agregarFactura('0018', 'Pendiente', '2024-06-06', 18, 18);
CALL sp_agregarFactura('0019', 'Pagada', '2024-06-07', 19, 19);
CALL sp_agregarFactura('0020', 'Pendiente', '2024-06-08', 20, 20);
CALL sp_agregarFactura('0021', 'Pagada', '2024-06-09', 20, 21);
CALL sp_agregarFactura('0022', 'Pendiente', '2024-06-10', 16, 22);
CALL sp_agregarFactura('0023', 'Pagada', '2024-06-11', 19, 23);
CALL sp_agregarFactura('0024', 'Pendiente', '2024-06-12', 12, 24);
CALL sp_agregarFactura('0025', 'Pagada', '2024-06-13', 13, 25);
CALL sp_agregarFactura('0026', 'Pendiente', '2024-06-14', 9, 26);

CALL sp_agregarDetalleFactura('0001', 7, '0001','A1B2C3');
CALL sp_agregarDetalleFactura('0002', 4, '0002', 'A1B2C3');
CALL sp_agregarDetalleFactura('0003', 9, '0003','D4E5F6');
CALL sp_agregarDetalleFactura('0004', 2, '0004', 'G7H8I9');
CALL sp_agregarDetalleFactura('0005', 11, '0005', 'J1K2L3');
CALL sp_agregarDetalleFactura('0006', 8, '0006', 'S1T2U3');
CALL sp_agregarDetalleFactura('0007', 13, '0007', 'M4N5O6');
CALL sp_agregarDetalleFactura('0008', 3, '0008', 'V4W5X6');
CALL sp_agregarDetalleFactura('0009', 6, '0009', 'J1K2L3');
CALL sp_agregarDetalleFactura('0010', 10, '0010', 'B2C3D4');
CALL sp_agregarDetalleFactura('0011', 1, '0011', 'S1T2U3');
CALL sp_agregarDetalleFactura('0012', 12, '0012', 'Y7Z8A1');
CALL sp_agregarDetalleFactura('0013', 5, '0013', 'E5F6G7');
CALL sp_agregarDetalleFactura('0014', 9, '0014', 'H8I9J1');
CALL sp_agregarDetalleFactura('0015', 2, '0015', 'M4N5O6');
CALL sp_agregarDetalleFactura('0016', 7, '0016', 'N5O6P7');
CALL sp_agregarDetalleFactura('0017', 11, '0017', 'Q8R9S1');
CALL sp_agregarDetalleFactura('0018', 4, '0018', 'W5X6Y7');
CALL sp_agregarDetalleFactura('0019', 8, '0019', 'Z8A1B2');
CALL sp_agregarDetalleFactura('0020', 13, '0020', 'L3M4N5');
CALL sp_agregarDetalleFactura('0021', 3, '0021', 'O6P7Q8');
CALL sp_agregarDetalleFactura('0022', 6, '0022', 'M4N5O6');
CALL sp_agregarDetalleFactura('0023', 10, '0023', 'U3V4W5');
CALL sp_agregarDetalleFactura('0024', 1, '0024', 'X6Y7Z8');
CALL sp_agregarDetalleFactura('0025', 12, '0025', 'A2B3C4');
CALL sp_agregarDetalleFactura('0026', 5, '0026', 'A2B3C4');

CALL sp_agregarDetalleCompra('0001', 10.50, 7,'A2B3C4', 1);
CALL sp_agregarDetalleCompra('0002', 15.25, 4, 'A1B2C3', 2);
CALL sp_agregarDetalleCompra('0003', 20.00, 9, 'N5O6P7', 3);
CALL sp_agregarDetalleCompra('0004', 8.75, 2, 'M4N5O6', 4);
CALL sp_agregarDetalleCompra('0005', 12.80, 11, 'S1T2U3', 5);
CALL sp_agregarDetalleCompra('0006', 18.30, 8, 'V4W5X6', 6);
CALL sp_agregarDetalleCompra('0007', 22.40, 13, 'Y7Z8A1', 7);
CALL sp_agregarDetalleCompra('0008', 9.60, 3, 'B2C3D4', 8);
CALL sp_agregarDetalleCompra('0009', 14.70, 6, 'V4W5X6', 9);
CALL sp_agregarDetalleCompra('0010', 27.50, 10, 'G7H8I9', 10);
CALL sp_agregarDetalleCompra('0011', 11.20, 1, 'D4E5F6', 11);
CALL sp_agregarDetalleCompra('0012', 28.90, 12, 'D4E5F6', 12);
CALL sp_agregarDetalleCompra('0013', 6.90, 5, 'N5O6P7', 13);
CALL sp_agregarDetalleCompra('0014', 16.40, 9, 'M4N5O6', 14);
CALL sp_agregarDetalleCompra('0015', 7.80, 2, 'E5F6G7', 15);
CALL sp_agregarDetalleCompra('0016', 13.60, 7, 'H8I9J1', 16);
CALL sp_agregarDetalleCompra('0017', 25.70, 11, 'M4N5O6', 17);
CALL sp_agregarDetalleCompra('0018', 9.30, 4, 'A1B2C3', 18);
CALL sp_agregarDetalleCompra('0019', 19.50, 8, 'K2L3M4', 19);
CALL sp_agregarDetalleCompra('0020', 30.00, 13, 'S1T2U3', 20);
CALL sp_agregarDetalleCompra('0021', 8.40, 3, 'N5O6P7', 21);
CALL sp_agregarDetalleCompra('0022', 17.90, 6, 'W5X6Y7', 21);
CALL sp_agregarDetalleCompra('0023', 26.20, 10, 'A1B2C3', 19);
CALL sp_agregarDetalleCompra('0024', 7.10, 1, 'D4E5F6', 1);
CALL sp_agregarDetalleCompra('0025', 24.80, 12, 'D4E5F6', 4);
CALL sp_agregarDetalleCompra('0026', 11.50, 5, 'W5X6Y7', 6);

package org.diegobercian.ui;
import java.util.*;
public final class Labels {
    private static final Map<String,String> NAMES=new HashMap<>();
    static {
        String[][] values={
            {"codigoProducto","Código de producto"},{"codigoCliente","Código de cliente"},{"codigoProveedor","Código de proveedor"},
            {"codigoCargoEmpleado","Cargo"},{"codigoTipoProducto","Tipo de producto"},{"codigoEmpleado","Empleado"},
            {"numeroDocumento","Número de compra"},{"numeroFactura","Número de factura"},{"codigoDetalleCompra","Código de detalle"},{"codigoDetalleFactura","Código de detalle"},
            {"codigoEmailProveedor","Código de correo"},{"codigoTelefonoProveedor","Código de teléfono"},
            {"NITClientes","NIT"},{"NITProveedor","NIT"},{"nombresCliente","Nombres"},{"apellidosCliente","Apellidos"},
            {"nombresProveedor","Nombres"},{"apellidosProveedor","Apellidos"},{"nombresEmpleado","Nombres"},{"apellidosEmpleado","Apellidos"},
            {"descripcionProducto","Descripción"},{"descripcion","Descripción"},{"descripcionCargo","Descripción"},{"nombreCargo","Nombre del cargo"},
            {"telefonoCliente","Teléfono"},{"correoCliente","Correo electrónico"},{"emailProveedor","Correo electrónico"},
            {"direccionCliente","Dirección"},{"direccionProveedor","Dirección"},{"direccion","Dirección"},
            {"razonSocial","Razón social"},{"contactoPrincipal","Contacto principal"},{"paginaWeb","Sitio web"},
            {"fechaFactura","Fecha de factura"},{"fechaDocumento","Fecha de compra"},{"costoUnitario","Costo unitario"},
            {"cantidad","Cantidad"},{"sueldo","Sueldo"},{"turno","Turno"},{"estado","Estado"},{"imagenProducto","Referencia de imagen"},
            {"numeroPrincipal","Teléfono principal"},{"numeroSecundario","Teléfono secundario"},{"observaciones","Observaciones"},
            {"precioUnitario","Precio unitario"},{"precioDocena","Precio por docena"},{"precioMayor","Precio al mayor"},{"existencia","Existencias"},
            {"totalDocumento","Total de compra"},{"totalFactura","Total de factura"}};
        for(String[] value:values)NAMES.put(value[0].toLowerCase(Locale.ROOT),value[1]);
    }
    private Labels(){}
    public static String caption(String key) {return NAMES.getOrDefault(key.toLowerCase(Locale.ROOT),key);}
}

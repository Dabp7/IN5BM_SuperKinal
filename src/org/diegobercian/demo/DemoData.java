package org.diegobercian.demo;

import java.sql.*;
import java.util.Arrays;
import org.diegobercian.db.Conexion;
import org.diegobercian.security.PasswordHash;

/** Synthetic data only. Refuses any schema outside the explicitly isolated demo/test names. */
public final class DemoData {
    public static final String USER = "demo";
    public static final String PASSWORD = "Demo-Kinal-2026!";
    private DemoData() {}
    public static void seed() throws Exception {
        try(Connection c=Conexion.getInstance().getConexion()) {
            String catalog=c.getCatalog();
            if(catalog==null || !(catalog.equals("superkinal_demo") || catalog.equals("superkinal_audit_test")))
                throw new SQLException("El seed solo admite superkinal_demo o superkinal_audit_test. No modifica bases personales.");
            try(Statement statement=c.createStatement();ResultSet rows=statement.executeQuery("SELECT COUNT(*) FROM Clientes")) {
                rows.next(); if(rows.getInt(1)>0) throw new SQLException("El seed requiere un esquema vacío recién creado.");
            }
            c.setAutoCommit(false);
            try {
                execute(c,"INSERT INTO Usuarios(ingresoUsuario,ingresoClave) VALUES(?,?)",USER,PasswordHash.hash(PASSWORD.toCharArray()));
                execute(c,"INSERT INTO Clientes VALUES(1,'DEMO001','Ana Demo','Ejemplo','Zona ficticia 1','00000001','ana@example.test')");
                execute(c,"INSERT INTO Clientes VALUES(2,'DEMO002','Luis Demo','Muestra','Zona ficticia 2','00000002','luis@example.test')");
                execute(c,"INSERT INTO Clientes VALUES(3,'DEMO003','Equipo Demo','Laboratorio','Zona ficticia 3','00000003','equipo@example.test')");
                execute(c,"INSERT INTO Proveedores VALUES(1,'PROV001','Suministros','Demo','Direccion ficticia','Proveedor de laboratorio','Contacto Demo','https://example.test')");
                execute(c,"INSERT INTO TipoProducto VALUES(1,'Abarrotes de demostracion')");
                execute(c,"INSERT INTO CargoEmpleado VALUES(1,'Cajero Demo','Puesto de laboratorio')");
                execute(c,"INSERT INTO Empleados VALUES(1,'Elena Demo','Ejemplo',3500.00,'Direccion ficticia','Matutino',1)");
                execute(c,"INSERT INTO Productos(codigoProducto,descripcionProducto,imagenProducto,codigoTipoProducto,codigoProveedor) VALUES('CAFE-DEMO','Cafe de muestra','',1,1),('ARROZ-DEMO','Arroz de muestra','',1,1),('TAZA-DEMO','Taza de muestra','',1,1)");
                execute(c,"INSERT INTO Compras(numeroDocumento,fechaDocumento,descripcion) VALUES(1,'2026-01-15','Inventario inicial sintetico')");
                execute(c,"INSERT INTO DetalleCompra(codigoDetalleCompra,costoUnitario,cantidad,codigoProducto,numeroDocumento) VALUES(1,25.00,30,'CAFE-DEMO',1),(2,12.50,40,'ARROZ-DEMO',1),(3,18.00,20,'TAZA-DEMO',1)");
                execute(c,"INSERT INTO Factura(numeroFactura,estado,fechaFactura,codigoCliente,codigoEmpleado) VALUES(1,'Demo', '2026-01-16',1,1),(2,'Demo','2026-01-16',2,1)");
                execute(c,"INSERT INTO DetalleFactura(codigoDetalleFactura,cantidad,numeroFactura,codigoProducto) VALUES(1,2,1,'CAFE-DEMO'),(2,3,1,'ARROZ-DEMO'),(3,1,2,'TAZA-DEMO')");
                c.commit();
            } catch(Exception error) {c.rollback();throw error;}
        }
    }
    private static void execute(Connection c,String sql,Object... values) throws SQLException {
        try(PreparedStatement statement=c.prepareStatement(sql)) {for(int i=0;i<values.length;i++) statement.setObject(i+1,values[i]);statement.executeUpdate();}
    }
    public static void main(String[] args) throws Exception {seed();System.out.println("Seed ficticio creado. Usuario demo / clave de laboratorio: "+PASSWORD);}
}

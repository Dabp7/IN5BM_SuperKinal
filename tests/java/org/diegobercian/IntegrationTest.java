package org.diegobercian;
import static org.junit.Assert.*;
import org.junit.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import org.diegobercian.db.Conexion;
import org.diegobercian.service.AuthService;
import org.diegobercian.repository.CrudRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

public class IntegrationTest {
    private final CrudRepository repo=new CrudRepository();
    private Connection connection() throws SQLException {return Conexion.getInstance().getConexion();}
    private void sql(String sql) throws SQLException {try(Connection c=connection();Statement s=c.createStatement()){s.execute(sql);}}
    private Object scalar(String query) throws SQLException {try(Connection c=connection();Statement s=c.createStatement();ResultSet r=s.executeQuery(query)){r.next();return r.getObject(1);}}
    private void amount(String query,String expected) throws SQLException {assertEquals(0,new BigDecimal(expected).compareTo(new BigDecimal(scalar(query).toString())));}
    private void call(String name,Object...args) throws SQLException {repo.call(name,Arrays.asList(args));}
    private void rejects(String query) {assertThrows(SQLException.class,()->sql(query));}
    @Before public void reset() throws Exception {
        Assume.assumeTrue("1".equals(System.getenv("SUPERKINAL_RUN_INTEGRATION")));
        String url=System.getenv("SUPERKINAL_DB_URL");assertTrue("Integration tests require an isolated audit_test schema",url!=null && url.contains("audit_test"));
        try(Connection c=connection();Statement s=c.createStatement()) {
            s.execute("SET FOREIGN_KEY_CHECKS=0");
            for(String t:new String[]{"MovimientosInventario","DetalleFactura","DetalleCompra","Factura","Compras","Productos","Empleados","TelefonoProveedor","EmailProveedor","Clientes","Proveedores","CargoEmpleado","TipoProducto","Usuarios"}) s.execute("TRUNCATE TABLE "+t);
            s.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        for(int i=1;i<=2;i++) {
            call("sp_agregarClientes",i,"123456789","NombrePrueba","Apellido","Direccion","12345678","test@example.com");
            call("sp_agregarProveedores",i,"123456789","Proveedor","Apellido","Direccion","Razon","Contacto","");
            call("sp_agregarCargoEmpleado",i,"Cargo"+i,"Descripcion");call("sp_agregarTipoProducto",i,"Tipo"+i);
            call("sp_agregarEmpleados",i,"Empleado"+i,"Apellido",new BigDecimal("3000.50"),"Direccion","Mañana",1);
            call("sp_agregarCompras",i,java.time.LocalDate.of(2026,10,20),"Compra"+i);
            call("sp_agregarFactura",i,"Abierta",java.time.LocalDate.of(2026,10,20),1,1);
            call("sp_agregarProductos","P"+i,"Producto"+i,"",1,1);
        }
    }
    @Test public void automaticIdentifiersAreUniqueAndLedgerUsesAssignedCodes() throws Exception {
        Object first=repo.call("sp_agregarClientes",Arrays.asList(null,"123","Auto","Apellido","Direccion","12345678","test@example.com"));
        Object second=repo.call("sp_agregarClientes",Arrays.asList(null,"123","Auto2","Apellido","Direccion","12345678","test@example.com"));
        assertNotNull(first);assertNotEquals(first,second);
        Object product=repo.call("sp_agregarProductos",Arrays.asList(null,"Producto automático","",1,1));
        assertTrue(product.toString().matches("P[0-9]{14}"));
        Object other=repo.call("sp_agregarProductos",Arrays.asList(null,"Otro producto","",1,1));assertNotEquals(product,other);
        Object purchase=repo.call("sp_agregarDetalleCompra",Arrays.asList(null,new BigDecimal("10"),3,product,1));
        Object sale=repo.call("sp_agregarDetalleFactura",Arrays.asList(null,1,1,product));
        amount("SELECT COUNT(*) FROM MovimientosInventario WHERE origen='compra:alta' AND referencia="+purchase,"1");
        amount("SELECT COUNT(*) FROM MovimientosInventario WHERE origen='venta:alta' AND referencia="+sale,"1");
        amount("SELECT existencia FROM Productos WHERE codigoProducto='"+product+"'","2");
        assertThrows(SQLException.class,()->call("sp_agregarClientes",-5,"123","Auto","Apellido","Direccion","12345678","test@example.com"));
    }
    @Test public void searchesReturnOnlyRequestedRow() throws Exception {
        for(String p:new String[]{"Clientes","Proveedores","CargoEmpleado","TipoProducto","Compras","Facturas","Empleados"}) {
            try(Connection c=connection();CallableStatement s=c.prepareCall("{call sp_buscar"+p+"(?)}")) {
                s.setInt(1,1);try(ResultSet r=s.executeQuery()){assertTrue(p,r.next());assertEquals(1,r.getInt(1));assertFalse(p,r.next());}
            }
        }
        assertThrows(SQLException.class,()->call("sp_actualizarClientes",999,"123","Nombre","Apellido","Direccion","12345678","test@example.com"));
        assertEquals("NombrePrueba",scalar("SELECT nombresCliente FROM Clientes WHERE codigoCliente=1"));
        assertEquals("123456789",scalar("SELECT NITClientes FROM Clientes WHERE codigoCliente=1"));
    }
    @Test public void correctDeleteTargets() throws Exception {
        call("sp_agregarCargoEmpleado",3,"Temporal","Descripcion");call("sp_agregarTipoProducto",3,"Temporal");
        call("sp_eliminarCargoEmpleado",3);amount("SELECT COUNT(*) FROM TipoProducto WHERE codigoTipoProducto=3","1");
        call("sp_agregarDetalleCompra",1,new BigDecimal("10"),2,"P1",1);call("sp_eliminarDetalleCompra",1);
        amount("SELECT COUNT(*) FROM Factura WHERE numeroFactura=1","1");
        call("sp_agregarDetalleCompra",2,new BigDecimal("10"),2,"P1",1);call("sp_agregarDetalleFactura",1,1,1,"P1");call("sp_eliminarDetalleFactura",1);
        amount("SELECT COUNT(*) FROM Factura WHERE numeroFactura=1","1");
        assertThrows(SQLException.class,()->call("sp_eliminarDetalleFactura",999));
    }
    @Test public void purchaseTotalsAndInventoryDeltas() throws Exception {
        call("sp_agregarDetalleCompra",1,new BigDecimal("10"),2,"P1",1);call("sp_agregarDetalleCompra",2,new BigDecimal("20"),3,"P1",1);
        amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","5");amount("SELECT totalDocumento FROM Compras WHERE numeroDocumento=1","80");
        call("sp_actualizarDetalleCompra",1,new BigDecimal("15"),2,"P1",1);amount("SELECT totalDocumento FROM Compras WHERE numeroDocumento=1","90");
        call("sp_actualizarDetalleCompra",2,new BigDecimal("20"),3,"P2",2);
        amount("SELECT totalDocumento FROM Compras WHERE numeroDocumento=1","30");amount("SELECT totalDocumento FROM Compras WHERE numeroDocumento=2","60");
        amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","2");amount("SELECT existencia FROM Productos WHERE codigoProducto='P2'","3");
        call("sp_eliminarDetalleCompra",2);amount("SELECT totalDocumento FROM Compras WHERE numeroDocumento=2","0");
        amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","2");
    }
    @Test public void saleTotalsStockHistoryAndMoves() throws Exception {
        call("sp_agregarDetalleCompra",1,new BigDecimal("10"),10,"P1",1);
        call("sp_agregarDetalleFactura",1,2,1,"P1");amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","8");
        sql("UPDATE Productos SET precioUnitario=77 WHERE codigoProducto='P1'");
        amount("SELECT precioUnitario FROM DetalleFactura WHERE codigoDetalleFactura=1","14");
        call("sp_agregarDetalleFactura",2,1,1,"P1");amount("SELECT totalFactura FROM Factura WHERE numeroFactura=1","105");
        call("sp_ActualizarDetalleFactura",1,new BigDecimal("999"),3,2,"P1");
        amount("SELECT totalFactura FROM Factura WHERE numeroFactura=1","77");amount("SELECT totalFactura FROM Factura WHERE numeroFactura=2","42");
        amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","6");
        call("sp_eliminarDetalleFactura",1);amount("SELECT totalFactura FROM Factura WHERE numeroFactura=2","0");
        amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","9");
    }
    @Test public void stockFailureRollsBackWholeStatement() throws Exception {
        call("sp_agregarDetalleCompra",1,new BigDecimal("10"),2,"P1",1);
        assertThrows(SQLException.class,()->call("sp_agregarDetalleFactura",1,3,1,"P1"));
        amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","2");amount("SELECT totalFactura FROM Factura WHERE numeroFactura=1","0");
        amount("SELECT COUNT(*) FROM DetalleFactura","0");
        call("sp_agregarDetalleFactura",1,2,1,"P1");
        assertThrows(SQLException.class,()->call("sp_eliminarDetalleCompra",1));
        amount("SELECT COUNT(*) FROM DetalleCompra","1");
    }
    @Test public void parentDeletesPreserveDocuments() throws Exception {
        call("sp_agregarDetalleCompra",1,new BigDecimal("10"),2,"P1",1);call("sp_agregarDetalleFactura",1,1,1,"P1");
        for(String query:new String[]{"DELETE FROM Productos WHERE codigoProducto='P1'","DELETE FROM Clientes WHERE codigoCliente=1","DELETE FROM Proveedores WHERE codigoProveedor=1","DELETE FROM CargoEmpleado WHERE codigoCargoEmpleado=1","DELETE FROM TipoProducto WHERE codigoTipoProducto=1","DELETE FROM Empleados WHERE codigoEmpleado=1","DELETE FROM Factura WHERE numeroFactura=1","DELETE FROM Compras WHERE numeroDocumento=1"}) rejects(query);
        amount("SELECT totalFactura FROM Factura WHERE numeroFactura=1","14");
    }
    @Test public void domainConstraintsRejectBadRows() throws Exception {
        for(String query:new String[]{"UPDATE Productos SET existencia=-1","UPDATE Productos SET codigoProveedor=NULL","UPDATE Factura SET totalFactura=NULL","UPDATE Factura SET fechaFactura='2026-02-30'","INSERT INTO Usuarios(ingresoUsuario,ingresoClave) VALUES ('','')"}) rejects(query);
        assertThrows(SQLException.class,()->call("sp_agregarDetalleCompra",1,new BigDecimal("1"),-1,"P1",1));
        amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","0");
    }
    @Test public void employeeUpdateAffectsOnlySelectedEmployee() throws Exception {
        call("sp_actualizarEmpleados",1,"Nuevo","Apellido",new BigDecimal("4000.50"),"Direccion","Tarde",2);
        assertEquals("Nuevo",scalar("SELECT nombresEmpleado FROM Empleados WHERE codigoEmpleado=1"));assertEquals("Empleado2",scalar("SELECT nombresEmpleado FROM Empleados WHERE codigoEmpleado=2"));
        assertEquals("Tarde",scalar("SELECT turno FROM Empleados WHERE codigoEmpleado=1"));amount("SELECT codigoCargoEmpleado FROM Empleados WHERE codigoEmpleado=1","2");
    }
    @Test public void remainingCrudContracts() throws Exception {
        call("sp_actualizarClientes",2,"NIT2","Ana","Lopez","Zona 1","87654321","ana@example.com");
        call("sp_actualizarProveedores",2,"NIT2","Prov","Apellido","Zona 2","Empresa","Contacto","");
        call("sp_actualizarCargoEmpleado",2,"Gerente","Descripcion");call("sp_actualizarTipoProducto",2,"Alimentos");
        call("sp_actualizarCompras",2,java.time.LocalDate.of(2026,10,21),"Compra editada");
        call("sp_actualizarFacturas",2,"Abierta",java.time.LocalDate.of(2026,10,21),2,2);
        call("sp_actualizarProductos","P2","Producto editado","",2,2);
        call("sp_agregarEmailProveedor",1,"prov@example.com","Contacto",2);call("sp_actualizarEmailProveedor",1,"nuevo@example.com","Nuevo",2);
        call("sp_agregarTelefonoProveedor",1,"12345678","","Contacto",2);call("sp_actualizarTelefonoProveedor",1,"87654321","12345678","Nuevo",2);
        assertEquals("Ana",scalar("SELECT nombresCliente FROM Clientes WHERE codigoCliente=2"));assertEquals("Empresa",scalar("SELECT razonSocial FROM Proveedores WHERE codigoProveedor=2"));
        assertEquals("Alimentos",scalar("SELECT descripcion FROM TipoProducto WHERE codigoTipoProducto=2"));
        assertEquals("nuevo@example.com",scalar("SELECT emailProveedor FROM EmailProveedor WHERE codigoEmailProveedor=1"));assertEquals("87654321",scalar("SELECT numeroPrincipal FROM TelefonoProveedor WHERE codigoTelefonoProveedor=1"));
        call("sp_eliminarEmailProveedor",1);call("sp_eliminarTelefonoProveedor",1);call("sp_eliminarProductos","P2");call("sp_eliminarCompras",2);call("sp_eliminarFacturas",2);call("sp_eliminarEmpleados",2);call("sp_eliminarClientes",2);call("sp_eliminarProveedores",2);call("sp_eliminarTipoProducto",2);call("sp_eliminarCargoEmpleado",2);
    }
    @Test public void concurrentSalesCannotOversell() throws Exception {
        call("sp_agregarDetalleCompra",1,new BigDecimal("10"),5,"P1",1);
        ExecutorService pool=Executors.newFixedThreadPool(2);CountDownLatch ready=new CountDownLatch(1);
        try {
            List<Future<Boolean>> futures=new ArrayList<>();
            for(int i=1;i<=2;i++){final int id=i;futures.add(pool.submit(()->{ready.await();try{call("sp_agregarDetalleFactura",id,4,id,"P1");return true;}catch(SQLException e){return false;}}));}
            ready.countDown();int successes=0;for(Future<Boolean> f:futures) if(f.get(20,TimeUnit.SECONDS))successes++;
            assertEquals(1,successes);amount("SELECT existencia FROM Productos WHERE codigoProducto='P1'","1");
            amount("SELECT SUM(totalFactura) FROM Factura","56");
        } finally {pool.shutdownNow();}
    }
    @Test public void registrationIsAtomicAndPasswordsAreHashed() throws Exception {
        AuthService auth=new AuthService();String pass="una clave larga 2026";
        ExecutorService pool=Executors.newFixedThreadPool(2);
        try {
            Callable<Boolean> register=()->{try{auth.register("usuario",pass.toCharArray());return true;}catch(SQLException e){return false;}};
            Future<Boolean> one=pool.submit(register),two=pool.submit(register);assertNotEquals(one.get(),two.get());
            assertTrue(scalar("SELECT ingresoClave FROM Usuarios").toString().startsWith("pbkdf2-sha256$"));assertTrue(auth.login("usuario",pass.toCharArray()));
            assertFalse(auth.login("usuario","incorrecta".toCharArray()));
        } finally {pool.shutdownNow();}
    }
    @Test public void legacyPasswordConversionIsIdempotent() throws Exception {
        sql("INSERT INTO Usuarios(ingresoUsuario,ingresoClave) VALUES ('legacy','clave anterior')");
        AuthService.main(new String[0]);
        String first=scalar("SELECT ingresoClave FROM Usuarios WHERE ingresoUsuario='legacy'").toString();
        assertTrue(first.startsWith("pbkdf2-sha256$"));
        assertTrue(new AuthService().login("legacy","clave anterior".toCharArray()));
        AuthService.main(new String[0]);
        assertEquals(first,scalar("SELECT ingresoClave FROM Usuarios WHERE ingresoUsuario='legacy'"));
    }
    @Test public void reportsFillWithRealDataAndPortableImages() throws Exception {
        call("sp_agregarDetalleCompra",1,new BigDecimal("10"),4,"P1",1);call("sp_agregarDetalleFactura",1,2,1,"P1");
        for(String name:new String[]{"Reporte","ProductosReporte","ProveedoresFactura","reporteFactura"}) {
            try(Connection c=connection();InputStream input=getClass().getResourceAsStream("/org/diegobercian/reportes/"+name+".jasper")) {
                assertNotNull(input);JasperReport report=(JasperReport)JRLoader.loadObject(input);Map<String,Object> params=new HashMap<>();params.put("numeroFactura",1);
                JasperPrint print=JasperFillManager.fillReport(report,params,c);assertFalse(name,print.getPages().isEmpty());
                JasperExportManager.exportReportToPdfFile(print,"target/"+name+"-test.pdf");
            }
        }
    }
    @Test public void tenThousandProductsArePagedAndResourcesClose() throws Exception {
        try(Connection c=connection();PreparedStatement s=c.prepareStatement("INSERT INTO Productos(codigoProducto,descripcionProducto,imagenProducto,codigoTipoProducto,codigoProveedor) VALUES (?,?, '',1,1)")) {
            c.setAutoCommit(false);for(int i=0;i<10000;i++){s.setString(1,"LOAD"+String.format("%05d",i));s.setString(2,"Producto "+i);s.addBatch();}s.executeBatch();c.commit();
        }
        long start=System.nanoTime();
        for(int i=0;i<30;i++) assertEquals(100,repo.page("Productos","codigoProducto",Arrays.asList("codigoProducto","descripcionProducto"),i*100,100).size());
        assertEquals(10002,repo.count("Productos"));System.out.println("30 pages / 10,002 products: "+TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-start)+" ms");
    }
}

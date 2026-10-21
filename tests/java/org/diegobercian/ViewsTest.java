package org.diegobercian;
import static org.junit.Assert.*;
import org.junit.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Window;
import org.diegobercian.controller.CrudController;
import org.diegobercian.system.Main;
import org.diegobercian.ui.Async;

public class ViewsTest {
    private static <T> T fx(Callable<T> task) throws Exception {
        FutureTask<T> future=new FutureTask<>(task);Platform.runLater(future);return future.get(10,TimeUnit.SECONDS);
    }
    @BeforeClass public static void start() throws Exception {
        Assume.assumeTrue("1".equals(System.getenv("SUPERKINAL_RUN_INTEGRATION")));
        new JFXPanel();Platform.setImplicitExit(false);
    }
    @AfterClass public static void closeWindows() throws Exception {if("1".equals(System.getenv("SUPERKINAL_RUN_INTEGRATION")))fx(()->{for(Window w:new ArrayList<>(Window.impl_getWindows()==null?Collections.<Window>emptyList():windows()))w.hide();return null;});}
    private static List<Window> windows(){List<Window> list=new ArrayList<>();Iterator<Window> it=Window.impl_getWindows();while(it.hasNext())list.add(it.next());return list;}
    private static Object field(Object object,String name) throws Exception {
        for(Class<?> cls=object.getClass();cls!=null;cls=cls.getSuperclass()) {
            try{Field f=cls.getDeclaredField(name);f.setAccessible(true);return f.get(object);}catch(NoSuchFieldException ignored){}
        }throw new NoSuchFieldException(name);
    }
    private static boolean busy(Object object) throws Exception {return (Boolean)field(object,"busy");}
    private static void awaitIdle(Object controller) throws Exception {
        long end=System.nanoTime()+TimeUnit.SECONDS.toNanos(20);
        while(System.nanoTime()<end){if(fx(()->!busy(controller)))return;Thread.sleep(25);}fail("UI request timed out");
    }
    @Test public void allEighteenViewsLoadAgainstRealDatabase() throws Exception {
        int count=0;
        try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get("src/org/diegobercian/view"),"*.fxml")) {
            for(Path path:files) {
                Object controller=fx(()->{FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/diegobercian/view/"+path.getFileName()));Parent parent=loader.load();assertNotNull(parent);return loader.getController();});
                if(controller instanceof CrudController) {
                    awaitIdle(controller);
                    fx(()->{((CrudController)controller).seleccionarElemento();TableView<?> table=(TableView<?>)field(controller,"table");assertFalse(table.isDisabled());return null;});
                }
                count++;
            }
        }
        assertEquals(18,count);
    }
    @Test public void savingAndDuplicateFailureKeepCorrectFormState() throws Exception {
        MenuResult result=fx(()->{FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/diegobercian/view/menuCliente.fxml"));loader.load();return new MenuResult(loader.getController());});
        CrudController controller=result.controller;awaitIdle(controller);
        fx(()->{controller.agregar();setClient(controller,"900001");controller.guardar();return null;});awaitIdle(controller);
        assertEquals("VIEW",fx(()->field(controller,"mode").toString()));
        fx(()->{controller.agregar();setClient(controller,"900001");controller.guardar();return null;});awaitIdle(controller);
        assertEquals("ADD",fx(()->field(controller,"mode").toString()));
        assertEquals("NombrePrueba",fx(()->((TextField)field(controller,"txtNombreC")).getText()));
        fx(()->{controller.cancelar();return null;});
    }
    private static class MenuResult {final CrudController controller;MenuResult(Object controller){this.controller=(CrudController)controller;}}
    private static void setClient(CrudController c,String id) throws Exception {
        String[][] values={{"txtCodigoC",id},{"txtNombreC","NombrePrueba"},{"txtApellidoC","Apellido"},{"txtNitC","123456789"},{"txtDireccionC","Direccion"},{"txtTelefonoC","12345678"},{"txtCorreoC","test@example.com"}};
        for(String[] pair:values)((TextField)field(c,pair[0])).setText(pair[1]);
    }
    public static class NavigationMain extends Main {
        String visited;
        @Override public void menuClientesView(){visited="clientes";}
        @Override public void ComprasView(){visited="compras";}
        @Override public void ProveedoresView(){visited="proveedores";}
    }
    @Test public void repairedNavigationActionsReachTheirScreens() throws Exception {
        NavigationMain main=new NavigationMain();
        fx(()->{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/diegobercian/view/menuGeneral.fxml"));loader.load();
            org.diegobercian.controller.MenuPrincipalController c=loader.getController();c.setEscenarioPrincipal(main);
            ((MenuItem)loader.getNamespace().get("btnMenuClientes")).fire();assertEquals("clientes",main.visited);
            ((MenuItem)loader.getNamespace().get("btnMenuCompras")).fire();assertEquals("compras",main.visited);
            loader=new FXMLLoader(getClass().getResource("/org/diegobercian/view/menuTelefonoProveedor.fxml"));loader.load();
            org.diegobercian.controller.MenuTelefonoProveedorController phone=loader.getController();phone.setEscenarioPrincipal(main);
            ((MenuItem)loader.getNamespace().get("btnProveedor")).fire();assertEquals("proveedores",main.visited);return null;
        });
    }
    @Test public void slowBackgroundWorkDoesNotBlockJavaFx() throws Exception {
        CountDownLatch finished=new CountDownLatch(1);CountDownLatch responsive=new CountDownLatch(1);
        fx(()->{Async.run(()->{Thread.sleep(500);return true;},ok->finished.countDown(),error->finished.countDown());Platform.runLater(responsive::countDown);return null;});
        assertTrue(responsive.await(300,TimeUnit.MILLISECONDS));assertEquals(1,finished.getCount());assertTrue(finished.await(3,TimeUnit.SECONDS));
    }
}

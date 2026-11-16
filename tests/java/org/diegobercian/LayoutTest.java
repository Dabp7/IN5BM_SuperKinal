package org.diegobercian;

import static org.junit.Assert.*;
import org.junit.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.Field;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.diegobercian.controller.CrudController;
import org.diegobercian.repository.CrudRepository;
import org.diegobercian.system.Main;
import org.diegobercian.ui.ResponsiveGrid;

public class LayoutTest {
    private static <T>T fx(Callable<T> work)throws Exception{FutureTask<T> task=new FutureTask<>(work);Platform.runLater(task);return task.get(20,TimeUnit.SECONDS);}
    private static Object field(Object object,String name)throws Exception{
        for(Class<?> type=object.getClass();type!=null;type=type.getSuperclass())try{Field f=type.getDeclaredField(name);f.setAccessible(true);return f.get(object);}catch(NoSuchFieldException ignored){}
        throw new NoSuchFieldException(name);
    }
    private static void idle(Object controller)throws Exception{
        long deadline=System.nanoTime()+TimeUnit.SECONDS.toNanos(20);
        while(System.nanoTime()<deadline){if(fx(()->!(Boolean)field(controller,"busy")))return;Thread.sleep(25);}fail("Pending request");
    }
    @BeforeClass public static void toolkit(){Assume.assumeTrue("1".equals(System.getenv("SUPERKINAL_RUN_INTEGRATION")));new JFXPanel();Platform.setImplicitExit(false);}
    @Test public void allScreensReflowAtFourViewportSizes()throws Exception{
        Path captures=Paths.get("target/ui-screenshots");Files.createDirectories(captures);
        try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get("src/org/diegobercian/view"),"*.fxml")){
            for(Path file:files){
                FXMLLoader loader=fx(()->{FXMLLoader value=new FXMLLoader(getClass().getResource("/org/diegobercian/view/"+file.getFileName()));value.load();return value;});
                Parent root=loader.getRoot();Object controller=loader.getController();
                if(controller instanceof CrudController)idle(controller);
                for(int[] size:new int[][]{{360,740},{768,720},{1366,768},{1920,1080}}){
                    fx(()->{
                        if(root.getScene()!=null)root.getScene().setRoot(new Group());
                        Scene scene=new Scene(root,size[0],size[1]);
                        Region region=(Region)root;
                        for(int pass=0;pass<5;pass++){region.resize(size[0],size[1]);root.applyCss();root.layout();}
                        for(Node node:root.lookupAll(".responsive-grid")){
                            ResponsiveGrid grid=(ResponsiveGrid)node;int count=0;
                            for(Node child:grid.getChildren())if(child.isManaged()){
                                assertTrue(file+" left",child.getBoundsInParent().getMinX()>=-1);
                                assertTrue(file+" right at "+size[0],child.getBoundsInParent().getMaxX()<=grid.getWidth()+1);
                                assertTrue(file+" form height at "+size[0],child.getBoundsInParent().getMaxY()<=grid.getHeight()+1);
                                if(Math.abs(child.getBoundsInParent().getMinY())<1)count++;
                            }
                            assertEquals(file.toString(),Math.min(grid.columnsFor(grid.getWidth()),(int)grid.getChildren().stream().filter(Node::isManaged).count()),count);
                        }
                        for(Node card:root.lookupAll(".auth-card")) {
                            assertTrue(card.getBoundsInParent().getMaxX()<=card.getParent().getLayoutBounds().getWidth()+1);
                            assertTrue(card.getLayoutBounds().getHeight()<=card.prefHeight(card.getLayoutBounds().getWidth())+1);
                        }
                        if(file.toString().contains("Productos") || file.toString().contains("menuGeneral") || file.toString().contains("menuLogin") || file.toString().contains("menuCliente")){
                            WritableImage image=root.snapshot(new SnapshotParameters(),null);
                            ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",captures.resolve(file.getFileName().toString().replace(".fxml","")+"-"+size[0]+".png").toFile());
                        }
                        if(file.toString().contains("menuProductosView")) {
                            ((CrudController)controller).agregar();
                            for(int pass=0;pass<5;pass++){root.applyCss();root.layout();}
                            WritableImage image=root.snapshot(new SnapshotParameters(),null);
                            ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",captures.resolve("producto-agregar-"+size[0]+".png").toFile());
                            ((CrudController)controller).cancelar();
                        }
                        return null;
                    });
                }
                if(controller instanceof CrudController)fx(()->{
                    CrudController crud=(CrudController)controller;TableView<?> table=(TableView<?>)field(crud,"table");
                    assertFalse(table.isEditable());crud.agregar();
                    Object[] fields=(Object[])field(crud,"fields");Control key=(Control)field(fields[0],"control");
                    assertFalse(key.getParent().isVisible());assertFalse(key.getParent().isManaged());assertFalse(((TextField)key).isEditable());
                    crud.cancelar();return null;
                });
            }
        }
    }
    @Test public void productsSaveWithoutARequiredCodeAndCalculatedValuesStayProtected()throws Exception{
        CrudRepository repo=new CrudRepository();
        if(repo.count("Proveedores")==0)repo.call("sp_agregarProveedores",Arrays.asList(null,"123","Proveedor","Apellido","Direccion","Razon","Contacto",""));
        if(repo.count("TipoProducto")==0)repo.call("sp_agregarTipoProducto",Arrays.asList(null,"Tipo"));
        CrudController controller=fx(()->{FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/diegobercian/view/menuProductosView.fxml"));loader.load();return loader.getController();});idle(controller);
        fx(()->{
            controller.agregar();((TextField)field(controller,"txtDescripcionPro")).setText("Producto de prueba adaptable");
            ((ComboBox<?>)field(controller,"cmbProveedores")).getSelectionModel().selectFirst();
            ((ComboBox<?>)field(controller,"cmbTipoProducto")).getSelectionModel().selectFirst();
            assertEquals("",((TextField)field(controller,"txtCodigoPro")).getText());controller.guardar();return null;
        });idle(controller);
        String code=fx(()->{
            assertEquals("VIEW",field(controller,"mode").toString());
            TextField identity=(TextField)field(controller,"txtCodigoPro");assertFalse(identity.isEditable());
            TableView<?> table=(TableView<?>)field(controller,"table");assertNotNull(table.getSelectionModel().getSelectedItem());
            String id=identity.getText();assertTrue(id.matches("P[0-9]{14}"));controller.editar();assertFalse(identity.isEditable());
            assertFalse(table.isEditable());controller.cancelar();return id;
        });
        assertEquals(1,repo.query("SELECT codigoProducto FROM Productos WHERE codigoProducto=?",code).size());
    }
    @Test public void navigationPreservesUserWindowSize()throws Exception{
        fx(()->{
            Main main=new Main();Stage stage=new Stage();main.start(stage);stage.setWidth(780);stage.setHeight(620);
            double width=stage.getWidth(),height=stage.getHeight();
            main.ProductosView();assertEquals(width,stage.getWidth(),1);assertEquals(height,stage.getHeight(),1);
            main.RegistroView();assertEquals(width,stage.getWidth(),1);assertEquals(height,stage.getHeight(),1);
            stage.close();return null;
        });
    }
}

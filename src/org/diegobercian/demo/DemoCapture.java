package org.diegobercian.demo;

import java.io.*;
import java.nio.file.*;
import java.lang.reflect.*;
import java.util.concurrent.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.Region;
import javax.imageio.ImageIO;
import org.diegobercian.controller.CrudController;

/** Renders the application's real FXML and database rows, without controlling desktop windows. */
public final class DemoCapture {
    private static <T> T fx(Callable<T> action) throws Exception {
        FutureTask<T> future=new FutureTask<>(action);Platform.runLater(future);return future.get(30,TimeUnit.SECONDS);
    }
    private static Field field(Class<?> type,String name) throws Exception {
        while(type!=null) {try{Field f=type.getDeclaredField(name);f.setAccessible(true);return f;}catch(NoSuchFieldException ignored){type=type.getSuperclass();}}
        throw new NoSuchFieldException(name);
    }
    public static void main(String[] args) throws Exception {
        Path output=Paths.get(args.length==0?"docs/demo":args[0]);Files.createDirectories(output);
        new JFXPanel();Platform.setImplicitExit(false);
        try {
            String[][] views={{"menuCliente.fxml","clientes.png"},{"menuProductosView.fxml","productos.png"},{"menuFacturas.fxml","facturas.png"}};
            for(String[] view:views) {
                final FXMLLoader loader=new FXMLLoader(DemoCapture.class.getResource("/org/diegobercian/view/"+view[0]));
                final Parent root=fx(()->{Parent p=loader.load();new Scene(p,1355,720);if(p instanceof Region)((Region)p).resize(1355,720);return p;});
                final CrudController controller=loader.getController();
                long timeout=System.nanoTime()+TimeUnit.SECONDS.toNanos(30);
                while(fx(()->(Boolean)field(controller.getClass(),"busy").get(controller))) {
                    if(System.nanoTime()>timeout)throw new IllegalStateException("La vista no terminó su consulta: "+view[0]);Thread.sleep(50);
                }
                fx(()->{root.applyCss();root.layout();ImageIO.write(SwingFXUtils.fromFXImage(root.snapshot(null,null),null),"png",output.resolve(view[1]).toFile());return null;});
                System.out.println("Captura real: "+output.resolve(view[1]));
            }
        } finally {Platform.exit();}
        System.exit(0);
    }
}

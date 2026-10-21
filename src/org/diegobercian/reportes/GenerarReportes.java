package org.diegobercian.reportes;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.diegobercian.db.Conexion;
import org.diegobercian.ui.Async;

public final class GenerarReportes {
    public static void mostrarReportes(String nombre, String titulo, Map<String,Object> parametros) {
        Map<String,Object> snapshot=new HashMap<>(parametros);
        Async.run(() -> {
            try (Connection connection=Conexion.getInstance().getConexion();
                 InputStream resource=GenerarReportes.class.getResourceAsStream(nombre)) {
                if(resource==null) throw new IllegalStateException("No se encontró el reporte. Compile el proyecto completo.");
                JasperReport report=(JasperReport)JRLoader.loadObject(resource);
                return JasperFillManager.fillReport(report,snapshot,connection);
            }
        }, print -> SwingUtilities.invokeLater(() -> {
            JasperViewer viewer=new JasperViewer(print,false);viewer.setTitle(titulo);viewer.setVisible(true);
        }), Async::error);
    }
}

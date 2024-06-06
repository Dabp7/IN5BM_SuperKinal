
package org.diegobercian.reportes;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.diegobercian.db.Conexion;

/**
 *
 * @author informatica
 */
public class GenerarReportes {
    
    
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        
        try{
            JasperReport ReporteCliente = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(ReporteCliente,parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}





/*
    Interface Map
    HashMap es uno de los objetos que implementa un conjunto de key-value
    Tiene un constructor sin parametros new Map() y su finalidad suele referirse  para agrupar
    información en un único objeto

    Tiene cierta similitud con la colección de objetos (ArrayList) pero con la diferencia 
    que estos no tienen orden

    Hash hace referencia a una tecnica de organización de archivos, Hasp abierto y cerrado en la que se almaneca una fucnion se aplica a la llave del registro en una direccion que es generada
    por una fucnion aplica a la llave que le indiquemos nosotros

    Cada objeto se identifica metienda algun identificador apropiado
*/

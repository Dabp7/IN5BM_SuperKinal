package org.diegobercian.reportes;
import java.nio.file.*;
import java.io.InputStream;
import net.sf.jasperreports.engine.JasperCompileManager;
public final class CompileReports {
    public static void main(String[] args) throws Exception {
        Path folder=Paths.get(args[0]);
        try (DirectoryStream<Path> files=Files.newDirectoryStream(folder,"*.jrxml")) {
            for (Path file:files) {
                JasperCompileManager.compileReportToFile(file.toString(),file.toString().replace(".jrxml",".jasper"));
                System.out.println("Compiled: "+file.getFileName());
            }
        }
    }
}

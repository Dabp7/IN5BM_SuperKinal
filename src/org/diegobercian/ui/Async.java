package org.diegobercian.ui;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public final class Async {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r, "superkinal-db"); t.setDaemon(true); return t;
    });
    private Async() { }
    public static <T> void run(Callable<T> work, Consumer<T> success, Consumer<Throwable> failure) {
        Task<T> task = new Task<T>() { @Override protected T call() throws Exception { return work.call(); } };
        task.setOnSucceeded(e -> success.accept(task.getValue()));
        task.setOnFailed(e -> failure.accept(task.getException()));
        EXECUTOR.execute(task);
    }
    public static void error(Throwable error) {
        if (!Platform.isFxApplicationThread()) { Platform.runLater(() -> error(error)); return; }
        String message = error.getMessage();
        if (error instanceof java.sql.SQLException) {
            java.sql.SQLException sql = (java.sql.SQLException) error;
            if (sql.getErrorCode() == 1062) message = "El código o usuario ya existe. Use uno diferente.";
            else if (sql.getErrorCode() == 1451) message = "Este registro tiene documentos o movimientos asociados y debe conservarse.";
            else if (sql.getErrorCode() == 1452) message = "La relación seleccionada ya no existe. Actualice los catálogos.";
            else if (sql.getSQLState() != null && sql.getSQLState().startsWith("08")) message = "No se pudo conectar. Revise la conexión y vuelva a intentar; sus entradas se conservan.";
            else if (sql.getErrorCode() == 1213 || sql.getErrorCode() == 1205) message = "Otra operación usa estos datos. Vuelva a intentar; sus entradas se conservan.";
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("SuperKinal"); alert.setHeaderText("No se completó la operación");
        alert.setContentText(message == null ? "Revise la conexión y vuelva a intentar." : message);
        alert.show();
    }
}

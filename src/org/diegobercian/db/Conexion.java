package org.diegobercian.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/** Stateless factory. Every caller owns and closes its connection. */
public final class Conexion {
    private static final Conexion INSTANCE = new Conexion();
    public static Conexion getInstance() { return INSTANCE; }
    public Connection getConexion() throws SQLException {
        String user = config("SUPERKINAL_DB_USER", null);
        String password = config("SUPERKINAL_DB_PASSWORD", null);
        if (user == null || password == null) {
            throw new SQLException("Configure la conexión local en " + configPath() + " o las variables SUPERKINAL_DB_USER y SUPERKINAL_DB_PASSWORD y vuelva a intentar.");
        }
        String url = config("SUPERKINAL_DB_URL", "jdbc:mysql://localhost:3306/DBKinalExpress2020527?sslMode=REQUIRED&connectTimeout=5000&socketTimeout=15000&serverTimezone=America/Guatemala");
        return DriverManager.getConnection(url, user, password);
    }
    private static String config(String key, String fallback) throws SQLException {
        String value = System.getProperty(key);
        if (value == null) value = System.getenv(key);
        if (value == null) {
            Path path = configPath();
            if (Files.exists(path)) {
                Properties local = new Properties();
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    local.load(reader);
                    value = local.getProperty(key);
                } catch (IOException | IllegalArgumentException e) {
                    throw new SQLException("No se pudo leer la configuración local de conexión: " + path);
                }
            }
        }
        return value == null ? fallback : value;
    }
    private static Path configPath() {
        String explicit = System.getProperty("superkinal.config");
        if (explicit != null) return Paths.get(explicit);
        String appData = System.getenv("APPDATA");
        if (appData != null && !appData.isEmpty()) return Paths.get(appData, "SuperKinal", "database.properties");
        return Paths.get(System.getProperty("user.home"), ".config", "SuperKinal", "database.properties");
    }
}

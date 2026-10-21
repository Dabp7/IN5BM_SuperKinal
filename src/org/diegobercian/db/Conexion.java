package org.diegobercian.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Stateless factory. Every caller owns and closes its connection. */
public final class Conexion {
    private static final Conexion INSTANCE = new Conexion();
    public static Conexion getInstance() { return INSTANCE; }
    public Connection getConexion() throws SQLException {
        String user = config("SUPERKINAL_DB_USER", null);
        String password = config("SUPERKINAL_DB_PASSWORD", null);
        if (user == null || password == null) {
            throw new SQLException("Configure SUPERKINAL_DB_USER y SUPERKINAL_DB_PASSWORD y vuelva a intentar.");
        }
        String url = config("SUPERKINAL_DB_URL", "jdbc:mysql://localhost:3306/DBKinalExpress2020527?sslMode=REQUIRED&connectTimeout=5000&socketTimeout=15000&serverTimezone=America/Guatemala");
        return DriverManager.getConnection(url, user, password);
    }
    private static String config(String key, String fallback) {
        String value = System.getProperty(key);
        if (value == null) value = System.getenv(key);
        return value == null ? fallback : value;
    }
}

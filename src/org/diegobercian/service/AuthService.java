package org.diegobercian.service;
import java.sql.*;
import java.util.Arrays;
import org.diegobercian.db.Conexion;
import org.diegobercian.security.PasswordHash;

public final class AuthService {
    public static void validate(String user,char[] password,boolean registration) {
        if(user==null || user.trim().isEmpty() || user.length()>60) throw new IllegalArgumentException("Ingrese un usuario de 1 a 60 caracteres.");
        if(password==null || password.length==0 || new String(password).trim().isEmpty() || password.length>128 || (registration && password.length<12)) throw new IllegalArgumentException(registration?"Use una contraseña de 12 a 128 caracteres.":"Ingrese su contraseña.");
    }
    public boolean login(String user,char[] password) throws Exception {
        try(Connection c=Conexion.getInstance().getConexion();PreparedStatement s=c.prepareStatement("SELECT ingresoClave FROM Usuarios WHERE ingresoUsuario=?")) {
            s.setString(1,user.trim());
            try(ResultSet rs=s.executeQuery()) {return rs.next() && PasswordHash.verify(password,rs.getString(1));}
        } finally {Arrays.fill(password,'\0');}
    }
    public void register(String user,char[] password) throws Exception {
        try {
            validate(user,password,true);
            String hash=PasswordHash.hash(password);
            try(Connection c=Conexion.getInstance().getConexion();PreparedStatement s=c.prepareStatement("INSERT INTO Usuarios(ingresoUsuario,ingresoClave) VALUES (?,?)")) {
                s.setString(1,user.trim());s.setString(2,hash);s.executeUpdate();
            }
        } finally {Arrays.fill(password,'\0');}
    }
    /** Run after schema migration with the application stopped. Never permit plaintext login. */
    public static void main(String[] args) throws Exception {
        try(Connection c=Conexion.getInstance().getConexion()) {
            c.setAutoCommit(false);
            try(PreparedStatement select=c.prepareStatement("SELECT idUsuario,ingresoClave FROM Usuarios FOR UPDATE");ResultSet rows=select.executeQuery();PreparedStatement update=c.prepareStatement("UPDATE Usuarios SET ingresoClave=? WHERE idUsuario=?")) {
                int count=0;
                while(rows.next()) {
                    String old=rows.getString(2);
                    if(old.startsWith("pbkdf2-sha256$")) continue;
                    char[] secret=old.toCharArray();String hash;
                    try {hash=PasswordHash.hash(secret);} finally {Arrays.fill(secret,'\0');}
                    update.setString(1,hash);update.setInt(2,rows.getInt(1));update.executeUpdate();count++;
                }
                c.commit();System.out.println("Contraseñas migradas: "+count);
            } catch(Exception e) {c.rollback();throw e;}
        }
    }
}

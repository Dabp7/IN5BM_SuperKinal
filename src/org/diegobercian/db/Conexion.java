/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author informatica
 */
public class Conexion {

    private Connection conexion;
    private static Conexion instancia;

    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/DBKinalExpress2020527?useSLL=false", "2020527_IN5BM", "abc123**");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException a) {
            a.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException a) {
            a.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
    
    public static Conexion getInstance(){
        
        if(instancia == null){
            instancia = new Conexion();
        }
        
        return instancia;
    }

}

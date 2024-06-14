/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.controller;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.diegobercian.bean.Usuarios;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuRegistroController implements Initializable {
 
    @FXML
    private PasswordField txtContraR;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnVolver;
    @FXML
    private TextField txtCorreoR;
    
    @FXML private Button btnRegresar;
    private Main escenarioPrincipal;
    
    
    public void crearUsuario(){
        try{
            ResultSet rs= null;
            PreparedStatement ps = null;
            Conexion conexion = new Conexion();
            
            String consulta = "insert into Usuarios(ingresoUsuario, ingresoClave) values(?,?);";
            ps = conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, txtCorreoR.getText());
            ps.setString(2, txtContraR.getText());
            
            Usuarios verificacion = null;
            
            verificacion = buscarRegistro(txtCorreoR.getText());
            
            if(verificacion == null){
                ps.setString(1, txtCorreoR.getText());
                ps.setString(2, txtContraR.getText());
                ps.execute();
                JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
            }else{
                JOptionPane.showMessageDialog(null, "¡Este usuario ya existe!");
            }
            

                
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public Usuarios buscarRegistro(String correo){
        Usuarios resultado = null;
        ResultSet registro = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall
            ("select * from Usuarios where Usuarios.ingresoUsuario = (?);");
            procedimiento.setString(1, correo);
            registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Usuarios(registro.getInt("idUsuario"),
                                             registro.getString("ingresoUsuario"),
                                             registro.getString("ingresoClave")
            
            );
            }  
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
    }
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
        public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.LoginView();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}

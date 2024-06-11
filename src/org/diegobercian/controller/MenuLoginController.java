/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.controller;

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
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuLoginController implements Initializable  {

    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtContra;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnRegistro;
    private Main escenarioPrincipal;
    
    
    public void validaUsuario(){
        try{
            ResultSet rs= null;
            PreparedStatement ps = null;
            Conexion conexion = new Conexion();
            
            String consulta = "select * from Usuarios where Usuarios.ingresoUsuario = (?) and Usuarios.ingresoClave = (?);";
            ps = conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, txtCorreo.getText());
            ps.setString(2, txtContra.getText());
            rs = ps.executeQuery();
            
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "Usuario ingresado correctamente");
                escenarioPrincipal.MenuPrincipalView();
                
            }else{
                JOptionPane.showMessageDialog(null, "Ingreso Incorrecto, Verifica los datos");
            }
            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegistro) {
            escenarioPrincipal.RegistroView();
        }
    }
    
}

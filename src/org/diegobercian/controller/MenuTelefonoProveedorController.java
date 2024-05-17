/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuTelefonoProveedorController implements Initializable {

    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnProveedores;
    @FXML private MenuItem btnEmail;
    private Main escenarioPrincipal;
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnProveedores){
            escenarioPrincipal.ProveedoresView();
        }if(event.getSource() == btnEmail){
            escenarioPrincipal.EmailProveedorView();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

}


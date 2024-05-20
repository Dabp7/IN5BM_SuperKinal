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
 * @author informatica
 */

public class MenuPrincipalController implements Initializable{
    
    @FXML MenuItem btnMenuClientes;
    @FXML MenuItem btnVistaProgramador;
    @FXML MenuItem btnMenuProveedor;
    @FXML MenuItem btnMenuCompras;
    @FXML MenuItem btnMenuTipoProducto;
    @FXML MenuItem btnMenuProductos;
    @FXML MenuItem btnMenuEmpleados;
    @FXML MenuItem btnMenuFacturas;
    
    private Main escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenuClientes) {
            escenarioPrincipal.menuClientesView();
        }if (event.getSource() == btnVistaProgramador) {
            escenarioPrincipal.programadorView();
        }if (event.getSource() == btnMenuProveedor) {
            escenarioPrincipal.ProveedoresView();
        }if (event.getSource() == btnMenuCompras) {
            escenarioPrincipal.ComprasView();
        }if (event.getSource() == btnMenuTipoProducto) {
            escenarioPrincipal.TipoProductoView();
        }if (event.getSource() == btnMenuProductos) {
            escenarioPrincipal.ProductosView();
        }if (event.getSource() == btnMenuEmpleados) {
            escenarioPrincipal.EmpleadosView();
        }if (event.getSource() == btnMenuFacturas) {
            escenarioPrincipal.FacturaView();
        }
    }
    
    
    
}

package org.diegobercian.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import org.diegobercian.bean.Usuarios;
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
    @FXML MenuItem btnClientesR;
    @FXML MenuItem btnProductosR;
    @FXML MenuItem btnProveedoresR;
    @FXML Button btnMenuHorarios;
    @FXML Button btnCompras;
    @FXML Button btnClientes;
    @FXML Button btnMenuUbicaciones;
    @FXML MenuItem btnSesionCerrar;
    
    
    private Main escenarioPrincipal;
    private Usuarios us = new Usuarios();
    private MenuClientesController clc = new MenuClientesController();
    private MenuProveedoresController prc = new MenuProveedoresController();
    private MenuProductosController pro = new MenuProductosController();

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
        if (event.getSource() == btnClientes) {
            escenarioPrincipal.menuClientesView();
        }if (event.getSource() == btnVistaProgramador) {
            escenarioPrincipal.programadorView();
        }if (event.getSource() == btnMenuProveedor) {
            escenarioPrincipal.ProveedoresView();
        }if (event.getSource() == btnCompras) {
            escenarioPrincipal.ComprasView();
        }if (event.getSource() == btnMenuTipoProducto) {
            escenarioPrincipal.TipoProductoView();
        }if (event.getSource() == btnMenuProductos) {
            escenarioPrincipal.ProductosView();
        }if (event.getSource() == btnMenuEmpleados) {
            escenarioPrincipal.EmpleadosView();
        }if (event.getSource() == btnMenuFacturas) {
            escenarioPrincipal.FacturaView();
        }if (event.getSource() == btnClientesR) {
            clc.imprimirReporteClientes();
        }if (event.getSource() == btnProductosR) {
            pro.imprimirReporteProductos();
        }if (event.getSource() == btnProveedoresR) {
            prc.imprimirReporteProveedores();
        }if (event.getSource() == btnMenuHorarios) {
            escenarioPrincipal.HorariosView();
        }if (event.getSource() == btnSesionCerrar) {
            escenarioPrincipal.LoginView();
        }if (event.getSource() == btnMenuUbicaciones) {
            escenarioPrincipal.UbicacionesView();
        }
    }
    
    
     
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.diegobercian.controller.*;


/**
 *
 * @author informatica
 */
public class Main extends Application {
    
    private Stage escenarioPrincipal;
    private Scene escena;
    
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Kinal Express");
        MenuPrincipalView();
        escenarioPrincipal.getIcons().add(new Image("/org/diegobercian/images/logo3.png"));
        /*Parent root = FXMLLoader.load(getClass().getResource("/org/diegobercian/view/superkinal.fxml"));
        Scene escena = new Scene(root);
        escenarioPrincipal.setScene(escena);*/
        escenarioPrincipal.show();

    }
    
    public Initializable cambiarEscena(String fxmlname, int width, int height) throws Exception{
     
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Main.class.getResourceAsStream("/org/diegobercian/view/" + fxmlname);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource("/org/diegobercian/view/" + fxmlname));
        
        escena = new Scene((AnchorPane)loader.load(file), width, height);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();   
        
        resultado = (Initializable)loader.getController();
        
        return resultado;
        
    }
    
    public void MenuPrincipalView(){
        try {
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)cambiarEscena("menuGeneral.fxml", 1020, 720);
            menuPrincipalView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuClientesView(){
        try{
            MenuClientesController menuClientesView = (MenuClientesController)cambiarEscena("menuCliente.fxml", 1355, 720);
            menuClientesView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }   
    
    public void programadorView(){
        try{
            ProgramadorController programadorView = (ProgramadorController)cambiarEscena("programadorView.fxml", 1020, 720);
            programadorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }  
    
     public void ProveedoresView(){
        try{
            MenuProveedoresController proveedoresView = (MenuProveedoresController)cambiarEscena("menuProveedores.fxml", 1355, 720);
            proveedoresView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    } 
     
     public void ComprasView(){
        try{
            MenuComprasController comprasView = (MenuComprasController)cambiarEscena("menuCompras.fxml", 1355, 720);
            comprasView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    } 
     
     public void TipoProductoView(){
        try{
            MenuTipoProductoController tipoProductoView = (MenuTipoProductoController)cambiarEscena("menuTipoProducto.fxml", 1355, 720);
            tipoProductoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    } 
     
     public void ProductosView(){
        try{
            MenuProductosController productosView = (MenuProductosController)cambiarEscena("menuProductosView.fxml", 1355, 720);
            productosView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    } 

     
    public void CargoEmpleadoView(){
        try{
            MenuCargoEmpleadoController cargoEmpleadoView = (MenuCargoEmpleadoController)cambiarEscena("menuCargoEmpleado.fxml", 1355, 720);
            cargoEmpleadoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }  
    
    public void EmpleadosView(){
        try{
            MenuEmpleadosController empleadoView = (MenuEmpleadosController)cambiarEscena("menuEmpleados.fxml", 1355, 720);
            empleadoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }  
    
    public void DetalleCompraView(){
        try{
            MenuDetalleComprasController detallaeCompraView = (MenuDetalleComprasController)cambiarEscena("menuDetalleCompra.fxml", 1355, 720);
            detallaeCompraView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }  
    
    public void EmailProveedorView(){
        try{
            MenuEmailProveedorController emailView = (MenuEmailProveedorController)cambiarEscena("menuEmailProveedor.fxml", 1355, 720);
            emailView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    } 
    
     public void TelefonoProveedorView(){
        try{
            MenuTelefonoProveedorController telefonoView = (MenuTelefonoProveedorController)cambiarEscena("menuTelefonoProveedor.fxml", 1355, 720);
            telefonoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }  
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

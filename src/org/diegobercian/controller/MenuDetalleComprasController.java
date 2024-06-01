/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.diegobercian.bean.*;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuDetalleComprasController implements Initializable {
    
    private enum operaciones {AGREGAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <DetalleCompras> listaDetalleCompras;
    private ObservableList <Productos> listaProductos;
    private ObservableList <Compras> listaCompras;
    @FXML
    private Button btnAgregar;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnReportes;
    @FXML
    private ImageView imgBuscar;
    @FXML
    private TableView tblDetalleCompra;
    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colCostoU;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colCodigoP;
    @FXML
    private TableColumn colNoD;
    @FXML
    private TextField txtCostoU;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtCodigo;
    @FXML
    private ComboBox cmbNoD;
    @FXML
    private ComboBox cmbCodigoP;
    
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnMenuCompras;
    private Main escenarioPrincipal;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodigoP.setItems(getProductos());
        cmbNoD.setItems(getCompras());
    }  
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    public void seleccionarElemento(){
        txtCodigo.setText(String.valueOf(((DetalleCompras) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
        txtCostoU.setText(String.valueOf(((DetalleCompras) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCostoUnitario()));
        txtCantidad.setText(String.valueOf(((DetalleCompras) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbCodigoP.getSelectionModel().select(buscarProducto(((DetalleCompras)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbNoD.getSelectionModel().select(buscarCompras(((DetalleCompras)tblDetalleCompra.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
    }
    
    public Productos buscarProducto(String codigoProducto){
        Productos resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarProductos(?);");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Productos(registro.getString("codigoProducto"),
                                             registro.getString("descripcionProducto"),
                                             registro.getDouble("precioUnitario"),
                                             registro.getDouble("precioDocena"),
                                             registro.getDouble("precioMayor"),
                                             registro.getString("imagenProducto"),
                                             registro.getInt("existencia"),
                                             registro.getInt("codigoTipoProducto"),
                                             registro.getInt("codigoProveedor")
            
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Compras buscarCompras(int numeroDocumento){
        Compras resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarCompras(?);");
            procedimiento.setInt(1, numeroDocumento);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Compras(registro.getInt("numeroDocumento"),
                                             registro.getString("fechaDocumento"),
                                             registro.getString("descripcion"),
                                             registro.getDouble("totalDocumento")
            
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void cargarDatos(){
        tblDetalleCompra.setItems(getDetalleCompra());
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleCompra"));
        colCostoU.setCellValueFactory(new PropertyValueFactory<>("costoUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCodigoP.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colNoD.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
    }
    
    public ObservableList<DetalleCompras> getDetalleCompra(){
        ArrayList<DetalleCompras> lista = new ArrayList<DetalleCompras>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarDetalleCompra();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleCompras(resultado.getInt("codigoDetalleCompra"),
                        resultado.getDouble("costoUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getString("codigoProducto"),
                        resultado.getInt("numeroDocumento")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaDetalleCompras = FXCollections.observableList(lista);    
    }
    
    public ObservableList<Productos> getProductos() {

        ArrayList<Productos> lista = new ArrayList<Productos>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarProductos();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos(resultado.getString("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getString("imagenProducto"),
                        resultado.getInt("existencia"),
                        resultado.getInt("codigoTipoProducto"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductos = FXCollections.observableList(lista);
    }
    
    public ObservableList<Compras> getCompras(){
        ArrayList<Compras> lista = new ArrayList<Compras>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarCompras();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Compras(resultado.getInt("numeroDocumento"),
                        resultado.getString("fechaDocumento"),
                        resultado.getString("descripcion"),
                        resultado.getDouble("totalDocumento")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaCompras = FXCollections.observableList(lista);    
    }
    
    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                imgAgregar.setImage(new Image("/org/diegobercian/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnReportes.setDisable(false);
                btnEditar.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgEliminar.setImage(new Image("/org/diegobercian/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                cargarDatos();
                break;
        }
    }
    
    public void guardar() {
        DetalleCompras registro = new DetalleCompras();
        registro.setCodigoDetalleCompra(Integer.parseInt(txtCodigo.getText()));
        registro.setCostoUnitario(Double.parseDouble(txtCostoU.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setCodigoProducto(((Productos)cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setNumeroDocumento(((Compras)cmbNoD.getSelectionModel().getSelectedItem()).getNumeroDocumento());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarDetalleCompra(?,?,?,?,?);");
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setString(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
            procedimiento.execute();
            listaDetalleCompras.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/diegobercian/images/actualizar.png"));
                    imgBuscar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                    activarControles();
                    txtCodigo.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un detalle para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgBuscar.setImage(new Image("/org/diegobercian/images/buscar.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarDetalleCompra(?,?,?,?,?);");
            DetalleCompras registro = (DetalleCompras)tblDetalleCompra.getSelectionModel().getSelectedItem();
            registro.setCostoUnitario(Double.parseDouble(txtCostoU.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setCodigoProducto(((Productos)cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setNumeroDocumento(((Compras)cmbNoD.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setString(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar() {

        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEliminar.setImage(new Image("/org/diegobercian/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default: 
                if(tblDetalleCompra.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el detalle?", "Eliminar Detalle Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarFacturas(?);");
                            procedimiento.setInt(1, ((DetalleCompras)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                            procedimiento.execute();
                            listaDetalleCompras.remove(tblDetalleCompra.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un detalle para eliminar");
                }
                break;
                
        }
    }
    
    public void reportes() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReportes.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgBuscar.setImage(new Image("/org/diegobercian/images/buscar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    

    public void activarControles(){
        txtCodigo.setEditable(true);
        txtCostoU.setEditable(true);
        txtCantidad.setEditable(true);
        cmbCodigoP.setDisable(false);
        cmbNoD.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtCostoU.setEditable(false);
        txtCantidad.setEditable(false);
        cmbCodigoP.setDisable(true);
        cmbNoD.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtCostoU.clear();
        txtCantidad.clear();
        cmbCodigoP.getSelectionModel().clearSelection();
        cmbNoD.getSelectionModel().clearSelection();
    }

    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnMenuCompras){
            escenarioPrincipal.ComprasView();
        }
    }
    
      
    
    
    
}

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
import org.diegobercian.bean.Facturas;
import org.diegobercian.bean.Productos;
import org.diegobercian.bean.DetalleCompras;
import org.diegobercian.bean.DetalleFactura;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuDetalleFacturaController implements Initializable {
    
    private enum operaciones {AGREGAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <DetalleFactura> listaDetalleFactura;
    private ObservableList <Productos> listaProductos;
    private ObservableList <Facturas> listaFacturas;
    @FXML
    private TableView tblDetalleFactura;
    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colPrecioU;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colFactura;
    @FXML
    private TableColumn colProducto;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtPrecioU;
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
    private ComboBox cmbNoF;
    @FXML
    private ComboBox cmbCodigoP;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnFactura;
    private Main escenarioPrincipal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbNoF.setItems(getFacturas());
        cmbCodigoP.setItems(getProductos());
    }    
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void seleccionarElemento(){
        txtCodigo.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura()));
        txtPrecioU.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtCantidad.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbNoF.getSelectionModel().select(buscarFactura(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        cmbCodigoP.getSelectionModel().select(buscarProducto(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }
    
    public Facturas buscarFactura(int numeroFactura){
        Facturas resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarFacturas(?);");
            procedimiento.setInt(1, numeroFactura);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Facturas(registro.getInt("numeroFactura"),
                                             registro.getString("estado"),
                                             registro.getDouble("totalFactura"),
                                             registro.getString("fechaFactura"),
                                             registro.getInt("codigoCliente"),
                                             registro.getInt("codigoEmpleado")
            
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
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
    
    public void cargarDatos(){
        tblDetalleFactura.setItems(getDetalleFactura());
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleFactura"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
    }
    
    public ObservableList<DetalleFactura> getDetalleFactura(){
        ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarDetalleFactura();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleFactura(resultado.getInt("codigoDetalleFactura"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("numeroFactura"),
                        resultado.getString("codigoProducto")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaDetalleFactura = FXCollections.observableList(lista);    
    }
    

    
    public ObservableList<Facturas> getFacturas(){
        ArrayList<Facturas> lista = new ArrayList<Facturas>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarFacturas();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Facturas(resultado.getInt("numeroFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getString("fechaFactura"),
                        resultado.getInt("codigoCliente"),
                        resultado.getInt("codigoEmpleado")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFacturas = FXCollections.observableList(lista);    
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
        DetalleFactura registro = new DetalleFactura();
        registro.setCodigoDetalleFactura(Integer.parseInt(txtCodigo.getText()));
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNumeroFactura(((Facturas)cmbNoF.getSelectionModel().getSelectedItem()).getNumeroFactura());
        registro.setCodigoProducto(((Productos)cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProducto());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarDetalleFactura(?,?,?,?,?);");
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroFactura());
            procedimiento.setString(5, registro.getCodigoProducto());
            procedimiento.execute();
            listaDetalleFactura.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarDetalleFactura(?,?,?,?,?);");
            DetalleFactura registro = (DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem();
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNumeroFactura(((Facturas)cmbNoF.getSelectionModel().getSelectedItem()).getNumeroFactura());
            registro.setCodigoProducto(((Productos)cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProducto());
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroFactura());
            procedimiento.setString(5, registro.getCodigoProducto());
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
                if(tblDetalleFactura.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el detalle?", "Eliminar Detalle Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarFacturas(?);");
                            procedimiento.setInt(1, ((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                            procedimiento.execute();
                            listaDetalleFactura.remove(tblDetalleFactura.getSelectionModel().getSelectedItem());
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
        txtPrecioU.setEditable(true);
        txtCantidad.setEditable(true);
        cmbNoF.setDisable(false);
        cmbCodigoP.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtPrecioU.setEditable(false);
        txtCantidad.setEditable(false);
        cmbNoF.setDisable(true);
        cmbCodigoP.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtPrecioU.clear();
        txtCantidad.clear();
        cmbNoF.getSelectionModel().clearSelection();
        cmbCodigoP.getSelectionModel().clearSelection();
    }
    
    
    
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnFactura){
            escenarioPrincipal.FacturaView();
        }
    }
    
    
    
}
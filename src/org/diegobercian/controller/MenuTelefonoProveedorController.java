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
import org.diegobercian.bean.TelefonoProveedor;
import org.diegobercian.bean.Proveedores;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuTelefonoProveedorController implements Initializable {
    private enum operaciones {AGREGAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <TelefonoProveedor> listaTelefonoProveedor;
    private ObservableList <Proveedores> listaProveedores;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnProveedores;
    @FXML private MenuItem btnEmail;
    private Main escenarioPrincipal;
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
    private TableView tblTelefonoProveedor;

    @FXML
    private TableColumn colCodigo;

    @FXML
    private TableColumn colNumPri;

    @FXML
    private TableColumn colNumSeg;

    @FXML
    private TableColumn colObservaciones;

    @FXML
    private TableColumn colProveedor;

    @FXML
    private TextField txtNumPri;

    @FXML
    private TextField txtNumSeg;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtObservaciones;

    @FXML
    private ComboBox cmbProveedor;
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbProveedor.setItems(getProveedores());
    } 
    
    public void seleccionarElemento(){
        txtCodigo.setText(String.valueOf(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
        txtNumPri.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroPrincipal());
        txtNumSeg.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroSegundario());
        txtObservaciones.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getObservaciones());
        cmbProveedor.getSelectionModel().select(buscarProveedores(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }
    
    public Proveedores buscarProveedores(int codigoProveedor){
        Proveedores resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarProveedores(?);");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                                             registro.getString("NITProveedor"),
                                             registro.getString("nombresProveedor"),
                                             registro.getString("apellidosProveedor"),
                                             registro.getString("direccionProveedor"),
                                             registro.getString("razonSocial"),
                                             registro.getString("contactoPrincipal"),
                                             registro.getString("paginaWeb")
            
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void cargarDatos(){
        tblTelefonoProveedor.setItems(getTelefonoProveedor());
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoTelefonoProveedor"));
        colNumPri.setCellValueFactory(new PropertyValueFactory<>("numeroPrincipal"));
        colNumSeg.setCellValueFactory(new PropertyValueFactory<>("numeroSegundario"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }
    
    public ObservableList<TelefonoProveedor> getTelefonoProveedor(){
        ArrayList<TelefonoProveedor> lista = new ArrayList<TelefonoProveedor>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarTelefonoProveedor;");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TelefonoProveedor(resultado.getInt("codigoTelefonoProveedor"),
                        resultado.getString("numeroPrincipal"),
                        resultado.getString("numeroSecundario"),
                        resultado.getString("observaciones"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTelefonoProveedor = FXCollections.observableList(lista);    
    }
    
    public ObservableList<Proveedores> getProveedores() {

        ArrayList<Proveedores> listaPro = new ArrayList<>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarProveedores;");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProveedores = FXCollections.observableList(listaPro);
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
                break;
        }
    }
    
    public void guardar() {
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setCodigoTelefonoProveedor(Integer.parseInt(txtCodigo.getText()));
        registro.setNumeroPrincipal(txtNumPri.getText());
        registro.setNumeroSegundario(txtNumSeg.getText());
        registro.setObservaciones(txtObservaciones.getText());
        registro.setCodigoProveedor(((Proveedores)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarTelefonoProveedor(?,?,?,?,?);");
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getNumeroPrincipal());
            procedimiento.setString(3, registro.getNumeroSegundario());
            procedimiento.setString(4, registro.getObservaciones());
            procedimiento.setInt(5, registro.getCodigoProveedor());
            procedimiento.execute();
            listaTelefonoProveedor.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null){
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
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un email para editar");
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarTelefonoProveedor(?,?,?,?,?);");
            TelefonoProveedor registro = (TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem();
            registro.setNumeroPrincipal(txtNumPri.getText());
            registro.setNumeroSegundario(txtNumSeg.getText());
            registro.setObservaciones(txtObservaciones.getText());
            registro.setCodigoProveedor(((Proveedores)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getNumeroPrincipal());
            procedimiento.setString(3, registro.getNumeroSegundario());
            procedimiento.setString(4, registro.getObservaciones());
            procedimiento.setInt(5, registro.getCodigoProveedor());
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
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el telefono?", "Eliminar Telefono", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarTelefonoProveedor(?);");
                            procedimiento.setInt(1, ((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
                            procedimiento.execute();
                            listaTelefonoProveedor.remove(tblTelefonoProveedor.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un telefono para eliminar");
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
        txtNumPri.setEditable(true);
        txtNumSeg.setEditable(true);
        txtObservaciones.setEditable(true);
        cmbProveedor.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtNumPri.setEditable(false);
        txtNumSeg.setEditable(false);
        txtObservaciones.setEditable(false);
        cmbProveedor.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtNumPri.clear();
        txtNumSeg.clear();
        txtObservaciones.clear();
        cmbProveedor.getSelectionModel().clearSelection();
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
    
      

}


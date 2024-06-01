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
import org.diegobercian.bean.EmailProveedor;
import org.diegobercian.bean.Proveedores;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuEmailProveedorController implements Initializable {
    private enum operaciones {AGREGAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <EmailProveedor> listaEmailProveedor;
    private ObservableList <Proveedores> listaProveedores;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnProveedores;
    @FXML private MenuItem btnTelefono;
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
    private TableView tblEmailProveedor;

    @FXML
    private TableColumn colCodigo;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colDescripcion;

    @FXML
    private TableColumn colProveedor;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtDescripcion;
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
        txtCodigo.setText(String.valueOf(((EmailProveedor) tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor()));
        txtEmail.setText(((EmailProveedor) tblEmailProveedor.getSelectionModel().getSelectedItem()).getEmailProveedor());
        txtDescripcion.setText(((EmailProveedor) tblEmailProveedor.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbProveedor.getSelectionModel().select(buscarProveedores(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
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
        tblEmailProveedor.setItems(getEmailProveedor());
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoEmailProveedor"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }
    
    public ObservableList<EmailProveedor> getEmailProveedor(){
        ArrayList<EmailProveedor> lista = new ArrayList<EmailProveedor>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarEmailProveedor;");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new EmailProveedor(resultado.getInt("codigoEmailProveedor"),
                        resultado.getString("emailProveedor"),
                        resultado.getString("descripcion"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmailProveedor = FXCollections.observableList(lista);    
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
        EmailProveedor registro = new EmailProveedor();
        registro.setCodigoEmailProveedor(Integer.parseInt(txtCodigo.getText()));
        registro.setEmailProveedor(txtEmail.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setCodigoProveedor(((Proveedores)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarEmailProveedor(?,?,?,?);");
            procedimiento.setInt(1, registro.getCodigoEmailProveedor());
            procedimiento.setString(2, registro.getEmailProveedor());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setInt(4, registro.getCodigoProveedor());
            procedimiento.execute();
            listaEmailProveedor.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblEmailProveedor.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/diegobercian/images/actualizar.png"));
                    imgBuscar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                    activarControles();
                    txtCodigo.setEditable(false);
                    tipoDeOperaciones = MenuEmailProveedorController.operaciones.ACTUALIZAR;
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
                tipoDeOperaciones = MenuEmailProveedorController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarEmailProveedor(?,?,?,?);");
            EmailProveedor registro = (EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem();
            registro.setEmailProveedor(txtEmail.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoProveedor(((Proveedores)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoEmailProveedor());
            procedimiento.setString(2, registro.getEmailProveedor());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setInt(4, registro.getCodigoProveedor());
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
                if(tblEmailProveedor.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el email?", "Eliminar Email", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarEmailProveedor(?);");
                            procedimiento.setInt(1, ((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
                            procedimiento.execute();
                            listaEmailProveedor.remove(tblEmailProveedor.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un email para eliminar");
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
        txtEmail.setEditable(true);
        txtDescripcion.setEditable(true);
        cmbProveedor.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtEmail.setEditable(false);
        txtDescripcion.setEditable(false);
        cmbProveedor.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtEmail.clear();
        txtDescripcion.clear();
        cmbProveedor.setValue(null);
    }
    
    
    
    
    
    
    
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnProveedores){
            escenarioPrincipal.ProveedoresView();
        }if(event.getSource() == btnTelefono){
            escenarioPrincipal.TelefonoProveedorView();
        }
    }
    
       

}

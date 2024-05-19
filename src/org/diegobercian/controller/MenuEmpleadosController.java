/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.controller;

import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.diegobercian.bean.CargoEmpleado;
import org.diegobercian.bean.Empleados;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuEmpleadosController implements Initializable {

    private enum operaciones {AGREGAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Empleados> listaEmpleados;
    private ObservableList <CargoEmpleado> listaCargoEmpleados;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnMenuCargoEmpleado;
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
    private TableView tblEmpleados;
    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colSueldo;
    @FXML
    private TableColumn  colDireccion;
    @FXML
    private TableColumn colTurno;
    @FXML
    private TableColumn colCargo;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtSueldo;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTurno;
    @FXML
    private ComboBox cmbCargo;
    
    
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCargo.setItems(getCargoEmpleado());
    }    
    
    
    public void seleccionarElemento(){
        txtCodigo.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNombres.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        txtApellidos.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
        txtSueldo.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
        txtDireccion.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTurno.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTurno());
        cmbCargo.getSelectionModel().select(buscarCargoEmpleado(((Empleados )tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
    }
    
    public CargoEmpleado buscarCargoEmpleado(int codigoCargoEmpleado){
        CargoEmpleado resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarCargoEmpleado(?);");
            procedimiento.setInt(1, codigoCargoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new CargoEmpleado(registro.getInt("codigoCargoEmpleado"),
                                             registro.getString("nombreCargo"),
                                             registro.getString("descripcionCargo")
            
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombresEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidosEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("codigoCargoEmpleado"));
    }
    
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarEmpleados();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getString("turno"),
                        resultado.getInt("codigoCargoEmpleado")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpleados = FXCollections.observableList(lista);    
    }
    
    public ObservableList<CargoEmpleado> getCargoEmpleado() {

        ArrayList<CargoEmpleado> listaPro = new ArrayList<>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarCargoEmpleado;");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new CargoEmpleado(resultado.getInt("codigoCargoEmpleado"),
                        resultado.getString("nombreCargo"),
                        resultado.getString("descripcionCargo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaCargoEmpleados = FXCollections.observableList(listaPro);
    }
    
    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEditar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReportes.setDisable(true);
                imgAgregar.setImage(new Image("/org/diegobercian/images/guardar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                tipoDeOperaciones = MenuEmpleadosController.operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEditar.setText("Editar");
                btnEliminar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                tipoDeOperaciones = MenuEmpleadosController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar() {
        Empleados registro = new Empleados();
        registro.setCodigoEmpleado(Integer.parseInt(txtCodigo.getText()));
        registro.setNombresEmpleado(txtNombres.getText());
        registro.setApellidosEmpleado(txtApellidos.getText());
        registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTurno(txtTurno.getText());
        registro.setCodigoCargoEmpleado(((CargoEmpleado)cmbCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarEmpleados(?,?,?,?,?,?,?);");
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombresEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
            procedimiento.execute();
            listaEmpleados.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/diegobercian/images/actualizar.png"));
                    imgBuscar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                    activarControles();
                    txtCodigo.setEditable(false);
                    tipoDeOperaciones = MenuEmpleadosController.operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para editar");
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
                tipoDeOperaciones = MenuEmpleadosController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarEmpleados(?,?,?,?,?,?,?);");
            Empleados registro = (Empleados)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNombresEmpleado(txtNombres.getText());
            registro.setApellidosEmpleado(txtApellidos.getText());
            registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
            registro.setDireccion(txtDireccion.getText());
            registro.setTurno(txtTurno.getText());
            registro.setCodigoCargoEmpleado(((CargoEmpleado)cmbCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombresEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
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
                btnEditar.setText("Editar");
                btnEliminar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                tipoDeOperaciones = MenuEmpleadosController.operaciones.NINGUNO;
                break;
            default: 
                if(tblEmpleados.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el empleado?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarEmpleados(?);");
                            procedimiento.setInt(1, ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un empleado para eliminar");
                }
                break;
                
        }
    }
    

    public void activarControles(){
        txtCodigo.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtSueldo.setEditable(true);
        txtDireccion.setEditable(true);
        txtTurno.setEditable(true);
        cmbCargo.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtSueldo.setEditable(false);
        txtDireccion.setEditable(false);
        txtTurno.setEditable(false);
        cmbCargo.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        cmbCargo.getSelectionModel().clearSelection();
    }
    
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnMenuCargoEmpleado){
            escenarioPrincipal.CargoEmpleadoView();
        }
    }
    
    
    
}


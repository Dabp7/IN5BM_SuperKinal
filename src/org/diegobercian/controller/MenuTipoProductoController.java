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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.diegobercian.bean.TipoProducto;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author gaber
 */
public class MenuTipoProductoController implements Initializable {

    private Main escenarioPrincipal;
    private ObservableList<TipoProducto> listaTipoProducto;

    private enum operaciones {
        AGREGAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;

    @FXML
    private MenuItem btnRegresar;
    @FXML
    private MenuItem btnProductos;
    @FXML
    private TextField txtCodigoTP;
    @FXML
    private TextField txtDescripcionTP;
    @FXML
    private TableView tblTipoProducto;
    @FXML
    private TableColumn colCodigoTP;
    @FXML
    private TableColumn colDescripcionTP;
    @FXML
    private Button btnAgregarTP;
    @FXML
    private Button btnEditarTP;
    @FXML
    private Button btnReportesTP;
    @FXML
    private Button btnEliminarTP;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgBuscar;
    @FXML
    private ImageView imgEliminar;

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblTipoProducto.setItems(getTipoProducto());
        colCodigoTP.setCellValueFactory(new PropertyValueFactory<TipoProducto, Integer>("codigoTipoProducto"));
        colDescripcionTP.setCellValueFactory(new PropertyValueFactory<TipoProducto, String>("descripcion"));
    }

    public void seleccionarElemento() {
        txtCodigoTP.setText(String.valueOf(((TipoProducto) tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtDescripcionTP.setText(((TipoProducto) tblTipoProducto.getSelectionModel().getSelectedItem()).getDescripcion());
    }

    public ObservableList<TipoProducto> getTipoProducto() {

        ArrayList<TipoProducto> lista = new ArrayList<>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarTipoProducto;");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoProducto(resultado.getInt("codigoTipoProducto"),
                        resultado.getString("descripcion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoProducto = FXCollections.observableList(lista);
    }

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregarTP.setText("Guardar");
                btnEditarTP.setText("Cancelar");
                btnEliminarTP.setDisable(true);
                btnReportesTP.setDisable(true);
                imgAgregar.setImage(new Image("/org/diegobercian/images/guardar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                tipoDeOperaciones = MenuTipoProductoController.operaciones.ACTUALIZAR;
                break;

            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarTP.setText("Agregar");
                btnEditarTP.setText("Editar");
                btnEliminarTP.setDisable(false);
                btnReportesTP.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                tipoDeOperaciones = MenuTipoProductoController.operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        TipoProducto registro = new TipoProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtCodigoTP.getText()));
        registro.setDescripcion(txtDescripcionTP.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarTipoProducto(?,?);");
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoProducto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTipoProducto.getSelectionModel().getSelectedItem() != null){
                    btnEditarTP.setText("Actualizar");
                    btnReportesTP.setText("Cancelar");
                    btnAgregarTP.setDisable(true);
                    btnEliminarTP.setDisable(true);
                    imgEditar.setImage(new Image("/org/diegobercian/images/actualizar.png"));
                    imgBuscar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                    activarControles();
                    txtCodigoTP.setEditable(false);
                    tipoDeOperaciones = MenuTipoProductoController.operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un tipo de producto para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarTP.setText("Editar");
                btnReportesTP.setText("Reportes");
                btnAgregarTP.setDisable(false);
                btnEliminarTP.setDisable(false);
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgBuscar.setImage(new Image("/org/diegobercian/images/buscar.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = MenuTipoProductoController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarTipoProducto(?,?);");
            TipoProducto registro = (TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTP.getText());
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
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
                btnAgregarTP.setText("Agregar");
                btnEditarTP.setText("Editar");
                btnEliminarTP.setDisable(false);
                btnReportesTP.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                tipoDeOperaciones = MenuTipoProductoController.operaciones.NINGUNO;
                break;
            default: 
                if(tblTipoProducto.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar este tipo de producto?", "Eliminar tipo de producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarTipoProducto(?);");
                            procedimiento.setInt(1, ((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoProducto.remove(tblTipoProducto.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un tipo de producto para eliminar");
                }
                break;
                
        }
    }

    public void desactivarControles() {
        txtCodigoTP.setEditable(false);
        txtDescripcionTP.setEditable(false);
    }

    public void activarControles() {
        txtCodigoTP.setEditable(true);
        txtDescripcionTP.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoTP.clear();
        txtDescripcionTP.clear();
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.MenuPrincipalView();
        }if (event.getSource() == btnProductos) {
            escenarioPrincipal.ProductosView();
        }
    }

}

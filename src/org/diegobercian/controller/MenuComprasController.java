
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
import org.diegobercian.bean.CargoEmpleado;
import org.diegobercian.bean.Compras;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;

/**
 *
 * @author informatica
 */
public class MenuComprasController implements Initializable {
    @FXML
    private MenuItem btnRegresar;
    @FXML
    private MenuItem btnDetalleCompras;
    private Main escenarioPrincipal;
 
    private ObservableList<Compras> listaCompra;

    private enum operaciones {
        AGREGAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    @FXML
    private TextField txtDoc;
    @FXML
    private TextField txtFecha;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtTotal;
    @FXML
    private TableView tblCompras;
    @FXML
    private TableColumn colDoc;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colTotal;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnEliminar;
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
        tblCompras.setItems(getCompras());
        colDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));
    }

    public void seleccionarElemento() {
        txtDoc.setText(String.valueOf(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtFecha.setText(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento());
        txtDescripcion.setText(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        txtTotal.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento()));
    }

    public ObservableList<Compras> getCompras() {

        ArrayList<Compras> lista = new ArrayList<>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarCompras;");
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

        return listaCompra = FXCollections.observableList(lista);
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
                tipoDeOperaciones = MenuComprasController.operaciones.ACTUALIZAR;
                break;

            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEditar.setText("Editar");
                btnEliminar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                tipoDeOperaciones = MenuComprasController.operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        Compras registro = new Compras();
        registro.setNumeroDocumento(Integer.parseInt(txtDoc.getText()));
        registro.setFechaDocumento(txtFecha.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTotalDocumento(Double.parseDouble(txtTotal.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarCompras(?,?,?,?);");
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getTotalDocumento());
            procedimiento.execute();
            listaCompra.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblCompras.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/diegobercian/images/actualizar.png"));
                    imgBuscar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                    activarControles();
                    txtDoc.setEditable(false);
                    tipoDeOperaciones = MenuComprasController.operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una compra para editar");
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
                tipoDeOperaciones = MenuComprasController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarCompras(?,?,?,?);");
            Compras registro = (Compras)tblCompras.getSelectionModel().getSelectedItem();
            registro.setFechaDocumento(txtFecha.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTotalDocumento(Double.parseDouble(txtTotal.getText()));
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getTotalDocumento());
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
                tipoDeOperaciones = MenuComprasController.operaciones.NINGUNO;
                break;
            default: 
                if(tblCompras.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar esta compra?", "Eliminar tipo de producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarCompras(?);");
                            procedimiento.setInt(1, ((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompra.remove(tblCompras.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una compra para eliminar");
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
    


    public void desactivarControles() {
        txtDoc.setEditable(false);
        txtFecha.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotal.setEditable(false);
    }

    public void activarControles() {
        txtDoc.setEditable(true);
        txtFecha.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTotal.setEditable(true);
    }

    public void limpiarControles() {
        txtDoc.clear();
        txtFecha.clear();
        txtDescripcion.clear();
        txtTotal.clear();
    }
    
    
    
    

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.MenuPrincipalView();
        }if (event.getSource() == btnDetalleCompras) {
            escenarioPrincipal.DetalleCompraView();
        }
    }

       
}

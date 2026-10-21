package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuEmailProveedorController extends CrudController implements Initializable {
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnProveedores;
    @FXML private MenuItem btnTelefono;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
    @FXML private TableView<Map<String,Object>> tblEmailProveedor;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigo;
    @FXML private TableColumn<Map<String,Object>,Object> colEmail;
    @FXML private TableColumn<Map<String,Object>,Object> colDescripcion;
    @FXML private TableColumn<Map<String,Object>,Object> colProveedor;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox<Choice> cmbProveedor;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigo,"codigoEmailProveedor");
        column(colEmail,"emailProveedor");
        column(colDescripcion,"descripcion");
        column(colProveedor,"codigoProveedor");
        configure(tblEmailProveedor,"EmailProveedor",new String[]{"codigoEmailProveedor","emailProveedor","descripcion","codigoProveedor"},
            new Field[]{new Field("codigoEmailProveedor","int",0,txtCodigo,false),
                new Field("emailProveedor","text",50,txtEmail,false),
                new Field("descripcion","text",100,txtDescripcion,false),
                new Field("codigoProveedor","int",0,cmbProveedor,false).lookup("Proveedores","codigoProveedor","nombresProveedor")},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarEmailProveedor","sp_actualizarEmailProveedor","sp_eliminarEmailProveedor",
            new String[]{"codigoEmailProveedor","emailProveedor","descripcion","codigoProveedor"},new String[]{"codigoEmailProveedor","emailProveedor","descripcion","codigoProveedor"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnProveedores){
            escenarioPrincipal.ProveedoresView();
        }if(event.getSource() == btnTelefono){
            escenarioPrincipal.TelefonoProveedorView();
        }

    }
}

package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuTelefonoProveedorController extends CrudController implements Initializable {
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnProveedor;
    @FXML private MenuItem btnEmail;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
    @FXML private TableView<Map<String,Object>> tblTelefonoProveedor;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigo;
    @FXML private TableColumn<Map<String,Object>,Object> colNumPri;
    @FXML private TableColumn<Map<String,Object>,Object> colNumSeg;
    @FXML private TableColumn<Map<String,Object>,Object> colObservaciones;
    @FXML private TableColumn<Map<String,Object>,Object> colProveedor;
    @FXML private TextField txtNumPri;
    @FXML private TextField txtNumSeg;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtObservaciones;
    @FXML private ComboBox<Choice> cmbProveedor;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigo,"codigoTelefonoProveedor");
        column(colNumPri,"numeroPrincipal");
        column(colNumSeg,"numeroSecundario");
        column(colObservaciones,"observaciones");
        column(colProveedor,"codigoProveedor");
        configure(tblTelefonoProveedor,"TelefonoProveedor",new String[]{"codigoTelefonoProveedor","numeroPrincipal","numeroSecundario","observaciones","codigoProveedor"},
            new Field[]{new Field("codigoTelefonoProveedor","int",0,txtCodigo,false),
                new Field("numeroPrincipal","text",8,txtNumPri,false),
                new Field("numeroSecundario","text",8,txtNumSeg,true),
                new Field("observaciones","text",45,txtObservaciones,true),
                new Field("codigoProveedor","int",0,cmbProveedor,false).lookup("Proveedores","codigoProveedor","nombresProveedor")},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarTelefonoProveedor","sp_actualizarTelefonoProveedor","sp_eliminarTelefonoProveedor",
            new String[]{"codigoTelefonoProveedor","numeroPrincipal","numeroSecundario","observaciones","codigoProveedor"},new String[]{"codigoTelefonoProveedor","numeroPrincipal","numeroSecundario","observaciones","codigoProveedor"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnProveedor){
            escenarioPrincipal.ProveedoresView();
        }if(event.getSource() == btnEmail){
            escenarioPrincipal.EmailProveedorView();
        }

    }
}

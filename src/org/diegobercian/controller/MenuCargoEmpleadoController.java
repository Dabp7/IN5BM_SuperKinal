package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuCargoEmpleadoController extends CrudController implements Initializable {
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnMenuEmpleado;
    @FXML private TextField txtCodigoCE;
    @FXML private TextField txtNombreCE;
    @FXML private TextField txtDescripcionCE;
    @FXML private TableView<Map<String,Object>> tblCargoEmpleado;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigoCE;
    @FXML private TableColumn<Map<String,Object>,Object> colNombreCE;
    @FXML private TableColumn<Map<String,Object>,Object> colDescripcionCE;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private Button btnEliminar;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigoCE,"codigoCargoEmpleado");
        column(colNombreCE,"nombreCargo");
        column(colDescripcionCE,"descripcionCargo");
        configure(tblCargoEmpleado,"CargoEmpleado",new String[]{"codigoCargoEmpleado","nombreCargo","descripcionCargo"},
            new Field[]{new Field("codigoCargoEmpleado","int",0,txtCodigoCE,false),
                new Field("nombreCargo","text",45,txtNombreCE,false),
                new Field("descripcionCargo","text",45,txtDescripcionCE,false)},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarCargoEmpleado","sp_actualizarCargoEmpleado","sp_eliminarCargoEmpleado",
            new String[]{"codigoCargoEmpleado","nombreCargo","descripcionCargo"},new String[]{"codigoCargoEmpleado","nombreCargo","descripcionCargo"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnMenuEmpleado){
            escenarioPrincipal.EmpleadosView();
        }

    }
}

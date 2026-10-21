package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuEmpleadosController extends CrudController implements Initializable {
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnMenuCargoEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
    @FXML private TableView<Map<String,Object>> tblEmpleados;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigo;
    @FXML private TableColumn<Map<String,Object>,Object> colNombres;
    @FXML private TableColumn<Map<String,Object>,Object> colApellidos;
    @FXML private TableColumn<Map<String,Object>,Object> colSueldo;
    @FXML private TableColumn<Map<String,Object>,Object> colDireccion;
    @FXML private TableColumn<Map<String,Object>,Object> colTurno;
    @FXML private TableColumn<Map<String,Object>,Object> colCargo;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtSueldo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTurno;
    @FXML private ComboBox<Choice> cmbCargo;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigo,"codigoEmpleado");
        column(colNombres,"nombresEmpleado");
        column(colApellidos,"apellidosEmpleado");
        column(colSueldo,"sueldo");
        column(colDireccion,"direccion");
        column(colTurno,"turno");
        column(colCargo,"codigoCargoEmpleado");
        configure(tblEmpleados,"Empleados",new String[]{"codigoEmpleado","nombresEmpleado","apellidosEmpleado","sueldo","direccion","turno","codigoCargoEmpleado"},
            new Field[]{new Field("codigoEmpleado","int",0,txtCodigo,false),
                new Field("nombresEmpleado","text",50,txtNombres,false),
                new Field("apellidosEmpleado","text",50,txtApellidos,false),
                new Field("sueldo","decimal",10,txtSueldo,false),
                new Field("direccion","text",150,txtDireccion,false),
                new Field("turno","text",15,txtTurno,false),
                new Field("codigoCargoEmpleado","int",0,cmbCargo,false).lookup("CargoEmpleado","codigoCargoEmpleado","nombreCargo")},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarEmpleados","sp_actualizarEmpleados","sp_eliminarEmpleados",
            new String[]{"codigoEmpleado","nombresEmpleado","apellidosEmpleado","sueldo","direccion","turno","codigoCargoEmpleado"},new String[]{"codigoEmpleado","nombresEmpleado","apellidosEmpleado","sueldo","direccion","turno","codigoCargoEmpleado"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnMenuCargoEmpleado){
            escenarioPrincipal.CargoEmpleadoView();
        }

    }
}

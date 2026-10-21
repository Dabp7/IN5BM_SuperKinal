package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuFacturaController extends CrudController implements Initializable {
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnDetalleFactura;
    @FXML private TableView<Map<String,Object>> tblFacturas;
    @FXML private TableColumn<Map<String,Object>,Object> colNumero;
    @FXML private TableColumn<Map<String,Object>,Object> colEstado;
    @FXML private TableColumn<Map<String,Object>,Object> colTotal;
    @FXML private TableColumn<Map<String,Object>,Object> colFecha;
    @FXML private TableColumn<Map<String,Object>,Object> colCliente;
    @FXML private TableColumn<Map<String,Object>,Object> colEmpleado;
    @FXML private TextField txtNumero;
    @FXML private TextField txtEstado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
    @FXML private TextField txtFecha;
    @FXML private ComboBox<Choice> cmbClientes;
    @FXML private ComboBox<Choice> cmbEmpleados;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colNumero,"numeroFactura");
        column(colEstado,"estado");
        column(colTotal,"totalFactura");
        column(colFecha,"fechaFactura");
        column(colCliente,"codigoCliente");
        column(colEmpleado,"codigoEmpleado");
        configure(tblFacturas,"Factura",new String[]{"numeroFactura","estado","totalFactura","fechaFactura","codigoCliente","codigoEmpleado"},
            new Field[]{new Field("numeroFactura","int",0,txtNumero,false),
                new Field("estado","text",50,txtEstado,false),
                new Field("fechaFactura","date",45,txtFecha,false),
                new Field("codigoCliente","int",0,cmbClientes,false).lookup("Clientes","codigoCliente","nombresCliente"),
                new Field("codigoEmpleado","int",0,cmbEmpleados,false).lookup("Empleados","codigoEmpleado","nombresEmpleado")},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarFactura","sp_actualizarFacturas","sp_eliminarFacturas",
            new String[]{"numeroFactura","estado","fechaFactura","codigoCliente","codigoEmpleado"},new String[]{"numeroFactura","estado","fechaFactura","codigoCliente","codigoEmpleado"},"reporteFactura.jasper");
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnDetalleFactura){
            escenarioPrincipal.DetalleFacturaView();
        }

    }
}

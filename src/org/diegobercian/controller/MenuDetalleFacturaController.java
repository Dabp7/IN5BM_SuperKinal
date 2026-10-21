package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuDetalleFacturaController extends CrudController implements Initializable {
    @FXML private TableView<Map<String,Object>> tblDetalleFactura;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigo;
    @FXML private TableColumn<Map<String,Object>,Object> colPrecioU;
    @FXML private TableColumn<Map<String,Object>,Object> colCantidad;
    @FXML private TableColumn<Map<String,Object>,Object> colFactura;
    @FXML private TableColumn<Map<String,Object>,Object> colProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtCodigo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
    @FXML private ComboBox<Choice> cmbNoF;
    @FXML private ComboBox<Choice> cmbCodigoP;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnFactura;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigo,"codigoDetalleFactura");
        column(colPrecioU,"precioUnitario");
        column(colCantidad,"cantidad");
        column(colFactura,"numeroFactura");
        column(colProducto,"codigoProducto");
        configure(tblDetalleFactura,"DetalleFactura",new String[]{"codigoDetalleFactura","precioUnitario","cantidad","numeroFactura","codigoProducto"},
            new Field[]{new Field("codigoDetalleFactura","int",0,txtCodigo,false),
                new Field("cantidad","int",0,txtCantidad,false),
                new Field("numeroFactura","int",0,cmbNoF,false).lookup("Factura","numeroFactura","estado"),
                new Field("codigoProducto","text",15,cmbCodigoP,false).lookup("Productos","codigoProducto","descripcionProducto")},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarDetalleFactura","sp_ActualizarDetalleFactura","sp_eliminarDetalleFactura",
            new String[]{"codigoDetalleFactura","cantidad","numeroFactura","codigoProducto"},new String[]{"codigoDetalleFactura","precioUnitario","cantidad","numeroFactura","codigoProducto"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnFactura){
            escenarioPrincipal.FacturaView();
        }

    }
}

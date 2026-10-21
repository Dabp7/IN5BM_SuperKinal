package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuDetalleComprasController extends CrudController implements Initializable {
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
    @FXML private TableView<Map<String,Object>> tblDetalleCompra;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigo;
    @FXML private TableColumn<Map<String,Object>,Object> colCostoU;
    @FXML private TableColumn<Map<String,Object>,Object> colCantidad;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigoP;
    @FXML private TableColumn<Map<String,Object>,Object> colNoD;
    @FXML private TextField txtCostoU;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtCodigo;
    @FXML private ComboBox<Choice> cmbNoD;
    @FXML private ComboBox<Choice> cmbCodigoP;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnMenuCompras;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigo,"codigoDetalleCompra");
        column(colCostoU,"costoUnitario");
        column(colCantidad,"cantidad");
        column(colCodigoP,"codigoProducto");
        column(colNoD,"numeroDocumento");
        configure(tblDetalleCompra,"DetalleCompra",new String[]{"codigoDetalleCompra","costoUnitario","cantidad","codigoProducto","numeroDocumento"},
            new Field[]{new Field("codigoDetalleCompra","int",0,txtCodigo,false),
                new Field("costoUnitario","decimal",10,txtCostoU,false),
                new Field("cantidad","int",0,txtCantidad,false),
                new Field("codigoProducto","text",15,cmbCodigoP,false).lookup("Productos","codigoProducto","descripcionProducto"),
                new Field("numeroDocumento","int",0,cmbNoD,false).lookup("Compras","numeroDocumento","descripcion")},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarDetalleCompra","sp_actualizarDetalleCompra","sp_eliminarDetalleCompra",
            new String[]{"codigoDetalleCompra","costoUnitario","cantidad","codigoProducto","numeroDocumento"},new String[]{"codigoDetalleCompra","costoUnitario","cantidad","codigoProducto","numeroDocumento"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnMenuCompras){
            escenarioPrincipal.ComprasView();
        }

    }
}

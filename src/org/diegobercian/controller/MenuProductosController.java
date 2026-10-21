package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuProductosController extends CrudController implements Initializable {
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnTipoProducto;
    @FXML private TableView<Map<String,Object>> tblProductos;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigoPro;
    @FXML private TableColumn<Map<String,Object>,Object> colDescripcionPro;
    @FXML private TableColumn<Map<String,Object>,Object> colPrecioUPro;
    @FXML private TableColumn<Map<String,Object>,Object> colPrecioDoPro;
    @FXML private TableColumn<Map<String,Object>,Object> colPrecioMPro;
    @FXML private TableColumn<Map<String,Object>,Object> colImagenPro;
    @FXML private TableColumn<Map<String,Object>,Object> colExistenciaPro;
    @FXML private TableColumn<Map<String,Object>,Object> colTipoProducto;
    @FXML private TableColumn<Map<String,Object>,Object> colProveedores;
    @FXML private TextField txtDescripcionPro;
    @FXML private TextField txtCodigoPro;
    @FXML private TextField txtImagenPro;
    @FXML private ComboBox<Choice> cmbTipoProducto;
    @FXML private ComboBox<Choice> cmbProveedores;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigoPro,"codigoProducto");
        column(colDescripcionPro,"descripcionProducto");
        column(colPrecioUPro,"precioUnitario");
        column(colPrecioDoPro,"precioDocena");
        column(colPrecioMPro,"precioMayor");
        column(colImagenPro,"imagenProducto");
        column(colExistenciaPro,"existencia");
        column(colTipoProducto,"codigoTipoProducto");
        column(colProveedores,"codigoProveedor");
        configure(tblProductos,"Productos",new String[]{"codigoProducto","descripcionProducto","precioUnitario","precioDocena","precioMayor","imagenProducto","existencia","codigoTipoProducto","codigoProveedor"},
            new Field[]{new Field("codigoProducto","text",15,txtCodigoPro,false),
                new Field("descripcionProducto","text",45,txtDescripcionPro,false),
                new Field("imagenProducto","text",45,txtImagenPro,true),
                new Field("codigoTipoProducto","int",0,cmbTipoProducto,false).lookup("TipoProducto","codigoTipoProducto","descripcion"),
                new Field("codigoProveedor","int",0,cmbProveedores,false).lookup("Proveedores","codigoProveedor","nombresProveedor")},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarProductos","sp_actualizarProductos","sp_eliminarProductos",
            new String[]{"codigoProducto","descripcionProducto","imagenProducto","codigoTipoProducto","codigoProveedor"},new String[]{"codigoProducto","descripcionProducto","imagenProducto","codigoTipoProducto","codigoProveedor"},"ProductosReporte.jasper");
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnTipoProducto){
            escenarioPrincipal.TipoProductoView();
        }

    }
}

package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuTipoProductoController extends CrudController implements Initializable {
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnProductos;
    @FXML private TextField txtCodigoTP;
    @FXML private TextField txtDescripcionTP;
    @FXML private TableView<Map<String,Object>> tblTipoProducto;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigoTP;
    @FXML private TableColumn<Map<String,Object>,Object> colDescripcionTP;
    @FXML private Button btnAgregarTP;
    @FXML private Button btnEditarTP;
    @FXML private Button btnReportesTP;
    @FXML private Button btnEliminarTP;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigoTP,"codigoTipoProducto");
        column(colDescripcionTP,"descripcion");
        configure(tblTipoProducto,"TipoProducto",new String[]{"codigoTipoProducto","descripcion"},
            new Field[]{new Field("codigoTipoProducto","int",0,txtCodigoTP,false),
                new Field("descripcion","text",45,txtDescripcionTP,false)},
            btnAgregarTP,btnEditarTP,btnEliminarTP,btnReportesTP,"sp_agregarTipoProducto","sp_actualizarTipoProducto","sp_eliminarTipoProducto",
            new String[]{"codigoTipoProducto","descripcion"},new String[]{"codigoTipoProducto","descripcion"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.MenuPrincipalView();
        }if (event.getSource() == btnProductos) {
            escenarioPrincipal.ProductosView();
        }

    }
}

package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuComprasController extends CrudController implements Initializable {
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnDetalleCompras;
    @FXML private TextField txtDoc;
    @FXML private DatePicker txtFecha;
    @FXML private TextField txtDescripcion;
    @FXML private TableView<Map<String,Object>> tblCompras;
    @FXML private TableColumn<Map<String,Object>,Object> colDoc;
    @FXML private TableColumn<Map<String,Object>,Object> colFecha;
    @FXML private TableColumn<Map<String,Object>,Object> colDescripcion;
    @FXML private TableColumn<Map<String,Object>,Object> colTotal;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private Button btnEliminar;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colDoc,"numeroDocumento");
        column(colFecha,"fechaDocumento");
        column(colDescripcion,"descripcion");
        column(colTotal,"totalDocumento");
        configure(tblCompras,"Compras",new String[]{"numeroDocumento","fechaDocumento","descripcion","totalDocumento"},
            new Field[]{new Field("numeroDocumento","int",0,txtDoc,false),
                new Field("fechaDocumento","date",0,txtFecha,false),
                new Field("descripcion","text",60,txtDescripcion,false)},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarCompras","sp_actualizarCompras","sp_eliminarCompras",
            new String[]{"numeroDocumento","fechaDocumento","descripcion"},new String[]{"numeroDocumento","fechaDocumento","descripcion"},null);
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.MenuPrincipalView();
        }if (event.getSource() == btnDetalleCompras) {
            escenarioPrincipal.DetalleCompraView();
        }

    }
}

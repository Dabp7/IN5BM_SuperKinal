package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuClientesController extends CrudController implements Initializable {
    @FXML private TextField txtNombreC;
    @FXML private TextField txtApellidoC;
    @FXML private TextField txtNitC;
    @FXML private TextField txtCodigoC;
    @FXML private TextField txtTelefonoC;
    @FXML private TextField txtDireccionC;
    @FXML private TextField txtCorreoC;
    @FXML private TableView<Map<String,Object>> tblClientes;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigoC;
    @FXML private TableColumn<Map<String,Object>,Object> colApellidoC;
    @FXML private TableColumn<Map<String,Object>,Object> colNombreC;
    @FXML private TableColumn<Map<String,Object>,Object> colNitC;
    @FXML private TableColumn<Map<String,Object>,Object> colDireccionC;
    @FXML private TableColumn<Map<String,Object>,Object> colCorreoC;
    @FXML private TableColumn<Map<String,Object>,Object> colTelefonoC;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private Button btnEliminar;
    @FXML private Button btnRegresarC;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigoC,"codigoCliente");
        column(colNombreC,"nombresCliente");
        column(colApellidoC,"apellidosCliente");
        column(colNitC,"nitClientes");
        column(colDireccionC,"direccionCliente");
        column(colTelefonoC,"telefonoCliente");
        column(colCorreoC,"correoCliente");
        configure(tblClientes,"Clientes",new String[]{"codigoCliente","NITClientes","nombresCliente","apellidosCliente","direccionCliente","telefonoCliente","correoCliente"},
            new Field[]{new Field("codigoCliente","int",0,txtCodigoC,false),
                new Field("NITClientes","text",10,txtNitC,false),
                new Field("nombresCliente","text",50,txtNombreC,false),
                new Field("apellidosCliente","text",50,txtApellidoC,false),
                new Field("direccionCliente","text",150,txtDireccionC,false),
                new Field("telefonoCliente","text",8,txtTelefonoC,false),
                new Field("correoCliente","text",45,txtCorreoC,false)},
            btnAgregar,btnEditar,btnEliminar,btnReportes,"sp_agregarClientes","sp_actualizarClientes","sp_eliminarClientes",
            new String[]{"codigoCliente","NITClientes","nombresCliente","apellidosCliente","direccionCliente","telefonoCliente","correoCliente"},new String[]{"codigoCliente","NITClientes","nombresCliente","apellidosCliente","direccionCliente","telefonoCliente","correoCliente"},"Reporte.jasper");
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresarC) {
            escenarioPrincipal.MenuPrincipalView();
        }

    }
}

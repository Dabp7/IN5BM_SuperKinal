package org.diegobercian.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

/** Screen bindings; validation, persistence and recovery are shared by CrudController. */
public class MenuProveedoresController extends CrudController implements Initializable {
    @FXML private TextField txtCodigoPr;
    @FXML private TextField txtNitPr;
    @FXML private TextField txtNombrePr;
    @FXML private TextField txtApellidoPr;
    @FXML private TextField txtDireccionPr;
    @FXML private TextField txtRazonSocialPr;
    @FXML private TextField txtContactoPr;
    @FXML private TextField txtPaginaWebPr;
    @FXML private TableView<Map<String,Object>> tblProveedores;
    @FXML private TableColumn<Map<String,Object>,Object> colCodigoP;
    @FXML private TableColumn<Map<String,Object>,Object> colNitP;
    @FXML private TableColumn<Map<String,Object>,Object> colNombresP;
    @FXML private TableColumn<Map<String,Object>,Object> colApellidosP;
    @FXML private TableColumn<Map<String,Object>,Object> colDireccionP;
    @FXML private TableColumn<Map<String,Object>,Object> colContactoP;
    @FXML private TableColumn<Map<String,Object>,Object> colRazonSocialP;
    @FXML private TableColumn<Map<String,Object>,Object> colPagWebP;
    @FXML private Button btnAgregarPr;
    @FXML private Button btnEditarPr;
    @FXML private Button btnEliminarPr;
    @FXML private Button btnReportesPr;
    @FXML private MenuItem btnRegresar;
    @FXML private MenuItem btnEmail;
    @FXML private MenuItem btnTelefono;

    @Override public void initialize(URL url, ResourceBundle resources) {
        column(colCodigoP,"codigoProveedor");
        column(colNitP,"NITProveedor");
        column(colNombresP,"nombresProveedor");
        column(colApellidosP,"apellidosProveedor");
        column(colDireccionP,"direccionProveedor");
        column(colContactoP,"contactoPrincipal");
        column(colRazonSocialP,"razonSocial");
        column(colPagWebP,"paginaWeb");
        configure(tblProveedores,"Proveedores",new String[]{"codigoProveedor","NITProveedor","nombresProveedor","apellidosProveedor","direccionProveedor","razonSocial","contactoPrincipal","paginaWeb"},
            new Field[]{new Field("codigoProveedor","int",0,txtCodigoPr,false),
                new Field("NITProveedor","text",10,txtNitPr,false),
                new Field("nombresProveedor","text",60,txtNombrePr,false),
                new Field("apellidosProveedor","text",60,txtApellidoPr,false),
                new Field("direccionProveedor","text",150,txtDireccionPr,false),
                new Field("razonSocial","text",60,txtRazonSocialPr,false),
                new Field("contactoPrincipal","text",100,txtContactoPr,false),
                new Field("paginaWeb","text",50,txtPaginaWebPr,true)},
            btnAgregarPr,btnEditarPr,btnEliminarPr,btnReportesPr,"sp_agregarProveedores","sp_actualizarProveedores","sp_eliminarProveedores",
            new String[]{"codigoProveedor","NITProveedor","nombresProveedor","apellidosProveedor","direccionProveedor","razonSocial","contactoPrincipal","paginaWeb"},new String[]{"codigoProveedor","NITProveedor","nombresProveedor","apellidosProveedor","direccionProveedor","razonSocial","contactoPrincipal","paginaWeb"},"ProveedoresFactura.jasper");
    }
    @FXML public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.MenuPrincipalView();
        }if (event.getSource() == btnEmail) {
            escenarioPrincipal.EmailProveedorView();
        }if (event.getSource() == btnTelefono) {
            escenarioPrincipal.TelefonoProveedorView();
        }

    }
}

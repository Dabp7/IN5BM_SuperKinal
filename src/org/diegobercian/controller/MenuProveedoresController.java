package org.diegobercian.controller;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.diegobercian.bean.Proveedores;
import org.diegobercian.db.Conexion;
import org.diegobercian.system.Main;


public class MenuProveedoresController implements Initializable  {
    @FXML
    private TextField txtCodigoPr;
    @FXML
    private TextField txtNitPr;
    @FXML
    private TextField txtNombrePr;
    @FXML
    private TextField txtApellidoPr;
    @FXML
    private TextField txtDireccionPr;
    @FXML
    private TextField txtRazonSocialPr;
    @FXML
    private TextField txtContactoPr;
    @FXML
    private TextField txtPaginaWebPr;
    @FXML
    private TableView tblProveedores;
    @FXML
    private TableColumn colCodigoP;
    @FXML
    private TableColumn colNitP;
    @FXML
    private TableColumn colNombresP;
    @FXML
    private TableColumn colApellidosP;
    @FXML
    private TableColumn colDireccionP;
    @FXML
    private TableColumn colContactoP;
    @FXML
    private TableColumn colRazonSocialP;
    @FXML
    private TableColumn colPagWebP;
    @FXML
    private Button btnAgregarPr;
    @FXML
    private Button btnEditarPr;
    @FXML
    private Button btnEliminarPr;
    @FXML
    private Button btnReportesPr;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgBuscar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private MenuItem btnRegresar;
    private MenuItem btnEmail;
    private MenuItem btnTelefono;
    
    private ObservableList<Proveedores> listaProveedores;
    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    } 

    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colCodigoP.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        colNitP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("NITProveedor"));
        colNombresP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombresProveedor"));
        colApellidosP.setCellValueFactory(new PropertyValueFactory<>("apellidosProveedor"));
        colDireccionP.setCellValueFactory(new PropertyValueFactory<>("direccionProveedor"));
        colContactoP.setCellValueFactory(new PropertyValueFactory<>("razonSocial"));
        colRazonSocialP.setCellValueFactory(new PropertyValueFactory<>("contactoPrincipal"));
        colPagWebP.setCellValueFactory(new PropertyValueFactory<>("paginaWeb"));
    }
    
    public void seleccionarElemento() {
        txtCodigoPr.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        txtNitPr.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNITProveedor());
        txtNombrePr.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNombresProveedor());
        txtApellidoPr.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getApellidosProveedor());
        txtDireccionPr.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
        txtRazonSocialPr.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
        txtContactoPr.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
        txtPaginaWebPr.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());
    }
    
    public ObservableList<Proveedores> getProveedores() {

        ArrayList<Proveedores> lista = new ArrayList<>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarProveedores;");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProveedores = FXCollections.observableList(lista);
    }
    
    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregarPr.setText("Guardar");
                btnEditarPr.setText("Cancelar");
                btnEliminarPr.setDisable(true);
                btnReportesPr.setDisable(true);
                imgAgregar.setImage(new Image("/org/diegobercian/images/guardar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                tipoDeOperaciones = MenuProveedoresController.operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarPr.setText("Agregar");
                btnEditarPr.setText("Editar");
                btnEliminarPr.setDisable(false);
                btnReportesPr.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                tipoDeOperaciones = MenuProveedoresController.operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar() {
        Proveedores registro = new Proveedores();
        registro.setCodigoProveedor(Integer.parseInt(txtCodigoPr.getText()));
        registro.setNITProveedor(txtNitPr.getText());
        registro.setNombresProveedor(txtNombrePr.getText());
        registro.setApellidosProveedor(txtApellidoPr.getText());
        registro.setDireccionProveedor(txtDireccionPr.getText());
        registro.setRazonSocial(txtRazonSocialPr.getText());
        registro.setContactoPrincipal(txtContactoPr.getText());
        registro.setPaginaWeb(txtPaginaWebPr.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarProveedores(?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.execute();
            listaProveedores.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    btnEditarPr.setText("Actualizar");
                    btnReportesPr.setText("Cancelar");
                    btnAgregarPr.setDisable(true);
                    btnEliminarPr.setDisable(true);
                    imgEditar.setImage(new Image("/org/diegobercian/images/actualizar.png"));
                    imgBuscar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                    activarControles();
                    txtCodigoPr.setEditable(false);
                    tipoDeOperaciones = MenuProveedoresController.operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un proveedor para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarPr.setText("Editar");
                btnReportesPr.setText("Reportes");
                btnAgregarPr.setDisable(false);
                btnEliminarPr.setDisable(false);
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgBuscar.setImage(new Image("/org/diegobercian/images/buscar.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = MenuProveedoresController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarProveedores(?,?,?,?,?,?,?,?)");
            Proveedores registro = (Proveedores)tblProveedores.getSelectionModel().getSelectedItem();
            
            registro.setNITProveedor(txtNitPr.getText());
            registro.setNombresProveedor(txtNombrePr.getText());
            registro.setApellidosProveedor(txtApellidoPr.getText());
            registro.setDireccionProveedor(txtDireccionPr.getText());
            registro.setRazonSocial(txtRazonSocialPr.getText());
            registro.setContactoPrincipal(txtContactoPr.getText());
            registro.setPaginaWeb(txtPaginaWebPr .getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar() {

        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarPr.setText("Agregar");
                btnEditarPr.setText("Editar");
                btnEliminarPr.setDisable(false);
                btnReportesPr.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default: 
                if(tblProveedores.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar al proveedor?", "Eliminar provedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarProveedores(?)");
                            procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un proveedor para eliminar");
                }
                break;
                
        }
    }
    
    
    
    public void desactivarControles() {
        txtCodigoPr.setEditable(false);
        txtNitPr.setEditable(false);
        txtNombrePr.setEditable(false);
        txtApellidoPr.setEditable(false);
        txtDireccionPr.setEditable(false);
        txtRazonSocialPr.setEditable(false);
        txtContactoPr.setEditable(false);
        txtPaginaWebPr.setEditable(false);
    }

    public void activarControles() {
        txtCodigoPr.setEditable(true);
        txtNitPr.setEditable(true);
        txtNombrePr.setEditable(true);
        txtApellidoPr.setEditable(true);
        txtDireccionPr.setEditable(true);
        txtRazonSocialPr.setEditable(true);
        txtContactoPr.setEditable(true);
        txtPaginaWebPr.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoPr.clear();
        txtNitPr.clear();
        txtNombrePr.clear();
        txtApellidoPr.clear();
        txtDireccionPr.clear();
        txtRazonSocialPr.clear();
        txtContactoPr.clear();
        txtPaginaWebPr.clear();
    }
    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.MenuPrincipalView();
        }if (event.getSource() == btnEmail) {
            escenarioPrincipal.EmailProveedorView();
        }if (event.getSource() == btnTelefono) {
            escenarioPrincipal.TelefonoProveedorView();
        }
    }

         
}

package org.diegobercian.controller;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.diegobercian.bean.Productos;
import org.diegobercian.bean.Proveedores;
import org.diegobercian.bean.TipoProducto;
import org.diegobercian.db.Conexion;
import org.diegobercian.reportes.GenerarReportes;
import org.diegobercian.system.Main;

/**
 *
 * @author informatica
 */
public class MenuProductosController implements Initializable {
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Productos> listaProductos;
    private ObservableList <Proveedores> listaProveedores;
    private ObservableList <TipoProducto> listaTipoProducto;
    @FXML
    private Button btnAgregar;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnReportes;
    @FXML
    private ImageView imgBuscar;
    @FXML
    private MenuItem btnRegresar;
    @FXML
    private MenuItem btnTipoProducto;
    @FXML
    private TableView tblProductos;
    @FXML
    private TableColumn colCodigoPro;
    @FXML
    private TableColumn colDescripcionPro;
    @FXML
    private TableColumn colPrecioUPro;
    @FXML
    private TableColumn colPrecioDoPro;
    @FXML
    private TableColumn colPrecioMPro;
    @FXML
    private TableColumn colImagenPro;
    @FXML
    private TableColumn colExistenciaPro;
    @FXML
    private TableColumn colTipoProducto;
    @FXML
    private TableColumn colProveedores;
    @FXML
    private TextField txtDescripcionPro;
    @FXML
    private TextField txtCodigoPro;
    @FXML
    private TextField txtImagenPro;
    @FXML
    private ComboBox cmbTipoProducto;
    @FXML
    private ComboBox cmbProveedores;
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cargarDatos();
       cmbTipoProducto.setItems(getTipoProducto());
       cmbProveedores.setItems(getProveedores());
    }  
    
    public void seleccionarElemento(){
        txtCodigoPro.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtDescripcionPro.setText(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtImagenPro.setText(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getImagenProducto());
        cmbTipoProducto.getSelectionModel().select(buscarTipoProducto(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        cmbProveedores.getSelectionModel().select(buscarProveedores(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }
    
    public TipoProducto buscarTipoProducto(int codigoTipoProducto){
        TipoProducto resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarTipoProducto(?);");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                                             registro.getString("descripcion")
            
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Proveedores buscarProveedores(int codigoProveedor){
        Proveedores resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarProveedores(?);");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                                             registro.getString("NITProveedor"),
                                             registro.getString("nombresProveedor"),
                                             registro.getString("apellidosProveedor"),
                                             registro.getString("direccionProveedor"),
                                             registro.getString("razonSocial"),
                                             registro.getString("contactoPrincipal"),
                                             registro.getString("paginaWeb")
            
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colCodigoPro.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colDescripcionPro.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
        colPrecioUPro.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colPrecioDoPro.setCellValueFactory(new PropertyValueFactory<>("precioDocena"));
        colPrecioMPro.setCellValueFactory(new PropertyValueFactory<>("precioMayor"));
        colImagenPro.setCellValueFactory(new PropertyValueFactory<>("imagenProducto"));
        colExistenciaPro.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colTipoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoTipoProducto"));
        colProveedores.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }
    
    
    
    public ObservableList<Productos> getProductos(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarProductos();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos(resultado.getString("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getString("imagenProducto"),
                        resultado.getInt("existencia"),
                        resultado.getInt("codigoTipoProducto"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductos = FXCollections.observableList(lista);
        
        
    }
    
    public ObservableList<Proveedores> getProveedores() {

        ArrayList<Proveedores> listaPro = new ArrayList<>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarProveedores;");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(resultado.getInt("codigoProveedor"),
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

        return listaProveedores = FXCollections.observableList(listaPro);
    }
    
    public ObservableList<TipoProducto> getTipoProducto() {

        ArrayList<TipoProducto> listaTip = new ArrayList<>();
        ResultSet resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarTipoProducto();");
            resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaTip.add(new TipoProducto(resultado.getInt("codigoTipoProducto"),
                        resultado.getString("descripcion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoProducto = FXCollections.observableList(listaTip);
    }
    
    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                imgAgregar.setImage(new Image("/org/diegobercian/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnReportes.setDisable(false);
                btnEditar.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgEliminar.setImage(new Image("/org/diegobercian/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                cargarDatos();
                break;
        }
    }
    
    public void guardar() {
        Productos registro = new Productos();
        registro.setCodigoProducto(txtCodigoPro.getText());
        registro.setDescripcionProducto(txtDescripcionPro.getText());
        registro.setImagenProducto(txtImagenPro.getText());
        registro.setCodigoTipoProducto(((TipoProducto)cmbTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setCodigoProveedor(((Proveedores)cmbProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_agregarProductos(?,?,?,?,?);");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setString(3, registro.getImagenProducto());
            procedimiento.setInt(4, registro.getCodigoTipoProducto());
            procedimiento.setInt(5, registro.getCodigoProveedor());
            procedimiento.execute();
            listaProductos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/diegobercian/images/actualizar.png"));
                    imgBuscar.setImage(new Image("/org/diegobercian/images/cancelar.png"));
                    activarControles();
                    txtCodigoPro.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgBuscar.setImage(new Image("/org/diegobercian/images/buscar.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarProductos(?,?,?,?,?);");
            Productos registro = (Productos)tblProductos.getSelectionModel().getSelectedItem();
            registro.setDescripcionProducto(txtDescripcionPro.getText());
            registro.setImagenProducto(txtImagenPro.getText());
            registro.setCodigoTipoProducto(((TipoProducto)cmbTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
            registro.setCodigoProveedor(((Proveedores)cmbProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setString(3, registro.getImagenProducto());
            procedimiento.setInt(4, registro.getCodigoTipoProducto());
            procedimiento.setInt(5, registro.getCodigoProveedor());
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
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/diegobercian/images/agregar.png"));
                imgEliminar.setImage(new Image("/org/diegobercian/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default: 
                if(tblProductos.getSelectionModel().getSelectedItem()  != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el producto?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta  == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarProductos(?);");
                            procedimiento.setString(1, ((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
                            limpiarControles(); 
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para eliminar");
                }
                break;
                
        }
    }
    
   public void reportes() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                imprimirReporteProductos();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReportes.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/diegobercian/images/editar.png"));
                imgBuscar.setImage(new Image("/org/diegobercian/images/buscar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
        }
    }
    
    public void imprimirReporteProductos(){
        Map parametros = new HashMap();
        parametros.put("codigoProveedor", null);
        GenerarReportes.mostrarReportes("ProductosReporte.jasper", "Reporte Productos", parametros);
        
    }
    
    
    public void activarControles(){
        txtCodigoPro.setEditable(true);
        txtDescripcionPro.setEditable(true);
        txtImagenPro.setEditable(true);
        cmbTipoProducto.setDisable(false);
        cmbProveedores.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoPro.setEditable(false);
        txtDescripcionPro.setEditable(false);
        txtImagenPro.setEditable(false);
        cmbTipoProducto.setDisable(true);
        cmbProveedores.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoPro.clear();
        txtDescripcionPro.clear();
        txtImagenPro.clear();
        tblProductos.getSelectionModel().getSelectedItem();
        cmbTipoProducto.getSelectionModel().getSelectedItem();
        cmbProveedores.getSelectionModel().getSelectedItem();
    }
    
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.MenuPrincipalView();
        }if(event.getSource() == btnTipoProducto){
            escenarioPrincipal.TipoProductoView();
        }
    }
    
      
}

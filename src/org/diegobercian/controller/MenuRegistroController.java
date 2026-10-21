package org.diegobercian.controller;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.diegobercian.service.AuthService;
import org.diegobercian.ui.Async;
import org.diegobercian.system.Main;

public class MenuRegistroController implements Initializable {
    @FXML private TextField txtCorreoR;
    @FXML private PasswordField txtContraR;
    @FXML private Button btnCrear,btnRegresar;
    private Main escenarioPrincipal;
    private boolean busy;
    @FXML public void crearUsuario() {
        if(busy) return;
        String user=txtCorreoR.getText().trim();char[] password=txtContraR.getText().toCharArray();
        try {AuthService.validate(user,password,true);} catch(IllegalArgumentException e) {Arrays.fill(password,'\0');Async.error(e);return;}
        busy=true;btnCrear.setDisable(true);btnRegresar.setDisable(true);
        Async.run(() -> {new AuthService().register(user,password);return true;}, ok -> {
            busy=false;btnCrear.setDisable(false);btnRegresar.setDisable(false);txtContraR.clear();
            new Alert(Alert.AlertType.INFORMATION,"Usuario registrado correctamente.").show();
        }, e -> {busy=false;btnCrear.setDisable(false);btnRegresar.setDisable(false);Async.error(e);});
    }
    public Main getEscenarioPrincipal() {return escenarioPrincipal;}
    public void setEscenarioPrincipal(Main main) {escenarioPrincipal=main;}
    @FXML public void handleButtonAction(ActionEvent e) {if(!busy && e.getSource()==btnRegresar) escenarioPrincipal.LoginView();}
    @Override public void initialize(URL url,ResourceBundle resources) {txtContraR.setPromptText("Contraseña: mínimo 12 caracteres");}
}

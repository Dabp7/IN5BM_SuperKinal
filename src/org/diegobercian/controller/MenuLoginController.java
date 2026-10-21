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

public class MenuLoginController implements Initializable {
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContra;
    @FXML private Button btnInicio,btnRegistro;
    private Main escenarioPrincipal;
    private boolean busy;
    @FXML public void validaUsuario() {
        if(busy) return;
        String user=txtCorreo.getText().trim();char[] password=txtContra.getText().toCharArray();
        try {AuthService.validate(user,password,false);} catch(IllegalArgumentException e) {Arrays.fill(password,'\0');Async.error(e);return;}
        busy=true;btnInicio.setDisable(true);btnRegistro.setDisable(true);
        Async.run(() -> new AuthService().login(user,password), ok -> {
            busy=false;btnInicio.setDisable(false);btnRegistro.setDisable(false);txtContra.clear();
            if(ok) escenarioPrincipal.MenuPrincipalView();else Async.error(new IllegalArgumentException("Usuario o contraseña incorrectos. Si actualizó una base anterior, migre las contraseñas."));
        }, e -> {busy=false;btnInicio.setDisable(false);btnRegistro.setDisable(false);Async.error(e);});
    }
    @Override public void initialize(URL url,ResourceBundle resources) {txtContra.setOnAction(e -> validaUsuario());}
    public Main getEscenarioPrincipal() {return escenarioPrincipal;}
    public void setEscenarioPrincipal(Main main) {escenarioPrincipal=main;}
    @FXML public void handleButtonAction(ActionEvent e) {if(!busy && e.getSource()==btnRegistro) escenarioPrincipal.RegistroView();}
}

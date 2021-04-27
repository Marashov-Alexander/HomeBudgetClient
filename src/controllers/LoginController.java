package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class LoginController {

    private Consumer<String[]> onLogin;

    public void setListeners(Consumer<String[]> onLogin) {
        this.onLogin = onLogin;
    }

    @FXML
    private TextField loginTB;

    @FXML
    private TextField passwordTB;

    @FXML
    private void onLoginBtnClick(ActionEvent actionEvent) {
        final String login = loginTB.getText();
        final String password = passwordTB.getText();
        onLogin.accept(new String[]{login, password});
    }
}

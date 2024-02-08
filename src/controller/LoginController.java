package controller;


import database.LoginQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button logInBtn;

    @FXML
    private Text logInHeaderText;

    @FXML
    private Text logInLocation;

    @FXML
    private Text logInLocationTxt;

    @FXML
    private TextField logInPasswordField;

    @FXML
    private Text logInPasswordTxt;

    @FXML
    private TextField logInUsernameField;

    @FXML
    private Text logInUsernameTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login screen initialized");
    }

    @FXML
    void onLoginAction(ActionEvent event){
        try{
            boolean loginIsValid = LoginQuery.checkUserCredentials(logInUsernameField.getText(), logInPasswordField.getText());

            if(loginIsValid){
                System.out.println("Successful login!");

                // Switching to home screen on successful login
                try{
                    changeToHome();
                }catch(Exception fxExeption){
                    fxExeption.printStackTrace();
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    void changeToHome() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
}

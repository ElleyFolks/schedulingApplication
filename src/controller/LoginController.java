package controller;


import database.AppointmentQuery;
import database.LoginQuery;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.Duration;
import main.Main;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginController implements Initializable {

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("language/language", Locale.getDefault());

    @FXML
    private Button logInBtn;

    @FXML
    private Text logInHeaderText;

    @FXML
    private Text logInLocation;

    @FXML
    private Text logInLocationText;

    @FXML
    private PasswordField logInPasswordField;

    @FXML
    private Text logInPasswordText;

    @FXML
    private TextField logInUsernameField;

    @FXML
    private Text logInUsernameText;

    @FXML
    private Text logInTimeZone;

    @FXML
    private Text logInTimeZoneText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Login screen initialized");

        resourceBundle = ResourceBundle.getBundle("language/language", Locale.getDefault());

        logInBtn.setText(resourceBundle.getString("loginBtnLabel"));
        logInHeaderText.setText(resourceBundle.getString("header"));
        logInLocation.setText(resourceBundle.getString("country"));
        logInLocationText.setText(resourceBundle.getString("locationText"));
        logInPasswordText.setText(resourceBundle.getString("passwordText"));
        logInUsernameText.setText(resourceBundle.getString("usernameText"));
        logInTimeZone.setText(String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));
        logInTimeZoneText.setText(resourceBundle.getString("timeZoneText"));
    }

    @FXML
    void onLoginAction(ActionEvent event){
        // input validation
        if(isValidCredentials(logInUsernameField.getText(), logInPasswordField.getText())) {

            // attempting to log in if valid
            try {
                boolean loginIsValid = LoginQuery.checkUserCredentials(logInUsernameField.getText(), logInPasswordField.getText());

                if (loginIsValid) {
                    System.out.println("Successful login!");

                    // switching to home screen on successful login
                    try {
                        changeToHome();
                        Appointment appointment = appointmentWithin15min();
                        if(appointment != null){
                            System.out.println("Appointment within 15 minutes of logging in!");

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information");
                            alert.setHeaderText("There is an appointment within the next 15 minutes!");
                            alert.setContentText("ID: "+appointment.getAppointmentId()+" Date/Time: "+appointment.getStartDateTime().toString());
                            alert.showAndWait();
                        }
                        else {
                            System.out.println("NO appointments within 15 minutes of logging in...");

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information");
                            alert.setHeaderText("There is not an appointment within the next 15 minutes.");
                            alert.showAndWait();
                        }
                    } catch (Exception fxExeption) {
                        fxExeption.printStackTrace();
                    }
                } else{
                    // shows error if unsuccessful login
                    System.out.println("Unsuccessful login.");
                    showAlertOnScreen("LoginUnsuccessful");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static Appointment appointmentWithin15min() {
        ObservableList<Appointment> appointments = AppointmentQuery.getAllAppointments();
        if (appointments != null) {
            for (Appointment existingAppointment : appointments) {
                //System.out.println("\n"+LocalDateTime.now());
                //System.out.println(existingAppointment.getEventStart());
                Duration duration = Duration.between(LocalDateTime.now(), existingAppointment.getStartDateTime());

                if (Math.abs(duration.toMinutes()) <= 15) {
                    return existingAppointment;
                }
            }
            return null;
        } else {
            System.out.println("Failed to find user appointments.");
            return null;
        }
    }
    private boolean isValidCredentials(String username, String password){
        if(username.isEmpty()){
            if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")){
                showAlertOnScreen("UserNameEmpty");
                return false;
            }
        }

        else if(password.isEmpty()){
            if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")){
                showAlertOnScreen("PasswordEmpty");
                return false;
            }
        }

        if(!username.isEmpty() && !password.isEmpty()){
        return true;
        }

        return false;
    }

    void showAlertOnScreen(String alertString){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertString){
            case "UserNameEmpty":
                alert.setTitle(resourceBundle.getString("errorTitle"));
                alert.setContentText(resourceBundle.getString("usernameErrorContext"));
                alert.showAndWait();
                break;
            case "PasswordEmpty":
                alert.setTitle(resourceBundle.getString("errorTitle"));
                alert.setContentText(resourceBundle.getString("passwordErrorContext"));
                alert.showAndWait();
                break;
            case "LoginUnsuccessful":
                alert.setTitle(resourceBundle.getString("errorTitle"));
                alert.setContentText(resourceBundle.getString("unsuccessfulLoginErrorContext"));
                alert.showAndWait();
                break;
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

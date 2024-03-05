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

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.Duration;
import main.Main;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Class that contains event handlers, controller methods,
 * and logical implementation for logging into the application.
 *<p>
 * LAMBDA EXPRESSION - Contains a lambda expression for retrieving a specific resource from the language resource bundle.
 * It is used for the logic that translates the login screen to English or French.
 *</p>
 * </p>
 * LAMBDA EXPRESSION - Contains a lambda expression for passing in the login_activity.txt filename to
 * functions implementing logging (reading and writing).
 *</p>
 * JUSTIFICATION - Improves readability by providing quicker, shorter and more efficient code.
 *
 * @author Elley Folks
 */
public class LoginController implements Initializable {
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("language/language", Locale.getDefault());

    /** Accesses language bundle and retrieves a specific translated message based on key.*/
    public interface retrieveLanguageBundle{

        public String getMsg(String key);
    }

    /**
     * Lambda Expression for retrieving specific language message from language resource bundle.
     */
    retrieveLanguageBundle language = (key) -> {
        return resourceBundle.getString(key);
    };

    /** Tracks login attempts by locating file path of log file*/
    public interface loginAttemptsRecord {
        public String getLogFile();
    }

    /**
     * Lambda Expression for retrieving the login attempt file path.
     */
    loginAttemptsRecord loginTracker = () -> {
        return "login_activity.txt";
    };

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

    /**
     * Initializes the login screen, setting labels and text values based on the user's language preferences.
     * Also prints a log message indicating the initialization of the login screen.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Login screen initialized");

        logInBtn.setText(language.getMsg("loginBtnLabel"));
        logInHeaderText.setText(language.getMsg("header"));
        logInLocation.setText(language.getMsg("country"));
        logInLocationText.setText(language.getMsg("locationText"));
        logInPasswordText.setText(language.getMsg("passwordText"));
        logInUsernameText.setText(language.getMsg("usernameText"));
        logInTimeZone.setText(String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));
        logInTimeZoneText.setText(language.getMsg("timeZoneText"));
    }

    /**
     * Used when login button pressed on form.
     * Validates user credentials, checks for appointments within the next 15 minutes,
     * and transitions to the home screen upon successful login.
     */
    @FXML
    void onLoginAction(){

        // generates log file to track login attempts
        createLoginFile();

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
                        Appointment appointment = checkAppointmentsIn15min();
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

                    // writes entry in log file for successful login
                    writeToLoginFile("Login Successful");

                } else{
                    // shows error if unsuccessful login
                    showAlertOnScreen("LoginUnsuccessful");

                    // writes entry in log file for unsuccessful login
                    writeToLoginFile("Login Unsuccessful");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Verifies if an appointment is scheduled within the upcoming 15 minutes.
     * @return The appointment object if one is found within the specified time frame, else returns null.
     */
    public static Appointment checkAppointmentsIn15min() {
        ObservableList<Appointment> appointments = AppointmentQuery.getAllAppointments();
        if (appointments != null) {
            for (Appointment existingAppointment : appointments) {
                Duration timeDelta = Duration.between(LocalDateTime.now(), existingAppointment.getStartDateTime());

                if (Math.abs(timeDelta.toMinutes()) <= 15) {
                    return existingAppointment;
                }
            }
            return null;
        } else {
            System.out.println("Failed to get appointments!");
            return null;
        }
    }

    /**
     * Validates the provided username and password for login.
     * Displays an appropriate alert if the username or password is empty based on the user's language preferences.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return True if both username and password are non-empty; otherwise, false.
     */
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

    /**
     * Creates a login log file if it doesn't exist and prints a message indicating the file's status.
     * The log file is created at the location specified by the login tracker.
     */
    private void createLoginFile() {
        try {
            File loginRecords = new File(loginTracker.getLogFile());
            if (loginRecords.createNewFile()) {
                System.out.println("Created new log file: " + loginRecords.getName());
            } else {
                System.out.println("Located pre-existing log file: " + loginRecords.getPath());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Writes login-related information to the login log file.
     * Information includes the login status, username, password, and UTC timestamp.
     * Appends the information to the existing log file.
     *
     * @param loginStatus The status of the login attempt (e.g., "Login Successful" or "Login Unsuccessful").
     */
    private void writeToLoginFile(String loginStatus){
        try {
            FileWriter fileWriter = new FileWriter(loginTracker.getLogFile(), true);
            SimpleDateFormat formatDateTimeUtc = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            formatDateTimeUtc.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date userDateTime = new Date(System.currentTimeMillis());
            String formattedDateUtc = formatDateTimeUtc.format(userDateTime);

            fileWriter.write(loginStatus + "! Information: Username = "+ logInUsernameField.getText() +", Password = "+ logInPasswordField.getText()
                    + ", UTC Timestamp = "+ formattedDateUtc + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert on the screen based on the provided alert string.
     * @param alertString The string indicating the type of alert to display.
     */
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

    /**
     * Used to transition current scene back to home scene.
     * Will load "Home.fxml" using FXMLLoader as the scene for the primary stage.
     * @throws IOException Occurs if there is a problem locating or loading the fxml file.
     */
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

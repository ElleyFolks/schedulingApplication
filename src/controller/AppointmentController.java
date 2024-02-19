package controller;

import database.ManageQuery;
import helper.Validation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AppointmentController implements Initializable {

    @FXML
    private Button appointmentCancel;

    @FXML
    private Button appointmentSave;

    @FXML
    private DatePicker endDate;

    @FXML
    private ComboBox<String> endHour;

    @FXML
    private ComboBox<String> endMinute;

    @FXML
    private ComboBox<String> endTimeCode;

    @FXML
    private DatePicker startDate;

    @FXML
    private ComboBox<String> startHour;

    @FXML
    private ComboBox<String> startMinute;

    @FXML
    private ComboBox<String> startTimeCode;

    @FXML
    private TextField appointmentTitle;

    @FXML
    private TextField appointmentDescription;

    @FXML
    private TextField appointmentLocation;

    @FXML
    private TextField appointmentType;

    @FXML
    private ComboBox<String> customerId;

    @FXML
    private ComboBox<String> userId;

    @FXML
    private ComboBox<String> contactId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // adds AM or PM options to time code combo box
        startTimeCode.getItems().add("AM");
        startTimeCode.getItems().add("PM");
        endTimeCode.getItems().add("AM");
        endTimeCode.getItems().add("PM");

        // Add combo box options for hours
        for (int i = 1; i <= 12; i++) {
            // adds zero in front if int single digit (for time code purposes)
            if(i<10){
                startHour.getItems().add("0"+String.valueOf(i));
                endHour.getItems().add("0"+String.valueOf(i));
            }else{
                startHour.getItems().add(String.valueOf(i));
                endHour.getItems().add(String.valueOf(i));
            }
        }
        // Combo box options for every 5 minutes
        for (int i = 0; i <= 59; i+=5) {
            if(i<10){
                startMinute.getItems().add("0"+String.valueOf(i));
                endMinute.getItems().add("0"+String.valueOf(i));
            }else{
                startMinute.getItems().add(String.valueOf(i));
                endMinute.getItems().add(String.valueOf(i));
            }
        }

        // adding combo box options for customer ID
        ManageQuery.setComboBoxOptions(customerId, "SELECT Customer_ID FROM customers ORDER BY Customer_ID");

        // adding combo box options for user ID
        ManageQuery.setComboBoxOptions(userId,"SELECT User_ID FROM users ORDER BY User_ID");

        // adding combo box options for contact ID
        ManageQuery.setComboBoxOptions(contactId, "SELECT Contact_ID FROM contacts ORDER BY Contact_ID");
    }

    @FXML
    void onSaveAction(){
        if(isValidAppointment()) {
            try {
                String title = appointmentTitle.getText();
                String description = appointmentDescription.getText();
                String location = appointmentLocation.getText();
                String type = appointmentType.getText();

                Integer customerID = Integer.parseInt(String.valueOf(customerId.getValue()));
                Integer userID = Integer.parseInt(String.valueOf(userId.getValue()));
                Integer contactID = Integer.parseInt(String.valueOf(contactId.getValue()));


                // TODO need to validate date range - start cannot be after end
                // string of date time in format of YYYY-MM-DD hh:hha
                String startDateTimeStr = startDate.getValue() + " " + startHour.getValue() + ":" + startMinute.getValue()
                        + startTimeCode.getValue();
                String endDateTimeStr = endDate.getValue() + " " + endHour.getValue() + ":" + endMinute.getValue()
                        + endTimeCode.getValue();

                // converting local time to UTC
                String utcStartDateTime = helper.Time.changeLocalToUTC(startDateTimeStr);
                String utcEndDateTime = helper.Time.changeLocalToUTC(endDateTimeStr);

                // creating new row and creates new appointment object with values
                boolean rowsChanged = ManageQuery.createRowQuery(title, description, location, type, utcStartDateTime,
                        utcEndDateTime, String.valueOf(contactID), String.valueOf(customerID), String.valueOf(userID));

                if (rowsChanged) {
                    System.out.println("Successfully added new row");

                    // switching back to home
                    try {
                        switchToHomeScene();
                    } catch (Exception fxmlException) {
                        System.out.println("FXML error: " + fxmlException.getMessage());
                    }
                } else {
                    // TODO add warning here
                    System.out.println("Adding row failed.");
                }
            } catch (Exception e) {
                System.out.println("Saving error: " + e.getMessage());
            }
        }
    }

    boolean isValidAppointment(){
        if (Validation.isEmptyString(appointmentTitle, "Title")){
            return false;
        }

        if (Validation.isEmptyString(appointmentDescription, "Description")){
            return false;
        }

        if (Validation.isEmptyString(appointmentLocation, "Location")){
            return false;
        }

        if (Validation.isEmptyString(appointmentType, "Type")){
            return false;
        }

        // add validation for dates here

        // add validation for




        else {
            return true;
        }
    }

    @FXML
    void switchToHomeScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Home.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
}




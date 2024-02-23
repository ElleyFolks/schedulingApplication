package controller;

import database.AppointmentQuery;
import database.HelperQuery;
import helper.Alerts;
import helper.Validation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    Appointment appointmentSelected = HomeController.getAppointmentToMod();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeComboBoxes();

        // will load in values of appointment is selected
        if (appointmentSelected != null) {

            appointmentTitle.setText(appointmentSelected.getAppointmentTitle());
            appointmentDescription.setText(appointmentSelected.getAppointmentDescription());
            appointmentLocation.setText(appointmentSelected.getAppointmentLocation());
            appointmentType.setText(appointmentSelected.getAppointmentType());

            LocalDateTime startDateTime = appointmentSelected.getStartDateTime();
            startHour.setValue(startDateTime.format(DateTimeFormatter.ofPattern("hh")));
            startMinute.setValue(startDateTime.format(DateTimeFormatter.ofPattern("mm")));
            startTimeCode.setValue(startDateTime.format(DateTimeFormatter.ofPattern("a")));

            LocalDateTime endDateTime = appointmentSelected.getEndDateTime();
            endHour.setValue(endDateTime.format(DateTimeFormatter.ofPattern("hh")));
            endMinute.setValue(endDateTime.format(DateTimeFormatter.ofPattern("mm")));
            endTimeCode.setValue(endDateTime.format(DateTimeFormatter.ofPattern("a")));

            startDate.setValue(appointmentSelected.getStartDateTime().toLocalDate());
            endDate.setValue(appointmentSelected.getEndDateTime().toLocalDate());

            customerId.setValue(String.valueOf(appointmentSelected.getCustomerId()));
            userId.setValue(String.valueOf(appointmentSelected.getUserId()));
            contactId.setValue(String.valueOf(appointmentSelected.getContactId()));
        }
    }

    void initializeComboBoxes(){
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
        HelperQuery.setComboBoxOptions(customerId, "SELECT Customer_ID FROM customers ORDER BY Customer_ID");

        // adding combo box options for user ID
        HelperQuery.setComboBoxOptions(userId,"SELECT User_ID FROM users ORDER BY User_ID");

        // adding combo box options for contact ID
        HelperQuery.setComboBoxOptions(contactId, "SELECT Contact_ID FROM contacts ORDER BY Contact_ID");
    }

    @FXML
    void onSaveAction(){
        if(isValidAppointment()) {
            try {
                String title = appointmentTitle.getText();
                String description = appointmentDescription.getText();
                String location = appointmentLocation.getText();
                String type = appointmentType.getText();

                String customerID = customerId.getValue();
                String userID = userId.getValue();
                String contactID = contactId.getValue();

                // string of date time in format of YYYY-MM-DD hh:hha
                String startDateTimeStr = startDate.getValue() + " " + startHour.getValue() + ":" + startMinute.getValue()
                        + startTimeCode.getValue();
                String endDateTimeStr = endDate.getValue() + " " + endHour.getValue() + ":" + endMinute.getValue()
                        + endTimeCode.getValue();

                // converting local time to military
                String militaryStartDateTime = helper.Time.localTo24String(startDateTimeStr);
                String militaryEndDateTime = helper.Time.localTo24String(endDateTimeStr);

                // adds new appointment if none selected
                if (appointmentSelected == null) {

                    // creating new row and creates new appointment object with values
                    boolean rowsChanged = HelperQuery.createRowQuery(
                            title,
                            description,
                            location,
                            type,
                            militaryStartDateTime,
                            militaryEndDateTime,
                            contactID,
                            customerID,
                            userID);

                    if (rowsChanged) {
                        System.out.println("Successfully added new row");

                        // switching back to home
                        try {
                            switchToHomeScene();
                        } catch (Exception fxmlException) {
                            System.out.println("FXML error: " + fxmlException.getMessage());
                        }
                    } else {
                        System.out.println("Adding row failed.");
                    }

                    //modifying selected appointment values
                }else{
                        System.out.println(customerID);
                        System.out.println(contactID);
                        System.out.println(userID);
                    boolean rowModified = AppointmentQuery.modifyAppointment(
                            String.valueOf(appointmentSelected.getAppointmentId()),
                            title,
                            description,
                            location,
                            type,
                            militaryStartDateTime,
                            militaryEndDateTime,
                            customerID,
                            userID,
                            contactID);
                    if (rowModified) {
                        System.out.println("Successfully modified row");

                        // switching back to home
                        try {
                            switchToHomeScene();
                        } catch (Exception fxmlException) {
                            System.out.println("FXML error: " + fxmlException.getMessage());
                        }
                    } else {
                        System.out.println("Modifying row failed.");
                    }
                }
            }catch(Exception e){
                System.out.println("Saving error: " + e.getMessage());
            }
        }
    }

    boolean isValidAppointment(){

        // validation for text fields
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

        // validation for dates
        if(Validation.isEmptyDatePicker(startDate, "Start Date")){
            return false;
        }
        if(Validation.isEmptyDatePicker(endDate, "End Date")){
            return false;
        }
        // checks if dates are valid
        if(Validation.isInvalidDateCombination(startDate, endDate, "Start Date or End Date")){
            return false;
        }

        // validation for time
        if(Validation.isEmptyComboBox(startHour, "Start time hour (hh of hh:mm)")){
            return false;
        }
        if(Validation.isEmptyComboBox(startMinute, "Start time minute (mm of hh:mm)")){
            return false;
        }
        if(Validation.isEmptyComboBox(startTimeCode, "Start time code (AM or PM)")){
            return false;
        }

        if(Validation.isEmptyComboBox(endHour, "End time hour (hh of hh:mm)")){
            return false;
        }
        if(Validation.isEmptyComboBox(endMinute, "End time minutes (mm of hh:mm)")){
            return false;
        }
        if(Validation.isEmptyComboBox(endTimeCode, "End Timecode (AM or PM)")){
            return false;
        }

        String startDateTimeStr = startDate.getValue() + " " + startHour.getValue() + ":" + startMinute.getValue()
                + startTimeCode.getValue();
        String endDateTimeStr = endDate.getValue() + " " + endHour.getValue() + ":" + endMinute.getValue()
                + endTimeCode.getValue();

        LocalDateTime militaryStartDateTime = helper.Time.localTo24DateTime(startDateTimeStr);
        LocalDateTime militaryEndDateTime = helper.Time.localTo24DateTime(endDateTimeStr);

        // checks if date is on weekend
        if(Validation.dateIsOutsideBusinessHours(militaryStartDateTime, militaryEndDateTime)){
            return false;
        }

        if(Validation.isInvalidTimeCombination(
                militaryStartDateTime,
                militaryEndDateTime,
                "Start time or End time")){
            return false;
        }

        // validation for ID's in combo boxes
        if(Validation.isEmptyComboBox(customerId, "Customer ID")){
            return false;
        }
        if(Validation.isEmptyComboBox(userId, "User ID")){
            return false;
        }
        if(Validation.isEmptyComboBox(contactId, "Contact ID")){
            return false;
        }



        // TODO add validation for time ranges. Look up business hours in EST.



        else {
            return true;
        }
    }

    @FXML
    void onCancelAction() {
        Optional<ButtonType> result = Alerts.showConfirmAlert("cancelConfirm", "");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                System.out.println("User canceled action... Switching back to home screen.");
                switchToHomeScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
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




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


/**
 * Class that contains event handlers, controller methods,
 * and logical implementation for validation and creating / updating / deleting appointments.
 *
 * @author Elley Folks
 */
public class AppointmentController implements Initializable {

    @FXML
    private TextField appointmentId;

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

    private Map<String, Integer> nameIdMap = new HashMap<>();

    Appointment appointmentSelected = HomeController.getAppointmentToModify();

    /**
     * Initializes appointment fxml file.
     * Initializes values in combo boxes for times and ID's.
     * If modifying an appointment, loads appropriate values into text fields.
     *
     * @param url Location of fxml file this controller controls behavior of, provided by FXMLLoader.
     * @param resourceBundle Used for localization and resource loading, provided by FXMLLoader.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeComboBoxes();

        // will load in values of appointment is selected
        if (appointmentSelected != null) {
            appointmentId.setText(String.valueOf(appointmentSelected.getAppointmentId()));
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
            contactId.setValue(appointmentSelected.getContactFullName());
        }
    }

    /**
     * Logic used to initialize options in time combo boxes, and ID combo boxes.
     */
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
        AppointmentQuery.getContactNameID(contactId, "SELECT Contact_Name, Contact_ID FROM contacts ORDER BY Contact_ID", nameIdMap);
    }

    /**
     * Used when save button is pressed on form.
     * First checks information entered in text fields, and checks if combo boxes were selected.
     * If no appointment is selected, will create a new record / row in database, and a new appointment class object.
     * If an appointment is selected, will modify and update the information of the existing appointment.
     */
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
                String contactID = String.valueOf(nameIdMap.get(contactId.getValue()));

                // string of date time in format of YYYY-MM-DD hh:hha
                String startDateTimeStr = startDate.getValue() + " " + startHour.getValue() + ":" + startMinute.getValue()
                        + startTimeCode.getValue();
                String endDateTimeStr = endDate.getValue() + " " + endHour.getValue() + ":" + endMinute.getValue()
                        + endTimeCode.getValue();

                // converting local time to military
                String militaryStartDateTime = helper.Time.localToMilitaryString(startDateTimeStr);
                String militaryEndDateTime = helper.Time.localToMilitaryString(endDateTimeStr);

                // adds new appointment if none selected
                if (appointmentSelected == null) {

                    // creating new row and creates new appointment object with values
                    boolean appointmentCreated = AppointmentQuery.createNewAppointment(
                            title,
                            description,
                            location,
                            type,
                            militaryStartDateTime,
                            militaryEndDateTime,
                            contactID,
                            customerID,
                            userID);

                    if (appointmentCreated) {
                        System.out.println("Successfully added new appointment");

                        // switching back to home
                        try {
                            switchToHomeScene();
                        } catch (Exception fxmlException) {
                            System.out.println("FXML error: " + fxmlException.getMessage());
                        }
                    } else {
                        System.out.println("Adding row appointment.");
                    }

                    //modifying selected appointment values
                }else{
                        System.out.println(customerID);
                        System.out.println(contactID);
                        System.out.println(userID);
                    boolean appointmentModified = AppointmentQuery.modifyAppointment(
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
                    if (appointmentModified) {
                        System.out.println("Successfully modified appointment");

                        // switching back to home
                        try {
                            switchToHomeScene();
                        } catch (Exception fxmlException) {
                            System.out.println("FXML error: " + fxmlException.getMessage());
                        }
                    } else {
                        System.out.println("Modifying appointment failed.");
                    }
                }
            }catch(Exception e){
                System.out.println("Saving appointment error: " + e.getMessage());
            }
        }
    }

    /**
     * Used to check information entered in form.
     * Checks for empty text fields.
     * Validates time selections, checks if in business hours, or if date is scheduled on weekend.
     * Checks for overlapping appointments for the same customer.
     *
     * @return true if appointment is valid, false otherwise.
     */
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

        LocalDateTime militaryStartDateTime = helper.Time.dateTimeLocalTo24(startDateTimeStr);
        LocalDateTime militaryEndDateTime = helper.Time.dateTimeLocalTo24(endDateTimeStr);

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

        // checking for overlapping appointments if modifying an existing appointment
        if(appointmentSelected != null){
            if(Validation.conflictingAppointment(militaryStartDateTime,
                    militaryEndDateTime,
                    Integer.parseInt(customerId.getValue()),
                    appointmentSelected.getAppointmentId())){
                return false;}
        }
        // checking for overlapping appointments when creating a new appointment
        else{
            if(Validation.conflictingAppointment(militaryStartDateTime,
                    militaryEndDateTime,
                    Integer.parseInt(customerId.getValue()))){
                return false;}
        }

        return true;
    }

    /**
     * Used when cancel button is pressed on form.
     * Shows an alert to user explaining that information entered will not be saved.
     * Changes scene back to home screen if user confirms they want to cancel changes.
     */
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

    /**
     * Used to transition current scene back to home scene.
     * Will load "Home.fxml" using FXMLLoader as the scene for the primary stage.
     *
     * @throws IOException Occurs if there is a problem locating or loading the fxml file.
     */
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




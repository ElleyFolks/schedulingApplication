package controller;

import database.JDBC;
import database.ManageQuery;
import main.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private TextField customerId;

    @FXML
    private TextField userId;

    @FXML
    private TextField contactId;

    private List<String> appointmentsAndContactColNames = new ArrayList<String>();
    private List<String> appointmentsColNames = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setJoinAppointmentContactColNames(appointmentsAndContactColNames);
        setAppointmentColNames(appointmentsColNames);

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
    }

    void setJoinAppointmentContactColNames(List<String> list){
        list.add("Title");
        list.add("Description");
        list.add("Location");
        list.add("Type");
        list.add("Start");
        list.add("End");
        list.add("Customer_ID");
        list.add("User_ID");
        list.add("Contact_ID");

        // a part of custom inner join view, NOT originally a part of appointments.
        list.add("Contact_Name");
    }

    void setAppointmentColNames(List<String> list){
        list.add("Title");
        list.add("Description");
        list.add("Location");
        list.add("Type");
        list.add("Start");
        list.add("End");
        list.add("Customer_ID");
        list.add("User_ID");
        list.add("Contact_ID");
    }

    @FXML
    void onSaveAction() throws SQLException {
        // TODO add try{}catch{} and validation logic
        String title = appointmentTitle.getText();
        String description = appointmentDescription.getText();
        String location = appointmentLocation.getText();
        String type = appointmentType.getText();
        Integer customerID = Integer.parseInt(customerId.getText());
        Integer userID = Integer.parseInt(userId.getText());
        Integer contactID = Integer.parseInt(contactId.getText());

        // string of date time in format of YYYY-MM-DD hh:hha
        String startDateTimeStr = startDate.getValue()+" " + startHour.getValue() +":"+ startMinute.getValue() + startTimeCode.getValue();
        String endDateTimeStr = endDate.getValue()+" " + endHour.getValue() +":"+ endMinute.getValue() + endTimeCode.getValue();

        // converting local time to UTC
        String utcStartDateTime = convertLocalToUTC(startDateTimeStr);
        String utcEndDateTime = convertLocalToUTC(endDateTimeStr);

        // double-checking format
        System.out.println("start date UTC time: " + utcStartDateTime);
        System.out.println("end date UTC time: "+ utcEndDateTime);

        //Appointment newAppointment = new Appointment(title,description,location,type,utcStartDateTime,utcEndDateTime,customerID,userID,contactID);
        
        // adding values to array list
        List<String> columnValuesList = new ArrayList<String>();
        columnValuesList.add(title);
        columnValuesList.add(description);
        columnValuesList.add(location);
        columnValuesList.add(type);

        columnValuesList.add(utcStartDateTime);
        columnValuesList.add(utcEndDateTime);

        columnValuesList.add(customerID.toString());
        columnValuesList.add(userID.toString());
        columnValuesList.add(contactID.toString());

        // creating new row and creates new appointment object with values
        int rowsChanged = ManageQuery.createRowQuery("appointments", columnValuesList, appointmentsColNames);
        System.out.println("Number of rows changed: "+rowsChanged);
    }

    private static String convertLocalToUTC(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Convert to UTC
        LocalDateTime utcDateTime = localDateTime
                .atZone(Main.userTimeZone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //HH denotes 24-hr time

        return utcDateTime.format(outputFormatter);
    }
}




package controller;

import database.ManageQuery;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
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
    private DatePicker startDate;

    @FXML
    private ComboBox<String> startHour;

    @FXML
    private ComboBox<String> startMinute;

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

    private Map<String, Integer> hourMap = new HashMap<>();

    private List<String> appointmentsColNames = new ArrayList<String>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setAppointmentColNames(appointmentsColNames);

        // Add combo box options for hours
        for (int i = 0; i <= 23; i++) {
            // displays options in AM or PM times
            String displayValue = formatTimeValue(i);

            startHour.getItems().add(displayValue);
            endHour.getItems().add(displayValue);

            // Creates pair mapping between 24hr value and display value
            hourMap.put(displayValue, i);
        }
        // Combo box options for every 5 minutes
        for (int i = 0; i <= 59; i = (i+5)) {
            startMinute.getItems().add(String.format("%02d", i));
            endMinute.getItems().add(String.format("%02d", i));
        }
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

        Integer convertedStartHour = hourMap.get(startHour.getValue());
        Integer convertedEndHour = hourMap.get(endHour.getValue());

        // string of date time in format of YYYY-MM-DD HR:MIN:SEC
        String startDateTimeStr = startDate.getValue()+"T" + convertedStartHour +":"+ startMinute.getSelectionModel().getSelectedItem()+":00";
        String endDateTimeStr = endDate.getValue()+"T" + convertedEndHour +":"+ endMinute.getSelectionModel().getSelectedItem()+":00";

        // double-checking format
        System.out.println("start date time: " + startDateTimeStr);
        System.out.println("end date time: "+ endDateTimeStr);

        // date time values in proper data type
        //LocalDateTime localStartTime = LocalDateTime.parse(startDateTimeStr);
        //LocalDateTime localEndTime = LocalDateTime.parse(endDateTimeStr);

        List<String> columnValuesList = new ArrayList<String>();
        columnValuesList.add(title);
        columnValuesList.add(description);
        columnValuesList.add(location);
        columnValuesList.add(type);
        columnValuesList.add(startDateTimeStr);
        columnValuesList.add(endDateTimeStr);
        columnValuesList.add(customerID.toString());
        columnValuesList.add(userID.toString());
        columnValuesList.add(contactID.toString());


        // creating new row
        ResultSet resultSet = ManageQuery.createRowQuery("appointments", columnValuesList, appointmentsColNames);

    }

    private String formatTimeValue(int value){
        if (value == 0) {
            // midnight
            return "12 am";
        } else if(value < 12){
            // 1-11 in the morning
            return value + " am";
        }
        else if (value == 12) {
            // noon
            return "12 pm";
        } else {
            // 1-11 in the evening
            return (value - 12) + " pm";
        }
    }
}




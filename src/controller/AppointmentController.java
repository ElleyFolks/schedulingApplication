package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML
    private Button appointmentCancel;

    @FXML
    private TextField appointmentDescription;

    @FXML
    private TextField appointmentLocation;

    @FXML
    private Button appointmentSave;

    @FXML
    private TextField appointmentTitle;

    @FXML
    private TextField appointmentType;

    @FXML
    private TextField contactId;

    @FXML
    private TextField customerId;

    @FXML
    private DatePicker endDate;

    @FXML
    private ComboBox<?> endHour;

    @FXML
    private ComboBox<?> endMinute;

    @FXML
    private DatePicker startDate;

    @FXML
    private ComboBox<?> startHour;

    @FXML
    private ComboBox<?> startMinute;

    @FXML
    private TextField userId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}

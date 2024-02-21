package controller;

import helper.Alerts;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class HomeController implements Initializable {
    @FXML
    TableView<ObservableList<String>> customersTable = new TableView<>();

    @FXML
    TableView<Appointment> appointmentsTable = new TableView<>();

    @FXML
    private RadioButton allRadioButton;

    @FXML
    private RadioButton monthRadioButton;

    @FXML
    private RadioButton weekRadioButton;

    @FXML
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            // Set padding to create a border around the TableView
            //customersTable.setPadding(new javafx.geometry.Insets(20));

            //PreparedStatement statement = ManageQuery.createSelectQuery("customers");
            //ResultSet results = ManageQuery.getQueryResults(statement);
            //LoadTable.formatAppointmentTable(customersTable, results);

            // all appointments displayed by default
            allRadioButton.setSelected(true);

            appointmentsTable.setPadding(new javafx.geometry.Insets(20));
            AppointmentQuery.formatAppointmentTable(appointmentsTable);
            ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments(appointments, appointmentsTable);

            populateAppointmentTable();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void populateAppointmentTable(){
        try{
            appointments.clear();

            if(allRadioButton.isSelected()){

                ObservableList<Appointment> allAppointments = AppointmentQuery.getAllAppointments(appointments, appointmentsTable);
                System.out.println("Displaying all appointments");
            }
            if(monthRadioButton.isSelected()){

                ObservableList<Appointment> monthAppointments = AppointmentQuery.getRangeAppointments(appointments, appointmentsTable, "month");
                System.out.println("Displaying appointments for month");
            }
            if(weekRadioButton.isSelected()){

                ObservableList<Appointment> weekAppointments = AppointmentQuery.getRangeAppointments(appointments, appointmentsTable, "week");
                System.out.println("Displaying appointments for week");
            }

        }catch(Exception e){
            System.out.println("Error loading appointment table: "+ e.getMessage());
        }
    }

    @FXML
    void onRemoveAction(){
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        // cannot delete item if nothing is selected
        if(selectedAppointment == null){
            Alerts.showErrorAlert("noSelectedItem", "appointment");
        }
        else{
            Optional<ButtonType> result = Alerts.showConfirmAlert("deleteConfirm", "appointment");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean successfulDelete = AppointmentQuery.removeAppointment(selectedAppointment.getAppointmentId());

                if(successfulDelete){
                    System.out.println("Appointment successfully deleted");
                    String deleteMessage = selectedAppointment.getAppointmentId() + " " +
                            selectedAppointment.getAppointmentType();
                    Alerts.showInfoAlert("successfulDelete", "appointment",deleteMessage);

                    // updating table
                    appointments.clear();
                    populateAppointmentTable();
                }
            }
        }

    }

    @FXML
    void switchToAddAppointmentScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AppointmentMenu.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }
}

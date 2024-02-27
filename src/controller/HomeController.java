package controller;

import database.CustomerQuery;
import helper.Alerts;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class HomeController implements Initializable {

    @FXML
    TableView<Appointment> appointmentsTable = new TableView<>();

    @FXML
    TableView<Customer> customersTable = new TableView<>();

    @FXML
    private RadioButton allRadioButton;

    @FXML
    private RadioButton monthRadioButton;

    @FXML
    private RadioButton weekRadioButton;

    @FXML
    private Button addAppointmentBtn;

    @FXML
    private Button modifyAppointmentBtn;

    public static Appointment appointmentToModify;

    public static Customer customerToModify;

    @FXML
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customersTable.setPadding(new javafx.geometry.Insets(20));
            CustomerQuery.formatCustomerTable(customersTable);
            CustomerQuery.getAllCustomers(customers, customersTable);

            // all appointments displayed by default
            allRadioButton.setSelected(true);

            appointmentsTable.setPadding(new javafx.geometry.Insets(20));
            AppointmentQuery.formatAppointmentTable(appointmentsTable);
            AppointmentQuery.getAllAppointments(appointments, appointmentsTable);

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

                AppointmentQuery.getAllAppointments(appointments, appointmentsTable);
                System.out.println("Displaying all appointments");
            }
            if(monthRadioButton.isSelected()){

                AppointmentQuery.getRangeAppointments(appointments, appointmentsTable, "month");
                System.out.println("Displaying appointments for month");
            }
            if(weekRadioButton.isSelected()){

                AppointmentQuery.getRangeAppointments(appointments, appointmentsTable, "week");
                System.out.println("Displaying appointments for week");
            }

        }catch(Exception e){
            System.out.println("Error loading appointment table: "+ e.getMessage());
        }
    }

    @FXML
    public static Appointment getAppointmentToModify(){return appointmentToModify;}

    @FXML
    public static Customer getCustomerToModify(){return customerToModify;}

    @FXML
    void onAddAppointmentAction(){
        appointmentToModify = null;
        try{
            switchToAppointmentScene();

        } catch(Exception fxmlException){
            System.out.println("Error loading appointment screen: "+fxmlException.getMessage());
        }
    }

    @FXML
    void onAddCustomerAction(){
        customerToModify = null;
        try{
            switchToCustomerScene();

        } catch(Exception fxmlException){
            System.out.println("Error loading customer screen: "+fxmlException.getMessage());
        }
    }

    @FXML
    void onModifyAppointmentAction(){
        // gets appointment selected by user (if any)
        appointmentToModify = appointmentsTable.getSelectionModel().getSelectedItem();

        // notifies user no appointment was selected to modify
        if(appointmentToModify == null){
            Alerts.showErrorAlert("noAppointmentSelected", "appointment");
        }else{
            try{switchToAppointmentScene();
            }catch(Exception fxmlException){
                System.out.println("Could not load appointment screen"+fxmlException.getMessage());
            }
        }
    }

    @FXML
    void onModifyCustomerAction(){
        // gets appointment selected by user (if any)
        customerToModify = customersTable.getSelectionModel().getSelectedItem();

        // notifies user no appointment was selected to modify
        if(customerToModify == null){
            Alerts.showErrorAlert("noCustomerSelected", "customer");
        }else{
            try{switchToCustomerScene();
            }catch(Exception fxmlException){
                System.out.println("Could not load customer screen"+fxmlException.getMessage());
            }
        }
    }

    @FXML
    void onRemoveAppointmentAction(){
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
    void onRemoveCustomerAction(){
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        // cannot delete item if nothing is selected
        if(selectedCustomer == null){
            Alerts.showErrorAlert("noSelectedItem", "customer");
        }
        else{
            Optional<ButtonType> result = Alerts.showConfirmAlert("deleteConfirm", "customer");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean successfulDelete = CustomerQuery.removeCustomer(selectedCustomer.getCustomerId());

                if(successfulDelete){
                    System.out.println("Customer successfully deleted");
                    String deleteMessage = String.valueOf(selectedCustomer.getCustomerId()) + " "+ selectedCustomer.getCustomerFullName();
                    Alerts.showInfoAlert("successfulDelete", "customer",deleteMessage);

                    // updating table
                    appointments.clear();
                    customersTable.refresh();
                }
            }
        }
    }

    @FXML
    void switchToAppointmentScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AppointmentMenu.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setTitle("Appointment");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void switchToCustomerScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/CustomerMenu.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setTitle("Customer");
        stage.setScene(scene);
        stage.show();
    }
}

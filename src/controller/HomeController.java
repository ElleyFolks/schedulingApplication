package controller;

import database.CustomerQuery;
import database.HelperQuery;
import helper.Alerts;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
    private RadioButton appointmentContactReport;

    @FXML
    private RadioButton appointmentMonthReport;

    @FXML
    private RadioButton appointmentTypeReport;

    @FXML
    private RadioButton appointmentCountryReport;

    @FXML
    private Label reportResultLabel;

    public static Appointment appointmentToModify;

    public static Customer customerToModify;

    @FXML
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    private Label reportComboBoxLabel;

    @FXML
    private ComboBox<String> reportComboBox;

    @FXML
    private TableView<Appointment> reportTableView;

    private Map<String, Integer> contactNameIdMap = new HashMap<>();

    private Map<String, Integer> customerNameIdMap = new HashMap<>();

    private Map<String, Integer> countryNameIdMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // populates customers table
            customersTable.setPadding(new javafx.geometry.Insets(20));
            CustomerQuery.formatCustomerTable(customersTable);
            CustomerQuery.getAllCustomers(customers, customersTable);

            // all appointments displayed by default
            allRadioButton.setSelected(true);

            appointmentsTable.setPadding(new javafx.geometry.Insets(20));
            AppointmentQuery.formatAppointmentTable(appointmentsTable);
            AppointmentQuery.getAllAppointments(appointments, appointmentsTable);

            populateAppointmentTable();

            // appointments by type selected by default
            appointmentTypeReport.setSelected(true);
            formatReport();

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
    private void formatReport(){

        // clearing results because these can change
        contactNameIdMap.clear();
        customerNameIdMap.clear();
        reportComboBox.getItems().clear();
        reportResultLabel.setText("");

        // populates reports combo box with appointment types
        if(appointmentTypeReport.isSelected()){
            reportComboBoxLabel.setText("Select type for report.");
            HelperQuery.setAppointmentTypes(reportComboBox);
        }

        // populates reports combo box with months
        if(appointmentMonthReport.isSelected()){
            reportComboBoxLabel.setText("Select appointment month.");
            for (int i = 1; i <= 12; i++) {
                if(i<10){
                    reportComboBox.getItems().add("0"+String.valueOf(i));
                }else{
                    reportComboBox.getItems().add(String.valueOf(i));
                }
            }
        }

        // populates report combo box with contact names
        if(appointmentContactReport.isSelected()){
            reportComboBoxLabel.setText("Select contact.");
            HelperQuery.setAppointmentContacts(reportComboBox, contactNameIdMap);
        }

        if(appointmentCountryReport.isSelected()){
            reportComboBoxLabel.setText("Select country.");
            CustomerQuery.getCountryNameID(reportComboBox,countryNameIdMap);
        }
    }

    @FXML
    private void createReport(){
        if(appointmentTypeReport.isSelected() && reportComboBox.getValue() != null){
            // resets table view
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected type
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsOfType(reportTableView,reportComboBox.getValue());
            reportResultLabel.setText("Total number of appointments with type "+ reportComboBox.getValue()
                    + " is "+ reportTableView.getItems().size());
        }

        if(appointmentMonthReport.isSelected() && reportComboBox.getValue() != null){
            // resets table view
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected month
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsOfMonth(reportTableView, reportComboBox.getValue());
            reportResultLabel.setText("Total number of appointments in the month of "+ reportComboBox.getValue()
                    + " is "+ reportTableView.getItems().size());
        }

        if(appointmentContactReport.isSelected() && reportComboBox.getValue() != null){
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected month
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsWithContactID(reportTableView, contactNameIdMap.get(reportComboBox.getValue()));
            reportResultLabel.setText("Total number of appointments for contact "+ reportComboBox.getValue()
            + " is " + reportTableView.getItems().size());
        }

        if(appointmentCountryReport.isSelected() && reportComboBox.getValue() != null){
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected month
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsWithCountryID(reportTableView, countryNameIdMap.get(reportComboBox.getValue()));
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
    void onRemoveCustomerAction() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

            // cannot delete item if nothing is selected
            if (selectedCustomer == null) {
                Alerts.showErrorAlert("noSelectedItem", "customer");
            }
            else {
                // checks if customer has appointments
                if (CustomerQuery.customerHasAppointment(selectedCustomer.getCustomerId())) {

                    Alerts.showErrorAlert("customerHasAppointment", selectedCustomer.getCustomerFullName());
                } else {

                    Optional<ButtonType> result = Alerts.showConfirmAlert("deleteConfirm", "customer");

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        boolean successfulDelete = CustomerQuery.removeCustomer(selectedCustomer.getCustomerId());

                        if (successfulDelete) {
                            System.out.println("Customer successfully deleted");
                            String deleteMessage = String.valueOf(selectedCustomer.getCustomerId()) + " " + selectedCustomer.getCustomerFullName();
                            Alerts.showInfoAlert("successfulDelete", "customer", deleteMessage);

                            // clearing observable list and updating table
                            customers.clear();
                            CustomerQuery.getAllCustomers(customers, customersTable);
                        }
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

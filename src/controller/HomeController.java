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
import javafx.scene.layout.VBox;
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

/**
 * Class that contains event handlers, controller methods,
 * and logical implementation that will redirect the user to appropriate forms for adding appointments or customers,
 * and selecting an appointment or customer to modify or delete.
 *
 * @author Elley Folks
 */
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
    private RadioButton appointmentMonthTypeReport;

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

    @FXML
    private VBox comboBoxVBox;

    @FXML
    private ComboBox<String> selectTypeComboBox;

    private Map<String, Integer> contactNameIdMap = new HashMap<>();

    private Map<String, Integer> customerNameIdMap = new HashMap<>();

    private Map<String, Integer> countryNameIdMap = new HashMap<>();

    /**
     * Initializes the controller with the necessary setup for the main screen.
     * Populates the customers and appointments tables, and sets default radio button selections.
     * Also initializes the report combo box based on the selected report type.
     *
     * @throws RuntimeException if an exception occurs during initialization.
     */
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
            appointmentMonthTypeReport.setSelected(true);
            formatReport();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the appointments table based on the selected time range.
     * Clears existing appointments and loads new ones accordingly.
     */
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

    /**
     * Formats the report view based on the selected report type.
     * Clears existing data and populates the report combo box with appropriate options.
     */
    @FXML
    private void formatReport(){

        // clearing results because these can change
        contactNameIdMap.clear();
        customerNameIdMap.clear();
        reportComboBox.getItems().clear();
        reportResultLabel.setText("");

        // populates reports combo box with months
        if(appointmentMonthTypeReport.isSelected()){
            reportComboBoxLabel.setText("Select appointment month and type.");
            for (int i = 1; i <= 12; i++) {
                if(i<10){
                    reportComboBox.getItems().add("0"+String.valueOf(i));
                }else{
                    reportComboBox.getItems().add(String.valueOf(i));
                }
            }
            createTypeComboBox();
            // populates combo box with appointment types
            HelperQuery.setAppointmentTypes(selectTypeComboBox);
        }

        // populates report combo box with contact names
        if(appointmentContactReport.isSelected()){
            removeExtraComboBox(selectTypeComboBox);
            reportComboBoxLabel.setText("Select contact.");
            HelperQuery.setAppointmentContacts(reportComboBox, contactNameIdMap);
        }

        if(appointmentCountryReport.isSelected()){
            removeExtraComboBox(selectTypeComboBox);
            reportComboBoxLabel.setText("Select country.");
            CustomerQuery.getCountryNameID(reportComboBox,countryNameIdMap);
        }
    }

    private void createTypeComboBox() {
        // Create the ComboBox and add options if needed
        selectTypeComboBox = new ComboBox<>();
        selectTypeComboBox.setPromptText("select type");
        selectTypeComboBox.setOnAction(event -> createReport());
        selectTypeComboBox.getItems().addAll("Option 1", "Option 2", "Option 3");

        // Add the ComboBox to the container
        comboBoxVBox.getChildren().add(selectTypeComboBox);
    }

    private void removeExtraComboBox(ComboBox<?> comboBox) {
        // Remove the ComboBox from the container
        comboBoxVBox.getChildren().remove(comboBox);
    }

    /**
     * Creates and displays the report based on the selected report type and options.
     * Resets the table view, then formats and populates the view with the relevant data.
     * Updates the report result label with the total number of records in the report.
     */
    @FXML
    private void createReport(){
        /*
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
         */


        if(appointmentMonthTypeReport.isSelected() && reportComboBox.getValue() != null){
            // resets table view
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected month
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsOfMonth(reportTableView, reportComboBox.getValue());
            reportResultLabel.setText("Total number of appointments in the month of "+ reportComboBox.getValue()
                    + " is "+ reportTableView.getItems().size());

        }
        
        if(appointmentMonthTypeReport.isSelected() && reportComboBox.getValue() != null && selectTypeComboBox.getValue() != null){
            // resets table view
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected month
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsOfMonthType(reportTableView, reportComboBox.getValue(), selectTypeComboBox.getValue());
            reportResultLabel.setText("Total number of appointments in the month of "+ reportComboBox.getValue()
                    + " and type of "+ selectTypeComboBox.getValue()+ " is "+ reportTableView.getItems().size());
        }


        if(appointmentContactReport.isSelected() && reportComboBox.getValue() != null){
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected month
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsOfContactID(reportTableView, contactNameIdMap.get(reportComboBox.getValue()));
            reportResultLabel.setText("Total number of appointments for contact "+ reportComboBox.getValue()
            + " is " + reportTableView.getItems().size());
        }

        if(appointmentCountryReport.isSelected() && reportComboBox.getValue() != null){
            reportTableView.getItems().clear();
            reportTableView.getColumns().clear();

            // formats and populates view with appointments of selected month
            AppointmentQuery.formatAppointmentTable(reportTableView);
            AppointmentQuery.getAppointmentsWithCountryID(reportTableView, countryNameIdMap.get(reportComboBox.getValue()));
            reportResultLabel.setText("Total number of appointments for country "+ reportComboBox.getValue()
                    + " is " + reportTableView.getItems().size());
        }
    }

    /**
     * Retrieves the selected appointment for modification.
     *
     * @return The appointment to be modified.
     */
    @FXML
    public static Appointment getAppointmentToModify(){return appointmentToModify;}

    /**
     * Retrieves the selected customer for modification.
     *
     * @return The customer to be modified.
     */
    @FXML
    public static Customer getCustomerToModify(){return customerToModify;}

    /**
     * Handles the action when the "Add Appointment" button is pressed.
     * Sets the appointment to be modified to null (if selected) and switches to the appointment scene.
     */
    @FXML
    void onAddAppointmentAction(){
        appointmentToModify = null;
        try{
            switchToAppointmentScene();

        } catch(Exception fxmlException){
            System.out.println("Error loading appointment screen: "+fxmlException.getMessage());
        }
    }

    /**
     * Handles the action when the "Add Customer" button is pressed.
     * Sets the customer to be modified to null (if selected) and switches to the customer scene.
     */
    @FXML
    void onAddCustomerAction(){
        customerToModify = null;
        try{
            switchToCustomerScene();

        } catch(Exception fxmlException){
            System.out.println("Error loading customer screen: "+fxmlException.getMessage());
        }
    }

    /**
     * Handles the action when the "Modify Appointment" button is pressed.
     * Gets the selected appointment, sets it as the appointment to be modified, and switches to the appointment scene.
     */
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

    /**
     * Handles the action when the "Modify Customer" button is pressed.
     * Gets the selected customer, sets it as the customer to be modified, and switches to the customer scene.
     */
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

    /**
     * Handles the action when the "Remove Appointment" button is pressed.
     * Retrieves the selected appointment, prompts the user for confirmation,
     * and removes the appointment if the user confirms the deletion.
     * Displays appropriate alerts for errors and successful deletions.
     * Updates the appointments table after a successful deletion.
     */
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

    /**
     * Handles the action when the "Remove Customer" button is pressed.
     * Retrieves the selected customer, checks for associated appointments,
     * prompts the user for confirmation, and removes the customer if the user confirms the deletion.
     * Displays appropriate alerts for errors and successful deletions.
     * Clears the observable list and updates the customers table after a successful deletion.
     */
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


    /**
     * Switches the application view to the Appointment Scene.
     * Loads the AppointmentMenu.fxml file and sets it as the new scene for the primary stage.
     * Hides the current stage, sets the new title, and shows the updated stage.
     *
     * @throws IOException if an error occurs during loading.
     */
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

    /**
     * Switches the application view to the Customer Menu Scene.
     * Loads the CustomerMenu.fxml file and sets it as the new scene for the primary stage.
     * Hides the current stage, sets the new title, and shows the updated stage.
     *
     * @throws IOException if an error occurs during loading.
     */
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

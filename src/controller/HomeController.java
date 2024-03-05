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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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
    private VBox comboBoxVBox;

    @FXML
    private ListView<String> reportListView;

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

            appointments = AppointmentQuery.getAllAppointments();
            customers = CustomerQuery.getAllCustomers();

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

    private String formatStringToMonthYear(LocalDateTime dateTime) {
        return dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + dateTime.getYear();
    }

    public ObservableList<String> createMonthTypeReport(List<Appointment> appointmentList){
        // Uses streams to group appointments by month and type, counts them
        Map<String, Long> groupedByMonthAndType = appointmentList.stream()
                .collect(Collectors.groupingBy(
                        appointment -> formatStringToMonthYear(appointment.getStartDateTime()) + " , Type: " + appointment.getAppointmentType(),
                        Collectors.counting()
                ));

        // Tree map populated with entries grouped by month and type, sorts keys (month and type) in ascending order
        Map<String, Long> treeMap = new TreeMap<>(groupedByMonthAndType);

        ObservableList<String> generatedReport = FXCollections.observableArrayList();
        treeMap.forEach((key, value) -> generatedReport.add(key + " , Count: " + value));

        return generatedReport;
    }

    private String formatAppointments(Appointment appointment){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        String formattedString = "Appointment ID: " + appointment.getAppointmentId() + " " +
                "Title: " + appointment.getAppointmentTitle() + " " +
                "Type: "+ appointment.getAppointmentType() + " " +
                "Appointment Description: "+ appointment.getAppointmentDescription() + " " +
                "Times " + appointment.getStartDateTime().format(formatter) + " " +
                "through " + appointment.getEndDateTime().format(formatter) + " " +
                "Customer ID: "+ appointment.getCustomerId();
        return formattedString;
    }

    public ObservableList<String> createContactReport(List<Appointment> appointmentList){
        ObservableList<String> report = FXCollections.observableArrayList();

        // Uses stream to group appointments by contact ID first, then by contact name
        Map<Integer, Map<String, List<Appointment>>> sortedByContactId = appointmentList.stream()
                .collect(Collectors.groupingBy(Appointment::getContactId,
                        Collectors.groupingBy(Appointment::getContactFullName)));

        // next sort by contact name
        sortedByContactId.forEach((contactId, sortedByContactFullName) -> {
            sortedByContactFullName.forEach((contactName, resultList) -> {
                // contact name added to the report
                report.add("Contact Name: " + contactName + " Contact ID: " + contactId);

                // Sort appointments by start date
                resultList.sort(Comparator.comparing(Appointment::getStartDateTime));

                // formatting, then adding appointment information to report
                resultList.forEach(appointment -> {
                    report.add(formatAppointments(appointment));
                });

                // reporting total appointments contact
                report.add("Number of appointments for " + contactName + ": " + resultList.size());

                // delimiter between each report
                report.add("____End Report____");
            });
        });

        return report;
    }

    private String formatCustomers(Customer customer){
        String formattedString = "Customer Name:" + customer.getCustomerFullName()+ " " +
                "Customer ID: " + customer.getCustomerId() + " " +
                "Country: "+ customer.getCountry() + " " +
                "Division: "+ customer.getDivision() + " " +
                "Division ID: " + customer.getDivisionId() + " " +
                "Address: " + customer.getCustomerAddress() + " " +
                "Postal Code: "+ customer.getPostalCode() + " " +
                "Phone: "+ customer.getCustomerPhoneNumber() + " ";

        return formattedString;
    }

    public ObservableList<String> createCounrtyReport(List<Customer> customerList){
        ObservableList<String> report = FXCollections.observableArrayList();

        // Uses stream to group appointments by contact ID first, then by contact name
        Map<String, Map<String, List<Customer>>> sortedByCountry = customerList.stream()
                .collect(Collectors.groupingBy(Customer::getCountry,
                        Collectors.groupingBy(Customer::getDivision)));

        // next sort by customer name
        sortedByCountry.forEach((customerId, sortedByCustomerFullName) -> {
            sortedByCustomerFullName.forEach((customerName, resultList) -> {
                // contact name added to the report
                report.add("Customer Name: " + customerName + " Customer ID: " + customerId);

                // formatting, then adding appointment information to report
                resultList.forEach(customer -> {
                    report.add(formatCustomers(customer));
                });

                // reporting total appointments contact
                report.add("Number of customers in country " + resultList.size());

                // delimiter between each report
                report.add("____End Report____");
            });
        });

        return report;
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
            // populates combo box with appointment types
            HelperQuery.setAppointmentTypes(selectTypeComboBox);
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


    /**
     * Creates and displays the report based on the selected report type and options.
     * Resets the table view, then formats and populates the view with the relevant data.
     * Updates the report result label with the total number of records in the report.
     */


    @FXML
    private void createReport(){

        contactNameIdMap.clear();
        customerNameIdMap.clear();

        if(appointmentMonthTypeReport.isSelected()){
            // resets table view
            reportListView.getItems().clear();
            reportListView.setItems(createMonthTypeReport(appointments));
        }

        if(appointmentContactReport.isSelected()){
            reportListView.getItems().clear();
            reportListView.setItems(createContactReport(appointments));
        }

        if(appointmentCountryReport.isSelected()){
            reportListView.getItems().clear();
            reportListView.setItems(createCounrtyReport(customers));
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

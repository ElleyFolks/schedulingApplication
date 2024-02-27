package controller;

import database.CustomerQuery;
import helper.Alerts;
import helper.Validation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Customer;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController  implements Initializable {

    @FXML
    private TextField customerId;

    @FXML
    private TextField customerAddress;

    @FXML
    private ComboBox<String> country;

    @FXML
    private ComboBox<String> division;

    @FXML
    private TextField customerName;

    @FXML
    private TextField customerPhone;

    @FXML
    private TextField customerPostalCode;

    private Map<String, Integer> countryNameIdMap = new HashMap<>();

    private Map<String, Integer> divisionNameIdMap = new HashMap<>();

    Customer customerSelected = HomeController.getCustomerToModify();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // initializing country combo box
        CustomerQuery.getCountryNameID(country, countryNameIdMap);

        if(customerSelected != null){

            customerId.setText(String.valueOf(customerSelected.getCustomerId()));
            customerName.setText(customerSelected.getCustomerFullName());
            customerAddress.setText(customerSelected.getCustomerAddress());
            customerPostalCode.setText(customerSelected.getPostalCode());
            customerPhone.setText(customerSelected.getCustomerPhoneNumber());
            country.setValue(customerSelected.getCountry());

            // initialing options for division combo box
            onCountrySelectAction();
            division.setValue(customerSelected.getDivision());
        }
    }

    public void onCountrySelectAction(){

        String countryName = country.getSelectionModel().getSelectedItem();
        Integer countryId = countryNameIdMap.get(countryName);
        CustomerQuery.getDivision(division, divisionNameIdMap, countryId);
    }

    @FXML
    void onSaveAction(){
        if(isValidCustomer()){
            try{
                String name = customerName.getText();
                String address = customerAddress.getText();
                String postalCode = customerPostalCode.getText();
                String phoneNumber = customerPhone.getText();
                String country = this.country.getValue();
                String division = this.division.getValue();

                // adds new customer if none selected
                if(customerSelected == null){
                    boolean customerCreated = CustomerQuery.createNewCustomer(
                            name,
                            address,
                            postalCode,
                            phoneNumber,
                            divisionNameIdMap.get(division)
                    );

                    if(customerCreated){
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
                }

                // modifies an existing customer
                else{
                    boolean customerModified = CustomerQuery.modifyCustomer(customerSelected.getCustomerId(),
                            name,
                            address,
                            postalCode,
                            phoneNumber,
                            String.valueOf(divisionNameIdMap.get(division))
                            );
                    if (customerModified) {
                        System.out.println("Successfully modified customer");

                        // switching back to home
                        try {
                            switchToHomeScene();
                        } catch (Exception fxmlException) {
                            System.out.println("FXML error: " + fxmlException.getMessage());
                        }
                    } else {
                        System.out.println("Modifying customer failed.");
                    }
                }

            }catch(Exception exception){
                System.out.println("Saving customer error: " + exception.getMessage());
            }
        }
    }

    @FXML
    boolean isValidCustomer(){
        if(Validation.isEmptyString(customerName,"Customer name")){
            return false;
        }
        if(Validation.isEmptyString(customerAddress, "Customer address")){
            return false;
        }
        if(Validation.isEmptyString(customerPostalCode, "Customer postal code")){
            return false;
        }
        if(Validation.isEmptyString(customerPhone, "Customer phone number")){
            return false;
        }
        if(Validation.isEmptyComboBox(country, "Customer country")){
            return false;
        }
        if(Validation.isEmptyComboBox(division, "Customer division")){
            return false;
        }
        else{
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

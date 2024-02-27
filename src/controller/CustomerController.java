package controller;

import database.CustomerQuery;
import helper.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController  implements Initializable {

    @FXML
    private TextField addressTxtField;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private Button customerCancel;

    @FXML
    private TextField customerIdTxtField;

    @FXML
    private Button customerSave;

    @FXML
    private ComboBox<String> divisionComboBox;

    @FXML
    private TextField nameTxtField;

    @FXML
    private TextField phoneTxtField;

    @FXML
    private TextField postalCodeTxtField;

    private Map<String, Integer> countryNameIdMap = new HashMap<>();

    private Map<String, Integer> divisionNameIdMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerQuery.getCountryNameID(countryComboBox, countryNameIdMap);
    }

    public void onCountrySelectAction(){

        String countryName = countryComboBox.getSelectionModel().getSelectedItem();
        Integer countryId = countryNameIdMap.get(countryName);

        CustomerQuery.getDivision(divisionComboBox, divisionNameIdMap, countryId);
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

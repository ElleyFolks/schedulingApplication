package helper;

import javafx.scene.control.Alert;

public class Alerts {

    public static void showErrorAlert(String alertCode, String textFieldName){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch(alertCode) {
            case "emptyTextField":
                alert.setTitle("Input error.");
                alert.setHeaderText("Could not add appointment.");
                alert.setContentText("Please enter valid information into "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "notValidInt":
                alert.setTitle("Input error.");
                alert.setHeaderText("Could not add appointment.");
                alert.setContentText("Please enter a valid integer into "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "comboBoxNotSelected":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Could not add appointment.");
                alert.setContentText("Please select an option for "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "datePickerNotSelected":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Could not add appointment.");
                alert.setContentText("Please select a date for "+ textFieldName +".");
                alert.showAndWait();
                break;
        }
    }
}

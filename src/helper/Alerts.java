package helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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

            case "invalidTimeCombination":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Could not add appointment.");
                alert.setContentText("Start time must be before the end time. " +
                        "Please change "+ textFieldName +".");
                alert.showAndWait();
                break;

            case"invalidDateCombination":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Could not add appointment.");
                alert.setContentText("Start date must be before the end date. " +
                        "Please change "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "noSelectedItem":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Could not delete appointment.");
                alert.setContentText("No "+textFieldName+" selected. Please select a(n) "+textFieldName);
                alert.showAndWait();
                break;
        }
    }

    public static Optional<ButtonType> showConfirmAlert(String alertCode, String textFieldName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        switch(alertCode){
            case "cancelConfirm":
                alert.setTitle("Cancel Confirmation");
                alert.setHeaderText("Are you sure you want to leave?");
                alert.setContentText("Any information entered will not be saved, click yes to proceed.");
                break;

            case "deleteConfirm":
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this "+ textFieldName +" ?");
                alert.setContentText("This entry will be permanently removed, click yes to proceed.");
                break;
        }
        return alert.showAndWait();
    }

    public static void showInfoAlert(String alertCode,String textFieldName, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        switch(alertCode){
            case "successfulDelete":
                alert.setTitle("Information");
                alert.setHeaderText("Successfully deleted "+ textFieldName);
                alert.setContentText("Appointment ID and type: "+ message);
                alert.showAndWait();
        }
    }
}

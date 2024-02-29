package helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Class that contains alert functions to warn and inform user about actions such as saving or canceling.
 * Also gives useful error messages when invalid information is entered, or a selection is not made.
 *
 * @author Elley Folks
 */
public class Alerts {

    /**
     * Displays an error alert based on the given alert code and text field name.
     *
     * @param alertCode      The code specifying the type of error alert to be shown.
     * @param textFieldName  The name of the text field associated with the error.
     */
    public static void showErrorAlert(String alertCode, String textFieldName){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch(alertCode) {
            case "emptyTextField":
                alert.setTitle("Input error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Please enter valid information into "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "notValidInt":
                alert.setTitle("Input error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Please enter a valid integer into "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "comboBoxNotSelected":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Please select an option for "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "datePickerNotSelected":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Please select a date for "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "invalidTimeCombination":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Start time must be before the end time. " +
                        "Please change "+ textFieldName +".");
                alert.showAndWait();
                break;

            case"invalidDateCombination":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Start date must be before the end date. " +
                        "Please change "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "noSelectedItem":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Could not delete appointment.");
                alert.setContentText("No "+textFieldName+" selected. Please select a(n) "+textFieldName+" to continue.");
                alert.showAndWait();
                break;

            case "noAppointmentSelected":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("No "+textFieldName+" selected. Please select a(n) "+textFieldName+" to continue.");
                alert.showAndWait();
                break;

            case "noCustomerSelected":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("No "+textFieldName+" selected. Please select a(n) "+textFieldName+" to continue.");
                alert.showAndWait();
                break;

            case "invalidDateScheduled":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Invalid "+textFieldName+" scheduled. Please select a(n) "+textFieldName+" during the week.");
                alert.showAndWait();
                break;

            case "notInBusinessHours":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Incorrect appointment details.");
                alert.setContentText("Invalid "+textFieldName+" scheduled. Please select a(n) "+textFieldName+" within business hours.");
                alert.showAndWait();
                break;

            case "overlappingAppointment":
                alert.setTitle("Scheduling error.");
                alert.setHeaderText("Overlapping appointments detected.");
                alert.setContentText("Overlapping "+textFieldName+" scheduled. Please select a(n) "+textFieldName+" at a different time.");
                alert.showAndWait();
                break;

            case "customerHasAppointment":
                alert.setTitle("Selection error.");
                alert.setHeaderText("Cannot delete customer.");
                alert.setContentText("The customer "+textFieldName+" selected still has scheduled appointments. Please delete associated appointments first.");
                alert.showAndWait();
                break;
        }
    }

    /**
     * Shows a confirmation alert based on the provided alert code and text field name.
     *
     * @param alertCode The code specifying the type of confirmation alert.
     * @param textFieldName The name of the text field associated with the confirmation.
     *
     * @return An {@link Optional} containing the user's response (Yes/No) to the confirmation.
     */
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

    /**
     * Shows an information alert based on the provided alert code, text field name, and additional message.
     *
     * @param alertCode The code specifying the type of information alert.
     * @param textFieldName The name of the text field associated with the information.
     *
     * @param message Additional message to be displayed in the alert.
     */
    public static void showInfoAlert(String alertCode,String textFieldName, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        switch(alertCode){
            case "successfulDelete":
                alert.setTitle("Information");
                alert.setHeaderText("Successfully deleted "+ textFieldName);
                alert.setContentText("Appointment ID and type: "+ message);
                alert.showAndWait();
                break;
        }
    }
}

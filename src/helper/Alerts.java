package helper;

import javafx.scene.control.Alert;

public class Alerts {

    public static void showErrorAlert(String alertCode, String textFieldName){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch(alertCode) {
            case "emptyTextField":
                alert.setTitle("Input Error");
                alert.setHeaderText("Cannot add appointment because a box is empty.");
                alert.setContentText("Please enter valid information into "+ textFieldName +".");
                alert.showAndWait();
                break;

            case "notValidInt":
                alert.setTitle("Input Error");
                alert.setHeaderText("Cannot add appointment.");
                alert.setContentText("Please enter valid information into "+ textFieldName +".");
                alert.showAndWait();
                break;
        }
    }
}

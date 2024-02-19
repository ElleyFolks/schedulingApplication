package helper;

import javafx.scene.control.TextField;

public class Validation {

    public static boolean isEmptyString(TextField textField, String textFieldName){
        if(textField.getText().isEmpty()){
            Alerts.showErrorAlert("emptyTextField", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isNotValidInt(TextField textField, String textFieldName){
        try{
            Integer.parseInt(textField.getText());
            return true;
        }catch(Exception e){
            Alerts.showErrorAlert("notValidInt", textFieldName);
            return false;
        }
    }
}

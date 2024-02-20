package helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

    public static boolean isEmptyComboBox(ComboBox<String> stringComboBox, String textFieldName){
        if(stringComboBox.getSelectionModel().isEmpty()){
            Alerts.showErrorAlert("comboBoxNotSelected", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isEmptyDatePicker(DatePicker datePicker, String textFieldName){
        if(datePicker.getValue() == null){
            Alerts.showErrorAlert("datePickerNotSelected", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

}

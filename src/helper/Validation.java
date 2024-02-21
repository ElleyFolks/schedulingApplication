package helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

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

    public static boolean isInvalidTimeCombination(LocalDateTime start, LocalDateTime end, String textFieldName){
        if(end.isBefore(start) || start.equals(end)){
            Alerts.showErrorAlert("invalidTimeCombination", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isInvalidDateCombination(DatePicker start, DatePicker end, String textFieldName){
        if(end.getValue().isBefore(start.getValue())){
            Alerts.showErrorAlert("invalidDateCombination", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

}
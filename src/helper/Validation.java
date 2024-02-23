package helper;

import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;

import java.time.DayOfWeek;
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

    public static boolean dateIsOnWeekend(LocalDateTime start, LocalDateTime end){
        DayOfWeek startDayOfWeek = start.getDayOfWeek();
        DayOfWeek endDayOfWeek = end.getDayOfWeek();

        if(startDayOfWeek == DayOfWeek.SATURDAY || startDayOfWeek == DayOfWeek.SUNDAY){
            Alerts.showErrorAlert("invalidDateScheduled", "date");
            return true;
        } else if (endDayOfWeek == DayOfWeek.SATURDAY || endDayOfWeek == DayOfWeek.SUNDAY) {
            Alerts.showErrorAlert("invalidDateScheduled", "date");
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean dateIsOutsideBusinessHours(LocalDateTime startTime, LocalDateTime endTime){
        LocalDateTime businessStartTime = Time.getBusinessOpenInLocal(startTime);
        LocalDateTime businessCloseTime = Time.getBusinessCloseInLocal(endTime);

        if(startTime.isBefore(businessStartTime) || endTime.isAfter(businessCloseTime)){
            Alerts.showErrorAlert("notInBusinessHours", "time");
            return true;
        } else if (startTime.isAfter(businessCloseTime) || endTime.isBefore(businessStartTime)) {
            Alerts.showErrorAlert("notInBusinessHours", "time");
            return true;
        } else if (dateIsOnWeekend(startTime, endTime)) {
            return true;
        } else{
            return false;
        }
    }

    // For adding NEW appointment
    public static boolean overlappingAppointment(LocalDateTime newStartDate, LocalDateTime newEndDate, int cusID){
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsWithCustomerID(cusID);
        if(appointments != null){
            for(Appointment existingAppointment: appointments){
                System.out.println("\n"+newStartDate);
                System.out.println(newEndDate);
                System.out.println(existingAppointment.getStartDateTime());
                System.out.println(existingAppointment.getEndDateTime());
                if(existingAppointment.getStartDateTime().isAfter(newStartDate) && existingAppointment.getStartDateTime().isBefore(newEndDate)){
                    System.out.println("Overlapping appointment found!");
                    Alerts.showErrorAlert("overlappingAppointment", "time or date");
                    return true;
                } else if (newStartDate.isAfter(existingAppointment.getStartDateTime()) && newStartDate.isBefore(existingAppointment.getEndDateTime())){
                    System.out.println("Overlapping appointment found!");
                    Alerts.showErrorAlert("overlappingAppointment", "time or date");
                    return true;
                } else if (newStartDate.equals(existingAppointment.getStartDateTime()) || newEndDate.equals(existingAppointment.getEndDateTime())) {
                    System.out.println("Overlapping appointment found!");
                    Alerts.showErrorAlert("overlappingAppointment", "time or date");
                    return true;
                }
            }
        }
        else{
            System.out.println("Failed to find user appointments.");
            return false;
        }
        return false;
    }

    // For modifying EXISTING appointment
    public static boolean overlappingAppointment(LocalDateTime newStartDate, LocalDateTime newEndDate, int cusID, int appointmentID){
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsWithCustomerID(cusID);
        if(appointments != null){
            for(Appointment existingAppointment: appointments){
                System.out.println("\n"+newStartDate);
                System.out.println(newEndDate);
                System.out.println(existingAppointment.getStartDateTime());
                System.out.println(existingAppointment.getEndDateTime());
                if(appointmentID != existingAppointment.getAppointmentId()){
                    if(existingAppointment.getStartDateTime().isAfter(newStartDate) && existingAppointment.getStartDateTime().isBefore(newEndDate)){
                        System.out.println("Overlapping appointment found!");
                        Alerts.showErrorAlert("overlappingAppointment", "time or date");
                        return true;
                    } else if (newStartDate.isAfter(existingAppointment.getStartDateTime()) && newStartDate.isBefore(existingAppointment.getEndDateTime())){
                        System.out.println("Overlapping appointment found!");
                        Alerts.showErrorAlert("overlappingAppointment", "time or date");
                        return true;
                    } else if (newStartDate.equals(existingAppointment.getStartDateTime()) || newEndDate.equals(existingAppointment.getAppointmentId())) {
                        System.out.println("Overlapping appointment found!");
                        Alerts.showErrorAlert("overlappingAppointment", "time or date");
                        return true;
                    }
                }
            }
        }
        else{
            System.out.println("Failed to find user appointments.");
            return false;
        }
        return false;
    }

}

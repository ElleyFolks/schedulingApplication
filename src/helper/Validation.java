package helper;

import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Class that contains validation functions for empty input, empty text fields, combo box widgets,
 * and date time pickers.
 * Time validation for valid start-end times and scheduling conflicts.
 *
 * @author Elley Folks
 */
public class Validation {

    /**
     * Checks if a TextField is empty and shows an error alert if it is.
     *
     * @param textField      The TextField to be checked.
     * @param textFieldName  The name of the TextField for the error message.
     *
     * @return               True if the TextField is empty, false otherwise.
     */
    public static boolean isEmptyString(TextField textField, String textFieldName){
        if(textField.getText().isEmpty()){
            Alerts.showErrorAlert("emptyTextField", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if a ComboBox is not selected and shows an error alert if it is.
     *
     * @param stringComboBox The ComboBox to be checked.
     * @param textFieldName  The name of the ComboBox for the error message.
     *
     * @return               True if the ComboBox is not selected, false otherwise.
     */
    public static boolean isEmptyComboBox(ComboBox<String> stringComboBox, String textFieldName){
        if(stringComboBox.getSelectionModel().isEmpty()){
            Alerts.showErrorAlert("comboBoxNotSelected", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if a DatePicker is not selected and shows an error alert if it is.
     *
     * @param datePicker     The DatePicker to be checked.
     * @param textFieldName  The name of the DatePicker for the error message.
     *
     * @return               True if the DatePicker is not selected, false otherwise.
     */
    public static boolean isEmptyDatePicker(DatePicker datePicker, String textFieldName){
        if(datePicker.getValue() == null){
            Alerts.showErrorAlert("datePickerNotSelected", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if the end time is before or equal to the start time and shows an error alert if it is.
     *
     * @param start          The start time.
     * @param end            The end time.
     * @param textFieldName  The name of the field for the error message.
     *
     * @return               True if the time combination is invalid, false otherwise.
     */
    public static boolean isInvalidTimeCombination(LocalDateTime start, LocalDateTime end, String textFieldName){
        if(end.isBefore(start) || start.equals(end)){
            Alerts.showErrorAlert("invalidTimeCombination", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if the end date is before the start date and shows an error alert if it is.
     *
     * @param start          The start date.
     * @param end            The end date.
     * @param textFieldName  The name of the field for the error message.
     *
     * @return               True if the date combination is invalid, false otherwise.
     */
    public static boolean isInvalidDateCombination(DatePicker start, DatePicker end, String textFieldName){
        if(end.getValue().isBefore(start.getValue())){
            Alerts.showErrorAlert("invalidDateCombination", textFieldName);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if the date falls on a weekend and shows an error alert if it does.
     *
     * @param start  The start date.
     * @param end    The end date.
     *
     * @return       True if the date falls on a weekend, false otherwise.
     */
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

    /**
     * Checks if the appointment time is outside business hours or on a weekend and shows an error alert if it is.
     *
     * @param startTime  The start time of the appointment.
     * @param endTime    The end time of the appointment.
     *
     * @return           True if the appointment time is outside business hours or on a weekend, false otherwise.
     */
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

    /**
     * Checks for overlapping appointments when adding a new appointment.
     *
     * @param newStartDate The start date and time of the new appointment.
     * @param newEndDate The end date and time of the new appointment.
     * @param cusID The customer ID associated with the appointment.
     *
     * @return True if there is an overlapping appointment, false otherwise.
     */
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

    /**
     * Checks for overlapping appointments when modifying an existing appointment.
     *
     * @param newStartDate The start date and time of the modified appointment.
     * @param newEndDate The end date and time of the modified appointment.
     * @param cusID The customer ID associated with the appointment.
     * @param appointmentID The ID of the appointment being modified.
     * @return True if there is an overlapping appointment, false otherwise.
     */
    public static boolean overlappingAppointment(LocalDateTime newStartDate, LocalDateTime newEndDate, int cusID,
                                                 int appointmentID){
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

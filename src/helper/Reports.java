package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class Reports {

    /**
     * Accepts a "YYYY MM DD HH:mm:ss" date time object, and formats it to display "MONTH YYYY"
     * @param dateTime The LocalDateTime start time of the appointment.
     * @return
     */
    private static String formatMonthYearToString(LocalDateTime dateTime) {
        return dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + dateTime.getYear();
    }

    /**
     * Creates a report based on the provided map grouping appointments by month and type.
     * The report is sorted by keys (month and type) in ascending order.
     *
     * @param groupedByMonthAndType Map grouping appointments by month and type.
     * @return ObservableList containing the formatted report.
     */
    private static ObservableList<String> createMonthTypeReport(Map<String, Long> groupedByMonthAndType){
        // Tree map populated with entries grouped by month and type, sorts keys (month and type) in ascending order
        Map<String, Long> treeMap = new TreeMap<>(groupedByMonthAndType);

        ObservableList<String> generatedReport = FXCollections.observableArrayList();
        treeMap.forEach((key, value) -> generatedReport.add(key + " , Count: " + value));

        return generatedReport;
    }

    /**
     * Groups appointments by month and type, then counts them.
     *
     * @param appointmentList List of appointments to be grouped.
     * @return Map containing appointments grouped by month and type, along with their counts.
     */
    private static Map<String, Long> sortAppointmentByMonthType(List<Appointment> appointmentList){
        // Uses streams to group appointments by month and type, counts them
        Map<String, Long> sortedByMonthAndType = appointmentList.stream()
                .collect(Collectors.groupingBy(
                        appointment -> formatMonthYearToString(appointment.getStartDateTime()) + " , Type: " + appointment.getAppointmentType(),
                        Collectors.counting()
                ));
        return sortedByMonthAndType;
    }

    /**
     * Gets a report of appointments sorted by month and type, based on the provided list of customers.
     * @param appointmentList List of appointments to generate the report.
     * @return ObservableList containing the formatted report.
     */
    public static ObservableList<String> getMonthTypeReport(List<Appointment> appointmentList){

        Map<String, Long> sortedAppointments = sortAppointmentByMonthType(appointmentList);
        ObservableList<String> finalReport = createMonthTypeReport(sortedAppointments);

        return finalReport;
    }

    /**
     * Formats the information of an appointment instance into a string for a report. Labels the information.
     * @param appointment Appointment object to be formatted.
     * @return Formatted string with information labeled.
     */
    private static String formatAppointmentsString(Appointment appointment){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        String formattedString = "Appointment ID: " + appointment.getAppointmentId() + ", " +
                "Title: " + appointment.getAppointmentTitle() + ", " +
                "Type: "+ appointment.getAppointmentType() + ", " +
                "Description: "+ appointment.getAppointmentDescription() + ", " +
                "START DATE TIME (" + appointment.getStartDateTime().format(timeFormatter) +  "), " +
                "END DATE TIME (" + appointment.getEndDateTime().format(timeFormatter) +  "), " +
                "Customer ID: "+ appointment.getCustomerId();

        return formattedString;
    }

    /**
     * Creates a report based on the provided map grouping contacts by contact ID.
     * The report is sorted by keys (month and type) in ascending order.
     *
     * @param sortedByContactId Map grouping contacts by contact ID.
     * @return ObservableList containing the formatted report.
     */
    private static ObservableList<String> createContactReport(Map<Integer, Map<String, List<Appointment>>> sortedByContactId){
        ObservableList<String> contactSchedule = FXCollections.observableArrayList();
        
        sortedByContactId.forEach((contactId, sortedByContactFullName) -> {
            sortedByContactFullName.forEach((contactName, resultList) -> {
                // contact name added to the report
                contactSchedule.add("Contact Name: " + contactName + "\n Contact ID: " + contactId);

                // Sort appointments by start date
                resultList.sort(Comparator.comparing(Appointment::getStartDateTime));

                // formatting, then adding appointment information to report
                resultList.forEach(appointment -> {
                    contactSchedule.add(formatAppointmentsString(appointment));
                });

                // reporting total appointments contact
                contactSchedule.add("Number of appointments for " + contactName + ": " + resultList.size());

                // delimiter between each report
                contactSchedule.add("____End Report____");
            });
        });

        return contactSchedule;
    }

    /**
     * Groups appointments by contact ID and contact name using Java streams.
     *
     * @param appointmentList List of appointments to be grouped.
     * @return Map containing appointments grouped by contact ID and further grouped by contact full name.
     */
    private static Map<Integer, Map<String, List<Appointment>>> sortContactsById(List<Appointment> appointmentList){
        // Uses stream to group appointments by contact ID
        Map<Integer, Map<String, List<Appointment>>> sortedByContactId = appointmentList.stream()
                .collect(Collectors.groupingBy(Appointment::getContactId,
                        Collectors.groupingBy(Appointment::getContactFullName)));
        return sortedByContactId;
    }

    /**
     * Gets a contact-focused report based on the provided list of customers.
     * @param appointmentList List of appointments to generate the report.
     * @return ObservableList containing the formatted report.
     */
    public static ObservableList<String> getContactReport(List<Appointment> appointmentList){

        Map<Integer, Map<String, List<Appointment>>> sortedAppointments = sortContactsById(appointmentList);
        ObservableList<String> finalReport = createContactReport(sortedAppointments);

        return finalReport;
    }

    /**
     * Formats the information of a customer instance into a string for a report. Labels the information.
     * @param customer Customer object to be formatted.
     * @return Formatted string with information labeled.
     */
    private static String formatCustomersString(Customer customer){
        String formattedString = "Customer Name: " + customer.getCustomerFullName()+ ", " +
                "Customer ID: " + customer.getCustomerId() + ", " +
                "Country: "+ customer.getCountry() + ", " +
                "Division: "+ customer.getDivision() + ", " +
                "Division ID: " + customer.getDivisionId() + ", " +
                "Address: " + customer.getCustomerAddress() + ", " +
                "Postal Code: "+ customer.getPostalCode() + ", " +
                "Phone: "+ customer.getCustomerPhoneNumber();

        return formattedString;
    }

    /**
     * Creates a report based on the provided map grouping customers by country.
     * The report is sorted by keys (month and type) in ascending order.
     *
     * @param sortedByCountry Map grouping customers by country.
     * @return ObservableList containing the formatted report.
     */
    private static ObservableList<String> createCountryReport(Map<String, List<Customer>> sortedByCountry){

        ObservableList<String> countryReport = FXCollections.observableArrayList();
        sortedByCountry.forEach((country, resultList) -> {
            // contact name added to the report
            countryReport.add("Country: " + country);

            // adding customer info to report
            resultList.forEach(customer -> countryReport.add(formatCustomersString(customer)));

            // reporting total appointments contact
            countryReport.add("Number of customers in "+ country +" = "+resultList.size());

            // delimiter between each report
            countryReport.add("____End Report____");
        });

        return countryReport;
    }

    /**
     * Groups customers by country using Java streams.
     *
     * @param customerList List of customers to be grouped by country.
     * @return Map containing customers grouped by country.
     */
    private static Map<String, List<Customer>> sortCustomersByCountry(List<Customer> customerList){

        // Uses stream to group appointments by country.
        Map<String, List<Customer>> sortedByCountry = customerList.stream()
                .collect(Collectors.groupingBy(Customer::getCountry));
        return sortedByCountry;
    }

    /**
     * Gets a country-focused report based on the provided list of customers.
     * @param customerList List of customers to generate the report.
     * @return ObservableList containing the formatted report.
     */
    public static ObservableList<String> getCountryReport(List<Customer> customerList){

        // Sorts customer objects in Customer class by country.
        Map<String, List<Customer>> sortedCustomers = sortCustomersByCountry(customerList);

        // Creates country report based on this grouping
        ObservableList<String> finalReport = createCountryReport(sortedCustomers);

        return finalReport;
    }
}

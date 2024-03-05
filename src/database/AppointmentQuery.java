package database;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Appointment;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Class that contains sql queries logical implementation for creating / updating / deleting appointments from the database
 * and the Application class.
 *
 * @author Elley Folks
 */
public class AppointmentQuery {

    /**
     * Dynamically creates and formats TableColumn instances for the TableView representing Appointment entities.
     * Uses reflection to iterate over Appointment class fields and excludes those not meant for display.
     *
     * @param tableView The TableView to which the TableColumn instances will be added.
     */
    public static void formatAppointmentTable(TableView<Appointment> tableView) {

        Class<Appointment> appointmentClass = Appointment.class;
        Field[] fields = appointmentClass.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getName().equals("serialVersionUID")) {
                String columnName = HelperQuery.formatColumnNames(field.getName());
                TableColumn<Appointment, Object> column = new TableColumn<>(HelperQuery.formatColumnNames(HelperQuery.removeClassPrefix(columnName, "Appointment")));
                column.setCellValueFactory(data -> {
                    try {
                        field.setAccessible(true);
                        return new SimpleObjectProperty<>(field.get(data.getValue()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return new SimpleObjectProperty<>(null);
                    }
                });

                tableView.getColumns().add(column);
            }
        }
        System.out.println("Table view created.");
    }

    /**
     * Retrieves all appointments from the database.
     * Populates the provided ObservableList and TableView.
     * Executes a SQL query to join appointments with contacts and orders results by Appointment_ID.
     *
     * @param appointments The ObservableList to store Appointment entities.
     * @param tableView The TableView to display Appointment entities.
     */
    public static void getAllAppointments(ObservableList<Appointment> appointments, TableView<Appointment> tableView) {
        String sqlQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID ORDER BY a.Appointment_ID;";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sqlQuery);
             ResultSet results = statement.executeQuery()) {
            if (results != null) {
                try {
                    while (results.next()) {
                        int appointmentId = results.getInt("Appointment_ID");
                        String title = results.getString("Title");
                        String description = results.getString("Description");
                        String location = results.getString("Location");
                        String type = results.getString("Type");
                        LocalDateTime startTime = results.getTimestamp("Start").toLocalDateTime();
                        LocalDateTime endTime = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                startTime, endTime, customerId, userId, contactId, contactName);

                        appointments.add(appointment);
                    }

                    tableView.setItems(appointments);
                    tableView.refresh();

                    System.out.println("Successfully added data!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to add table data...");
                }
            }
        } catch (SQLException sqlException) {
            // Log or throw a more specific exception
            System.err.println("Error executing query: " + sqlException.getMessage());
        }
    }

    /**
     * Retrieves all appointments from the database and returns them as an ObservableList.
     * Executes a SQL query to join appointments with contacts and orders results by Appointment_ID.
     *
     * @return ObservableList containing Appointment entities retrieved from the database.
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID = c.Contact_ID ORDER BY a.Appointment_ID;";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
             ResultSet results = statement.executeQuery()) {
            if (results != null) {
                try {
                    while (results.next()) {
                        int appointmentId = results.getInt("Appointment_ID");
                        String title = results.getString("Title");
                        String description = results.getString("Description");
                        String location = results.getString("Location");
                        String type = results.getString("Type");
                        LocalDateTime startDate = results.getTimestamp("Start").toLocalDateTime();
                        LocalDateTime endDate = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(
                                appointmentId,
                                title,
                                description,
                                location,
                                type,
                                startDate,
                                endDate,
                                customerId,
                                userId,
                                contactId,
                                contactName);

                        appointments.add(appointment);
                    }

                    return appointments;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Retrieves appointments within a specified time interval of either a week or month from the current day
     * and populates the provided TableView.
     *
     * @param appointments An ObservableList to store the retrieved appointments.
     * @param tableView    The TableView to display the appointments.
     * @param interval     The time interval for which appointments are to be retrieved ("week" or "month").
     */
    public static void getRangeAppointments(ObservableList<Appointment> appointments,
                                            TableView<Appointment> tableView, String interval) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
        String currentDate = LocalDateTime.now().format(formatter);
        String selectedRange = null;

        switch(interval) {
            case "week":
                LocalDateTime dateRange1 = LocalDateTime.now().plusWeeks(1);
                selectedRange = dateRange1.format(formatter);
                break;

            case "month":
                LocalDateTime dateRange2 = LocalDateTime.now().plusMonths(1);
                selectedRange = dateRange2.format(formatter);
                break;
        }

        String sqlQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID " +
                "WHERE a.Start >= ? AND a.Start < ? ORDER BY a.Appointment_ID;";

             try{
                 PreparedStatement statement = JDBC.getConnection().prepareStatement(sqlQuery);
                 statement.setObject(1,currentDate);
                 statement.setObject(2,selectedRange);
                 //System.out.println(statement);

                 ResultSet results = statement.executeQuery();

                if (results != null) {
                    try {
                        while (results.next()) {
                            int appointmentId = results.getInt("Appointment_ID");
                            String title = results.getString("Title");
                            String description = results.getString("Description");
                            String location = results.getString("Location");
                            String type = results.getString("Type");
                            LocalDateTime startTime = results.getTimestamp("Start").toLocalDateTime();
                            LocalDateTime endTime = results.getTimestamp("End").toLocalDateTime();
                            int customerId = results.getInt("Customer_ID");
                            int userId = results.getInt("User_ID");
                            int contactId = results.getInt("Contact_ID");
                            String contactName = results.getString("Contact_Name");

                            Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                    startTime, endTime, customerId, userId, contactId, contactName);

                            appointments.add(appointment);
                        }

                        tableView.setItems(appointments);
                        tableView.refresh();

                        System.out.println("Successfully added data!");

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Failed to add table data...");
                    }
                }
            } catch (SQLException sqlException) {
                // Log or throw a more specific exception
                System.err.println("Error executing query: " + sqlException.getMessage());
            }
    }

    /**
     * Retrieves appointments of a specific type and populates the provided TableView.
     *
     * @param tableView     The TableView to display the appointments.
     * @param selectedType  The type of appointments to retrieve.
     */
    public static void getAppointmentsOfType(TableView<Appointment> tableView, String selectedType) {
        String sqlQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c " +
                "ON a.Contact_ID=c.Contact_ID WHERE Type=? ORDER BY a.Appointment_ID; ";

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sqlQuery)){
             statement.setString(1, selectedType);
             ResultSet results = statement.executeQuery();
            if (results != null) {
                try {
                    while (results.next()) {
                        int appointmentId = results.getInt("Appointment_ID");
                        String title = results.getString("Title");
                        String description = results.getString("Description");
                        String location = results.getString("Location");
                        String type = results.getString("Type");
                        LocalDateTime startTime = results.getTimestamp("Start").toLocalDateTime();
                        LocalDateTime endTime = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                startTime, endTime, customerId, userId, contactId, contactName);

                        appointments.add(appointment);
                    }

                    tableView.setItems(appointments);
                    tableView.refresh();

                    System.out.println("Successfully added data!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to add table data...");
                }
            }
        } catch (SQLException sqlException) {
            // Log or throw a more specific exception
            System.err.println("Error executing query: " + sqlException.getMessage());
        }
    }

    /**
     * Retrieves appointments within a specific month and populates the provided TableView.
     *
     * @param tableView        The TableView to display the appointments.
     * @param selectedMonth    The month for which appointments are to be retrieved (format: "MM").
     */
    public static void getAppointmentsOfMonth(TableView<Appointment> tableView, String selectedMonth) {
        String sqlQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c " +
                "ON a.Contact_ID=c.Contact_ID WHERE MONTH(Start)=? ORDER BY a.Appointment_ID; ";

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sqlQuery)){

            statement.setString(1, selectedMonth);
            ResultSet results = statement.executeQuery();
            if (results != null) {
                try {
                    while (results.next()) {
                        int appointmentId = results.getInt("Appointment_ID");
                        String title = results.getString("Title");
                        String description = results.getString("Description");
                        String location = results.getString("Location");
                        String type = results.getString("Type");
                        LocalDateTime startTime = results.getTimestamp("Start").toLocalDateTime();
                        LocalDateTime endTime = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                startTime, endTime, customerId, userId, contactId, contactName);

                        appointments.add(appointment);
                    }

                    tableView.setItems(appointments);
                    tableView.refresh();

                    System.out.println("Successfully added data!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to add table data...");
                }
            }
        } catch (SQLException sqlException) {
            // Log or throw a more specific exception
            System.err.println("Error executing query: " + sqlException.getMessage());
        }
    }

    /**
     * Retrieves appointments within a specific month and a selected type, populates the provided TableView.
     *
     * @param tableView        The TableView to display the appointments.
     * @param selectedMonth    The month for which appointments are to be retrieved (format: "MM").
     */
    public static void getAppointmentsOfMonthType(TableView<Appointment> tableView, String selectedMonth, String selectedType) {
        String sqlQuery = "SELECT * FROM appointments AS a INNER JOIN contacts AS c " +
                "ON a.Contact_ID=c.Contact_ID " +
                "WHERE MONTH(Start)=? " +
                "AND Type=? "+
                "ORDER BY a.Appointment_ID; ";

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sqlQuery)){

            statement.setString(1, selectedMonth);
            statement.setString(2, selectedType);

            ResultSet results = statement.executeQuery();
            if (results != null) {
                try {
                    while (results.next()) {
                        int appointmentId = results.getInt("Appointment_ID");
                        String title = results.getString("Title");
                        String description = results.getString("Description");
                        String location = results.getString("Location");
                        String type = results.getString("Type");
                        LocalDateTime startTime = results.getTimestamp("Start").toLocalDateTime();
                        LocalDateTime endTime = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                startTime, endTime, customerId, userId, contactId, contactName);

                        appointments.add(appointment);
                    }

                    tableView.setItems(appointments);
                    tableView.refresh();

                    System.out.println("Successfully added data!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to add table data...");
                }
            }
        } catch (SQLException sqlException) {
            // Log or throw a more specific exception
            System.err.println("Error executing query: " + sqlException.getMessage());
        }
    }

    /**
     * Creates a new appointment in the database with the provided details.
     *
     * @param title         The title of the appointment.
     * @param description   The description of the appointment.
     * @param location      The location of the appointment.
     * @param type          The type of the appointment.
     * @param start         The start date time of the appointment.
     * @param end           The end date time of the appointment.
     * @param contactID     The ID of the contact associated with the appointment.
     * @param customerId    The ID of the customer associated with the appointment.
     * @param userID        The ID of the user associated with the appointment.
     * @return              True if the appointment is created successfully, false otherwise.
     */
    public static boolean createNewAppointment(String title, String description, String location, String type, String start,
                                               String end, String contactID, String customerId, String userID){

        String sqlString = "INSERT INTO appointments(Title, Description, Location, Type, Start, " +
                "End, Customer_ID, Contact_ID, User_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Creating prepared statement
            HelperQuery.createPreparedStatement(JDBC.getConnection(), sqlString);
            PreparedStatement preparedStatement = HelperQuery.getPreparedStatement();

            // setting IN parameters
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
            preparedStatement.setInt(7, Integer.parseInt(customerId));
            preparedStatement.setInt(8, Integer.parseInt(contactID));
            preparedStatement.setInt(9, Integer.parseInt(userID));

            try {
                //executing prepared statement, returns number of rows affected
                int rowsChanged = preparedStatement.executeUpdate();

                if(rowsChanged > 0){
                    System.out.println("Number of rows affected: "+rowsChanged);
                }else{
                    System.out.println("No rows affected");
                }

                return true;

            }catch(Exception executeException){
                System.out.println("Error: " + executeException.getMessage());

            }
        }catch(Exception exception){
            System.out.println("Database Error?! " + exception.getMessage());
        }
        return false;
    }

    /**
     * Modifies an existing appointment in the database with the provided details.
     *
     * @param appointmentId The ID of the appointment to be modified.
     * @param title         The new title of the appointment.
     * @param description   The new description of the appointment.
     * @param location      The new location of the appointment.
     * @param type          The new type of the appointment.
     * @param startTime     The new start time of the appointment.
     * @param endTime       The new end time of the appointment.
     * @param customerId    The new ID of the customer associated with the appointment.
     * @param userId        The new ID of the user associated with the appointment.
     * @param contactId     The new ID of the contact associated with the appointment.
     * @return              True if the appointment is modified successfully, false otherwise.
     */
    public static boolean modifyAppointment(String appointmentId, String title, String description, String location,
                                            String type, String startTime, String endTime, String customerId,
                                            String userId, String contactId) {
        String sqlQuery = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, " +
                "Start=?, End=?, Customer_ID=?, Contact_ID=?, User_ID=? WHERE Appointment_ID = ?;";

        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(endTime));
            preparedStatement.setInt(7, Integer.parseInt(customerId));
            preparedStatement.setInt(8, Integer.parseInt(contactId));
            preparedStatement.setInt(9, Integer.parseInt(userId));
            preparedStatement.setInt(10, Integer.parseInt(appointmentId));

            try {
                preparedStatement.execute();
                if (preparedStatement.getUpdateCount() > 0) {
                    System.out.println("Number rows changed: " + preparedStatement.getUpdateCount());
                } else {
                    System.out.println("No rows changed.");
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();

                return false;
            }

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    /**
     * Removes an appointment from the database based on the provided Appointment ID.
     *
     * @param appointmentId The ID of the appointment to be removed.
     * @return True if the appointment was successfully removed, false otherwise.
     */
    public static boolean removeAppointment(int appointmentId){
        String sqlQuery = "DELETE from appointments WHERE Appointment_Id=?";

        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setObject(1, appointmentId);
            try {
                Integer numberRowsChanged = preparedStatement.executeUpdate();
                if (numberRowsChanged != null && preparedStatement.getUpdateCount() > 0) {
                    System.out.println("Number rows changed: " + preparedStatement.getUpdateCount());
                } else {
                    System.out.println("No rows changed.");
                }
                return true;

            }catch(Exception e){
                System.out.println("Could not delete row! "+ e.getMessage());
                return false;
            }
        }catch(SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    /**
     * Retrieves a list of appointments associated with a specific customer ID from the database.
     *
     * @param customerID The ID of the customer for whom to retrieve appointments.
     *
     * @return An ObservableList containing the appointments associated with the specified customer.
     *         Returns null in case of an error.
     */
    public static ObservableList<Appointment> getAppointmentsOfCustomerID(int customerID) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String query = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID " +
                "WHERE Customer_ID=?;";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)){

            statement.setInt(1, customerID);
            ResultSet results = statement.executeQuery();
            if (results != null) {
                try {
                    while (results.next()) {
                        int appointmentId = results.getInt("Appointment_ID");
                        String title = results.getString("Title");
                        String description = results.getString("Description");
                        String location = results.getString("Location");
                        String type = results.getString("Type");
                        LocalDateTime startDate = results.getTimestamp("Start").toLocalDateTime();
                        LocalDateTime endDate = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                startDate, endDate, customerId, userId, contactId, contactName);

                        appointments.add(appointment);
                    }
                    return appointments;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return null;
        }
        return appointments;
    }

    /**
     * Retrieves appointments associated with a specific contact ID and populates a TableView with the results.
     *
     * @param tableView The TableView to be populated with appointment data.
     */
    public static void getAppointmentsOfContactID(TableView<Appointment> tableView) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String query = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID;";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)){

            ResultSet results = statement.executeQuery();
            if (results != null) {
                try {
                    while (results.next()) {
                        int appointmentId = results.getInt("Appointment_ID");
                        String title = results.getString("Title");
                        String description = results.getString("Description");
                        String location = results.getString("Location");
                        String type = results.getString("Type");
                        LocalDateTime startDate = results.getTimestamp("Start").toLocalDateTime();
                        LocalDateTime endDate = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                startDate, endDate, customerId, userId, contactId, contactName);

                        appointments.add(appointment);
                    }

                    tableView.setItems(appointments);
                    tableView.refresh();

                    System.out.println("Successfully added data!");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    /**
     * Retrieves appointments associated with a specific country ID and populates a TableView.
     */
    public static void getCustomersWithCountryID() {
        ObservableList<String> appointmentWithCountry = FXCollections.observableArrayList();
        String query = "SELECT Appointment_ID, Customer_ID, Contact_ID FROM appointments AS a " +
                "INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID " +
                "INNER JOIN customers AS cu ON a.Customer_ID = cu.Customer_ID "+
                "INNER JOIN first_level_divisions AS d ON cu.Division_ID = d.Division_ID;";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)){

            ResultSet results = statement.executeQuery();
            if (results != null) {
                try {
                    while (results.next()) {

                        appointmentWithCountry.add(String.valueOf(results));
                    }

                    System.out.println("Successfully added data!");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    /**
     * Populates a ComboBox with contact names and builds a mapping of names to their corresponding IDs.
     *
     * @param comboBox   The ComboBox to be populated with contact names.
     * @param query      The SQL query to retrieve contact names and IDs.
     * @param nameIdMap  A Map to store the mapping of contact names to their corresponding IDs.
     */
    public static void getContactNameID(ComboBox<String> comboBox, String query, Map<String, Integer> nameIdMap) {
        ObservableList<String> items = FXCollections.observableArrayList();

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                String name = results.getString(1);
                items.add(name);

                // Assuming the ID is in the second column of the result set
                int id = results.getInt(2);
                nameIdMap.put(name, id);
            }

            comboBox.setItems(items);
        } catch (SQLException e) {
            System.err.println("Could not get contact name: " + e.getMessage());
        }
    }

}



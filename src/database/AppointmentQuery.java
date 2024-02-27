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

public class AppointmentQuery {

    public static void formatAppointmentTable(TableView<Appointment> tableView) {

        // Get the list of all properties of the Appointment class using reflection
        Class<Appointment> appointmentClass = Appointment.class;
        Field[] fields = appointmentClass.getDeclaredFields();

        for (Field field : fields) {
            // Exclude fields that should not be displayed in the TableView
            if (!field.getName().equals("serialVersionUID")) {
                // Create TableColumn dynamically
                TableColumn<Appointment, Object> column = new TableColumn<>(field.getName());
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
     * Runs a select SQL command to get all appointments from DB. Stores this information in a class object
     * for appointments.
     * Sets table view
     * @return
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

    public static void getRangeAppointments(ObservableList<Appointment> appointments,
                                                                   TableView<Appointment> tableView, String interval) {
        // TODO figure out if this needs to be converted?
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

    public static boolean removeAppointment(int columnId){
        String sqlQuery = "DELETE from appointments WHERE Appointment_Id=?";

        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setObject(1, columnId);
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

    public static ObservableList<Appointment> getAppointmentsWithCustomerID(int customerID) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String query = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Customer_ID=?;";

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



package database;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Appointment;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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


    public static ObservableList<Appointment> getAllAppointments(ObservableList<Appointment> appointments, TableView<Appointment> tableView) {
        String query = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID;";

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
                    return appointments;
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to add table data...");
                }
            }
        } catch (SQLException sqlException) {
            // Log or throw a more specific exception
            System.err.println("Error executing query: " + sqlException.getMessage());
        }
        return null;
    }

    public static ObservableList<Appointment> getRangeAppointments(ObservableList<Appointment> appointments, TableView<Appointment> tableView, String interval) {
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

        String query = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID " +
                "WHERE a.Start >= ? AND a.Start < ? ;";

             try{
                 PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
                 statement.setObject(1,currentDate);
                 statement.setObject(2,selectedRange);
                 System.out.println(statement);

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
                        return appointments;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Failed to add table data...");
                    }
                }
            } catch (SQLException sqlException) {
                // Log or throw a more specific exception
                System.err.println("Error executing query: " + sqlException.getMessage());
            }
        return null;
    }

    public static boolean removeAppointment(int columnId){
        String query = "DELETE from appointments WHERE Appointment_Id=?";

        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(query);
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
}



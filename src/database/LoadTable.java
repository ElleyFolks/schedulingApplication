package database;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Appointment;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoadTable{

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
                        LocalDate startDate = results.getDate("Start").toLocalDate();
                        LocalDateTime startTime = results.getTimestamp("Start").toLocalDateTime();
                        LocalDate endDate = results.getDate("End").toLocalDate();
                        LocalDateTime endTime = results.getTimestamp("End").toLocalDateTime();
                        int customerId = results.getInt("Customer_ID");
                        int userId = results.getInt("User_ID");
                        int contactId = results.getInt("Contact_ID");
                        String contactName = results.getString("Contact_Name");

                        Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                                startDate, startTime, endDate, endTime, customerId, userId, contactId, contactName);

                        appointments.add(appointment);
                    }

                    tableView.setItems(appointments);
                    tableView.refresh();


                    System.out.println("Successfully populated table data!");
                    return appointments;
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to populate table data...");
                }
            }
        } catch (SQLException e) {
            // Log or throw a more specific exception
            System.err.println("Error executing query: " + e.getMessage());
        }
        return null;
    }

}

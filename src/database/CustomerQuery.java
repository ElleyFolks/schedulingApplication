package database;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Appointment;
import model.Customer;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CustomerQuery {

    public static void formatCustomerTable(TableView<Customer> tableView) {

        // Get the list of all properties of the Appointment class using reflection
        Class<Customer> customerClass = Customer.class;
        Field[] fields = customerClass.getDeclaredFields();

        for (Field field : fields) {
            // Exclude fields that should not be displayed in the TableView
            if (!field.getName().equals("serialVersionUID")) {
                // Create TableColumn dynamically
                TableColumn<Customer, Object> column = new TableColumn<>(field.getName());
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

    public static ObservableList<Customer> getAllCustomers(ObservableList<Customer> customers, TableView<Customer> tableView) {

        String query = "SELECT * FROM customers AS c " +
                "INNER JOIN first_level_divisions AS d ON c.Division_ID = d.Division_ID " +
                "INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID;";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
             ResultSet results = statement.executeQuery()) {
            if (results != null) {
                try {
                    while (results.next()) {
                        int customerID = results.getInt("Customer_ID");
                        String customerName = results.getString("Customer_Name");
                        String address = results.getString("Address");
                        String postal = results.getString("Postal_Code");
                        String phone = results.getString("Phone");
                        String division = results.getString("Division");
                        String country = results.getString("Country");
                        int divisionID = results.getInt("Division_ID");

                        Customer customer = new Customer(
                                customerID,
                                customerName,
                                address,
                                postal,
                                phone,
                                division,
                                country,
                                divisionID
                        );

                        customers.add(customer);
                    }

                    tableView.setItems(customers);
                    tableView.refresh();

                    System.out.println("Successfully added data!");
                    return customers;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
        return null;
    }
}

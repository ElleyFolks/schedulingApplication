package database;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Customer;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Class that contains sql queries logical implementation for creating / updating / deleting customers from the database
 * and the Customer class.
 *
 * @author Elley Folks
 */
public class CustomerQuery {

    /**
     * Formats the TableView for displaying Customer data dynamically.
     *
     * @param tableView The TableView to be formatted.
     */
    public static void formatCustomerTable(TableView<Customer> tableView) {
        Class<Customer> customerClass = Customer.class;
        Field[] fields = customerClass.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getName().equals("serialVersionUID")) {
                String columnName = HelperQuery.formatColumnNames(field.getName());
                TableColumn<Customer, Object> column = new TableColumn<>(HelperQuery.removeClassPrefix(columnName, "Customer"));
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
     * Retrieves all customer data from the database and populates the provided TableView.
     *
     * @param customers The ObservableList to store retrieved Customer objects.
     * @param tableView The TableView to be populated with customer data.
     */
    public static void getAllCustomers(ObservableList<Customer> customers, TableView<Customer> tableView) {

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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    /**
     * Creates a new customer in the database with the provided information.
     *
     * @param customerName The name of the customer.
     * @param address      The address of the customer.
     * @param postalCode   The postal code of the customer.
     * @param phoneNumber  The phone number of the customer.
     * @param divisionID   The division ID associated with the customer.
     * @return True if the customer creation is successful, false otherwise.
     */
    public static boolean createNewCustomer(String customerName, String address, String postalCode, String phoneNumber,
                                            Integer divisionID) {


        String sqlString = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            // Creating prepared statement
            HelperQuery.createPreparedStatement(JDBC.getConnection(), sqlString);
            PreparedStatement preparedStatement = HelperQuery.getPreparedStatement();

            // setting IN parameters
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setInt(5, divisionID);

            try {
                //executing prepared statement, returns number of rows affected
                int rowsChanged = preparedStatement.executeUpdate();

                if (rowsChanged > 0) {
                    System.out.println("Number of rows affected: " + rowsChanged);
                } else {
                    System.out.println("No rows affected");
                }

                return true;

            } catch (Exception executeException) {
                System.out.println("Error: " + executeException.getMessage());

            }
        } catch (Exception exception) {
            System.out.println("Database Error?! " + exception.getMessage());
        }
        return false;
    }

    /**
     * Modifies an existing customer entry in the database with the provided information.
     *
     * @param customerID   The ID of the customer to be modified.
     * @param customerName The updated name of the customer.
     * @param address      The updated address of the customer.
     * @param postalCode   The updated postal code of the customer.
     * @param phoneNumber  The updated phone number of the customer.
     * @param divisionId   The updated division ID associated with the customer.
     * @return True if the customer modification is successful, false otherwise.
     */
    public static boolean modifyCustomer(Integer customerID, String customerName, String address, String postalCode,
                                         String phoneNumber, String divisionId) {
        String sqlQuery = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, " +
                "Division_ID=? WHERE Customer_ID = ?;";
        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);

            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setInt(5, Integer.parseInt(divisionId));
            preparedStatement.setInt(6, customerID);

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
     * Removes a customer from the database based on the given customer ID.
     *
     * @param columnId The ID of the customer to be removed.
     * @return True if the removal is successful, false otherwise.
     */
    public static boolean removeCustomer(int columnId) {
        String sqlQuery = "DELETE from customers WHERE Customer_ID=?";

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

            } catch (Exception e) {
                System.out.println("Could not delete row! " + e.getMessage());
                return false;
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    /**
     * Retrieves country names and their corresponding IDs from the database
     * and populates them into the provided ComboBox and map.
     *
     * @param comboBox         The ComboBox to be populated with country names.
     * @param countryNameIdMap The map to store country names and their IDs.
     */
    public static void getCountryNameID(ComboBox<String> comboBox, Map<String, Integer> countryNameIdMap) {
        ObservableList<String> countryNames = FXCollections.observableArrayList();

        String sqlQuery = "SELECT Country, Country_ID FROM countries;";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sqlQuery);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                String name = results.getString(1);
                countryNames.add(name);

                // Assuming the ID is in the second column of the result set
                int id = results.getInt(2);
                countryNameIdMap.put(name, id);
            }

            comboBox.setItems(countryNames);
        } catch (SQLException e) {
            System.err.println("Could not get contact name: " + e.getMessage());
        }
    }

    /**
     * Retrieves division names and their corresponding IDs from the database
     * for a given country and populates them into the provided ComboBox and map.
     *
     * @param comboBox         The ComboBox to be populated with division names.
     * @param countryNameIdMap The map containing country names and their IDs.
     * @param countryID        The ID of the selected country.
     */
    public static void getDivision(ComboBox<String> comboBox, Map<String, Integer> countryNameIdMap, Integer countryID) {
        ObservableList<String> divisionNames = FXCollections.observableArrayList();

        String sqlQuery = "SELECT Division,Division_ID  FROM first_level_divisions WHERE COUNTRY_ID = ?;";

        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, countryID);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                String name = results.getString(1);
                divisionNames.add(name);

                // Assuming the ID is in the second column of the result set
                int id = results.getInt(2);
                countryNameIdMap.put(name, id);
            }

            comboBox.setItems(divisionNames);
        } catch (SQLException e) {
            System.err.println("Could not get contact name: " + e.getMessage());
        }
    }

    /**
     * Checks if a customer has any associated appointments in the database.
     *
     * @param customerId The ID of the customer to check for appointments.
     * @return True if the customer has appointments, false otherwise.
     */
    public static boolean customerHasAppointment(Integer customerId) {
        String sqlQuery = "SELECT * FROM appointments " +
                "AS a INNER JOIN contacts AS c ON a.Contact_ID = c.Contact_ID " +
                "WHERE Customer_ID = ?;";
        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, customerId);

            try (ResultSet results = preparedStatement.executeQuery()) {
                System.out.println("Number rows: " + preparedStatement.getUpdateCount());
                return results.next();
            }
        } catch (Exception e) {
            System.out.println("Error executing query. " + e.getMessage());
            return false;
        }
    }
}


package database;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Appointment;
import model.Customer;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

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

    public static boolean createNewCustomer(String customerName, String address, String postalCode, String phoneNumber,
                                            Integer divisionID){


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

    public static boolean removeCustomer(int columnId){
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

            }catch(Exception e){
                System.out.println("Could not delete row! "+ e.getMessage());
                return false;
            }
        }catch(SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

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

    public static void getDivision(ComboBox<String> comboBox, Map<String, Integer> countryNameIdMap, Integer countryID) {
        ObservableList<String> divisionNames = FXCollections.observableArrayList();

        String sqlQuery = "SELECT Division,Division_ID  FROM first_level_divisions WHERE COUNTRY_ID = ?;";

        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery)){
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



}

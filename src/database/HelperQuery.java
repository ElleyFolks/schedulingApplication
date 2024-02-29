package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;

public class HelperQuery {

    private static PreparedStatement pStatement;

    public static void createPreparedStatement(Connection connection, String sqlStatement) throws SQLException{
        pStatement = connection.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreparedStatement(){
        return pStatement;
    }

    public static String formatColumnNames(String columnName){
        // tests if string is empty
        if(columnName == null || columnName.isEmpty()){
            return "";
        }
        StringBuilder formattedString = new StringBuilder();
        formattedString.append(Character.toUpperCase(columnName.charAt(0)));

        for (int i = 1; i < columnName.length(); i++) {
            char currentChar = columnName.charAt(i);
            char previousChar = columnName.charAt(i - 1);

            if (Character.isUpperCase(currentChar) && !Character.isUpperCase(previousChar)) {
                formattedString.append(" ").append(currentChar);
            } else {
                formattedString.append(currentChar);
            }

            formattedString = new StringBuilder(formattedString.toString().replace("Id", "ID"));
        }
        return formattedString.toString();
    }

    public static String removeClassPrefix(String columnName, String className){
        String formattedName = columnName.replaceFirst(className,"");
        return formattedName;
    }

    /**
     * Used to set values for a combo box of a menu based on a column from a database table.
     * @param comboBox The specific fxml object that will have options populated.
     * @param sqlQuery A query that selects a column from a table.
     */
    public static void setComboBoxOptions(ComboBox<String> comboBox, String sqlQuery){
        ObservableList<String> boxItems = FXCollections.observableArrayList();

        try{
            createPreparedStatement(JDBC.getConnection(),sqlQuery);
            ResultSet resultSet = getPreparedStatement().executeQuery();

            while(resultSet.next()){
                String resultValue = resultSet.getString(1);
                boxItems.add(resultValue);
            }

            // settings items to be drop down options in combo box
            comboBox.setItems(boxItems);

        }catch(SQLException sqlException) {
            System.err.println("Could not execute query. " + sqlException.getMessage());
        }
    }

    /**
     * Populates combo boxes in report tab with appropriate options, based on which radio button is selected.
     * @param comboBox
     */
    public static void setAppointmentTypes(ComboBox<String> comboBox){
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();

        String sqlQuery = "SELECT Type FROM appointments;";

        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery)) {
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                String type = results.getString(1);
                appointmentTypes.add(type);
            }

            comboBox.setItems(appointmentTypes);
        } catch (SQLException e) {
            System.err.println("Could not get contact name: " + e.getMessage());
        }
    }

    public static void setAppointmentContacts(ComboBox<String> comboBox, Map<String, Integer> namesToIds){
        ObservableList<String> appointmentContacts = FXCollections.observableArrayList();

        String sqlQuery = "SELECT Contact_Name, Contact_ID FROM contacts ORDER BY Contact_ID;";

        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery)) {
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {

                String name = results.getString(1);
                appointmentContacts.add(name);

                // Assuming the ID is in the second column of the result set
                int id = results.getInt(2);
                namesToIds.put(name, id); // maps name:id
            }

            comboBox.setItems(appointmentContacts);
        } catch (SQLException e) {
            System.err.println("Could not get contact name: " + e.getMessage());
        }
    }


}

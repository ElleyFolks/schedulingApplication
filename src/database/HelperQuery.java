package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;


/**
 * Class that contains quality of life sql queries that are widely usable.
 * Contains functions to create / execute prepared statements, reformatting strings, and populating combo boxes.
 * @author Elley Folks
 */
public class HelperQuery {

    private static PreparedStatement pStatement;

    /**
     * Creates a PreparedStatement using the provided SQL statement and the database connection.
     *
     * @param connection   The database connection.
     * @param sqlStatement The SQL statement to be prepared.
     * @throws SQLException If a database access error occurs.
     */
    public static void createPreparedStatement(Connection connection, String sqlStatement) throws SQLException{
        pStatement = connection.prepareStatement(sqlStatement);
    }

    /**
     * Retrieves the previously created PreparedStatement.
     *
     * @return The PreparedStatement.
     */
    public static PreparedStatement getPreparedStatement(){
        return pStatement;
    }

    /**
     * Formats column names by adding spaces between camel-case words and replacing "Id" with "ID".
     *
     * @param columnName The original column name.
     * @return The formatted column name.
     */
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

    /**
     * Removes the prefix (class name) from a column name.
     *
     * @param columnName The original column name.
     * @param className  The class name prefix.
     * @return The column name without the prefix.
     */
    public static String removeClassPrefix(String columnName, String className){
        // keeps class name if something like 'Appointment ID' or 'Customer ID'.
        if(columnName.contains(className+" ID")) {
            return columnName;
            }

        // removes class name if anything other than the class ID or primary key.
        else{
            String formattedName = columnName.replaceFirst(className, "");
            return formattedName;
        }
    }

    /**
     * Sets options for a ComboBox based on a column from a database table.
     *
     * @param comboBox The ComboBox to be populated.
     * @param sqlQuery  A query that selects a column from a table.
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
}

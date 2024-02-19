package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class ManageQuery {

    private static PreparedStatement pStatement;

    public static void createPreparedStatement(Connection connection, String sqlStatement) throws SQLException{
        pStatement = connection.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreparedStatement(){
        return pStatement;
    }

    public static boolean createRowQuery(String title, String description, String location, String type, String start,
                                     String end, String contactID, String customerId, String userID){


        String sqlString = "INSERT INTO appointments(Title, Description, Location, Type, Start, " +
                "End, Customer_ID, Contact_ID, User_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Creating prepared statement
            ManageQuery.createPreparedStatement(JDBC.getConnection(), sqlString);
            PreparedStatement preparedStatement = ManageQuery.getPreparedStatement();

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
}

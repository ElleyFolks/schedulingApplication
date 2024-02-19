package database;

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

    public static PreparedStatement createSelectQuery(String tableName) throws SQLException{
        String searchString = "SELECT * from " + tableName + " AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID;";

        // Creating prepared statement
        ManageQuery.createPreparedStatement(JDBC.getConnection(),searchString);

        return ManageQuery.getPreparedStatement();
    }


    /**
     * A function that creates and executes a query to add a row to a table.
     * @param tableName Name of table to add row to.
     * @param appointment Actual data that will be contained in row
     * @param columnNames The names of the column (Appointment_ID,Type,...) in the table of the database.
     * @return
     */
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

    public static ResultSet getQueryResults(PreparedStatement preparedStatement) throws SQLException{
        ResultSet resultSet = null;

        // Attempting to execute prepared statement
        try{
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();

        } catch(Exception exception){
            System.out.println("Database Error " + exception.getMessage());
        }
        return resultSet;
    }


    public static String getValueAtID(String columnName, String tableName, String id) throws SQLException {

        ManageQuery.createPreparedStatement(JDBC.getConnection(), "SELECT "+ columnName +" FROM "+ tableName +" WHERE Contact_ID = " + id);
        PreparedStatement preparedStatement = ManageQuery.getPreparedStatement();
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();

        return String.valueOf(resultSet);
    }
}

package database;

import java.sql.*;
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
        String searchString = "SELECT * from " + tableName;

        // Creating prepared statement
        ManageQuery.createPreparedStatement(JDBC.getConnection(),searchString);

        return ManageQuery.getPreparedStatement();
    }


    /**
     * A function that creates and executes a query to add a row to a table.
     * @param tableName Name of table to add row to.
     * @param columnValues Actual data that will be contained in row
     * @param columnNames The names of the column (Appointment_ID,Type,...) in the table of the database.
     * @return
     */
    public static ResultSet createRowQuery(String tableName, List<String> columnValues, List<String> columnNames){

        StringBuilder sqlString = new StringBuilder("INSERT INTO " + tableName + " (");

        // adds columns to query
        for(int i = 1; i<= columnNames.size(); i++){
            sqlString.append(columnNames.get(i-1));

            if(i<columnNames.size()){
                sqlString.append(", ");
            }
        }

        // adds start of VALUES statement
        sqlString.append(") VALUES (");

        // adds placeholders for values
        for(int i = 1; i<= columnNames.size(); i++){
            sqlString.append("?");

            if(i<columnNames.size()){
                sqlString.append(", ");
            }
        }
        sqlString.append(")");
        System.out.println(sqlString.toString());

        try {
            // Creating prepared statement
            ManageQuery.createPreparedStatement(JDBC.getConnection(), sqlString.toString());
            PreparedStatement preparedStatement = ManageQuery.getPreparedStatement();

            // setting values of IN ? values
            for(int i = 1; i<= columnNames.size(); i++){
                preparedStatement.setObject(i, columnValues.get(i-1));
            }

            ResultSet resultSet = ManageQuery.getQueryResults(preparedStatement);
            return resultSet;

        }catch(Exception exception){
            System.out.println("Database Error?! " + exception.getMessage());
        }
        return null;
    }

    public static PreparedStatement createSelectWQuery(String tableName, String start, String end) throws SQLException{
        //TODO test this
        String searchString = "SELECT * from " + tableName + "WHERE start=? to start=?";

        // Creating prepared statement
        ManageQuery.createPreparedStatement(JDBC.getConnection(),searchString);

        return ManageQuery.getPreparedStatement();
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

}

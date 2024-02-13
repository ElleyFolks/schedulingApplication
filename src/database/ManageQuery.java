package database;

import java.sql.*;
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

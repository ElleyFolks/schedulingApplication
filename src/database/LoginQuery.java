package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginQuery {

    public static boolean checkUserCredentials(String username, String password) throws SQLException {
        String searchString = "SELECT * FROM users WHERE User_Name=? AND Password=?";

        // Creating prepared statement
        ManageQuery.createPreparedStatement(JDBC.getConnection(),searchString);
        PreparedStatement preparedStatement = ManageQuery.getPreparedStatement();

        // Setting IN (?) parameter placeholders of prepared statement
        preparedStatement.setString(1,username);
        preparedStatement.setString(2, password);

        // Attempting to execute prepared statement
        try{
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            return(resultSet.next());
        } catch(Exception exception){
            System.out.println("Database Error " + exception.getMessage());
            return false;
        }
    }
}

package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that contains quality sql queries for checking user credentials.
 *
 * @author Elley Folks
 */
public class LoginQuery {

    /**
     * Checks user credentials by querying the database for a matching username and password.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return True if the credentials are valid, false otherwise.
     * @throws SQLException If there is an issue with the database connection or query execution.
     */
    public static boolean checkUserCredentials(String username, String password) throws SQLException {
        String searchString = "SELECT * FROM users WHERE User_Name=? AND Password=?";

        // Creating prepared statement
        HelperQuery.createPreparedStatement(JDBC.getConnection(),searchString);
        PreparedStatement preparedStatement = HelperQuery.getPreparedStatement();

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

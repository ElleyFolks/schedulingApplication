package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql";
    static final String ipAddress = "://127.0.0.1/";
    private static final String dbName = "client_schedule";
    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName + "?connectionTimeZone=SERVER";
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    /**
     * Gets the existing database connection.
     *
     * @return The current database connection.
     */
    public static Connection getConnection(){
        return conn;
    }

    /**
     * Establishes a new connection to the SQL database.
     *
     * @return The newly established database connection.
     */
    public static Connection establishConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);

            conn = DriverManager.getConnection(jdbcURL, username, password);

            System.out.println("Connection successful");
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return conn;
    }

    /**
     * Closes the existing database connection.
     */
    public static void endConnection() {

        try {
            conn.close();
            System.out.println("Connection closed");
        }

        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
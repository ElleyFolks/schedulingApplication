package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql";

    private static final String location = "//localhost/";

    // WGU Database - Dear EVALUATOR, please fill out this information below for your connection. Thanks!
    private static final String ipAddress = "://wgudb.ucertify.com:3306/";

    private static final String dbName = "WJ06YG0";// name of DB
    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";

    // Personal Database for my own development.
    //private static final String ipAddress = "://192.168.1.210:3306/";
    //private static final String dbName = "SchedulingApplication";
    //private static final String username = DatabaseConfig.getUsername();
    //private static final String password = DatabaseConfig.getPassword();


    //private static final String jdbcURL = protocol + vendorName + ipAddress + dbName + "?connectionTimeZone=SERVER"; // used for personal set up
    private static final String jdbcURL = protocol + vendorName + location + ipAddress + dbName + "?connectionTimeZone=SERVER";
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    public static Connection getConnection(){
        return conn;
    }

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
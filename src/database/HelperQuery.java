package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.*;

public class HelperQuery {

    private static PreparedStatement pStatement;

    public static void createPreparedStatement(Connection connection, String sqlStatement) throws SQLException{
        pStatement = connection.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreparedStatement(){
        return pStatement;
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

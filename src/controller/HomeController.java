package controller;

import database.ManageQuery;
import javafx.fxml.Initializable;
import database.LoadTable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class HomeController implements Initializable {
    @FXML
    TableView<ObservableList<String>> customersTable = new TableView<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            // Set padding to create a border around the TableView
            customersTable.setPadding(new javafx.geometry.Insets(20));

            PreparedStatement statement = ManageQuery.createSelectQuery("customers");
            ResultSet results = ManageQuery.getQueryResults(statement);
            LoadTable.loadData(customersTable, results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

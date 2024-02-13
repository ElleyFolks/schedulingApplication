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
    TableView<ObservableList<String>> homeTable = new TableView<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            PreparedStatement statement = ManageQuery.createSelectQuery("contacts");
            ResultSet results = ManageQuery.getQueryResults(statement);
            LoadTable.loadData(homeTable, results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

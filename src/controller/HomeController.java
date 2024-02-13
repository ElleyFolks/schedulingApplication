package controller;

import javafx.fxml.Initializable;
import database.LoadTable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class HomeController implements Initializable {
    @FXML
    TableView<ObservableList<String>> homeTable = new TableView<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            LoadTable.loadData(homeTable, "appointments");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

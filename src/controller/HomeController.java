package controller;

import database.ManageQuery;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import database.LoadTable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class HomeController implements Initializable {
    @FXML
    TableView<ObservableList<String>> customersTable = new TableView<>();

    @FXML
    TableView<Appointment> appointmentsTable = new TableView<>();

    @FXML
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            // Set padding to create a border around the TableView
            //customersTable.setPadding(new javafx.geometry.Insets(20));

            //PreparedStatement statement = ManageQuery.createSelectQuery("customers");
            //ResultSet results = ManageQuery.getQueryResults(statement);
            //LoadTable.formatAppointmentTable(customersTable, results);

            appointmentsTable.setPadding(new javafx.geometry.Insets(20));
            LoadTable.formatAppointmentTable(appointmentsTable);
            ObservableList<Appointment> allAppointments = LoadTable.getAllAppointments(appointments, appointmentsTable);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void switchToAddAppointmentScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AppointmentMenu.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = Main.getPrimaryStage();
        stage.hide();
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }
}

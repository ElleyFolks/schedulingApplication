package main;

import database.JDBC;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class Main extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());// scene

        primaryStage = stage;
        stage.setTitle("Scheduling Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){

        Locale.setDefault(new Locale("fr"));
        JDBC.establishConnection();

        launch(args);

        JDBC.endConnection();
    }
}



package main;

import database.JDBC;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;

public class Main extends Application {

    private static Stage primaryStage;

    public static Locale userLocale;
    public static ZoneId userTimeZone;
    public static String userCounty;

    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws IOException {

        userLocale = Locale.getDefault();
        userTimeZone = ZoneId.systemDefault();
        userCounty = userLocale.getCountry();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());// scene

        primaryStage = stage;
        //stage.setTitle("Scheduling Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){

        //Locale.setDefault(new Locale("fr")); TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));

        JDBC.establishConnection();

        launch(args);

        JDBC.endConnection();
    }
}



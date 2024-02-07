package main;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        // container for GUI elements
        Parent root = FXMLLoader.load(getClass().getResource("/view/FirstScreen.fxml"));

        stage.setTitle("First View");
        stage.setScene(new Scene( root, 800, 600));

        // displays stage
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}



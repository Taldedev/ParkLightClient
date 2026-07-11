package com.parklight.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX entry point. Loads the map screen from FXML.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/parklight/client/view/welcome.fxml"));
        Parent root = loader.load();

        stage.setTitle("ParkLight");
        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example.timelinebuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TimelineApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Always start with the main menu
        FXMLLoader fxmlLoader = new FXMLLoader(TimelineApp.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        // Get the controller and set the stage
        MenuController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Multiverse Creator Project");
        stage.setScene(scene);
        // Set to full screen or maximized
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

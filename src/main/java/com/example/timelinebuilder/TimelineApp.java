package com.example.timelinebuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class TimelineApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(TimelineApp.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MenuController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Timeline Builder");
        stage.setScene(scene);
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button createTimelineButton;

    @FXML
    private Button editTimelineButton;

    @FXML
    private Button viewTimelineButton;

    @FXML
    private Button exitButton;

    @FXML Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void initialize() {
        createTimelineButton.setOnAction(e -> onCreateTimeline());
        editTimelineButton.setOnAction(e -> onEditTimeline());
        viewTimelineButton.setOnAction(e -> onViewTimeline());
        exitButton.setOnAction(e -> onExit());
    }

    private void onCreateTimeline() {
        System.out.println("Button clicked");
        try {
            stage.setMaximized(false);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/create-timeline-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage stage = (Stage) createTimelineButton.getScene().getWindow();

            // Set the stage size to the full screen size
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            ;

            // Set the scene for the stage
            stage.setScene(scene);

            // Adjust the stage to be centered, so it appears properly on full screen or resized window
            stage.setX(bounds.getMinX());  // Align to the left edge
            stage.setY(bounds.getMinY());  // Align to the top edge

            // Set maximized state if required

            stage.setMaximized(true);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void onEditTimeline() {
        System.out.println("Load Timeline clicked!");
        // Add logic to load an existing timeline
    }

    private void onViewTimeline() {
        System.out.println("viewing timeline");
    }

    private void onExit() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
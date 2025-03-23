package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class TimelineController {
    @FXML
    private Label multiverseNameLabel;

    @FXML
    private HBox universesContainer;

    private String multiversePath;

    public void setMultiversePaths(String multiversePath) {
        this.multiversePath = multiversePath;
        loadMultiverse();
    }

    private void loadMultiverse() {
        // Load multiverse data and populate the view
        // This is where you'd implement the timeline visualization

        // For now, just set the multiverse name
        String multiverseName = multiversePath.substring(multiversePath.lastIndexOf(System.getProperty("file.separator")) + 1);
        multiverseNameLabel.setText(multiverseName);
    }

    @FXML
    private void handleAddUniverse() {
        // Implement universe addition logic
        System.out.println("Add Universe clicked");
    }

    @FXML
    private void handleAddEvent() {
        // Implement event addition logic
        System.out.println("Add Event clicked");
    }

    @FXML
    private void handleRefresh() {
        // Refresh the timeline view
        loadMultiverse();
    }

    @FXML
    private void handleBackToMenu() {
        try {
            // Close the current timeline window
            Stage currentStage = (Stage) multiverseNameLabel.getScene().getWindow();
            currentStage.close();

            // Open the main menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Scene scene = new Scene(loader.load(), 400, 400);

            Stage menuStage = new Stage();
            menuStage.setTitle("Timeline Builder");
            menuStage.setScene(scene);
            menuStage.setMaximized(true);
            menuStage.show();

            // Get the controller and set the stage
            MenuController controller = loader.getController();
            controller.setStage(menuStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

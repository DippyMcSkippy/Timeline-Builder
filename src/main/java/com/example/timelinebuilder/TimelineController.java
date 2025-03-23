package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.example.file.MultiverseGet;
import com.example.file.MultiverseCreate;
import java.io.IOException;
import java.io.File;

public class TimelineController {
    @FXML
    private Label multiverseNameLabel;

    @FXML
    private HBox universesContainer;

    private String multiversePath;
    private MultiverseGet multiverseGet;

    public void setMultiversePaths(String multiversePath) {
        this.multiversePath = multiversePath;

        // Get the multiverse CSV path
        String multiverseName = multiversePath.substring(multiversePath.lastIndexOf(File.separator) + 1);
        String multiverseCsvPath = multiversePath + File.separator + multiverseName + ".csv";

        // Create MultiverseGet instance
        this.multiverseGet = new MultiverseGet(multiverseCsvPath);

        loadMultiverse();
    }

    private void loadMultiverse() {
        // Use MultiverseGet to get the multiverse name
        if (multiverseGet != null) {
            multiverseNameLabel.setText(multiverseGet.getMultiverseName());
        }

        // Here you would also load and display the universes and events
    }

    @FXML
    private void handleAddUniverse() {
        try {
            // Get the universes folder path from the multiverse path
            String universesFolderPath = multiversePath + File.separator + "Universes";

            // Get the multiverse CSV path
            String multiverseName = multiversePath.substring(multiversePath.lastIndexOf(File.separator) + 1);
            String multiverseCsvPath = multiversePath + File.separator + multiverseName + ".csv";

            // Create a custom MultiverseCreate object without showing directory chooser
            MultiverseCreate multiverseCreate = new MultiverseCreate() {
                // Override the constructor behavior to prevent directory chooser from showing
                @Override
                public void showDirectoryChooser() {
                    // Do nothing - we don't want to show the chooser here
                }
            };

            // Set the multiverse CSV path manually
            multiverseCreate.setMultiverseName(multiverseName);

            // Load the universe creation dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("universe-create-view.fxml"));
            Scene scene = new Scene(loader.load(), 500, 300);

            // Get the controller and set the necessary paths
            UniverseCreateController controller = loader.getController();
            controller.setUniversesFolderPath(universesFolderPath);
            controller.setMultiverseCreate(multiverseCreate);

            // Create and show the stage
            Stage stage = new Stage();
            stage.setTitle("Create New Universe");
            stage.setScene(scene);
            stage.showAndWait(); // Use showAndWait to block until dialog is closed

            // After dialog is closed, refresh the view
            loadMultiverse();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEvent() {
        // Implement event addition logic
        System.out.println("Add Event clicked");

        // This would need to be implemented to show a dialog for selecting a universe
        // and then creating an event in that universe
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

package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button createMultiverseButton;

    @FXML
    private Button editMultiverseButton;

    @FXML
    private Button viewMultiverseButton;

    @FXML
    private Button exitButton;

    @FXML Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void initialize() {
        createMultiverseButton.setOnAction(e -> onCreateMultiverse());
        editMultiverseButton.setOnAction(e -> onEditTimeline());
        viewMultiverseButton.setOnAction(e -> onViewTimeline());
        exitButton.setOnAction(e -> onExit());
    }

    private void onCreateMultiverse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/multiverse-create-view.fxml"));
            Scene scene = new Scene(loader.load(), 300, 200);
            Stage newStage = new Stage();
            newStage.setTitle("Create New Multiverse");
            newStage.setScene(scene);
            newStage.show();
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
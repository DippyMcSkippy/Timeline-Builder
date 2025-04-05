package com.example.timelinebuilder;

import com.example.file.Multiverse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;

public class MenuController {
    @FXML
    private Button createMultiverseButton;

    @FXML
    private Button openMultiverseButton;

    @FXML
    private Button exitButton;

    @FXML
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        createMultiverseButton.setOnAction(e -> onCreateMultiverse());
        exitButton.setOnAction(e -> onExit());
    }

    @FXML
    private void handleOpenMultiverse() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Multiverse Folder");
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String multiverseFolderPath = selectedDirectory.getAbsolutePath();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/multiverse-display-view.fxml"));
                Scene scene = new Scene(loader.load(), 800, 600);
                MultiverseDisplayController controller = loader.getController();
                Multiverse multiverse = new Multiverse();
                multiverse.setRootPath(selectedDirectory.getParent()); // Set the parent directory as the root path
                multiverse.setMultiverseName(selectedDirectory.getName()); // Set the selected directory name as the multiverse name
                controller.initializeWithMultiverse(multiverse);

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        Stage stage = (Stage) createMultiverseButton.getScene().getWindow();
        stage.close();
    }

    private void onExit() {
        //System.out.println("Exiting application...");
        System.exit(0);
    }
}
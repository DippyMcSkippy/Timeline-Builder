package com.example.timelinebuilder;

import com.example.file.Multiverse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class MultiverseCreateController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> datingSystem;

    @FXML
    private VBox relativeFields;

    @FXML
    private TextField beforeField;

    @FXML
    private TextField duringField;

    @FXML
    private TextField afterField;

    @FXML
    private Button submitButton;

    private Stage menuStage;
    private Multiverse multiverse;

    public void setMenuStage(Stage menuStage) {
        this.menuStage = menuStage;
    }

    @FXML
    public void initialize() {
        // Initialize the Multiverse instance
        multiverse = new Multiverse();

        datingSystem.getItems().addAll("Numeral", "Relative");
        datingSystem.setValue("Numeral");

        // Show/hide era fields based on dating system
        datingSystem.setOnAction(e -> {
            boolean isRelative = "Relative".equals(datingSystem.getValue());
            relativeFields.setVisible(isRelative);
        });

        submitButton.setOnAction(event -> createMultiverse());
    }

    private void createMultiverse() {
        String multiverseName = nameField.getText();
        String datingSystemValue = datingSystem.getValue();

        if (multiverseName == null || multiverseName.isEmpty()) {
            System.out.println("Multiverse name is empty");
            return;
        }

        // Set multiverse properties
        multiverse.setMultiverseName(multiverseName);
        multiverse.setDatingSystem(datingSystemValue);

        if ("Relative".equals(datingSystemValue)) {
            String beforeEra = beforeField.getText();
            String duringEra = duringField.getText();
            String afterEra = afterField.getText();
            multiverse.setRelativeEras(beforeEra, duringEra, afterEra);
        }

        // Show directory chooser and create the multiverse
        multiverse.showDirectoryChooser();
        multiverse.createMultiverse();

        // Open Multiverse Display window first
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/multiverse-display-view.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);

            // Optionally, pass data to the MultiverseDisplayController
            MultiverseDisplayController controller = loader.getController();
            controller.initializeWithMultiverse(multiverse);

            Stage newStage = new Stage();
            newStage.setTitle("Multiverse Display");
            newStage.setScene(scene);
            newStage.setMaximized(true); // Set the new stage to full screen
            newStage.show();

            // Close the menu-view.fxml stage
            Stage currentStage = (Stage) nameField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open Universe Creator window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/universe-create-view.fxml"));
            Scene scene = new Scene(loader.load(), 300, 200);

            Stage newStage = new Stage();
            newStage.setTitle("Create New Universe");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
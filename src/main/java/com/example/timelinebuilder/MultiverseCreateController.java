package com.example.timelinebuilder;

import com.example.file.MultiverseCreate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
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
    private MultiverseCreate multiverseCreator;

    public void setMenuStage(Stage menuStage) {
        this.menuStage = menuStage;
    }

    @FXML
    public void initialize() {
        // Initialize the MultiverseCreate instance
        multiverseCreator = new MultiverseCreate();

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

        // Check if root path was selected
        if (multiverseCreator.getRootPath() == null) {
            System.out.println("No directory selected");
            return;
        }

        // Set multiverse properties
        multiverseCreator.setMultiverseName(multiverseName);
        multiverseCreator.setDatingSystem(datingSystemValue);

        if ("Relative".equals(datingSystemValue)) {
            String beforeEra = beforeField.getText();
            String duringEra = duringField.getText();
            String afterEra = afterField.getText();
            multiverseCreator.setRelativeEras(beforeEra, duringEra, afterEra);
        }

        // Create the multiverse file
        multiverseCreator.createMultiverseFile();

        // Close the multiverse creation window
        Stage currentStage = (Stage) submitButton.getScene().getWindow();
        currentStage.close();

        // Close the menu window if it exists
        if (menuStage != null) {
            menuStage.close();
        }

        // Open the timeline view in a new window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("timeline-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);

            TimelineController controller = loader.getController();
            controller.setMultiversePaths(multiverseCreator.getMultiverseFolderPath());

            Stage timelineStage = new Stage();
            timelineStage.setTitle("Timeline Builder - " + multiverseName);
            timelineStage.setScene(scene);
            timelineStage.setMaximized(true);
            timelineStage.show();
        } catch (IOException e) {
            System.out.println("Error loading timeline view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

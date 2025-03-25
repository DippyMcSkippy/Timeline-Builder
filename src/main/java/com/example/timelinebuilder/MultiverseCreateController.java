package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.example.file.MultiverseCreate;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import com.example.timelinebuilder.MultiverseDisplayController;

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

    @FXML
    public void initialize() {
        datingSystem.getItems().addAll("Numeral", "Relative");

        datingSystem.setOnAction(event -> {
            boolean isRelative = "Relative".equals(datingSystem.getValue());
            relativeFields.setVisible(isRelative);
            relativeFields.setManaged(isRelative);
        });

        submitButton.setOnAction(event -> handleSubmit());
    }

    @FXML
    private void handleSubmit() {
        String name = nameField.getText();
        if (!name.isEmpty()) {
            MultiverseCreate multiverseCreate = new MultiverseCreate();
            multiverseCreate.setMultiverseName(name);
            multiverseCreate.setDatingSystem(datingSystem.getValue());

            if ("Relative".equals(datingSystem.getValue())) {
                multiverseCreate.setRelativeEras(
                        beforeField.getText(),
                        duringField.getText(),
                        afterField.getText()
                );
            }

            multiverseCreate.createMultiverseFile();

            // Open Multiverse Display window first
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/multiverse-display-view.fxml"));
                Scene scene = new Scene(loader.load(), 400, 300);

                // Optionally, pass data to the MultiverseDisplayController
                MultiverseDisplayController controller = loader.getController();
                // Pass any necessary data to the controller here

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

                // Get controller and set the universes folder path
                UniverseCreateController controller = loader.getController();
                controller.setUniversesFolderPath(multiverseCreate.getUniversesFolderPath());
                controller.setMultiverseCreate(multiverseCreate);  // Pass the MultiverseCreate instance

                Stage newStage = new Stage();
                newStage.setTitle("Create New Universe");
                newStage.setScene(scene);
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

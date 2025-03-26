package com.example.timelinebuilder;

import com.example.file.Multiverse;
import com.example.file.Universe;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class UniverseCreateController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Integer> priorityComboBox;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField hexColorField;

    @FXML
    private Button submitButton;

    private Multiverse multiverse;

    public void setMultiverse(Multiverse multiverse) {
        this.multiverse = multiverse;
    }

    @FXML
    public void initialize() {
        // Populate priority combo box
        priorityComboBox.getItems().addAll(1, 2, 3, 4, 5);

        // Bind color picker to hex color field
        colorPicker.setOnAction(event -> {
            Color color = colorPicker.getValue();
            hexColorField.setText(toHexString(color));
        });

        // Update color picker when hex field changes
        hexColorField.setOnAction(event -> {
            try {
                Color color = Color.web(hexColorField.getText());
                colorPicker.setValue(color);
            } catch (IllegalArgumentException e) {
                // Invalid hex color, ignore
            }
        });

        submitButton.setOnAction(event -> handleSubmit());
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void handleSubmit() {
        System.out.println("UCC handleSubmit: Submit button clicked");

        if (multiverse == null) {
            System.out.println("UCC handleSubmit: Multiverse is null");
            return;
        }

        String name = nameField.getText();
        int priority = priorityComboBox.getValue();
        String colorHex = hexColorField.getText();

        System.out.println("UCC handleSubmit: Name: " + name);
        System.out.println("UCC handleSubmit: Priority: " + priority);
        System.out.println("UCC handleSubmit: Color: " + colorHex);

        String universesFolderPath = multiverse.getUniversesFolderPath();
        System.out.println("UCC handleSubmit: Universes folder path: " + universesFolderPath);
        System.out.println("UCC handleSubmit: Multiverse CSV path: " + multiverse.getMultiverseCsvPath());

        if (!name.isEmpty() && universesFolderPath != null) {
            try {
                System.out.println("UCC handleSubmit: Creating universe...");
                Universe universe = new Universe(universesFolderPath);
                universe.setUniverseName(name);
                universe.setUniversePriority(priority);
                universe.setUniverseColor(colorHex);
                universe.createUniverse();
                System.out.println("UCC handleSubmit: Universe created successfully");

                System.out.println("UCC handleSubmit: Events folder: " + universe.getEventsFolder());

                try {
                    System.out.println("UCC handleSubmit: Loading event creation view...");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/event-create-view.fxml"));
                    Scene scene = new Scene(loader.load(), 500, 400);

                    EventCreateController controller = loader.getController();
                    controller.setEventsFolder(universe.getEventsFolder());

                    // Pass the multiverse object
                    controller.initializeWithMultiverse(multiverse);

                    Stage newStage = new Stage();
                    newStage.setTitle("Create New Event");
                    newStage.setScene(scene);
                    newStage.show();

                    System.out.println("UCC handleSubmit: Event creation view loaded successfully");
                } catch (IOException e) {
                    System.err.println("UCC handleSubmit: Error loading event creation view: " + e.getMessage());
                    e.printStackTrace();
                }

                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                System.err.println("UCC handleSubmit: Error in handleSubmit: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("UCC handleSubmit: Name is empty or universesFolderPath is null");
        }
    }
}
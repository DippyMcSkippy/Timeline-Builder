package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.file.UniverseCreate;
import com.example.file.MultiverseCreate;
import java.io.IOException;
import java.io.File;

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

    private String universesFolderPath;
    private MultiverseCreate multiverseCreate;

    public void setUniversesFolderPath(String path) {
        this.universesFolderPath = path;
    }

    public void setMultiverseCreate(MultiverseCreate multiverseCreate) {
        this.multiverseCreate = multiverseCreate;
    }

    @FXML
    public void initialize() {
        // Populate the priority dropdown with values 0-9
        for (int i = 0; i <= 9; i++) {
            priorityComboBox.getItems().add(i);
        }

        // Set default value to 0 (highest priority)
        priorityComboBox.setValue(0);

        // Set default color
        colorPicker.setValue(Color.BLUE);
        hexColorField.setText(toHexString(Color.BLUE));

        // Update hex field when color picker changes
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
        System.out.println("Submit button clicked");

        String name = nameField.getText();
        int priority = priorityComboBox.getValue();
        String colorHex = hexColorField.getText();

        System.out.println("Name: " + name);
        System.out.println("Priority: " + priority);
        System.out.println("Color: " + colorHex);
        System.out.println("Universes folder path: " + universesFolderPath);

        if (multiverseCreate != null) {
            System.out.println("MultiverseCreate is not null");
            System.out.println("Multiverse CSV path: " + multiverseCreate.getMultiverseCsvPath());
        } else {
            System.out.println("MultiverseCreate is null");
        }

        if (!name.isEmpty() && universesFolderPath != null) {
            try {
                System.out.println("Creating universe...");
                UniverseCreate universeCreate = new UniverseCreate(universesFolderPath);
                universeCreate.setUniverseName(name);
                universeCreate.setUniversePriority(priority);
                universeCreate.setUniverseColor(colorHex);
                universeCreate.createUniverseFile();
                System.out.println("Universe created successfully");

                System.out.println("Events folder: " + universeCreate.getEventsFolder());

                try {
                    System.out.println("Loading event creation view...");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/event-create-view.fxml"));
                    Scene scene = new Scene(loader.load(), 500, 400);

                    EventCreateController controller = loader.getController();
                    controller.setEventsFolder(universeCreate.getEventsFolder());

                    // Pass the multiverse CSV path from MultiverseCreate
                    if (multiverseCreate != null && multiverseCreate.getMultiverseCsvPath() != null) {
                        controller.setMultiverseCsvPath(multiverseCreate.getMultiverseCsvPath());
                    } else {
                        System.out.println("Warning: MultiverseCreate or its CSV path is null");
                    }

                    Stage newStage = new Stage();
                    newStage.setTitle("Create New Event");
                    newStage.setScene(scene);
                    newStage.show();

                    System.out.println("Event creation view loaded successfully");
                } catch (IOException e) {
                    System.err.println("Error loading event creation view: " + e.getMessage());
                    e.printStackTrace();
                }

                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                System.err.println("Error in handleSubmit: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Name is empty or universesFolderPath is null");
        }
    }

}
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
import com.example.file.Universe;
import com.example.file.Multiverse;
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

    private String universesFolderPath;
    private Multiverse multiverse;

    public void setMultiverse(Multiverse multiverse) {
        this.multiverse = multiverse;
        this.universesFolderPath = multiverse.getUniversesFolderPath(); // Initialize universesFolderPath
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
        System.out.println("UCC: Submit button clicked");

        String name = nameField.getText();
        int priority = priorityComboBox.getValue();
        String colorHex = hexColorField.getText();

        System.out.println("UCC: Name: " + name);
        System.out.println("UCC: Priority: " + priority);
        System.out.println("UCC: Color: " + colorHex);
        System.out.println("UCC: Universes folder path: " + universesFolderPath);

        if (multiverse != null) {
            System.out.println("UCC: Multiverse is not null");
            System.out.println("UCC: Multiverse CSV path: " + multiverse.getMultiverseCsvPath());
        } else {
            System.out.println("UCC: Multiverse is null");
        }

        if (!name.isEmpty() && universesFolderPath != null) {
            try {
                System.out.println("UCC: Creating universe...");
                Universe universe = new Universe(universesFolderPath);
                universe.setUniverseName(name);
                universe.setUniversePriority(priority);
                universe.setUniverseColor(colorHex);
                universe.createUniverse();
                System.out.println("UCC: Universe created successfully");

                System.out.println("UCC: Events folder: " + universe.getEventsFolder());

                try {
                    System.out.println("UCC: Loading event creation view...");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/event-create-view.fxml"));
                    Scene scene = new Scene(loader.load(), 500, 400);

                    EventCreateController controller = loader.getController();
                    controller.setEventsFolder(universe.getEventsFolder());

                    // Pass the multiverse CSV path from Multiverse
                    if (multiverse != null && multiverse.getMultiverseCsvPath() != null) {
                        controller.setMultiverseCsvPath(multiverse.getMultiverseCsvPath());
                    } else {
                        System.out.println("UCC: Warning: Multiverse or its CSV path is null");
                    }

                    Stage newStage = new Stage();
                    newStage.setTitle("UCC: Create New Event");
                    newStage.setScene(scene);
                    newStage.show();

                    System.out.println("UCC: Event creation view loaded successfully");
                } catch (IOException e) {
                    System.err.println("UCC: Error loading event creation view: " + e.getMessage());
                    e.printStackTrace();
                }

                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                System.err.println("UCC: Error in handleSubmit: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("UCC: Name is empty or universesFolderPath is null");
        }
    }
}
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
        String name = nameField.getText();
        int priority = priorityComboBox.getValue();
        String colorHex = hexColorField.getText();

        if (!name.isEmpty() && universesFolderPath != null) {
            UniverseCreate universeCreate = new UniverseCreate(universesFolderPath);
            universeCreate.setUniverseName(name);
            universeCreate.setUniversePriority(priority);
            universeCreate.setUniverseColor(colorHex);
            universeCreate.createUniverseFile();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/event-create-view.fxml"));
                Scene scene = new Scene(loader.load(), 500, 400);

                EventCreateController controller = loader.getController();
                controller.setEventsFolder(universeCreate.getEventsFolder());

                // Pass the multiverse CSV path from MultiverseCreate
                controller.setMultiverseCsvPath(multiverseCreate.getMultiverseCsvPath());

                Stage newStage = new Stage();
                newStage.setTitle("Create New Event");
                newStage.setScene(scene);
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}
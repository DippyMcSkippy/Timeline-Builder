package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class CreateTimelineController {

    private FileManager fileManager;

    @FXML
    private TextField timelineFileNameField;

    @FXML
    private TextField primeUniverseNameField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> datingSystemDropdown;

    @FXML
    private VBox eventBasedFields;

    @FXML
    private TextField beforeNameField;

    @FXML
    private TextField duringNameField;

    @FXML
    private TextField afterNameField;

    @FXML
    private VBox startDateFields;

    @FXML
    private TextField startDateNameField;

    @FXML
    private Button saveButton;

    @FXML
    public void  initialize(){
        fileManager = new FileManager(new Stage());
        datingSystemDropdown.getItems().addAll("Event-Based Calendar","Start-Date Calendar");

        datingSystemDropdown.setOnAction(e -> updateCalendarFields());

        saveButton.setOnAction(e -> saveTimeline());
    }


    private void updateCalendarFields() {
        String selectedSystem = datingSystemDropdown.getValue();
        if("Event-Based Calendar".equals(selectedSystem)){
            eventBasedFields.setVisible(true);
            eventBasedFields.setManaged(true);
            startDateFields.setVisible(false);
            startDateFields.setManaged(false);
        } else if ("Start-Date Calendar".equals(selectedSystem)){
            eventBasedFields.setVisible(false);
            eventBasedFields.setManaged(false);
            startDateFields.setVisible(true);
            startDateFields.setManaged(true);
        }

    }

    private void saveTimeline() {
        // Collect data from fields
        String fileName = timelineFileNameField.getText();
        String universeName = primeUniverseNameField.getText();
        String color = colorPicker.getValue().toString();
        String datingSystem = datingSystemDropdown.getValue();

        String calendarDetails = "";
        if ("Event-Based Calendar".equals(datingSystem)) {
            calendarDetails = String.format(
                    "Before: %s, During: %s, After: %s",
                    beforeNameField.getText(),
                    duringNameField.getText(),
                    afterNameField.getText()
            );
        } else if ("Start-Date Calendar".equals(datingSystem)) {
            calendarDetails = "Name: " + startDateNameField.getText();
        }

        // Create Timeline object
        Timeline timeline = new Timeline(fileName, universeName, color, datingSystem, calendarDetails);

        // Write the object to a JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Create or overwrite the JSON file
            File file = new File("timeline.json");
            objectMapper.writeValue(file, timeline);
            System.out.println("Timeline saved to timeline.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



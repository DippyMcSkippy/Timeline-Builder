package com.example.timelinebuilder;

import com.example.file.MultiverseGet;
import com.opencsv.exceptions.CsvValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCreateController {
    @FXML
    private TextField eventNameField;

    @FXML
    private ComboBox<String> eventTypeComboBox;

    @FXML
    private VBox normalEventControls;

    @FXML
    private VBox numeralStartDateControls;

    @FXML
    private VBox relativeStartDateControls;

    @FXML
    private VBox numeralEndDateControls;

    @FXML
    private VBox relativeEndDateControls;

    @FXML
    private TextField startYearField;

    @FXML
    private TextField endYearField;

    @FXML
    private ComboBox<String> startEraComboBox;

    @FXML
    private ComboBox<String> endEraComboBox;

    @FXML
    private TextField startYearRelativeField;

    @FXML
    private TextField endYearRelativeField;

    @FXML
    private ComboBox<String> startMonthComboBox;

    @FXML
    private ComboBox<String> endMonthComboBox;

    @FXML
    private ComboBox<String> startDayComboBox;

    @FXML
    private ComboBox<String> endDayComboBox;

    @FXML
    private Button submitButton;

    private String eventsFolder;
    private String multiverseCsvPath;
    private MultiverseGet multiverseGet;

    public void setEventsFolder(String folder) {
        this.eventsFolder = folder;
    }

    public void setMultiverseCsvPath(String path) {
        this.multiverseCsvPath = path;
        multiverseGet = new MultiverseGet(multiverseCsvPath);
        setupDateControls();
    }

    private void setupDateControls() {
        if (multiverseGet.isNumeralDating()) {
            numeralStartDateControls.setVisible(true);
            numeralStartDateControls.setManaged(true);
            numeralEndDateControls.setVisible(true);
            numeralEndDateControls.setManaged(true);
        } else if (multiverseGet.isRelativeDating()) {
            relativeStartDateControls.setVisible(true);
            relativeStartDateControls.setManaged(true);
            relativeEndDateControls.setVisible(true);
            relativeEndDateControls.setManaged(true);

            // Populate era dropdowns
            List<String> eras = multiverseGet.getEras();
            startEraComboBox.getItems().addAll(eras);
            endEraComboBox.getItems().addAll(eras);

            if (!eras.isEmpty()) {
                startEraComboBox.setValue(eras.get(0));
                endEraComboBox.setValue(eras.get(0));
            }
        }

        // Setup month dropdowns
        List<String> months = Arrays.asList(
                "Unspecified", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        startMonthComboBox.getItems().addAll(months);
        endMonthComboBox.getItems().addAll(months);
        startMonthComboBox.setValue("Unspecified");
        endMonthComboBox.setValue("Unspecified");

        // Add listeners for month changes to update days
        startMonthComboBox.setOnAction(e -> updateDaysInMonth(startMonthComboBox, startDayComboBox, startYearField));
        endMonthComboBox.setOnAction(e -> updateDaysInMonth(endMonthComboBox, endDayComboBox, endYearField));

        // Initialize days to "Unspecified"
        startDayComboBox.getItems().add("Unspecified");
        endDayComboBox.getItems().add("Unspecified");
        startDayComboBox.setValue("Unspecified");
        endDayComboBox.setValue("Unspecified");
    }

    private void updateDaysInMonth(ComboBox<String> monthComboBox, ComboBox<String> dayComboBox, TextField yearField) {
        String selectedMonth = monthComboBox.getValue();
        dayComboBox.getItems().clear();
        dayComboBox.getItems().add("Unspecified");

        if ("Unspecified".equals(selectedMonth)) {
            dayComboBox.setValue("Unspecified");
            return;
        }

        int daysInMonth;
        switch (selectedMonth) {
            case "February":
                // Check for leap year
                int year;
                try {
                    year = Integer.parseInt(yearField.getText());
                } catch (NumberFormatException e) {
                    year = 0;
                }

                if (isLeapYear(year)) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }
                break;
            case "April":
            case "June":
            case "September":
            case "November":
                daysInMonth = 30;
                break;
            default:
                daysInMonth = 31;
                break;
        }

        for (int i = 1; i <= daysInMonth; i++) {
            dayComboBox.getItems().add(String.valueOf(i));
        }
        dayComboBox.setValue("1");
    }

    private boolean isLeapYear(int year) {
        // Complex leap year rule
        if (year % 1000 == 0) return true;
        if (year % 400 == 0) return false;
        return year % 4 == 0;
    }

    @FXML
    public void initialize() {
        // Populate event types
        eventTypeComboBox.getItems().addAll("Normal", "Incursion", "Nexus", "Travel");
        eventTypeComboBox.setValue("Normal");

        // Show/hide date controls based on event type
        eventTypeComboBox.setOnAction(e -> {
            boolean isNormal = "Normal".equals(eventTypeComboBox.getValue());
            normalEventControls.setVisible(isNormal);
            normalEventControls.setManaged(isNormal);
        });

        submitButton.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        String eventName = eventNameField.getText();
        String eventType = eventTypeComboBox.getValue();

        if (!eventName.isEmpty() && eventType != null) {
            // Create event logic will go here

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}
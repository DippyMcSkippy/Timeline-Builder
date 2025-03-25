package com.example.timelinebuilder;

import com.example.file.EventCreate;
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
        //System.out.println("ECC setEventsFolder: Events folder set to: " + folder);
    }

    public void setMultiverseCsvPath(String path) {
        this.multiverseCsvPath = path;
        //System.out.println("ECC setMultiverseCsvPath: Multiverse CSV path set to: " + path);
        multiverseGet = new MultiverseGet(multiverseCsvPath);
        setupDateControls();
    }

    private void setupDateControls() {
        //System.out.println("ECC setupDateControls: Setting up date controls");
        if (multiverseGet.isNumeralDating()) {
            numeralStartDateControls.setVisible(true);
            numeralStartDateControls.setManaged(true);
            numeralEndDateControls.setVisible(true);
            numeralEndDateControls.setManaged(true);
            //System.out.println("ECC setupDateControls: Using numeral dating");
        } else if (multiverseGet.isRelativeDating()) {
            relativeStartDateControls.setVisible(true);
            relativeStartDateControls.setManaged(true);
            relativeEndDateControls.setVisible(true);
            relativeEndDateControls.setManaged(true);
            //System.out.println("ECC setupDateControls: Using relative dating");

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

        // Show normal event controls by default
        normalEventControls.setVisible(true);
        normalEventControls.setManaged(true);

        // Show/hide date controls based on event type
        eventTypeComboBox.setOnAction(e -> {
            boolean isNormal = "Normal".equals(eventTypeComboBox.getValue());
            normalEventControls.setVisible(isNormal);
            normalEventControls.setManaged(isNormal);
        });

        submitButton.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        //System.out.println("ECC handleSubmit: Submit button clicked");

        String eventName = eventNameField.getText();
        String eventType = eventTypeComboBox.getValue();

        if (!eventName.isEmpty() && eventType != null) {
            EventCreate eventCreate = new EventCreate(eventsFolder);
            eventCreate.setEventName(eventName);
            eventCreate.setEventType(eventType);

            if ("Normal".equals(eventType)) {
                // Get start date values
                String startYear = multiverseGet.isRelativeDating() ?
                        startYearRelativeField.getText() : startYearField.getText();
                String startMonth = startMonthComboBox.getValue();
                String startDay = startDayComboBox.getValue();
                String startEra = multiverseGet.isRelativeDating() ?
                        startEraComboBox.getValue() : null;

                // Get end date values
                String endYear = multiverseGet.isRelativeDating() ?
                        endYearRelativeField.getText() : endYearField.getText();
                String endMonth = endMonthComboBox.getValue();
                String endDay = endDayComboBox.getValue();
                String endEra = multiverseGet.isRelativeDating() ?
                        endEraComboBox.getValue() : null;

                // Set dates in event create
                eventCreate.setStartDate(startYear, startMonth, startDay, startEra);
                eventCreate.setEndDate(endYear, endMonth, endDay, endEra);
            }
            // Add other event type handling here when implemented

            // Create the event file
            eventCreate.createEventFile();
            //System.out.println("ECC handleSubmit: Event created successfully");

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        } else {
            //System.out.println("ECC handleSubmit: Event name or type is empty");
        }
    }
}
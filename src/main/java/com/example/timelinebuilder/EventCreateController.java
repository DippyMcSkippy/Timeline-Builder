package com.example.timelinebuilder;

import com.example.file.Event;
import com.example.file.Multiverse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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
    private ComboBox<String> universeComboBox; // ComboBox for universes

    @FXML
    private Button submitButton;

    private String eventsFolder;
    private Multiverse multiverse;

    public void setEventsFolder(String folder) {
        this.eventsFolder = folder;
        System.out.println("ECC setEventsFolder: Events folder set to: " + folder);
    }

    public void initializeWithMultiverse(Multiverse multiverse) {
        this.multiverse = multiverse;
        setupDateControls();
        setupUniverses(); // Setup universes after setting multiverse
    }

    private void setupDateControls() {
        System.out.println("ECC setupDateControls: Setting up date controls");
        if (multiverse.isNumeralDating()) {
            numeralStartDateControls.setVisible(true);
            numeralStartDateControls.setManaged(true);
            numeralEndDateControls.setVisible(true);
            numeralEndDateControls.setManaged(true);
            System.out.println("ECC setupDateControls: Using numeral dating");
        } else if (multiverse.isRelativeDating()) {
            relativeStartDateControls.setVisible(true);
            relativeStartDateControls.setManaged(true);
            relativeEndDateControls.setVisible(true);
            relativeEndDateControls.setManaged(true);
            System.out.println("ECC setupDateControls: Using relative dating");

            // Populate era dropdowns
            List<String> eras = multiverse.getEras();
            startEraComboBox.getItems().addAll(eras);
            endEraComboBox.getItems().addAll(eras);

            if (!eras.isEmpty()) {
                startEraComboBox.setValue(eras.get(0));
                endEraComboBox.setValue(eras.get(0));
            }
        }

        // Setup month dropdowns
        List<String> months = List.of(
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

    private void setupUniverses() {
        System.out.println("ECC setupUniverses: Setting up universes");
        List<String> universes = multiverse.getUniverses();
        universeComboBox.getItems().addAll(universes);
        if (!universes.isEmpty()) {
            universeComboBox.setValue(universes.get(0));
        }
    }

    private void openMultiverseDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/multiverse-display-view.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);

            // Pass data to the MultiverseDisplayController
            MultiverseDisplayController controller = loader.getController();
            controller.initializeWithMultiverse(multiverse);

            Stage newStage = new Stage();
            newStage.setTitle("Multiverse Display");
            newStage.setScene(scene);
            newStage.setMaximized(true); // Set the new stage to full screen
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println("ECC handleSubmit: Submit button clicked");

        String eventName = eventNameField.getText();
        String eventType = eventTypeComboBox.getValue();
        String selectedUniverse = universeComboBox.getValue(); // Get selected universe

        if (!eventName.isEmpty() && eventType != null && selectedUniverse != null) {
            Event event = new Event(eventsFolder);
            event.setEventName(eventName);
            event.setEventType(eventType);
            event.setUniverseName(selectedUniverse); // Set the universe name in event create

            if ("Normal".equals(eventType)) {
                // Get start date values
                String startYear = multiverse.isRelativeDating() ?
                        startYearRelativeField.getText() : startYearField.getText();
                String startMonth = startMonthComboBox.getValue();
                String startDay = startDayComboBox.getValue();
                String startEra = multiverse.isRelativeDating() ?
                        startEraComboBox.getValue() : null;

                // Get end date values
                String endYear = multiverse.isRelativeDating() ?
                        endYearRelativeField.getText() : endYearField.getText();
                String endMonth = endMonthComboBox.getValue();
                String endDay = endDayComboBox.getValue();
                String endEra = multiverse.isRelativeDating() ?
                        endEraComboBox.getValue() : null;

                // Set dates in event create
                event.setStartDate(startYear, startMonth, startDay, startEra);
                event.setEndDate(endYear, endMonth, endDay, endEra);
            }
            // Add other event type handling here when implemented

            // Create the event file
            event.createEventFile();
            System.out.println("ECC handleSubmit: Event created successfully");

            openMultiverseDisplay(); // Open the multiverse display before closing the current stage

            // Close the current stage
            Stage currentStage = (Stage) submitButton.getScene().getWindow();
            currentStage.close();
        } else {
            System.out.println("ECC handleSubmit: Event name, type, or universe is empty");
        }
    }
}
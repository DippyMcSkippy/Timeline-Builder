package com.example.timelinebuilder;

import com.example.file.Event;
import com.example.file.Multiverse;
import com.example.config.GlobalConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
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
            System.out.println("ECC setupDateControls: Using numeral dating - numeralStartDateControls and numeralEndDateControls set to visible and managed");
        } else if (multiverse.isRelativeDating()) {
            relativeStartDateControls.setVisible(true);
            relativeStartDateControls.setManaged(true);
            relativeEndDateControls.setVisible(true);
            relativeEndDateControls.setManaged(true);
            System.out.println("ECC setupDateControls: Using relative dating - relativeStartDateControls and relativeEndDateControls set to visible and managed");

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
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        startMonthComboBox.getItems().addAll(months);
        endMonthComboBox.getItems().addAll(months);
        startMonthComboBox.setValue("January");
        endMonthComboBox.setValue("January");

        // Add listeners for month changes to update days
        startMonthComboBox.setOnAction(e -> updateDaysInMonth(startMonthComboBox, startDayComboBox, startYearField));
        endMonthComboBox.setOnAction(e -> updateDaysInMonth(endMonthComboBox, endDayComboBox, endYearField));

        // Initialize days to "1"
        for (int i = 1; i <= 31; i++) {
            startDayComboBox.getItems().add(String.valueOf(i));
            endDayComboBox.getItems().add(String.valueOf(i));
        }
        startDayComboBox.setValue("1");
        endDayComboBox.setValue("1");
    }

    private void updateDaysInMonth(ComboBox<String> monthComboBox, ComboBox<String> dayComboBox, TextField yearField) {
        String selectedMonth = monthComboBox.getValue();
        dayComboBox.getItems().clear();

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
        var leap = year % 4 == 0;
        if (year % 100 == 0) {
            leap = false;
        }
        if (year % 400 == 0) {
            leap = true;
        }
        return leap;
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
            newStage.setMaximized(true);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            System.out.println("ECC handleSubmit: Creating event in folder - " + eventsFolder);

            String startYear = multiverse.isRelativeDating() ? startYearRelativeField.getText() : startYearField.getText();
            String startMonth = startMonthComboBox.getValue();
            String startDay = startDayComboBox.getValue();
            String startEra = multiverse.isRelativeDating() ? startEraComboBox.getValue() : null;

            String endYear = multiverse.isRelativeDating() ? endYearRelativeField.getText() : endYearField.getText();
            String endMonth = endMonthComboBox.getValue();
            String endDay = endDayComboBox.getValue();
            String endEra = multiverse.isRelativeDating() ? endEraComboBox.getValue() : null;

            // Ensure year fields are not empty
            if (startYear.isEmpty() || endYear.isEmpty()) {
                System.out.println("ECC handleSubmit: Year fields are empty");
                return;
            }

            // Set dates in event create
            event.setStartDate(startYear, startMonth, startDay, startEra);
            event.setEndDate(endYear, endMonth, endDay, endEra);

            // Create the event file
            event.createEventFile();
            System.out.println("ECC handleSubmit: Event created successfully");

            // Calculate the size (difference between start and end dates in days)
            int size = calculateSize(startYear, startMonth, startDay, endYear, endMonth, endDay);

            // Add event to the global linked list for the universe in chronological order
            addEventToUniverseLinkedList(eventName, startYear, startMonth, startDay, endYear, endMonth, endDay, size, selectedUniverse);

            openMultiverseDisplay(); // Open the multiverse display before closing the current stage

            // Close the current stage
            Stage currentStage = (Stage) submitButton.getScene().getWindow();
            currentStage.close();
        } else {
            System.out.println("ECC handleSubmit: Event name, type, or universe is empty");
        }
    }

    private void addEventToUniverseLinkedList(String eventName, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay, int size, String universeName) {
        LinkedList<String[]> eventLinkedList = GlobalConfig.getEventLinkedList(universeName);

        if (eventLinkedList == null) {
            eventLinkedList = new LinkedList<>();
            GlobalConfig.eventLinkedLists.put(universeName + "eventLinkedList", eventLinkedList);
        }

        String[] newEvent = {eventName, startYear, startMonth, startDay, endYear, endMonth, endDay, String.valueOf(size)};
        int index = 0;
        for (String[] event : eventLinkedList) {
            LocalDate eventEndDate = LocalDate.of(Integer.parseInt(event[4]), convertMonthToNumber(event[5]), Integer.parseInt(event[6]));
            LocalDate newEventStartDate = LocalDate.of(Integer.parseInt(startYear), convertMonthToNumber(startMonth), Integer.parseInt(startDay));

            if (eventEndDate.isBefore(newEventStartDate) || eventEndDate.isEqual(newEventStartDate)) {
                break;
            }
            index++;
        }
        eventLinkedList.add(index, newEvent);
    }

    private int calculateSize(String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) {
        LocalDate startDate = LocalDate.of(Integer.parseInt(startYear), convertMonthToNumber(startMonth), Integer.parseInt(startDay));
        LocalDate endDate = LocalDate.of(Integer.parseInt(endYear), convertMonthToNumber(endMonth), Integer.parseInt(endDay));
        return calculateDaysBetween(startDate, endDate);
    }

    private int calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        int daysBetween = 0;
        while (!startDate.isAfter(endDate)) {
            daysBetween++;
            startDate = startDate.plusDays(1);
        }
        return daysBetween;
    }

    private int convertMonthToNumber(String month) {
        switch (month) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return 1; // Default to January if unspecified
        }
    }

    private int compareDates(String year1, String month1, String day1, String year2, String month2, String day2) {
        LocalDate date1 = LocalDate.of(Integer.parseInt(year1), convertMonthToNumber(month1), Integer.parseInt(day1));
        LocalDate date2 = LocalDate.of(Integer.parseInt(year2), convertMonthToNumber(month2), Integer.parseInt(day2));
        return date1.compareTo(date2);
    }
}
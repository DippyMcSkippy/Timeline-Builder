package com.example.timelinebuilder;

import com.example.file.Multiverse;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MultiverseDisplayController {

    @FXML
    private Label multiverseNameLabel;

    @FXML
    private VBox universesContainer;

    private String multiverseFolderPath;
    private String multiverseCsvFilename;
    private String universesFolderPath;

    @FXML
    public void initialize() {
        // This method will be empty if we are initializing with Multiverse
    }

    public void initializeWithMultiverse(Multiverse multiverse) {
        // Get paths from Multiverse
        this.multiverseFolderPath = multiverse.getMultiverseFolderPath();
        this.multiverseCsvFilename = multiverse.getMultiverseCsvPath();
        this.universesFolderPath = multiverse.getUniversesFolderPath();

        //System.out.println("MDC: Using folder path: " + multiverseFolderPath);
        //System.out.println("MDC: Using CSV path: " + multiverseCsvFilename);
        //System.out.println("MDC: Using universes folder path: " + universesFolderPath);

        // Load multiverse data
        loadMultiverseData();

        // Set the multiverse name to the label
        String multiverseName = getMultiverseName();
        //System.out.println("MDC: Retrieved multiverse name: " + multiverseName);
        multiverseNameLabel.setText(multiverseName);

        // Display universe names and events
        displayUniverseNamesAndEvents();
    }

    private void loadMultiverseData() {
        //System.out.println("MDC: Loading multiverse data from CSV file: " + multiverseCsvFilename);
        File csvFile = new File(multiverseCsvFilename);
        if (!csvFile.exists()) {
            //System.out.println("MDC: CSV file does not exist: " + multiverseCsvFilename);
            return;
        }

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            try {
                while ((line = reader.readNext()) != null) {
                    if (line.length >= 2) {
                        //System.out.println("MDC: Processing line: " + String.join(", ", line));
                        if ("Multiverse Name".equals(line[0])) {
                            multiverseName = line[1];
                            //System.out.println("MDC: Multiverse Name found: " + multiverseName);
                        } else if ("Dating System".equals(line[0])) {
                            datingSystem = line[1];
                            //System.out.println("MDC: Dating System found: " + datingSystem);
                        } else if ("Before Era".equals(line[0]) ||
                                "During Era".equals(line[0]) ||
                                "After Era".equals(line[0])) {
                            eras.add(line[1]);
                            //System.out.println("MDC: Era added: " + line[1]);
                        }
                    }
                }
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            //System.out.println("MDC: Error loading multiverse data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String multiverseName;
    private String datingSystem;
    private List<String> eras = new ArrayList<>();

    public String getMultiverseName() {
        //System.out.println("MDC: Getting multiverse name: " + multiverseName);
        return multiverseName;
    }

    public String getDatingSystem() {
        //System.out.println("MDC: Getting dating system: " + datingSystem);
        return datingSystem;
    }

    public List<String> getEras() {
        //System.out.println("MDC: Getting eras: " + eras);
        return eras;
    }

    public boolean isRelativeDating() {
        //System.out.println("MDC: Checking if dating system is Relative: " + "Relative".equals(datingSystem));
        return "Relative".equals(datingSystem);
    }

    public boolean isNumeralDating() {
        //System.out.println("MDC: Checking if dating system is Numeral: " + "Numeral".equals(datingSystem));
        return "Numeral".equals(datingSystem);
    }

    private void displayUniverseNamesAndEvents() {
        File universesDirectory = new File(universesFolderPath);
        File[] universeFiles = universesDirectory.listFiles();

        if (universeFiles != null) {
            for (File universeFile : universeFiles) {
                if (universeFile.isDirectory()) {
                    String universeName = universeFile.getName();
                    String universeCsvPath = universeFile.getPath() + File.separator + universeName + ".csv";
                    String universeColor = getUniverseColorFromCsv(universeCsvPath);

                    if (universeColor != null) {
                        VBox universeNameBox = new VBox();
                        universeNameBox.setPadding(new Insets(10));
                        universeNameBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                        universeNameBox.setAlignment(Pos.CENTER); // Center align the text

                        Text universeNameText = new Text(universeName);
                        universeNameText.setFill(Color.web(universeColor));
                        universeNameText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                        universeNameBox.getChildren().add(universeNameText);

                        universesContainer.getChildren().add(universeNameBox);

                        List<Event> events = getEventsForUniverse(universeFile.getPath() + File.separator + "Events");
                        Collections.sort(events, Comparator.comparing(Event::getStartYear));

                        for (Event event : events) {
                            VBox eventBox = new VBox();
                            eventBox.setSpacing(5);
                            eventBox.setPadding(new Insets(5));
                            eventBox.setBackground(new Background(new BackgroundFill(Color.web(universeColor), new CornerRadii(5), Insets.EMPTY)));
                            eventBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
                            eventBox.setAlignment(Pos.CENTER); // Center align the text

                            Text eventNameText = new Text(event.getName());
                            eventNameText.setFill(Color.WHITE);
                            eventNameText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                            eventBox.getChildren().add(eventNameText);

                            Text eventDateText = new Text(event.getDateRange());
                            eventDateText.setFill(Color.WHITE);
                            eventDateText.setStyle("-fx-font-size: 12px;");
                            eventBox.getChildren().add(eventDateText);

                            universesContainer.getChildren().add(eventBox);
                        }
                    }
                }
            }
        }
    }

    private List<Event> getEventsForUniverse(String eventsFolderPath) {
        List<Event> events = new ArrayList<>();
        File eventsDirectory = new File(eventsFolderPath);
        File[] eventFiles = eventsDirectory.listFiles();

        if (eventFiles != null) {
            for (File eventFile : eventFiles) {
                if (eventFile.isFile() && eventFile.getName().endsWith(".csv")) {
                    try (CSVReader reader = new CSVReader(new FileReader(eventFile))) {
                        String[] line;
                        Event event = new Event();
                        while ((line = reader.readNext()) != null) {
                            if (line.length >= 2) {
                                switch (line[0]) {
                                    case "Event Name":
                                        event.setName(line[1]);
                                        //System.out.println("Event Name: " + line[1]);
                                        break;
                                    case "Start Year":
                                    case "Start Month":
                                    case "Start Day":
                                    case "End Year":
                                    case "End Month":
                                    case "End Day":
                                        if (!line[1].equalsIgnoreCase("Unspecified")) {
                                            updateEventDate(event, line[0], line[1]);
                                        }
                                        break;
                                }
                            }
                        }
                        events.add(event);
                    } catch (IOException | CsvValidationException e) {
                        //System.out.println("MDC: Error reading event CSV file: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

        return events;
    }

    private void updateEventDate(Event event, String dateType, String value) {
        switch (dateType) {
            case "Start Year":
                event.setStartYear(value);
                break;
            case "Start Month":
                event.setStartMonth(value);
                break;
            case "Start Day":
                event.setStartDay(value);
                break;
            case "End Year":
                event.setEndYear(value);
                break;
            case "End Month":
                event.setEndMonth(value);
                break;
            case "End Day":
                event.setEndDay(value);
                break;
        }
        //System.out.println(dateType + ": " + value);
    }

    private String getUniverseColorFromCsv(String csvPath) {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 2 && "Color".equals(line[0])) {
                    return line[1];
                }
            }
        } catch (IOException | CsvValidationException e) {
            //System.out.println("MDC: Error reading universe CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static class Event {
        private String name;
        private String startYear;
        private String startMonth;
        private String startDay;
        private String endYear;
        private String endMonth;
        private String endDay;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStartYear(String startYear) {
            this.startYear = startYear;
        }

        public void setStartMonth(String startMonth) {
            this.startMonth = startMonth;
        }

        public void setStartDay(String startDay) {
            this.startDay = startDay;
        }

        public void setEndYear(String endYear) {
            this.endYear = endYear;
        }

        public void setEndMonth(String endMonth) {
            this.endMonth = endMonth;
        }

        public void setEndDay(String endDay) {
            this.endDay = endDay;
        }

        public Integer getStartYear() {
            return startYear != null ? Integer.parseInt(startYear) : Integer.MAX_VALUE;
        }

        public String getDateRange() {
            StringBuilder dateRange = new StringBuilder();
            if (startYear != null) {
                dateRange.append(startYear);
                if (startMonth != null) {
                    dateRange.append(" ").append(startMonth);
                    if (startDay != null) {
                        dateRange.append(" ").append(startDay);
                    }
                }
            }
            dateRange.append(" - ");
            if (endYear != null) {
                dateRange.append(endYear);
                if (endMonth != null) {
                    dateRange.append(" ").append(endMonth);
                    if (endDay != null) {
                        dateRange.append(" ").append(endDay);
                    }
                }
            }
            return dateRange.toString();
        }
    }
}
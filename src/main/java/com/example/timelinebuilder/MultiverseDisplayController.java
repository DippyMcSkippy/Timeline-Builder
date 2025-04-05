package com.example.timelinebuilder;

import com.example.file.Multiverse;
import com.example.config.GlobalConfig;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultiverseDisplayController {

    @FXML
    private Label multiverseNameLabel;

    @FXML
    private HBox universesContainer;

    @FXML
    private Button createUniverseButton;

    @FXML
    private Button createEventButton;

    private Multiverse multiverse;

    @FXML
    public void initialize() {
        // This method will be empty if we are initializing with Multiverse
        createUniverseButton.setOnAction(e -> openUniverseCreateScreen());
        createEventButton.setOnAction(e -> openEventCreateScreen());
    }

    public void initializeWithMultiverse(Multiverse multiverse) {
        this.multiverse = multiverse;

        // Load multiverse data
        loadMultiverseData();

        // Set the multiverse name to the label
        String multiverseName = multiverse.getMultiverseName();
        multiverseNameLabel.setText(multiverseName);

        // Display universe names and events
        displayUniverseNamesAndEvents();
    }

    private void openUniverseCreateScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/universe-create-view.fxml"));
            Scene scene = new Scene(loader.load(), 500, 400);

            UniverseCreateController controller = loader.getController();
            controller.setMultiverse(multiverse); // Properly pass the multiverse object

            Stage newStage = new Stage();
            newStage.setTitle("Create New Universe");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEventCreateScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/event-create-view.fxml"));
            Scene scene = new Scene(loader.load(), 500, 400);

            EventCreateController controller = loader.getController();
            controller.setEventsFolder(multiverse.getUniversesFolderPath() + File.separator + "Events");
            controller.initializeWithMultiverse(multiverse); // Properly pass the multiverse object

            Stage newStage = new Stage();
            newStage.setTitle("Create New Event");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMultiverseData() {
        File csvFile = new File(multiverse.getMultiverseCsvPath());
        if (!csvFile.exists()) {
            return;
        }

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            try {
                while ((line = reader.readNext()) != null) {
                    if (line.length >= 2) {
                        if ("Multiverse Name".equals(line[0])) {
                            multiverseName = line[1];
                        } else if ("Dating System".equals(line[0])) {
                            datingSystem = line[1];
                        } else if ("Before Era".equals(line[0]) ||
                                "During Era".equals(line[0]) ||
                                "After Era".equals(line[0])) {
                            eras.add(line[1]);
                        }
                    }
                }
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String multiverseName;
    private String datingSystem;
    private List<String> eras = new ArrayList<>();

    public String getMultiverseName() {
        return multiverseName;
    }

    public String getDatingSystem() {
        return datingSystem;
    }

    public List<String> getEras() {
        return eras;
    }

    public boolean isRelativeDating() {
        return "Relative".equals(datingSystem);
    }

    public boolean isNumeralDating() {
        return "Numeral".equals(datingSystem);
    }

    private void displayUniverseNamesAndEvents() {
        universesContainer.getChildren().clear();

        for (String[] universe : GlobalConfig.universeLinkedList) {
            String universeName = universe[0];
            String universeCsvPath = multiverse.getUniversesFolderPath() + File.separator + universeName + File.separator + universeName + ".csv";
            String universeColor = getUniverseColorFromCsv(universeCsvPath);

            if (universeColor != null) {
                VBox universeBox = new VBox();
                universeBox.setSpacing(10);
                universeBox.setPadding(new Insets(10));
                universeBox.setBackground(new Background(new BackgroundFill(Color.web("#f0f0f0"), new CornerRadii(5), Insets.EMPTY)));
                universeBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));

                // Display the universe name
                Text universeNameText = new Text(universeName);
                universeNameText.setFill(Color.web(universeColor));
                universeNameText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                universeBox.getChildren().add(universeNameText);

                // Display the events for this universe
                LinkedList<String[]> events = GlobalConfig.getEventLinkedList(universeName);
                if (events != null) {
                    for (String[] event : events) {
                        VBox eventBox = new VBox();
                        eventBox.setSpacing(5);
                        eventBox.setPadding(new Insets(5));
                        eventBox.setBackground(new Background(new BackgroundFill(Color.web(universeColor), new CornerRadii(5), Insets.EMPTY)));
                        eventBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
                        eventBox.setAlignment(Pos.CENTER); // Center align the text

                        // Set the height of the event box based on the size parameter
                        int size = Integer.parseInt(event[7]);
                        eventBox.setPrefHeight(size * 10); // Adjust the multiplier as needed for visual clarity

                        Text eventNameText = new Text(event[0]);
                        eventNameText.setFill(Color.WHITE);
                        eventNameText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                        eventBox.getChildren().add(eventNameText);

                        Text eventDateText = new Text(event[1] + " " + event[2] + " " + event[3] + " - " + event[4] + " " + event[5] + " " + event[6]);
                        eventDateText.setFill(Color.WHITE);
                        eventDateText.setStyle("-fx-font-size: 12px;");
                        eventBox.getChildren().add(eventDateText);

                        universeBox.getChildren().add(eventBox);
                    }
                }

                universesContainer.getChildren().add(universeBox);
            }
        }
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
            e.printStackTrace();
        }
        return null;
    }
}
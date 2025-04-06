package com.example.timelinebuilder;

import com.example.file.Multiverse;
import com.example.config.GlobalConfig;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;

public class MenuController {
    @FXML
    private Button createMultiverseButton;

    @FXML
    private Button openMultiverseButton;

    @FXML
    private Button exitButton;

    @FXML
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        createMultiverseButton.setOnAction(e -> onCreateMultiverse());
        openMultiverseButton.setOnAction(e -> handleOpenMultiverse());
        exitButton.setOnAction(e -> onExit());
    }

    @FXML
    private void handleOpenMultiverse() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Multiverse Folder");
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String multiverseFolderPath = selectedDirectory.getAbsolutePath();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/multiverse-display-view.fxml"));
                Scene scene = new Scene(loader.load(), 800, 600);
                MultiverseDisplayController controller = loader.getController();
                Multiverse multiverse = new Multiverse();
                multiverse.setRootPath(selectedDirectory.getParent());
                multiverse.setMultiverseName(selectedDirectory.getName());

                initializeUniversesAndEvents(multiverseFolderPath + File.separator + "Universes");

                controller.initializeWithMultiverse(multiverse);

                stage.setScene(scene);
                stage.setMaximized(true); // Ensure the stage is maximized
                Platform.runLater(() -> stage.setMaximized(true)); // Always maximize the stage
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeUniversesAndEvents(String universesFolderPath) {
        File universesFolder = new File(universesFolderPath);
        File[] universeFolders = universesFolder.listFiles(File::isDirectory);

        if (universeFolders != null) {
            for (File universeFolder : universeFolders) {
                String universeName = universeFolder.getName();
                if ("Events".equals(universeName)) {
                    continue;
                }
                String universeCsvPath = universeFolder.getAbsolutePath() + File.separator + universeName + ".csv";

                addUniverseToLinkedList(universeName, universeCsvPath);

                String eventsFolderPath = universeFolder.getAbsolutePath() + File.separator + "Events";
                addEventsToLinkedList(universeName, eventsFolderPath);
            }
        }
    }

    private void addUniverseToLinkedList(String universeName, String universeCsvPath) {
        try (CSVReader reader = new CSVReader(new FileReader(universeCsvPath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 2 && "Priority".equals(line[0])) {
                    int priority = Integer.parseInt(line[1]);
                    GlobalConfig.addUniverse(universeName, priority);
                    break;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void addEventsToLinkedList(String universeName, String eventsFolderPath) {
        File eventsDirectory = new File(eventsFolderPath);
        File[] eventFiles = eventsDirectory.listFiles();

        if (eventFiles != null) {
            for (File eventFile : eventFiles) {
                if (eventFile.isFile() && eventFile.getName().endsWith(".csv")) {
                    try (CSVReader reader = new CSVReader(new FileReader(eventFile))) {
                        String[] line;
                        String eventName = null;
                        String startYear = null;
                        String startMonth = "January";
                        String startDay = "1";
                        String endYear = null;
                        String endMonth = "January";
                        String endDay = "1";
                        int size = 0;

                        while ((line = reader.readNext()) != null) {
                            if (line.length >= 2) {
                                switch (line[0]) {
                                    case "Event Name":
                                        eventName = line[1];
                                        break;
                                    case "Start Year":
                                        startYear = line[1];
                                        break;
                                    case "Start Month":
                                        startMonth = line[1];
                                        break;
                                    case "Start Day":
                                        startDay = line[1];
                                        break;
                                    case "End Year":
                                        endYear = line[1];
                                        break;
                                    case "End Month":
                                        endMonth = line[1];
                                        break;
                                    case "End Day":
                                        endDay = line[1];
                                        break;
                                }
                            }
                        }

                        if (startYear != null && endYear != null) {
                            size = calculateSize(startYear, startMonth, startDay, endYear, endMonth, endDay);
                            String[] newEvent = {eventName, startYear, startMonth, startDay, endYear, endMonth, endDay, String.valueOf(size)};
                            LinkedList<String[]> eventLinkedList = GlobalConfig.getEventLinkedList(universeName);
                            if (eventLinkedList == null) {
                                eventLinkedList = new LinkedList<>();
                                GlobalConfig.eventLinkedLists.put(universeName + "eventLinkedList", eventLinkedList);
                            }
                            insertEventInOrder(eventLinkedList, newEvent);
                        }
                    } catch (IOException | CsvValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void insertEventInOrder(LinkedList<String[]> eventLinkedList, String[] newEvent) {
        LocalDate newEventStartDate = LocalDate.of(Integer.parseInt(newEvent[1]), convertMonthToNumber(newEvent[2]), Integer.parseInt(newEvent[3]));
        int index = 0;
        for (String[] event : eventLinkedList) {
            LocalDate eventStartDate = LocalDate.of(Integer.parseInt(event[1]), convertMonthToNumber(event[2]), Integer.parseInt(event[3]));
            if (newEventStartDate.isAfter(eventStartDate)) {
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
                return 1;
        }
    }

    private void onCreateMultiverse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timelinebuilder/multiverse-create-view.fxml"));
            Scene scene = new Scene(loader.load(), 300, 200);
            Stage newStage = new Stage();
            newStage.setTitle("Create New Multiverse");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) createMultiverseButton.getScene().getWindow();
        stage.close();
    }

    private void onExit() {
        System.exit(0);
    }
}
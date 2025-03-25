package com.example.timelinebuilder;

import com.example.file.Multiverse;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MultiverseDisplayController {

    @FXML
    private Label multiverseNameLabel;

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

        System.out.println("MDC: Using folder path: " + multiverseFolderPath);
        System.out.println("MDC: Using CSV path: " + multiverseCsvFilename);
        System.out.println("MDC: Using universes folder path: " + universesFolderPath);

        // Load multiverse data
        loadMultiverseData();

        // Set the multiverse name to the label
        String multiverseName = getMultiverseName();
        System.out.println("MDC: Retrieved multiverse name: " + multiverseName);
        multiverseNameLabel.setText(multiverseName);
    }

    private void loadMultiverseData() {
        System.out.println("MDC: Loading multiverse data from CSV file: " + multiverseCsvFilename);
        try (CSVReader reader = new CSVReader(new FileReader(multiverseCsvFilename))) {
            String[] line;
            try {
                while ((line = reader.readNext()) != null) {
                    if (line.length >= 2) {
                        System.out.println("MDC: Processing line: " + String.join(", ", line));
                        if ("Multiverse Name".equals(line[0])) {
                            multiverseName = line[1];
                            System.out.println("MDC: Multiverse Name found: " + multiverseName);
                        } else if ("Dating System".equals(line[0])) {
                            datingSystem = line[1];
                            System.out.println("MDC: Dating System found: " + datingSystem);
                        } else if ("Before Era".equals(line[0]) ||
                                "During Era".equals(line[0]) ||
                                "After Era".equals(line[0])) {
                            eras.add(line[1]);
                            System.out.println("MDC: Era added: " + line[1]);
                        }
                    }
                }
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("MDC: Error loading multiverse data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String multiverseName;
    private String datingSystem;
    private List<String> eras = new ArrayList<>();

    public String getMultiverseName() {
        System.out.println("MDC: Getting multiverse name: " + multiverseName);
        return multiverseName;
    }

    public String getDatingSystem() {
        System.out.println("MDC: Getting dating system: " + datingSystem);
        return datingSystem;
    }

    public List<String> getEras() {
        System.out.println("MDC: Getting eras: " + eras);
        return eras;
    }

    public boolean isRelativeDating() {
        System.out.println("MDC: Checking if dating system is Relative: " + "Relative".equals(datingSystem));
        return "Relative".equals(datingSystem);
    }

    public boolean isNumeralDating() {
        System.out.println("MDC: Checking if dating system is Numeral: " + "Numeral".equals(datingSystem));
        return "Numeral".equals(datingSystem);
    }
}
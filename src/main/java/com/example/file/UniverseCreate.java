package com.example.file;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UniverseCreate {
    private String universeName;
    private int universePriority;
    private String universeColor;
    private String universesFolderPath;
    private String universeFolderPath;
    private String universeCsvPath;
    private String eventsFolder;

    // Constructor
    public UniverseCreate(String universesFolderPath) {
        this.universesFolderPath = universesFolderPath;
    }

    // Setter for name
    public void setUniverseName(String name) {
        this.universeName = name;
    }

    // Setter for priority
    public void setUniversePriority(int priority) {
        this.universePriority = priority;
    }

    // Setter for color
    public void setUniverseColor(String color) {
        this.universeColor = color;
    }

    // Getter for events folder
    public String getEventsFolder() {
        return eventsFolder;
    }

    // Update createUniverseFile method to include color and events folder
    public void createUniverseFile() {
        try {
            // Set up paths
            universeFolderPath = universesFolderPath + File.separator + universeName;
            universeCsvPath = universeFolderPath + File.separator + universeName + ".csv";
            eventsFolder = universeFolderPath + File.separator + "Events";

            // Create universe directory
            File directory = new File(universeFolderPath);
            directory.mkdir();

            // Create events directory
            File eventsDir = new File(eventsFolder);
            eventsDir.mkdir();

            // Create CSV file
            CSVWriter writer = new CSVWriter(new FileWriter(universeCsvPath));

            // Write universe configuration data
            String[] header = {"Property", "Value"};
            writer.writeNext(header);

            String[] nameRow = {"Universe Name", universeName};
            String[] priorityRow = {"Priority", String.valueOf(universePriority)};
            String[] colorRow = {"Color", universeColor};

            writer.writeNext(nameRow);
            writer.writeNext(priorityRow);
            writer.writeNext(colorRow);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
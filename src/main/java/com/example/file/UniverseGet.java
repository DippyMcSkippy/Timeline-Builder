package com.example.file;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UniverseGet {
    private String universeCsvPath;
    private String universeName;
    private int universePriority;
    private String universeColor;
    private String eventsFolder;

    public UniverseGet(String universeCsvPath) {
        this.universeCsvPath = universeCsvPath;
        this.eventsFolder = new File(universeCsvPath).getParent() + File.separator + "Events";
        loadUniverseData();
    }

    private void loadUniverseData() {
        try (CSVReader reader = new CSVReader(new FileReader(universeCsvPath))) {
            String[] line;
            try {
                while ((line = reader.readNext()) != null) {
                    if (line.length >= 2) {
                        if ("Universe Name".equals(line[0])) {
                            universeName = line[1];
                        } else if ("Priority".equals(line[0])) {
                            try {
                                universePriority = Integer.parseInt(line[1]);
                            } catch (NumberFormatException e) {
                                universePriority = 0;
                            }
                        } else if ("Color".equals(line[0])) {
                            universeColor = line[1];
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

    public String getUniverseName() {
        return universeName;
    }

    public int getUniversePriority() {
        return universePriority;
    }

    public String getUniverseColor() {
        return universeColor;
    }

    public String getEventsFolder() {
        return eventsFolder;
    }

    public List<File> getEventFiles() {
        List<File> eventFiles = new ArrayList<>();
        File folder = new File(eventsFolder);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (files != null) {
                for (File file : files) {
                    eventFiles.add(file);
                }
            }
        }
        return eventFiles;
    }
}
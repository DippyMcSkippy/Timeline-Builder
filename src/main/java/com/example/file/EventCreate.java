package com.example.file;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventCreate {
    private String eventsFolder;
    private String eventName;
    private String eventType;
    private String universeName; // New field for universe name

    // Normal event date fields
    private String startYear;
    private String startMonth;
    private String startDay;
    private String startEra;

    private String endYear;
    private String endMonth;
    private String endDay;
    private String endEra;

    // Incursion event fields
    private String sourceUniverse;
    private String targetUniverse;

    // Nexus event fields
    private List<String> connectedUniverses = new ArrayList<>();

    // Travel event fields
    private String originUniverse;
    private String destinationUniverse;
    private String traveler;

    // Constructor
    public EventCreate(String eventsFolder) {
        this.eventsFolder = eventsFolder;
    }

    // Basic setters
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setUniverseName(String universeName) {
        this.universeName = universeName;
    }

    // Normal event setters
    public void setStartDate(String year, String month, String day, String era) {
        this.startYear = year;
        this.startMonth = month;
        this.startDay = day;
        this.startEra = era;
    }

    public void setEndDate(String year, String month, String day, String era) {
        this.endYear = year;
        this.endMonth = month;
        this.endDay = day;
        this.endEra = era;
    }

    // Incursion event setters
    public void setIncursionDetails(String sourceUniverse, String targetUniverse) {
        this.sourceUniverse = sourceUniverse;
        this.targetUniverse = targetUniverse;
    }

    // Nexus event setters
    public void addConnectedUniverse(String universe) {
        connectedUniverses.add(universe);
    }

    public void setConnectedUniverses(List<String> universes) {
        this.connectedUniverses = new ArrayList<>(universes);
    }

    // Travel event setters
    public void setTravelDetails(String originUniverse, String destinationUniverse, String traveler) {
        this.originUniverse = originUniverse;
        this.destinationUniverse = destinationUniverse;
        this.traveler = traveler;
    }

    // Create the event file
    public void createEventFile() {
        if (eventName == null || eventName.isEmpty() || eventType == null || eventType.isEmpty()) {
            System.err.println("Event name and type must be set before creating the event file");
            return;
        }

        try {
            String eventFilePath = eventsFolder + File.separator + eventName.replaceAll("[^a-zA-Z0-9]", "_") + ".csv";

            CSVWriter writer = new CSVWriter(new FileWriter(eventFilePath));

            // Write header
            String[] header = {"Property", "Value"};
            writer.writeNext(header);

            // Write basic event info
            writer.writeNext(new String[]{"Event Name", eventName});
            writer.writeNext(new String[]{"Event Type", eventType});
            writer.writeNext(new String[]{"Universe Name", universeName}); // Add universe name to event info

            // Write type-specific details
            if ("Normal".equals(eventType)) {
                writeNormalEventDetails(writer);
            } else if ("Incursion".equals(eventType)) {
                writeIncursionEventDetails(writer);
            } else if ("Nexus".equals(eventType)) {
                writeNexusEventDetails(writer);
            } else if ("Travel".equals(eventType)) {
                writeTravelEventDetails(writer);
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNormalEventDetails(CSVWriter writer) {
        // Start date
        if (startYear != null) {
            writer.writeNext(new String[]{"Start Year", startYear});
        }
        if (startMonth != null) {
            writer.writeNext(new String[]{"Start Month", startMonth});
        }
        if (startDay != null) {
            writer.writeNext(new String[]{"Start Day", startDay});
        }
        if (startEra != null) {
            writer.writeNext(new String[]{"Start Era", startEra});
        }

        // End date
        if (endYear != null) {
            writer.writeNext(new String[]{"End Year", endYear});
        }
        if (endMonth != null) {
            writer.writeNext(new String[]{"End Month", endMonth});
        }
        if (endDay != null) {
            writer.writeNext(new String[]{"End Day", endDay});
        }
        if (endEra != null) {
            writer.writeNext(new String[]{"End Era", endEra});
        }
    }

    private void writeIncursionEventDetails(CSVWriter writer) {
        if (sourceUniverse != null) {
            writer.writeNext(new String[]{"Source Universe", sourceUniverse});
        }
        if (targetUniverse != null) {
            writer.writeNext(new String[]{"Target Universe", targetUniverse});
        }
    }

    private void writeNexusEventDetails(CSVWriter writer) {
        for (int i = 0; i < connectedUniverses.size(); i++) {
            writer.writeNext(new String[]{"Connected Universe " + (i + 1), connectedUniverses.get(i)});
        }
    }

    private void writeTravelEventDetails(CSVWriter writer) {
        if (originUniverse != null) {
            writer.writeNext(new String[]{"Origin Universe", originUniverse});
        }
        if (destinationUniverse != null) {
            writer.writeNext(new String[]{"Destination Universe", destinationUniverse});
        }
        if (traveler != null) {
            writer.writeNext(new String[]{"Traveler", traveler});
        }
    }
}
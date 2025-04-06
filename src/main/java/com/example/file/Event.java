package com.example.file;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Event {
    private String eventsFolder;
    private String eventName;
    private String eventType;
    private String universeName;

    // Normal event date fields
    private String startYear;
    private String startMonth;
    private String startDay;
    private String startEra;

    private String endYear;
    private String endMonth;
    private String endDay;
    private String endEra;

    // Constructor
    public Event(String eventsFolder) {
        this.eventsFolder = eventsFolder;
        System.out.println("Event Constructor: Events folder set to: " + eventsFolder);
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

    // Create the event file
    public void createEventFile() {
        if (eventName == null || eventName.isEmpty() || eventType == null || eventType.isEmpty()) {
            System.err.println("Event name and type must be set before creating the event file");
            return;
        }

        try {
            String eventFilePath = eventsFolder + File.separator + eventName.replaceAll("[^a-zA-Z0-9]", "_") + ".csv";
            System.out.println("Event createEventFile: Event file path - " + eventFilePath);

            File eventDir = new File(eventsFolder);
            if (!eventDir.exists()) {
                eventDir.mkdirs();
                System.out.println("Event createEventFile: Created directories - " + eventDir.getAbsolutePath());
            }

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
            } else {
                System.err.println("Unknown event type: " + eventType);
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
}
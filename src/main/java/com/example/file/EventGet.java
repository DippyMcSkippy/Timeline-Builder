package com.example.file;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EventGet {
    private String eventCsvPath;
    private String eventName;
    private String eventType;
    private Map<String, String> eventProperties = new HashMap<>();

    public EventGet(String eventCsvPath) {
        this.eventCsvPath = eventCsvPath;
        loadEventData();
    }

    private void loadEventData() {
        try (CSVReader reader = new CSVReader(new FileReader(eventCsvPath))) {
            String[] line;
            try {
                // Skip header
                reader.readNext();

                while ((line = reader.readNext()) != null) {
                    if (line.length >= 2) {
                        if ("Event Name".equals(line[0])) {
                            eventName = line[1];
                        } else if ("Event Type".equals(line[0])) {
                            eventType = line[1];
                        } else {
                            // Store all other properties
                            eventProperties.put(line[0], line[1]);
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

    public String getEventName() {
        return eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public String getProperty(String key) {
        return eventProperties.get(key);
    }

    public boolean isNormalEvent() {
        return "Normal".equals(eventType);
    }

    public boolean isIncursionEvent() {
        return "Incursion".equals(eventType);
    }

    public boolean isNexusEvent() {
        return "Nexus".equals(eventType);
    }

    public boolean isTravelEvent() {
        return "Travel".equals(eventType);
    }

    public String getStartDate() {
        if (isNormalEvent()) {
            String year = getProperty("Start Year");
            String month = getProperty("Start Month");
            String day = getProperty("Start Day");
            String era = getProperty("Start Era");

            StringBuilder date = new StringBuilder();
            if (era != null && !era.isEmpty()) {
                date.append(year).append(" ").append(era);
            } else {
                date.append(year);
            }

            if (month != null && !month.equals("Unspecified")) {
                date.append(", ").append(month);

                if (day != null && !day.equals("Unspecified")) {
                    date.append(" ").append(day);
                }
            }

            return date.toString();
        }
        return "";
    }

    public String getEndDate() {
        if (isNormalEvent()) {
            String year = getProperty("End Year");
            String month = getProperty("End Month");
            String day = getProperty("End Day");
            String era = getProperty("End Era");

            StringBuilder date = new StringBuilder();
            if (era != null && !era.isEmpty()) {
                date.append(year).append(" ").append(era);
            } else {
                date.append(year);
            }

            if (month != null && !month.equals("Unspecified")) {
                date.append(", ").append(month);

                if (day != null && !day.equals("Unspecified")) {
                    date.append(" ").append(day);
                }
            }

            return date.toString();
        }
        return "";
    }
}
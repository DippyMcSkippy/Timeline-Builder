package com.example.timelinebuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Timeline {
    private String fileName;
    private String universeName;
    private String universeColor;
    private String datingSystem;
    private String calendarDetails;

    // Constructor
    public Timeline(String fileName, String universeName, String universeColor, String datingSystem, String calendarDetails) {
        this.fileName = fileName;
        this.universeName = universeName;
        this.universeColor = universeColor;
        this.datingSystem = datingSystem;
        this.calendarDetails = calendarDetails;
    }

    // Getters and setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUniverseName() {
        return universeName;
    }

    public void setUniverseName(String universeName) {
        this.universeName = universeName;
    }

    public String getUniverseColor() {
        return universeColor;
    }

    public void setUniverseColor(String universeColor) {
        this.universeColor = universeColor;
    }

    public String getDatingSystem() {
        return datingSystem;
    }

    public void setDatingSystem(String datingSystem) {
        this.datingSystem = datingSystem;
    }

    public String getCalendarDetails() {
        return calendarDetails;
    }

    public void setCalendarDetails(String calendarDetails) {
        this.calendarDetails = calendarDetails;
    }
}

package com.example.file;

import javafx.stage.DirectoryChooser;
import java.io.File;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Multiverse {
    private String rootPath;
    private String multiverseName;
    private String datingSystem;
    private String beforeEra;
    private String duringEra;
    private String afterEra;

    private String multiverseFolderPath;
    private String universesFolderPath;
    private String multiverseCsvPath;

    public Multiverse() {
        // Constructor no longer shows the directory chooser
    }

    // Getters for files and directories
    public String getMultiverseFolderPath() {
        return multiverseFolderPath;
    }

    public String getUniversesFolderPath() {
        return universesFolderPath;
    }

    public String getMultiverseCsvPath() {
        return multiverseCsvPath;
    }

    public void showDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Save Location For Multiverse");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            this.rootPath = selectedDirectory.getAbsolutePath();
            System.out.println("M: Selected directory path: " + rootPath);
        }
    }

    public void setMultiverseName(String name) {
        this.multiverseName = name;
        System.out.println("M: Multiverse name set to: " + name);
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getMultiverseName() {
        return multiverseName;
    }

    public void setDatingSystem(String system) {
        this.datingSystem = system;
        System.out.println("M: Dating system set to: " + system);
    }

    public void setRelativeEras(String before, String during, String after) {
        this.beforeEra = before;
        this.duringEra = during;
        this.afterEra = after;
        System.out.println("M: Relative eras set to - Before: " + before + ", During: " + during + ", After: " + after);
    }

    public void createMultiverse() {
        try {
            // Set up paths
            multiverseFolderPath = rootPath + File.separator + multiverseName;
            universesFolderPath = multiverseFolderPath + File.separator + "Universes";
            multiverseCsvPath = multiverseFolderPath + File.separator + multiverseName + ".csv";

            // Create directories
            File directory = new File(multiverseFolderPath);
            directory.mkdir();
            File universes = new File(universesFolderPath);
            universes.mkdir();

            // Create CSV file
            CSVWriter writer = new CSVWriter(new FileWriter(multiverseCsvPath));

            // Write multiverse config data
            String[] header = {"Property", "Value"};
            writer.writeNext(header);

            String[] nameRow = {"Multiverse Name", multiverseName};
            String[] datingSystemRow = {"Dating System", datingSystem};
            writer.writeNext(nameRow);
            writer.writeNext(datingSystemRow);

            if ("Relative".equals(datingSystem)) {
                writer.writeNext(new String[]{"Before Era", beforeEra});
                writer.writeNext(new String[]{"During Era", duringEra});
                writer.writeNext(new String[]{"After Era", afterEra});
            }

            writer.close();
            System.out.println("M: Multiverse created with name: " + multiverseName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("M: Multiverse folder path " + multiverseFolderPath);
        System.out.println("M: Universe folder path " + universesFolderPath);
        System.out.println("M: Multiverse CSV path " + multiverseCsvPath);
    }

    public List<String> getUniverses() {
        List<String> universes = new ArrayList<>();
        File folder = new File(universesFolderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        universes.add(file.getName());
                        System.out.println("M: Found universe: " + file.getName());
                    }
                }
            }
        }
        return universes;
    }

    public String getDatingSystem() {
        return datingSystem;
    }

    public List<String> getEras() {
        List<String> eras = new ArrayList<>();
        if ("Relative".equals(datingSystem)) {
            if (beforeEra != null) eras.add(beforeEra);
            if (duringEra != null) eras.add(duringEra);
            if (afterEra != null) eras.add(afterEra);
        }
        return eras;
    }

    public boolean isNumeralDating() {
        return "Numeral".equals(datingSystem);
    }

    public boolean isRelativeDating() {
        return "Relative".equals(datingSystem);
    }
}
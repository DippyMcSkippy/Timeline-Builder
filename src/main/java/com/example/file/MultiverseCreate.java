package com.example.file;

import javafx.stage.DirectoryChooser;
import java.io.File;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MultiverseCreate {
    private String rootPath;
    private String multiverseName;
    private String datingSystem;
    private String beforeEra;
    private String duringEra;
    private String afterEra;

    private String multiversePath;
    private String universesPath;
    private String csvPath;

    public MultiverseCreate() {
        showDirectoryChooser();
    }

    //getters for files and dirctories
    public String getMultiversePath() {
        return multiversePath;
    }

    public String getUniversesPath() {
        return universesPath;
    }

    public String getCsvPath() {
        return csvPath;
    }

    public void showDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Save Location For Multiverse");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            this.rootPath = selectedDirectory.getAbsolutePath();
        }
    }

    public void setMultiverseName(String name){
        this.multiverseName = name;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getMultiverseName() {
        return multiverseName;
    }

    public void setDatingSystem(String system) {
        this.datingSystem = system;
    }

    public void setRelativeEras(String before, String during, String after) {
        this.beforeEra = before;
        this.duringEra = during;
        this.afterEra = after;
    }

    public void createMultiverseFile() {
        try {
            //create folder and directory
            String dirPath = rootPath + File.separator + multiverseName;
            File directory = new File(dirPath);
            directory.mkdir();

            //creat csv file
            String csvFilePath = dirPath + File.separator + multiverseName + ".csv";
            CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));



            // Write the multiverse configuration data
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

            //create universe folder
            String uniPath = dirPath + File.separator + "Universes";
            File universes = new File(uniPath);
            universes.mkdir();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //Create Multiverse File in multiverse folder
        //create csv file named the same as the multiverse name
        //write to file the name of the



    //create universes folder
    //universe create in universe folder
}

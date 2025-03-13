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

    private String multiverseFolderPath;
    private String universesFolderPath;
    private String multiverseCsvPath;

    public MultiverseCreate() {
        showDirectoryChooser();
    }

    //getters for files and directories
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
            // set up paths
            multiverseFolderPath = rootPath + File.separator + multiverseName;
            universesFolderPath = multiverseFolderPath + File.separator + "Universes";
            multiverseCsvPath = multiverseFolderPath + File.separator + multiverseName + ".csv";

            // create directories
            File directory = new File(multiverseFolderPath);
            directory.mkdir();
            File universes = new File(universesFolderPath);
            universes.mkdir();

            //create csv file
            CSVWriter writer = new CSVWriter(new FileWriter(multiverseCsvPath));



            // write multiverse config data
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




        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Multiverse folder path "+ multiverseFolderPath);
        //System.out.println("Universe folder Path "+ universesFolderPath);
        //System.out.println("Multiverse csv path "+ multiverseCsvPath);


    }



    //Create Multiverse File in multiverse folder
        //create csv file named the same as the multiverse name
        //write to file the name of the



    //create universes folder
    //universe create in universe folder
}

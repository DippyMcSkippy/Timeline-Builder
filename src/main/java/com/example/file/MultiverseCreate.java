package com.example.file;

import javafx.stage.DirectoryChooser;
import java.io.File;

public class MultiverseCreate {
    private String rootPath;
    private String multiverseName;

    public MultiverseCreate() {
        showDirectoryChooser();
    }

    private void showDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Root Directory for " + multiverseName);
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

    //Create Multiverse File in multiverse folder
    //Dating System

    //create universe folder
    //universe create in universe folder
}

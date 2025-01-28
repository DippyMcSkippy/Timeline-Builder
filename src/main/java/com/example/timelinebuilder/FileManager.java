package com.example.timelinebuilder;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {

    private final Stage stage;

    public FileManager(Stage stage) {
        this.stage = stage;
    }

    public String saveFile(String content) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Timeline File");

        // Set default directory to user home
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Set allowed file extensions
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Timeline Files", "*.timeline")
        );

        // Show save dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                Files.write(Paths.get(file.getAbsolutePath()), content.getBytes());
                System.out.println("File saved: " + file.getAbsolutePath());
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save file.");
            }
        }
        return null;



    }

    public String loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Timeline File");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Timeline Files", "*.timeline")
        );

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                String content = Files.readString(Paths.get(file.getAbsolutePath()));
                System.out.println("File loaded: " + file.getAbsolutePath());
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to read file.");
            }
        }
        return null;
    }
}


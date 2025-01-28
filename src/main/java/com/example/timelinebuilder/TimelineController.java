package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TimelineController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
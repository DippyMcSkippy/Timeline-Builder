package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UniverseCreateController {
    @FXML
    private TextField nameField;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        String name = nameField.getText();
        if (!name.isEmpty()) {
            // Universe creation logic will go here
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}
package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.example.file.MultiverseCreate;

public class MultiverseCreateController {
    @FXML
    private TextField nameField;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
    }

    @FXML
    private void handleSubmit() {
        String name = nameField.getText();
        if (!name.isEmpty()) {
            MultiverseCreate multiverseCreate = new MultiverseCreate();
            multiverseCreate.setMultiverseName(name);
            multiverseCreate.showDirectoryChooser();

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}

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

    private MultiverseCreate multiverseCreate;

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> onSubmit());
    }

    private void onSubmit() {
        String name = nameField.getText();
        if (!name.isEmpty()) {
            multiverseCreate = new MultiverseCreate();
            multiverseCreate.setMultiverseName(name);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}

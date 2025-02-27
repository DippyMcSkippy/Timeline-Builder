package com.example.timelinebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.example.file.MultiverseCreate;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class MultiverseCreateController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> datingSystem;

    @FXML
    private VBox relativeFields;

    @FXML
    private TextField beforeField;

    @FXML
    private TextField duringField;

    @FXML
    private TextField afterField;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        datingSystem.getItems().addAll("Numeral", "Relative");

        datingSystem.setOnAction(event -> {
            boolean isRelative = "Relative".equals(datingSystem.getValue());
            relativeFields.setVisible(isRelative);
            relativeFields.setManaged(isRelative);
        });

        submitButton.setOnAction(event -> handleSubmit());
    }

    @FXML
    private void handleSubmit() {
        String name = nameField.getText();
        if (!name.isEmpty()) {
            MultiverseCreate multiverseCreate = new MultiverseCreate();
            multiverseCreate.setMultiverseName(name);
            multiverseCreate.setDatingSystem(datingSystem.getValue());

            if ("Relative".equals(datingSystem.getValue())) {
                multiverseCreate.setRelativeEras(
                        beforeField.getText(),
                        duringField.getText(),
                        afterField.getText()
                );
            }
            //multiverseCreate.showDirectoryChooser(); this was removed because it would open the file manager twice

            multiverseCreate.createMultiverseFile();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }
}

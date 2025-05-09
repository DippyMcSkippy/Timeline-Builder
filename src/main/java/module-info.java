module com.example.timelinebuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.opencsv;

    opens com.example.timelinebuilder to javafx.fxml;
    exports com.example.timelinebuilder;
    exports com.example.config;
    opens com.example.config to javafx.fxml;
}
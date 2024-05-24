module com.example.proiectfis2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.commons;
    requires org.junit.platform.launcher;
    requires org.junit.platform.engine;
    requires org.testng;

    opens com.example.proiectfis2 to javafx.fxml, org.junit.platform.commons, org.junit.jupiter.api, org.junit.jupiter.engine, org.junit.platform.launcher, org.junit.platform.engine, com.google.gson;
    exports com.example.proiectfis2;
}

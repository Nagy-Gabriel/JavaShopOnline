package com.example.proiectfis2;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inițializează interfața grafică aici
        primaryStage.setTitle("Aplicație JavaFX"); // Setează titlul ferestrei
        primaryStage.show(); // Afișează fereastra
    }

    public static void main(String[] args) {
        launch(args); // Lansează aplicația
    }
}

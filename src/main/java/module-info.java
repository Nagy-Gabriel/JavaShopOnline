module com.example.proiectfis2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.example.proiectfis2 to com.google.gson, javafx.fxml;
    exports com.example.proiectfis2;
}

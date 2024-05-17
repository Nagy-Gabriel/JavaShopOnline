module com.example.proiectfis2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.proiectfis2 to javafx.fxml;
    exports com.example.proiectfis2;
}
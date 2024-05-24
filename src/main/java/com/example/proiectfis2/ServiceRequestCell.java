package com.example.proiectfis2;

import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ServiceRequestCell extends ListCell<ServiceRequest> {
    private VBox vbox = new VBox();
    private Label customerLabel = new Label();
    private Label descriptionLabel = new Label();
    private Label dateLabel = new Label();

    public ServiceRequestCell() {
        customerLabel.getStyleClass().add("name-label");
        descriptionLabel.getStyleClass().add("description-label");
        dateLabel.getStyleClass().add("date-label");

        vbox.getChildren().addAll(customerLabel, descriptionLabel, dateLabel);
        setGraphic(vbox);
    }

    @Override
    protected void updateItem(ServiceRequest serviceRequest, boolean empty) {
        super.updateItem(serviceRequest, empty);

        if (empty || serviceRequest == null) {
            setText(null);
            setGraphic(null);
        } else {
            customerLabel.setText("Client: " + serviceRequest.getCustomer().getUsername());
            descriptionLabel.setText("Desciere: " + serviceRequest.getDescription());
            dateLabel.setText("Data: " + serviceRequest.getDate());
            setGraphic(vbox);
        }
    }
}
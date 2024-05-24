package com.example.proiectfis2;

import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OrderCell extends ListCell<Order> {
    private VBox vbox = new VBox();
    private Label customerLabel = new Label();
    private Label productsLabel = new Label();
    private Label statusLabel = new Label();

    public OrderCell() {
        customerLabel.getStyleClass().add("name-label");
        productsLabel.getStyleClass().add("description-label");
        statusLabel.getStyleClass().add("status-label");

        vbox.getChildren().addAll(customerLabel, productsLabel, statusLabel);
        setGraphic(vbox);
    }

    @Override
    protected void updateItem(Order order, boolean empty) {
        super.updateItem(order, empty);

        if (empty || order == null) {
            setText(null);
            setGraphic(null);
        } else {
            customerLabel.setText("Client: " + order.getCustomer().getUsername());
            StringBuilder productsStringBuilder = new StringBuilder("Produse:\n");
            for (Product product : order.getProducts()) {
                productsStringBuilder.append(" - ").append(product.getName())
                        .append(" (Preet: ").append(product.getPrice())
                        .append(", Descriere: ").append(product.getDescription()).append(")\n");
            }
            productsLabel.setText(productsStringBuilder.toString());
            statusLabel.setText("Status: " + order.getStatus());
            setGraphic(vbox);
        }
    }
}

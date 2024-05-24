package com.example.proiectfis2;

import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProductCell extends ListCell<Product> {
    private VBox vbox = new VBox();
    private Label nameLabel = new Label();
    private Label priceLabel = new Label();
    private Label descriptionLabel = new Label();
    private Label ratingLabel = new Label();

    public ProductCell() {
        nameLabel.getStyleClass().add("name-label");
        priceLabel.getStyleClass().add("price-label");
        descriptionLabel.getStyleClass().add("description-label");
        ratingLabel.getStyleClass().add("rating-label");

        vbox.getChildren().addAll(nameLabel, priceLabel, descriptionLabel, ratingLabel);
        setGraphic(vbox);
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);

        if (empty || product == null) {
            setText(null);
            setGraphic(null);
        } else {
            nameLabel.setText("Nume: " + product.getName());
            priceLabel.setText("Pret: " + product.getPrice());
            descriptionLabel.setText("Descriere: " + product.getDescription());
            ratingLabel.setText("Rating: " + product.getRating());
            setGraphic(vbox);
        }
    }
}

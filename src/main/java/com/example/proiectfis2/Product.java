package com.example.proiectfis2;

public class Product {
    private String name;
    private Category category;
    private double price;
    private String description;
    private int rating;
    private boolean isPart;

    public Product(String name, Category category, double price, String description, int rating, boolean isPart) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.isPart = isPart;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public boolean isPart() {
        return isPart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setPart(boolean part) {
        isPart = part;
    }

    @Override
    public String toString() {
        return String.format("Nume: %s\nPret: %.2f\nDescriere: %s\nRating: %d",
                name, price, description, rating);
    }
}

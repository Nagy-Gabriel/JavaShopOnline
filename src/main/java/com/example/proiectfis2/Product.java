package com.example.proiectfis2;

public class Product {
    private String name;
    Category category;
    private double price;
    private String description;
    private int criticScore;

    public Product(String name, Category category, double price, String description, int criticScore) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.criticScore = criticScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCriticScore() {
        return criticScore;
    }

    public void setCriticScore(int criticScore) {
        this.criticScore = criticScore;
    }
}

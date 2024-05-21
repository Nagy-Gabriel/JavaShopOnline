package com.example.proiectfis2;

public class Product {
    private String name;
    private Category category;
    private double price;
    private String description;
    private int rating;

    public Product(String name, Category category, double price, String description, int rating) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.rating = rating;
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

    @Override
    public String toString() {
        return "Name: " + this.name + ", Price: " + this.price + ", Description: " + this.description + ", Rating: " + this.rating;
    }

}

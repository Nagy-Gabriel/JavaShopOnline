package com.example.proiectfis2;

import java.util.List;


class Promotion {
    private String name;
    private List<Product> products; // Produsele incluse în promoție
    private double discountPercent; // Procentul de reducere

    public Promotion(String name, List<Product> products, double discountPercent) {
        this.name = name;
        this.products = products;
        this.discountPercent = discountPercent;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
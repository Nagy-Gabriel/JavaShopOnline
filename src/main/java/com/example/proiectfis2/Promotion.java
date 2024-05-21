package com.example.proiectfis2;

import java.util.List;

class Promotion {
    private String name;
    private List<Product> products;
    private double discountPercent;

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

    public double calculateDiscount() {
        double totalValue = products.stream().mapToDouble(Product::getPrice).sum();
        return totalValue * (discountPercent / 100);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "name='" + name + '\'' +
                ", products=" + products +
                ", discountPercent=" + discountPercent +
                '}';
    }
}

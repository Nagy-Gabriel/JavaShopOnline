package com.example.proiectfis2;

import java.util.List;

public class Order {
    private Customer customer;
    private List<Product> products;
    Type type; // "cumparare" sau "service"
    private String description; //pt service


    public Order(Customer customer, List<Product> products, Type type, String description) {
        this.customer = customer;
        this.products = products;
        this.type = type;
        this.description = description;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

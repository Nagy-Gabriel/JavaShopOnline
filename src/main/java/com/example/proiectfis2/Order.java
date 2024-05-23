package com.example.proiectfis2;

import java.util.List;

public class Order {
    private Customer customer;
    private List<Product> products;
    private String status;

    public Order(Customer customer, List<Product> products, String status) {
        this.customer = customer;
        this.products = products;
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

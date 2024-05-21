package com.example.proiectfis2;

import java.util.Date;
import java.util.List;

public class Order {
    private Customer customer;
    private List<Product> products;
    private Type type; // CUMPĂRARE sau SERVICE
    private String serviceDescription;
    private Date serviceDate;
    private String status; // Statusul comenzii

    public Order(Customer customer, List<Product> products, Type type) {
        this.customer = customer;
        this.products = products;
        this.type = type;
        this.status = "New"; // Status inițial
    }

    public Order(Customer customer, List<Product> products, Type type, String serviceDescription, Date serviceDate) {
        this.customer = customer;
        this.products = products;
        this.type = type;
        this.serviceDescription = serviceDescription;
        this.serviceDate = serviceDate;
        this.status = "New"; // Status inițial
    }

    // Getters și Setters
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

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", products=" + products +
                ", type=" + type +
                ", serviceDescription='" + serviceDescription + '\'' +
                ", serviceDate=" + serviceDate +
                ", status='" + status + '\'' +
                '}';
    }
}

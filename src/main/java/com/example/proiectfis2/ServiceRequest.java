package com.example.proiectfis2;

public class ServiceRequest {
    private Customer customer;
    private String description;
    private String date;
    private String status;

    public ServiceRequest(Customer customer, String description, String date) {
        this.customer = customer;
        this.description = description;
        this.date = date;
        this.status = "Pending";
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

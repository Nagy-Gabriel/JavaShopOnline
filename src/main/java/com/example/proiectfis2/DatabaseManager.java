package com.example.proiectfis2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private List<Customer> customers;
    private List<Employee> employees;
    private List<Product> products;
    private List<Order> orders;
    private List<Promotion> promotions;

    public DatabaseManager() {
        this.customers = loadCustomers();
        this.employees = loadEmployees();
        this.products = loadProducts();
        this.orders = loadOrders();
        this.promotions = loadPromotions();
    }

    private List<Customer> loadCustomers() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/com/example/proiectfis2/customers.json"))) {
            Type customerListType = new TypeToken<ArrayList<Customer>>(){}.getType();
            return new Gson().fromJson(reader, customerListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Employee> loadEmployees() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/com/example/proiectfis2/employees.json"))) {
            Type employeeListType = new TypeToken<ArrayList<Employee>>(){}.getType();
            return new Gson().fromJson(reader, employeeListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Product> loadProducts() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/com/example/proiectfis2/products.json"))) {
            Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
            return new Gson().fromJson(reader, productListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Order> loadOrders() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/com/example/proiectfis2/orders.json"))) {
            Type orderListType = new TypeToken<ArrayList<Order>>(){}.getType();
            return new Gson().fromJson(reader, orderListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Promotion> loadPromotions() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/com/example/proiectfis2/promotions.json"))) {
            Type promotionListType = new TypeToken<ArrayList<Promotion>>(){}.getType();
            return new Gson().fromJson(reader, promotionListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Gestionarea clienților
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Customer getCustomer(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    public boolean authenticateCustomer(String username, String password) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Gestionarea angajaților
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public boolean authenticateEmployee(String username, String password) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public Employee getEmployee(String username) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username)) {
                return employee;
            }
        }
        return null;
    }

    // Gestionarea produselor
    public void addProduct(Product product, Employee employee) {
        if (employee != null && employee.getRole().equals("senior")) {
            products.add(product);
            saveProducts();
        } else {
            System.out.println("Doar angajații seniori pot adăuga produse.");
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    // Gestionarea comenzilor
    public void placeOrder(Order order) {
        orders.add(order);
        saveOrders();
    }

    public List<Order> getOrders() {
        return orders;
    }

    // Gestionarea promotiilor
    public void addPromotion(Promotion promotion, Employee employee) {
        if (employee != null && employee.getRole().equals("manager")) {
            promotions.add(promotion);
            for (Product product : promotion.getProducts()) {
                if (!products.contains(product)) {
                    products.add(product);
                }
            }
            saveProducts();
        } else {
            System.out.println("Doar managerii pot adăuga promoții noi.");
        }
    }

    public void removePromotion(Promotion promotion, Employee employee) {
        if (employee != null && employee.getRole().equals("manager")) {
            promotions.remove(promotion);
            saveProducts();
        } else {
            System.out.println("Doar managerii pot șterge promoții.");
        }
    }
    public double getDiscountForProduct(Product product) {
        for (Promotion promotion : promotions) {
            if (promotion.getProducts().contains(product)) {
                return promotion.getDiscountPercent();
            }
        }
        return 0;
    }


    public List<Promotion> getPromotions() {
        return promotions;
    }

    // Metode pentru salvarea și încărcarea datelor (simulare)
    private void saveOrders() {
        // Salvarea comenzilor
    }

    private void saveProducts() {
        // Salvarea produselor
    }
}

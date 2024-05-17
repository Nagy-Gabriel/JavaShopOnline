package com.example.proiectfis2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DatabaseManager {
    private List<Employee> employees;
    private List<Product> products;
    private List<Customer> customers;
    private List<Order> orders;
    private List<Promotion> promotions;
    private List<String> services;
    private Map<String, List<Product>> catalog; // Catalogul categoriilor de produse

    public DatabaseManager() {
        // Inițializare listele
        employees = new ArrayList<>();
        products = new ArrayList<>();
        customers = new ArrayList<>();
        orders = new ArrayList<>();
        promotions = new ArrayList<>();
        services = new ArrayList<>();
        catalog = new HashMap<>();
        // Adăugare servicii inițiale
        services.add("cumpărarea de sisteme electronice pre-asamblate");
        services.add("construirea unor desktop pc din piese puse la dispoziție");
        services.add("service");
        // Adăugare categorii de produse în catalog
        catalog.put(Category.DESKTOP_PC.toString(), new ArrayList<>());
        catalog.put(Category.LAPTOP.toString(), new ArrayList<>());
        catalog.put(Category.IMPRIMANTE.toString(), new ArrayList<>());
        catalog.put(Category.PERIFERICE.toString(), new ArrayList<>());


    }

    // Metode pentru gestionarea angajaților
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void viewEmployees() {
        for (Employee employee : employees) {
            System.out.println(employee.getName() + " - " + employee.getRole());
        }
    }

    // Metode pentru gestionarea produselor
    public void addProduct(Product product, Employee employee) {
        if (employee.getRole().equals("senior")) {
            products.add(product);
            catalog.get(product.getCategory().toString()).add(product); // Convertim enum-ul în șir
        } else {
            System.out.println("Doar angajații seniori pot adăuga produse noi.");
        }
    }
    // Metoda pentru obținerea unui produs după nume
    public Product getProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null; // Returnează null dacă produsul nu este găsit
    }


    public List<Product> getProducts() {
        return products;
    }

    public Map<String, List<Product>> getCatalog() {
        return catalog;
    }

    // Metode pentru gestionarea clienților
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Customer getCustomerByUsername(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    public boolean doesCustomerExist(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean authenticateCustomer(String username, String password) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Metode pentru gestionarea comenzilor
    public void placeOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrdersByUsername(String username) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getUsername().equals(username)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }
    public List<Order> getOrders() {
        return orders;
    }

    // Metode pentru gestionarea serviciilor
    public List<String> getServices() {
        return services;
    }

    public void addService(String service) {
        services.add(service);
    }

    // Metode pentru gestionarea promoțiilor
    public void addPromotion(Promotion promotion, Employee employee) {
        if (employee.getRole().equals("senior")) {
            promotions.add(promotion);
            for (Product product : promotion.getProducts()) {
                products.add(product); // Adăugăm produsele incluse în promoție în lista de produse
            }
        } else {
            System.out.println("Doar angajații seniori pot adăuga promoții noi.");
        }
    }

    public void removePromotion(Promotion promotion, Employee employee) {
        if (employee.getRole().equals("senior")) {
            promotions.remove(promotion);
            for (Product product : promotion.getProducts()) {
                products.remove(product); // Ștergem produsele incluse în promoție din lista de produse
            }
        } else {
            System.out.println("Doar angajații seniori pot șterge promoții.");
        }
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    // Metoda pentru calcularea prețului total al unei comenzi
    public double calculateTotalPrice(Order order) {
        double totalPrice = 0.0;
        for (Product product : order.getProducts()) {
            totalPrice += product.getPrice();
        }
        // Adăugăm taxa suplimentară pentru piesele pre-asamblate
        if (order.getType() == Type.CUMPARARE) { // Verificăm tipul comenzii folosind enum-ul
            for (Product product : order.getProducts()) {
                if (product.getCategory() == Category.DESKTOP_PC || product.getCategory() == Category.LAPTOP) {
                    totalPrice += 100.0; // Taxa suplimentară pentru piese pre-asamblate
                }
            }
        }
        return totalPrice;
    }
}
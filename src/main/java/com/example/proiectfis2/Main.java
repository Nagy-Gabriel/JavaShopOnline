package com.example.proiectfis2;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Creăm un manager de baze de date
        DatabaseManager databaseManager = new DatabaseManager();

        // Adăugăm câțiva angajați
        Employee seniorEmployee = new Employee("John Doe", "senior");
        Employee juniorEmployee = new Employee("Jane Smith", "junior");
        databaseManager.addEmployee(seniorEmployee);
        databaseManager.addEmployee(juniorEmployee);

        // Vizualizăm angajații
        System.out.println("Angajați:");
        databaseManager.viewEmployees();

        // Adăugăm câteva produse
        Product desktopPC = new Product("Desktop PC", Category.DESKTOP_PC, 1500.0, "Un desktop puternic", 4);
        Product laptop = new Product("Laptop", Category.LAPTOP, 2000.0, "Un laptop ușor și portabil", 5);
        databaseManager.addProduct(desktopPC, seniorEmployee);
        databaseManager.addProduct(laptop, juniorEmployee);

        // Vizualizăm produsele
        System.out.println("\nProduse:");
        for (Product product : databaseManager.getProducts()) {
            System.out.println(product.getName() + " - " + product.getCategory() + " - " + product.getPrice());
        }

        // Plasăm o comandă
        Customer customer = new Customer("customer1", "password123");
        List<Product> orderedProducts = new ArrayList<>();
        orderedProducts.add(desktopPC);
        Order order = new Order(customer, orderedProducts, Type.CUMPARARE, "Descriere pentru service");
        databaseManager.placeOrder(order);

        // Calculăm prețul total al comenzii
        double totalPrice = databaseManager.calculateTotalPrice(order);
        System.out.println("\nPreț total comandă: " + totalPrice);
    }
}

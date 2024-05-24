package com.example.proiectfis2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TestNagyGabriel {
    private DatabaseManager databaseManager;
    private Employee seniorEmployee;
    private Employee juniorEmployee;
    private Product validProduct;
    private Product anotherProduct;
    private Customer validCustomer;
    private Order validOrder;

    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager();
        seniorEmployee = new Employee("seniorUser", "password", "senior");
        juniorEmployee = new Employee("juniorUser", "password", "junior");
        validProduct = new Product("Test produs", Category.DESKTOP_PC, 1000.0, "Test descriere", 5, false);
        anotherProduct = new Product("Alt produs", Category.LAPTOP_PC, 1500.0, "Alta descriere", 3, false);
        validCustomer = new Customer("TestClient", "password123", "Test Client ", "Test@example.com");
        validOrder = new Order(validCustomer, Collections.singletonList(validProduct), "Pending");
    }

    @Test
    void testAddProductWithSeniorEmployee() {
        System.out.println("Testul: testAddProductWithSeniorEmployee a inceput");
        int initialSize = databaseManager.getProducts().size();
        databaseManager.addProduct(validProduct, seniorEmployee);
        assertEquals(initialSize + 1, databaseManager.getProducts().size());
        assertTrue(databaseManager.getProducts().contains(validProduct));
    }

    @Test
    void testAddProductWithJuniorEmployee() {
        System.out.println("Testul: testAddProductWithJuniorEmployee a inceput");
        int initialSize = databaseManager.getProducts().size();
        databaseManager.addProduct(validProduct, juniorEmployee);
        assertEquals(initialSize, databaseManager.getProducts().size());
        assertFalse(databaseManager.getProducts().contains(validProduct));
    }

    @Test
    void testAddProductWithoutEmployee() {
        System.out.println("Testul: testAddProductWithNullEmployee a inceput");
        int initialSize = databaseManager.getProducts().size();
        databaseManager.addProduct(validProduct, null);
        assertEquals(initialSize, databaseManager.getProducts().size());
        assertFalse(databaseManager.getProducts().contains(validProduct));
    }

    @Test
    void testPlaceValidOrder() {
        System.out.println("Testul: testPlaceValidOrder a inceput");
        int initialSize = databaseManager.getOrders().size();
        databaseManager.placeOrder(validOrder);
        assertEquals(initialSize + 1, databaseManager.getOrders().size());
        assertTrue(databaseManager.getOrders().contains(validOrder));
    }

    @Test
    void testPlaceOrderWithoutCustomer() {
        System.out.println("Testul: testPlaceOrderWithoutCustomer a inceput");
        Order invalidOrder = new Order(null, Collections.singletonList(validProduct), "Pending");
        int initialSize = databaseManager.getOrders().size();
        databaseManager.placeOrder(invalidOrder);
        assertEquals(initialSize, databaseManager.getOrders().size());
        assertFalse(databaseManager.getOrders().contains(invalidOrder));
    }
    @Test
    void testPlaceOrderWithoutAnyProductInList() {
        System.out.println("Testul: testPlaceOrderWithoutAnyProductInList a inceput");
        Order invalidOrder = new Order(validCustomer, Collections.emptyList(), "Pending");
        int initialSize = databaseManager.getOrders().size();
        databaseManager.placeOrder(invalidOrder);
        assertEquals(initialSize, databaseManager.getOrders().size());
        assertFalse(databaseManager.getOrders().contains(invalidOrder));
    }

    @Test
    void testAddServiceRequest() {
        System.out.println("Testul: testAddServiceRequest a inceput");
        ServiceRequest serviceRequest = new ServiceRequest(validCustomer, "Test Service Request", "2024-05-24");
        int initialSize = databaseManager.getServiceRequests().size();
        databaseManager.addServiceRequest(serviceRequest);
        assertEquals(initialSize + 1, databaseManager.getServiceRequests().size());
        assertTrue(databaseManager.getServiceRequests().contains(serviceRequest));
    }
    @Test
    void testAddPromotionWithManager() {
        System.out.println("Testul: testAddPromotionWithManager a inceput");
        Employee managerEmployee = new Employee("managerUser", "password", "manager");
        Promotion promotion = new Promotion("Test Promotion", Collections.singletonList(validProduct), 10.0);
        int initialSize = databaseManager.getPromotions().size();
        databaseManager.addPromotion(promotion, managerEmployee);
        assertEquals(initialSize + 1, databaseManager.getPromotions().size());
        assertTrue(databaseManager.getPromotions().contains(promotion));
    }


    @Test
    void testAddPromotionWithNonManager() {
        System.out.println("Testul: testAddPromotionWithNonManager a inceput");
        Employee nonManagerEmployee = new Employee("nonManagerUser", "password", "junior");
        Promotion promotion = new Promotion("Test Promotion", Collections.singletonList(validProduct), 10.0);
        int initialSize = databaseManager.getPromotions().size();
        databaseManager.addPromotion(promotion, nonManagerEmployee);
        assertEquals(initialSize, databaseManager.getPromotions().size());
        assertFalse(databaseManager.getPromotions().contains(promotion));
    }
}

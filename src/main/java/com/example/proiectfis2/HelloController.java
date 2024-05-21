package com.example.proiectfis2;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML
    private ListView<Product> productList;

    @FXML
    private ListView<Product> cartList;

    @FXML
    private ListView<Order> orderList;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button addButton;

    @FXML
    private Button addPromoButton;

    @FXML
    private Button removePromoButton;

    @FXML
    private Button addToCartButton;

    @FXML
    private Button removeFromCartButton;

    @FXML
    private Button placeOrderButton;

    @FXML
    private Button changeStatusButton;

    @FXML
    private Button addEmployeeButton;

    @FXML
    private Button viewEmployeesButton;

    @FXML
    private TextArea serviceDescriptionArea;

    @FXML
    private TextField serviceDateField;

    private DatabaseManager databaseManager = new DatabaseManager();
    private Employee currentEmployee;
    private Customer currentCustomer;
    private List<Product> cart = new ArrayList<>();

    @FXML
    public void initialize() {
        // Inițializare liste și butoane
        // Exemplu de angajați pentru testare
        Employee manager = new Employee("manager", "managerpass", "manager");
        Employee senior = new Employee("senior", "seniorpass", "senior");
        Employee junior = new Employee("junior", "juniorpass", "junior");
        databaseManager.addEmployee(manager);
        databaseManager.addEmployee(senior);
        databaseManager.addEmployee(junior);

        // Inițializare ListView produse și comenzi
        productList.setCellFactory(param -> new ProductCell());
        cartList.setCellFactory(param -> new ProductCell());
        orderList.setCellFactory(param -> new OrderCell());

        updateProductListView();
        updateOrderListView();
        updateCartListView();

        // Dezactivarea butoanelor la început
        setButtonAccess(false, false, false, false, false, false);
    }

    private void setButtonAccess(boolean productAccess, boolean promotionAccess, boolean orderAccess, boolean employeeAccess, boolean viewEmployeeAccess, boolean changeOrderStatusAccess) {
        addButton.setDisable(!productAccess);
        addPromoButton.setDisable(!promotionAccess);
        removePromoButton.setDisable(!promotionAccess);
        addToCartButton.setDisable(!orderAccess);
        removeFromCartButton.setDisable(!orderAccess);
        placeOrderButton.setDisable(!orderAccess);
        changeStatusButton.setDisable(!changeOrderStatusAccess);
        addEmployeeButton.setDisable(!employeeAccess);
        viewEmployeesButton.setDisable(!viewEmployeeAccess);
    }

    private void updateProductListView() {
        List<Product> products = databaseManager.getProducts();
        productList.setItems(FXCollections.observableArrayList(products));
    }

    private void updateOrderListView() {
        List<Order> orders = databaseManager.getOrders();
        orderList.setItems(FXCollections.observableArrayList(orders));
    }

    private void updateCartListView() {
        cartList.setItems(FXCollections.observableArrayList(cart));
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Reset current user
        currentEmployee = null;
        currentCustomer = null;

        // Autentificare client
        if (databaseManager.authenticateCustomer(username, password)) {
            currentCustomer = databaseManager.getCustomer(username);
            System.out.println("Customer logged in: " + username);
            setButtonAccess(false, false, true, false, false, false);  // Customers can only place orders
        } else if (databaseManager.authenticateEmployee(username, password)) {
            // Autentificare angajat
            currentEmployee = databaseManager.getEmployee(username);
            System.out.println("Logged in as: " + currentEmployee.getUsername());
            switch (currentEmployee.getRole()) {
                case "manager":
                    setButtonAccess(true, true, true, true, true, true);
                    break;
                case "senior":
                    setButtonAccess(true, false, true, false, false, true);
                    break;
                case "junior":
                    setButtonAccess(false, false, true, false, false, true);
                    break;
            }
        } else {
            System.out.println("Invalid login credentials");
            setButtonAccess(false, false, false, false, false, false);
        }
    }

    @FXML
    private void handleAddProduct(ActionEvent event) {
        if (currentEmployee != null && "senior".equals(currentEmployee.getRole())) {
            ChoiceDialog<Category> categoryDialog = new ChoiceDialog<>(Category.DESKTOP_PC, Category.values());
            categoryDialog.setTitle("Add Product");
            categoryDialog.setHeaderText("Select product category");
            categoryDialog.setContentText("Category:");
            Optional<Category> resultCategory = categoryDialog.showAndWait();
            if (!resultCategory.isPresent()) {
                System.out.println("Product category not selected.");
                return;
            }

            TextInputDialog priceDialog = new TextInputDialog();
            priceDialog.setTitle("Add Product");
            priceDialog.setHeaderText("Enter product price");
            priceDialog.setContentText("Price:");
            Optional<String> resultPrice = priceDialog.showAndWait();
            if (!resultPrice.isPresent()) {
                System.out.println("Product price not provided.");
                return;
            }

            TextInputDialog descriptionDialog = new TextInputDialog();
            descriptionDialog.setTitle("Add Product");
            descriptionDialog.setHeaderText("Enter product description");
            descriptionDialog.setContentText("Description:");
            Optional<String> resultDescription = descriptionDialog.showAndWait();
            if (!resultDescription.isPresent()) {
                System.out.println("Product description not provided.");
                return;
            }

            try {
                Category category = resultCategory.get();
                double price = Double.parseDouble(resultPrice.get());
                String description = resultDescription.get();

                Product product = new Product(category.name(), category, price, description, 4);
                databaseManager.addProduct(product, currentEmployee);
                updateProductListView();
                System.out.println("Product added: " + product.getName());
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format.");
            }
        } else {
            System.out.println("You must log in as a senior employee to add products.");
        }
    }

    @FXML
    private void handleAddPromotion(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            List<Product> products = databaseManager.getProducts();
            ChoiceDialog<Product> productDialog = new ChoiceDialog<>(products.get(0), products);
            productDialog.setTitle("Add Promotion");
            productDialog.setHeaderText("Select product to apply promotion");
            productDialog.setContentText("Product:");
            Optional<Product> resultProduct = productDialog.showAndWait();
            if (!resultProduct.isPresent()) {
                System.out.println("Product not selected.");
                return;
            }

            Product selectedProduct = resultProduct.get();
            Promotion promotion = new Promotion("Special Promo", List.of(selectedProduct), 10.0);
            databaseManager.addPromotion(promotion, currentEmployee);
            updateProductListView();
            System.out.println("Promotion added to product: " + selectedProduct.getName());
        } else {
            System.out.println("You must log in as a manager to add promotions.");
        }
    }

    @FXML
    private void handleRemovePromotion(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            List<Promotion> promotions = databaseManager.getPromotions();
            ChoiceDialog<Promotion> promoDialog = new ChoiceDialog<>(promotions.get(0), promotions);
            promoDialog.setTitle("Remove Promotion");
            promoDialog.setHeaderText("Select promotion to remove");
            promoDialog.setContentText("Promotion:");
            Optional<Promotion> resultPromotion = promoDialog.showAndWait();
            if (!resultPromotion.isPresent()) {
                System.out.println("Promotion not selected.");
                return;
            }

            Promotion selectedPromotion = resultPromotion.get();
            databaseManager.removePromotion(selectedPromotion, currentEmployee);
            updateProductListView();
            System.out.println("Promotion removed: " + selectedPromotion.getName());
        } else {
            System.out.println("You must log in as a manager to remove promotions.");
        }
    }

    @FXML
    private void handleAddToCart(ActionEvent event) {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Check if the product has any promotion applied
            double discount = databaseManager.getDiscountForProduct(selectedProduct);
            Product productToAdd = new Product(
                    selectedProduct.getName(),
                    selectedProduct.getCategory(),
                    selectedProduct.getPrice() * (1 - discount / 100),
                    selectedProduct.getDescription(),
                    selectedProduct.getRating()
            );

            cart.add(productToAdd);
            updateCartListView();
            System.out.println("Product added to cart: " + productToAdd.getName());
        } else {
            System.out.println("You must select a product to add to the cart.");
        }
    }

    @FXML
    private void handleRemoveFromCart(ActionEvent event) {
        Product selectedProduct = cartList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            cart.remove(selectedProduct);
            updateCartListView();
            System.out.println("Product removed from cart: " + selectedProduct.getName());
        } else {
            System.out.println("You must select a product to remove from the cart.");
        }
    }

    @FXML
    private void handlePlaceOrder(ActionEvent event) {
        if (currentCustomer != null) {
            if (cart.isEmpty()) {
                System.out.println("Your cart is empty. Add products to the cart before placing an order.");
                return;
            }

            Order order = new Order(currentCustomer, new ArrayList<>(cart), Type.CUMPARARE);
            double totalPrice = 0.0;
            for (Product product : order.getProducts()) {
                totalPrice += product.getPrice();
                if (product.getCategory() == Category.DESKTOP_PC || product.getCategory() == Category.LAPTOP_PC) {
                    totalPrice += 100.0; // Taxă suplimentară pentru sisteme pre-asamblate
                }
            }
            System.out.println("Total price: " + totalPrice);
            databaseManager.placeOrder(order);
            updateOrderListView();
            cart.clear();
            updateCartListView();
            System.out.println("Order placed: " + order);
        } else {
            System.out.println("You must log in as a customer to place an order.");
        }
    }

    @FXML
    private void handleOrderStatusChange(ActionEvent event) {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // Exemplu simplu pentru schimbarea statusului unei comenzi
            selectedOrder.setStatus("Completed");
            updateOrderListView();
            System.out.println("Order status updated: " + selectedOrder.getStatus());
        } else {
            System.out.println("You must select an order to change its status.");
        }
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Employee");
            dialog.setHeaderText("Enter employee details");

            dialog.setContentText("Username:");
            Optional<String> resultUsername = dialog.showAndWait();
            if (!resultUsername.isPresent()) {
                System.out.println("Employee username not provided.");
                return;
            }

            dialog.setContentText("Password:");
            Optional<String> resultPassword = dialog.showAndWait();
            if (!resultPassword.isPresent()) {
                System.out.println("Employee password not provided.");
                return;
            }

            dialog.setContentText("Role (junior/senior):");
            Optional<String> resultRole = dialog.showAndWait();
            if (!resultRole.isPresent()) {
                System.out.println("Employee role not provided.");
                return;
            }

            String username = resultUsername.get();
            String password = resultPassword.get();
            String role = resultRole.get();

            Employee newEmployee = new Employee(username, password, role);
            databaseManager.addEmployee(newEmployee);
            System.out.println("Employee added: " + newEmployee.getUsername());
        } else {
            System.out.println("You must log in as a manager to add employees.");
        }
    }

    @FXML
    private void handleViewEmployees(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            List<Employee> employees = databaseManager.getEmployees();
            StringBuilder employeeList = new StringBuilder();
            for (Employee employee : employees) {
                employeeList.append(employee.getUsername()).append(" - ").append(employee.getRole()).append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("View Employees");
            alert.setHeaderText("List of employees");
            alert.setContentText(employeeList.toString());
            alert.showAndWait();
        } else {
            System.out.println("You must log in as a manager to view employees.");
        }
    }

    @FXML
    private void handleServiceRequest(ActionEvent event) {
        if (currentCustomer != null) {
            TextInputDialog descriptionDialog = new TextInputDialog();
            descriptionDialog.setTitle("Service Request");
            descriptionDialog.setHeaderText("Enter service description");
            descriptionDialog.setContentText("Description:");
            Optional<String> resultDescription = descriptionDialog.showAndWait();
            if (!resultDescription.isPresent()) {
                System.out.println("Service description not provided.");
                return;
            }

            TextInputDialog dateDialog = new TextInputDialog();
            dateDialog.setTitle("Service Request");
            dateDialog.setHeaderText("Enter service date");
            dateDialog.setContentText("Date:");
            Optional<String> resultDate = dateDialog.showAndWait();
            if (!resultDate.isPresent()) {
                System.out.println("Service date not provided.");
                return;
            }

            String description = resultDescription.get();
            String date = resultDate.get();
            Order serviceOrder = new Order(currentCustomer, new ArrayList<>(), Type.SERVICE);
            serviceOrder.setStatus("Pending");
            Product serviceProduct = new Product("Service Request", Category.COMPONENTE, 0.0, description + " - Date: " + date, 0);
            serviceOrder.getProducts().add(serviceProduct);
            databaseManager.placeOrder(serviceOrder);
            updateOrderListView();
            orderList.getItems().add(serviceOrder);
            System.out.println("Service request added: " + description + " on " + date);
        } else {
            System.out.println("You must log in as a customer to place a service request.");
        }
    }
}

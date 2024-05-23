package com.example.proiectfis2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelloController {
    @FXML
    private ListView<Product> productList;

    @FXML
    private ListView<Product> cartList;

    @FXML
    private ListView<Order> orderList;

    @FXML
    private ListView<ServiceRequest> serviceRequestList; // Add service request list

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton, addButton, addPromoButton, removePromoButton, addToCartButton, removeFromCartButton, placeOrderButton, changeStatusButton, addEmployeeButton, viewEmployeesButton, serviceRequestButton, removeCompletedButton;

    private DatabaseManager databaseManager = new DatabaseManager();
    private Employee currentEmployee;
    private Customer currentCustomer;
    private List<Product> cart = new ArrayList<>();

    @FXML
    public void initialize() {
        // Set cell factories for custom list view cells
        productList.setCellFactory(param -> new ProductCell());
        orderList.setCellFactory(param -> new OrderCell());
        serviceRequestList.setCellFactory(param -> new ServiceRequestCell());
        cartList.setCellFactory(param -> new ProductCell());

        // Load initial data and update list views
        updateProductListView();
        updateOrderListView();
        updateCartListView();
        updateServiceRequestListView();

        // Set button access based on initial conditions
        setButtonAccess(false, false, false, false, false, false, false, false, false, false);
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

    private void updateServiceRequestListView() {
        List<ServiceRequest> serviceRequests = databaseManager.getServiceRequests();
        serviceRequestList.setItems(FXCollections.observableArrayList(serviceRequests));
    }

    private void setButtonAccess(boolean addProduct, boolean addPromo, boolean removePromo, boolean addToCart, boolean removeFromCart, boolean placeOrder, boolean changeStatus, boolean addEmployee, boolean viewEmployees, boolean removeCompleted) {
        addButton.setVisible(addProduct);
        addPromoButton.setVisible(addPromo);
        removePromoButton.setVisible(removePromo);
        addToCartButton.setVisible(addToCart);
        removeFromCartButton.setVisible(removeFromCart);
        placeOrderButton.setVisible(placeOrder);
        changeStatusButton.setVisible(changeStatus);
        addEmployeeButton.setVisible(addEmployee);
        viewEmployeesButton.setVisible(viewEmployees);
        serviceRequestButton.setVisible(true); // Always visible for customers
        removeCompletedButton.setVisible(removeCompleted);

        // Set ListView visibility
        orderList.setVisible(addEmployee || changeStatus || removeCompleted);
        serviceRequestList.setVisible(addEmployee || changeStatus || removeCompleted);
    }

    private void showAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    private void setListViewVisibility(boolean isVisible) {
        orderList.setVisible(isVisible);
        serviceRequestList.setVisible(isVisible);
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
            setButtonAccess(false, false, false, true, true, true, false, false, false, false);
        } else if (databaseManager.authenticateEmployee(username, password)) {
            // Autentificare angajat
            currentEmployee = databaseManager.getEmployee(username);
            System.out.println("Logged in as: " + currentEmployee.getUsername());
            switch (currentEmployee.getRole()) {
                case "manager":
                    setButtonAccess(false, true, true, true, true, true, false, true, true, true);
                    break;
                case "senior":
                    setButtonAccess(true, false, false, true, true, true, true, false, false, true);
                    break;
                case "junior":
                    setButtonAccess(false, false, false, true, true, true, true, false, false, true);
                    break;
            }
        } else {
            System.out.println("Invalid login credentials");
            setButtonAccess(false, false, false, false, false, false, false, false, false, false);
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
                showAlert(Alert.AlertType.ERROR, "Add Product", "Product category not selected", "Please select a category.");
                return;
            }

            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Add Product");
            nameDialog.setHeaderText("Enter product name");
            nameDialog.setContentText("Name:");
            Optional<String> resultName = nameDialog.showAndWait();
            if (!resultName.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Add Product", "Product name not provided", "Please enter a product name.");
                return;
            }

            TextInputDialog priceDialog = new TextInputDialog();
            priceDialog.setTitle("Add Product");
            priceDialog.setHeaderText("Enter product price");
            priceDialog.setContentText("Price:");
            Optional<String> resultPrice = priceDialog.showAndWait();
            if (!resultPrice.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Add Product", "Product price not provided", "Please enter a product price.");
                return;
            }

            TextInputDialog descriptionDialog = new TextInputDialog();
            descriptionDialog.setTitle("Add Product");
            descriptionDialog.setHeaderText("Enter product description");
            descriptionDialog.setContentText("Description:");
            Optional<String> resultDescription = descriptionDialog.showAndWait();
            if (!resultDescription.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Add Product", "Product description not provided", "Please enter a product description.");
                return;
            }

            try {
                Category category = resultCategory.get();
                String name = resultName.get();
                double price = Double.parseDouble(resultPrice.get());
                String description = resultDescription.get();

                Product product = new Product(name, category, price, description, 4, category == Category.COMPONENTE_PRE);
                databaseManager.addProduct(product, currentEmployee); // This method saves the products list to JSON
                updateProductListView();
                showAlert(Alert.AlertType.INFORMATION, "Add Product", "Product added", "Product added: " + product.getName());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Add Product", "Invalid price format", "Please enter a valid price.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Add Product", "Permission denied", "You must log in as a senior employee to add products.");
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
                showAlert(Alert.AlertType.ERROR, "Add Promotion", "Product not selected", "Please select a product.");
                return;
            }

            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Add Promotion");
            nameDialog.setHeaderText("Enter promotion name");
            nameDialog.setContentText("Name:");
            Optional<String> resultName = nameDialog.showAndWait();
            if (!resultName.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Add Promotion", "Promotion name not provided", "Please enter a promotion name.");
                return;
            }

            Product selectedProduct = resultProduct.get();
            String promotionName = resultName.get();
            Promotion promotion = new Promotion(promotionName, List.of(selectedProduct), 10.0);
            databaseManager.addPromotion(promotion, currentEmployee);
            updateProductListView();
            showAlert(Alert.AlertType.INFORMATION, "Add Promotion", "Promotion added", "Promotion added to product: " + selectedProduct.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Add Promotion", "Permission denied", "You must be a manager to add new promotions.");
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
                showAlert(Alert.AlertType.ERROR, "Remove Promotion", "Promotion not selected", "Please select a promotion.");
                return;
            }

            Promotion selectedPromotion = resultPromotion.get();
            databaseManager.removePromotion(selectedPromotion, currentEmployee);
            updateProductListView();
            showAlert(Alert.AlertType.INFORMATION, "Remove Promotion", "Promotion removed", "Promotion removed: " + selectedPromotion.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Remove Promotion", "Permission denied", "You must be a manager to remove promotions.");
        }
    }

    @FXML
    private void handleAddToCart(ActionEvent event) {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            double discount = databaseManager.getDiscountForProduct(selectedProduct);
            double discountedPrice = selectedProduct.getPrice() * (1 - discount / 100);

            Product productToAdd = new Product(
                    selectedProduct.getName(),
                    selectedProduct.getCategory(),
                    discountedPrice,
                    selectedProduct.getDescription(),
                    selectedProduct.getRating(),
                    selectedProduct.isPart()
            );

            cart.add(productToAdd);
            updateCartListView();
            showAlert(Alert.AlertType.INFORMATION, "Add to Cart", "Product added to cart", "Product added: " + productToAdd.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Add to Cart", "No product selected", "Please select a product to add to cart.");
        }
    }

    @FXML
    private void handleRemoveFromCart(ActionEvent event) {
        Product selectedProduct = cartList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            cart.remove(selectedProduct);
            updateCartListView();
            showAlert(Alert.AlertType.INFORMATION, "Remove from Cart", "Product removed from cart", "Product removed: " + selectedProduct.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Remove from Cart", "No product selected", "Please select a product to remove from cart.");
        }
    }

    @FXML
    private void handlePlaceOrder(ActionEvent event) {
        if (currentCustomer != null) {
            if (!cart.isEmpty()) {
                double totalPrice = 0;
                boolean hasPreAssembledParts = false;

                for (Product product : cart) {
                    totalPrice += product.getPrice();
                    if (product.getCategory() == Category.COMPONENTE_PRE) {
                        hasPreAssembledParts = true;
                    }
                }

                if (hasPreAssembledParts) {
                    totalPrice += 100; // Adding extra charge for pre-assembled parts
                }

                List<Product> orderProducts = new ArrayList<>(cart);
                cart.clear();
                updateCartListView();
                Order order = new Order(currentCustomer, orderProducts, "Pending");
                databaseManager.placeOrder(order); // This method saves the orders list to JSON
                updateOrderListView();
                showAlert(Alert.AlertType.INFORMATION, "Place Order", "Order placed", "Order placed. Total price: " + totalPrice);
            } else {
                showAlert(Alert.AlertType.ERROR, "Place Order", "Cart is empty", "Please add products to the cart before placing an order.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Place Order", "Permission denied", "You must log in as a customer to place an order.");
        }
    }

    @FXML
    private void handleOrderStatusChange(ActionEvent event) {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            ChoiceDialog<String> statusDialog = new ChoiceDialog<>("Pending", "Pending", "Processing", "Completed", "Cancelled");
            statusDialog.setTitle("Change Order Status");
            statusDialog.setHeaderText("Select new status for the order");
            statusDialog.setContentText("Status:");
            Optional<String> resultStatus = statusDialog.showAndWait();
            if (!resultStatus.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Change Order Status", "Order status not selected", "Please select a new order status.");
                return;
            }

            String newStatus = resultStatus.get();
            selectedOrder.setStatus(newStatus);
            updateOrderListView();
            showAlert(Alert.AlertType.INFORMATION, "Change Order Status", "Order status changed", "Order status changed to: " + newStatus);
        } else {
            showAlert(Alert.AlertType.ERROR, "Change Order Status", "No order selected", "Please select an order to change its status.");
        }
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Add Employee");
            usernameDialog.setHeaderText("Enter employee username");
            usernameDialog.setContentText("Username:");
            Optional<String> resultUsername = usernameDialog.showAndWait();
            if (!resultUsername.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Add Employee", "Employee username not provided", "Please enter an employee username.");
                return;
            }

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Add Employee");
            passwordDialog.setHeaderText("Enter employee password");
            passwordDialog.setContentText("Password:");
            Optional<String> resultPassword = passwordDialog.showAndWait();
            if (!resultPassword.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Add Employee", "Employee password not provided", "Please enter an employee password.");
                return;
            }

            ChoiceDialog<String> roleDialog = new ChoiceDialog<>("junior", "junior", "senior");
            roleDialog.setTitle("Add Employee");
            roleDialog.setHeaderText("Select employee role");
            roleDialog.setContentText("Role:");
            Optional<String> resultRole = roleDialog.showAndWait();
            if (!resultRole.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Add Employee", "Employee role not selected", "Please select an employee role.");
                return;
            }

            String username = resultUsername.get();
            String password = resultPassword.get();
            String role = resultRole.get();

            Employee employee = new Employee(username, password, role);
            databaseManager.addEmployee(employee); // This method saves the employees list to JSON
            showAlert(Alert.AlertType.INFORMATION, "Add Employee", "Employee added", "Employee added: " + username);
        } else {
            showAlert(Alert.AlertType.ERROR, "Add Employee", "Permission denied", "You must log in as a manager to add employees.");
        }
    }

    @FXML
    private void handleRemoveCompleted(ActionEvent event) {
        if (currentEmployee != null) {
            if ("manager".equals(currentEmployee.getRole()) || "senior".equals(currentEmployee.getRole()) || "junior".equals(currentEmployee.getRole())) {
                // Retrieve the items in the orderList
                ObservableList<Order> items = orderList.getItems();

                // Remove completed and canceled orders
                List<Order> completedOrCanceledOrders = items.stream()
                        .filter(order -> order.getStatus().equals("Completed") || order.getStatus().equals("Cancelled"))
                        .collect(Collectors.toList());
                items.removeAll(completedOrCanceledOrders);

                // Update the orderList view
                orderList.setItems(items);

                // Update orders in the database
                databaseManager.updateOrders(items);

                showAlert(Alert.AlertType.INFORMATION, "Remove Completed", "Completed or canceled orders removed", "Completed or canceled orders removed.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Remove Completed", "Permission denied", "Only managers, senior, or junior employees can remove completed or canceled orders.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Remove Completed", "Permission denied", "You must be logged in as a manager, senior, or junior employee to remove completed or canceled orders.");
        }
    }

    @FXML
    private void handleViewEmployees(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            List<Employee> employees = databaseManager.getEmployees();
            StringBuilder employeeInfo = new StringBuilder("Employees:\n");
            for (Employee employee : employees) {
                employeeInfo.append("Username: ").append(employee.getUsername()).append(", Role: ").append(employee.getRole()).append("\n");
            }
            showAlert(Alert.AlertType.INFORMATION, "View Employees", "Employee Information", employeeInfo.toString());
        } else {
            showAlert(Alert.AlertType.ERROR, "View Employees", "Permission denied", "You must log in as a manager to view employees.");
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
                showAlert(Alert.AlertType.ERROR, "Service Request", "Service description not provided", "Please enter a service description.");
                return;
            }

            TextInputDialog dateDialog = new TextInputDialog();
            dateDialog.setTitle("Service Request");
            dateDialog.setHeaderText("Enter service date");
            dateDialog.setContentText("Date:");
            Optional<String> resultDate = dateDialog.showAndWait();
            if (!resultDate.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Service Request", "Service date not provided", "Please enter a service date.");
                return;
            }

            String description = resultDescription.get();
            String date = resultDate.get();

            ServiceRequest serviceRequest = new ServiceRequest(currentCustomer, description, date);
            databaseManager.addServiceRequest(serviceRequest);
            updateServiceRequestListView(); // Update the view
            showAlert(Alert.AlertType.INFORMATION, "Service Request", "Service request submitted", "Service request submitted: " + description);
        } else {
            showAlert(Alert.AlertType.ERROR, "Service Request", "Permission denied", "You must log in as a customer to request service.");
        }
    }
}

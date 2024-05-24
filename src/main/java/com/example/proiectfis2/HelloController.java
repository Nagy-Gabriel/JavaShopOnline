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
    private ListView<ServiceRequest> serviceRequestList;

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
        //PENTRU A VIZIONA MAI FRUMOS PRODUSELE
        productList.setCellFactory(param -> new ProductCell());
        orderList.setCellFactory(param -> new OrderCell());
        serviceRequestList.setCellFactory(param -> new ServiceRequestCell());
        cartList.setCellFactory(param -> new ProductCell());

        //incarcam datele initiale si dam update la listview.
        updateProductListView();
        updateOrderListView();
        updateCartListView();
        updateServiceRequestListView();

       //setam  accesul la butoane cand pornim aplicatie. initial sunt toate false adic off.
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
        serviceRequestButton.setVisible(true);
        removeCompletedButton.setVisible(removeCompleted);

      //pentru a seta limitele la listview.
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
        //resetam userul curent
        currentEmployee = null;
        currentCustomer = null;

        // Autentificare client
        if (databaseManager.authenticateCustomer(username, password)) {
            currentCustomer = databaseManager.getCustomer(username);
            showAlert(Alert.AlertType.INFORMATION,"Log In","Bine ai venit!","Cont client: " + username);
            setButtonAccess(false, false, false, true, true, true, false, false, false, false);
        } else if (databaseManager.authenticateEmployee(username, password)) {
            // Autentificare angajat
            currentEmployee = databaseManager.getEmployee(username);
            showAlert(Alert.AlertType.INFORMATION,"Log In","Bine ai venit!","Angajatul este:: " + currentEmployee.getUsername());
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
            showAlert(Alert.AlertType.ERROR, "Eroare autentificare", "Date invalide", "Username sau parola gresita!");
            setButtonAccess(false, false, false, false, false, false, false, false, false, false);
        }
    }

    @FXML
    private void handleAddProduct(ActionEvent event) {
        if (currentEmployee != null && "senior".equals(currentEmployee.getRole())) {
            ChoiceDialog<Category> categoryDialog = new ChoiceDialog<>(Category.DESKTOP_PC, Category.values());
            categoryDialog.setTitle("Adaugare produs");
            categoryDialog.setHeaderText("Alegeti categoria din care face parte produsul");
            categoryDialog.setContentText("Categorie:");
            Optional<Category> resultCategory = categoryDialog.showAndWait();
            if (!resultCategory.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugare produs", "Categoria produsul nu a fost selectata", "Va rugam selectati o categorie.");
                return;
            }

            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Adaugare produs");
            nameDialog.setHeaderText("Introduceti numele produsului");
            nameDialog.setContentText("Nume:");
            Optional<String> resultName = nameDialog.showAndWait();
            if (!resultName.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugare produs", "Numele produsuli nu a fost dat", "Va rugam introduceti un nume pentru produs");
                return;
            }

            TextInputDialog priceDialog = new TextInputDialog();
            priceDialog.setTitle("Adaugare produs");
            priceDialog.setHeaderText("Pretul produsului");
            priceDialog.setContentText("Pret:");
            Optional<String> resultPrice = priceDialog.showAndWait();
            if (!resultPrice.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugare produs", "Pretul produsului nu a fost dat", "Va rugam introduceti un pret.");
                return;
            }

            TextInputDialog descriptionDialog = new TextInputDialog();
            descriptionDialog.setTitle("Adaugare produs");
            descriptionDialog.setHeaderText("Introduceti descrierea produslui");
            descriptionDialog.setContentText("Descriere:");
            Optional<String> resultDescription = descriptionDialog.showAndWait();
            if (!resultDescription.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugare produs", "Descrierea produsului nu a fost scrisa", "Va rugam introduceti o descriere");
                return;
            }

            try {
                Category category = resultCategory.get();
                String name = resultName.get();
                double price = Double.parseDouble(resultPrice.get());
                String description = resultDescription.get();

                Product product = new Product(name, category, price, description, 4, category == Category.COMPONENTE_PRE);
                databaseManager.addProduct(product, currentEmployee); //salvam produsele in json.
                updateProductListView();
                showAlert(Alert.AlertType.INFORMATION, "Adaugare produse", "Produs adaugat", "Product added: " + product.getName());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Adaugare produse", "Formatul pretului este gresit", "Introduceti un pret valid(Ex:1000.0)");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Adaugare produse", "Lipsa permisiuni", "Trebuie sa fiti angajat senior pentru a putea adauga produse!");
        }
    }

    @FXML
    private void handleAddPromotion(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            List<Product> products = databaseManager.getProducts();
            ChoiceDialog<Product> productDialog = new ChoiceDialog<>(products.get(0), products);
            productDialog.setTitle("Adaugarea promotie");
            productDialog.setHeaderText("Selectati produsul caruia doriti sa-i adaugati promotie");
            productDialog.setContentText("Produs:");
            Optional<Product> resultProduct = productDialog.showAndWait();
            if (!resultProduct.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugarea promotie", "Nici-un produs selectat", "Va rugam selectati un produs");
                return;
            }

            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Adaugarea promotie");
            nameDialog.setHeaderText("Numele promotiei:");
            nameDialog.setContentText("Nume:");
            Optional<String> resultName = nameDialog.showAndWait();
            if (!resultName.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugarea promotie", "Numele promotiei nu a fost dat", "Va rugam introduceti un nume pentru promotie");
                return;
            }

            Product selectedProduct = resultProduct.get();
            String promotionName = resultName.get();
            Promotion promotion = new Promotion(promotionName, List.of(selectedProduct), 10.0);
            databaseManager.addPromotion(promotion, currentEmployee);
            updateProductListView();
            showAlert(Alert.AlertType.INFORMATION, "Adaugarea promotie", "Promotie adaugata", "Promotia a fost adauga produslui: " + selectedProduct.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Adaugarea promotie", "Lipsa permisiuni", "Trebuie sa fiti manager pentru a adauga/sterge promotii");
        }
    }

    @FXML
    private void handleRemovePromotion(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            List<Promotion> promotions = databaseManager.getPromotions();
            ChoiceDialog<Promotion> promoDialog = new ChoiceDialog<>(promotions.get(0), promotions);
            promoDialog.setTitle("Stergere promotie");
            promoDialog.setHeaderText("Selectati promotia pe care doriti sa o stergeti");
            promoDialog.setContentText("Promotii:");

            Optional<Promotion> resultPromotion = promoDialog.showAndWait();
            if (!resultPromotion.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Stergere promotii", "Nici-o promotie selectata", "Va rugam selectati o promotie");
                return;
            }

            Promotion selectedPromotion = resultPromotion.get();
            databaseManager.removePromotion(selectedPromotion, currentEmployee);
            updateProductListView();
            showAlert(Alert.AlertType.INFORMATION, "Stergere promotii", "Promotie stearsa", "Promotie stearsa: " + selectedPromotion.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Stergere promotii", "Lipsa permisiuni", "Trebuie sa fiti manager pentru a putea adauga/sterge promotii");
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
            showAlert(Alert.AlertType.INFORMATION, "Adauga in cos", "Produs adaugat in cos", "Produs adaugat: " + productToAdd.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Adauga in cos", "Nici-un produs selectat", "Va rugam selectati produsul pe care dortii sa il adaugati.");
        }
    }

    @FXML
    private void handleRemoveFromCart(ActionEvent event) {
        Product selectedProduct = cartList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            cart.remove(selectedProduct);
            updateCartListView();
            showAlert(Alert.AlertType.INFORMATION, "Stergere din cos", "Produs sters din cos", "Produs sters: " + selectedProduct.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Stergere din cos", "Nici-un produs selectat", "Va rugam selectati produsul pe care doriti sa-l stergeti.");
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
                    totalPrice += 100; //adaugam o taxa suplimentara de 100 ron pentr produse preasamblate.
                }

                List<Product> orderProducts = new ArrayList<>(cart);
                cart.clear();
                updateCartListView();
                Order order = new Order(currentCustomer, orderProducts, "Pending");
                databaseManager.placeOrder(order); // This method saves the orders list to JSON
                updateOrderListView();
                showAlert(Alert.AlertType.INFORMATION, "Plasati comanda", "Comanda plasata", "Comanda plasata. Pret total: " + totalPrice);
            } else {
                showAlert(Alert.AlertType.ERROR, "Plasati comanda", "Cosul este gol", "Va rugam adaugati produse in cos pentru a putea plasa o comanda");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Plasati comanda", "Lipsa permisiuni", "Trebuie sa fiti logat ca si client pentru a trimite o comanda");
        }
    }

    @FXML
    private void handleOrderStatusChange(ActionEvent event) {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            ChoiceDialog<String> statusDialog = new ChoiceDialog<>("Pending", "Pending", "Processing", "Completed", "Cancelled");
            statusDialog.setTitle("Schimba status comanda");
            statusDialog.setHeaderText("Selectati noul status al comenzii");
            statusDialog.setContentText("Status:");
            Optional<String> resultStatus = statusDialog.showAndWait();
            if (!resultStatus.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Schimba status comanda", "Nici-un status nu a fost selectat", "Va rugam selectati un nou status pentru comanda");
                return;
            }

            String newStatus = resultStatus.get();
            selectedOrder.setStatus(newStatus);
            updateOrderListView();
            showAlert(Alert.AlertType.INFORMATION, "Schimba status comanda", "Status comanda schimbat", "Status-ul comenzii a fost schimbat: " + newStatus);
        } else {
            showAlert(Alert.AlertType.ERROR, "Schimba status comanda", "Nici-o comanda selectata", "Va rugam selectati o comanda pentru a schimba statusul.");
        }
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Adaugarea angajati");
            usernameDialog.setHeaderText("Nume angajat");
            usernameDialog.setContentText("Username:");
            Optional<String> resultUsername = usernameDialog.showAndWait();
            if (!resultUsername.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugare angajat", "Nume anagajat invalid", "Va rugam introduceti un nume pentru noul angajat");
                return;
            }

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Adaugare angajat");
            passwordDialog.setHeaderText("Parola angajat");
            passwordDialog.setContentText("Password:");
            Optional<String> resultPassword = passwordDialog.showAndWait();
            if (!resultPassword.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugare angajat", "Parola angajat invalida", "Va rugam introduceti o parola pentru noul angajat");
                return;
            }

            ChoiceDialog<String> roleDialog = new ChoiceDialog<>("junior", "junior", "senior");
            roleDialog.setTitle("Adaugare angajat");
            roleDialog.setHeaderText("Rol angajat");
            roleDialog.setContentText("Rol:");
            Optional<String> resultRole = roleDialog.showAndWait();
            if (!resultRole.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Adaugare angajat", "Rol angajat invalid", "Va rugam selectati un rol pentru angajat");
                return;
            }

            String username = resultUsername.get();
            String password = resultPassword.get();
            String role = resultRole.get();

            Employee employee = new Employee(username, password, role);
            databaseManager.addEmployee(employee); // This method saves the employees list to JSON
            showAlert(Alert.AlertType.INFORMATION, "Adaugare angajat", "Angajat adaugat", "Angajat adaugat: " + username);
        } else {
            showAlert(Alert.AlertType.ERROR, "Adaugare angajat", "Lipsa permisiuni", "Trebuie sa fiti logat ca manager pentru a adauga noi angajati");
        }
    }

    @FXML
    private void handleRemoveCompleted(ActionEvent event) {
        if (currentEmployee != null) {
            if ("manager".equals(currentEmployee.getRole()) || "senior".equals(currentEmployee.getRole()) || "junior".equals(currentEmployee.getRole())) {
                ObservableList<Order> items = orderList.getItems();

               //stergem comenziile terminate sau care au fost anulate
                List<Order> completedOrCanceledOrders = items.stream()
                        .filter(order -> order.getStatus().equals("Completed") || order.getStatus().equals("Cancelled"))
                        .collect(Collectors.toList());
                items.removeAll(completedOrCanceledOrders);

                //updatam lista
                orderList.setItems(items);

                //updatam in baza de date.
                databaseManager.updateOrders(items);

                showAlert(Alert.AlertType.INFORMATION, "Comenzi sterse", "Comenziile terminate sau anulate au fost sterse", "Stergere cu succes");
            } else {
                showAlert(Alert.AlertType.ERROR, "Comenzi sterse", "Fara permisiune", "Doar angajatii pot sterge listele cu comenzi");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Comenzi sterse", "Permisiuni invalide", "Doar anagajtii pot sterge comenzi din aceasta lista!");
        }
    }

    @FXML
    private void handleViewEmployees(ActionEvent event) {
        if (currentEmployee != null && "manager".equals(currentEmployee.getRole())) {
            List<Employee> employees = databaseManager.getEmployees();
            StringBuilder employeeInfo = new StringBuilder("Angajati:\n");
            for (Employee employee : employees) {
                employeeInfo.append("Username: ").append(employee.getUsername()).append(", Rol: ").append(employee.getRole()).append("\n");
            }
            showAlert(Alert.AlertType.INFORMATION, "Vizualizare angajati", "Informatii angajati", employeeInfo.toString());
        } else {
            showAlert(Alert.AlertType.ERROR, "Vizualizare angajati", "Permisiuni invalide", "Doar managerii pot sa vada lista de angajati");
        }
    }

    @FXML
    private void handleServiceRequest(ActionEvent event) {
        if (currentCustomer != null) {
            TextInputDialog descriptionDialog = new TextInputDialog();
            descriptionDialog.setTitle("Cerere service");
            descriptionDialog.setHeaderText("Introduceti o descriere a problemei");
            descriptionDialog.setContentText("Desciere:");
            Optional<String> resultDescription = descriptionDialog.showAndWait();
            if (!resultDescription.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Cerere service", "Descrierea invalida", "Va rugam introduceti o scurta descriere a problemei");
                return;
            }

            TextInputDialog dateDialog = new TextInputDialog();
            dateDialog.setTitle("Cerere service");
            dateDialog.setHeaderText("Data in care doriti service-ul:");
            dateDialog.setContentText("Data:");
            Optional<String> resultDate = dateDialog.showAndWait();
            if (!resultDate.isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Cerere service", "Data service invalida", "Va rugam introducetii o data pentru preluare in service");
                return;
            }

            String description = resultDescription.get();
            String date = resultDate.get();

            ServiceRequest serviceRequest = new ServiceRequest(currentCustomer, description, date);
            databaseManager.addServiceRequest(serviceRequest);
            updateServiceRequestListView(); // Update the view
            showAlert(Alert.AlertType.INFORMATION, "Cerere service", "Cerere service trimisa", "Cerere service trimisa: " + description);
        } else {
            showAlert(Alert.AlertType.ERROR, "Cerere service", "Permisiuni invalide", "Trebuie sa aveti cont de client pentru trimiterea unei cerere service!");
        }
    }
}

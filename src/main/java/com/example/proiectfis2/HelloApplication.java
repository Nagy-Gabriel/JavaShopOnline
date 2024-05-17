package com.example.proiectfis2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class HelloApplication extends Application {

    private DatabaseManager databaseManager;

    public HelloApplication() {
        // Initializează managerul bazei de date
        databaseManager = new DatabaseManager();
    }
    @Override
    public void start(Stage primaryStage) {
        // Elemente UI
        Label titleLabel = new Label("Aplicatie Componente Calculatoare");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button viewEmployeesButton = new Button("Vizualizează Angajații");
        viewEmployeesButton.setOnAction(e -> viewEmployees());

        Button viewProductsButton = new Button("Vizualizează Produsele");
        viewProductsButton.setOnAction(e -> viewProducts());

        Button viewOrdersButton = new Button("Vizualizează Comenzile");
        viewOrdersButton.setOnAction(e -> viewOrders());

        Button addEmployeeButton = new Button("Adaugă Angajat");
        addEmployeeButton.setOnAction(e -> addEmployee());

        Button addProductButton = new Button("Adaugă Produs");
        addProductButton.setOnAction(event -> {
            // Aici poți afișa o fereastră de dialog sau alte componente pentru a solicita informațiile despre noul produs
            // De exemplu, putem folosi un Dialog pentru a afișa câmpurile necesare pentru a crea un nou produs
            Dialog<Product> dialog = new Dialog<>();
            dialog.setTitle("Adăugare Produs");

            // Setăm butoanele pentru dialog
            ButtonType addButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            // Creăm câmpurile pentru introducerea datelor
            TextField nameField = new TextField();
            nameField.setPromptText("Nume");

            ComboBox<Category> categoryComboBox = new ComboBox<>();
            categoryComboBox.getItems().addAll(Category.values());
            categoryComboBox.setPromptText("Categorie");

            TextField priceField = new TextField();
            priceField.setPromptText("Preț");

            TextField descriptionField = new TextField();
            descriptionField.setPromptText("Descriere");

            TextField criticScoreField = new TextField();
            criticScoreField.setPromptText("Scor critic");

            // Adăugăm câmpurile la layout-ul dialogului
            GridPane grid = new GridPane();
            grid.add(new Label("Nume:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Categorie:"), 0, 1);
            grid.add(categoryComboBox, 1, 1);
            grid.add(new Label("Preț:"), 0, 2);
            grid.add(priceField, 1, 2);
            grid.add(new Label("Descriere:"), 0, 3);
            grid.add(descriptionField, 1, 3);
            grid.add(new Label("Scor critic:"), 0, 4);
            grid.add(criticScoreField, 1, 4);

            dialog.getDialogPane().setContent(grid);

            // Așteptăm până când utilizatorul introduce datele și apasă un buton
            Optional<Product> result = dialog.showAndWait();

            // Dacă utilizatorul a apăsat butonul de adăugare și au fost introduse date valide, adăugăm produsul
            result.ifPresent(newProduct -> {
                if (newProduct != null) {
                    // Adăugăm produsul în baza de date folosind angajatul curent
                 //   addProduct();
                    System.out.println("Produs adăugat cu succes.");
                }
            });
        });


        Button placeOrderButton = new Button("Plasează Comandă");
        placeOrderButton.setOnAction(e -> placeOrder());

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                titleLabel,
                viewEmployeesButton,
                viewProductsButton,
                viewOrdersButton,
                addEmployeeButton,
                addProductButton,
                placeOrderButton
        );

        // Scene
        Scene scene = new Scene(layout, 400, 400);

        // Stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Aplicație de Management");
        primaryStage.show();
    }

    private void viewEmployees() {
        List<Employee> employees = databaseManager.getEmployees();
        for (Employee employee : employees) {
            System.out.println(employee.getName() + " - " + employee.getRole());
        }
    }

    private void viewProducts() {
        List<Product> products = databaseManager.getProducts();
        for (Product product : products) {
            System.out.println(product.getName() + " - " + product.getCategory() + " - " + product.getPrice());
        }
    }

    private void viewOrders() {
        List<Order> orders = databaseManager.getOrders();
        if (orders.isEmpty()) {
            System.out.println("Nu există comenzi disponibile.");
        } else {
            for (Order order : orders) {
                System.out.println("Client: " + order.getCustomer().getUsername() +
                        ", Produse: " + order.getProducts() +
                        ", Tip: " + order.getType() +
                        ", Descriere: " + order.getDescription());
            }
        }
    }


    private void addEmployee() {
        // Aici poți adăuga logica pentru a solicita informațiile despre noul angajat de la utilizator și a-l adăuga în baza de date
        Employee newEmployee = new Employee("Nume", "Rol");
        databaseManager.addEmployee(newEmployee);
        System.out.println("Angajat adăugat cu succes.");
    }

    private void addProduct(Employee employee) {
        // Aici poți adăuga logica pentru a solicita informațiile despre noul produs de la utilizator și a-l adăuga în baza de date
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceți numele produsului:");
        String productName = scanner.nextLine();
        System.out.println("Introduceți categoria produsului (DESKTOP_PC, LAPTOP, IMPRIMANTE sau PERIFERICE):");
        Category category = Category.valueOf(scanner.nextLine());
        System.out.println("Introduceți prețul produsului:");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consumăm newline
        System.out.println("Introduceți descrierea produsului:");
        String description = scanner.nextLine();
        System.out.println("Introduceți stocul produsului:");
        int stock = scanner.nextInt();

        Product newProduct = new Product(productName, category, price, description, stock);
        databaseManager.addProduct(newProduct, employee);
        System.out.println("Produs adăugat cu succes.");
    }



    private void placeOrder() {
        Scanner scanner = new Scanner(System.in);

        // Obțineți clientul care plasează comanda
        System.out.println("Introduceți numele de utilizator al clientului:");
        String username = scanner.nextLine();
        Customer customer = databaseManager.getCustomerByUsername(username);
        if (customer == null) {
            System.out.println("Clientul nu există în baza de date.");
            return;
        }

        // Obțineți lista de produse comandate de la utilizator
        List<Product> orderedProducts = new ArrayList<>();
        System.out.println("Introduceți numărul de produse comandate:");
        int numProducts = scanner.nextInt();
        scanner.nextLine(); // Consumăm newline
        for (int i = 0; i < numProducts; i++) {
            System.out.println("Introduceți numele produsului " + (i + 1) + ":");
            String productName = scanner.nextLine();
            // Verificați dacă produsul există în baza de date și adăugați-l în lista de produse comandate
            Product product = databaseManager.getProductByName(productName);
            if (product != null) {
                orderedProducts.add(product);
            } else {
                System.out.println("Produsul " + productName + " nu există în baza de date.");
            }
        }

        // Obțineți tipul comenzii de la utilizator
        System.out.println("Introduceți tipul comenzii (CUMPARARE sau SERVICE):");
        String orderType = scanner.nextLine();
        Type type = Type.valueOf(orderType);

        // Obțineți descrierea comenzii de la utilizator
        System.out.println("Introduceți descrierea comenzii:");
        String description = scanner.nextLine();

        // Creeazăți noua comandă și plasați-o în baza de date
        Order newOrder = new Order(customer, orderedProducts, type, description);
        databaseManager.placeOrder(newOrder);
        System.out.println("Comandă plasată cu succes.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

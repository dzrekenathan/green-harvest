package com.exams.farmingsystem.examsproject;

import com.exams.farmingsystem.examsproject.models.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SalesController implements Initializable {

    @FXML
    private TextField dateTF;

    @FXML
    private SplitMenuButton productName;

    @FXML
    private TextField salesProductQty;

    @FXML
    private TextField salesProductAmnt;

    @FXML
    private Button submitBtn;

    @FXML
    private Button clearBtn;

    private ObservableList<Product> products;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the current date
        dateTF.setText(formatDateWithSuffix(LocalDate.now()));

        // Fetch products from the database
        products = DatabaseClass.fetchProductsForSale();
        populateProductMenu();

        // Handle button actions
        submitBtn.setOnAction(event -> handleSubmit());
        clearBtn.setOnAction(event -> clearForm());
    }

    private void populateProductMenu() {
        if (products == null || products.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Products", "No products available in the database.");
            return;
        }

        for (Product product : products) {
            MenuItem menuItem = new MenuItem(product.getProductName());
            menuItem.setOnAction(event -> {
                productName.setText(product.getProductName());
                // Optionally pre-fill quantity or amount based on product data if desired
            });
            productName.getItems().add(menuItem);
        }
    }

    private void handleSubmit() {
        try {
            String selectedProductName = productName.getText();
            if (selectedProductName.equals("Select Product")) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a product.");
                return;
            }

            // Validate and parse inputs
            String qtyText = salesProductQty.getText().trim();
            String amountText = salesProductAmnt.getText().trim();
            if (qtyText.isEmpty() || amountText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity and Amount cannot be empty.");
                return;
            }

            int selectedQuantity = Integer.parseInt(qtyText);
            double selectedAmount = Double.parseDouble(amountText);

            if (selectedQuantity <= 0 || selectedAmount < 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity must be positive and Amount cannot be negative.");
                return;
            }

            // Find the selected product
            Product selectedProduct = products.stream()
                    .filter(product -> product.getProductName().equals(selectedProductName))
                    .findFirst()
                    .orElse(null);

            if (selectedProduct == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected product not found.");
                return;
            }

            // Validate quantity against stock
            if (selectedQuantity > selectedProduct.getQuantity()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity exceeds available stock (" + selectedProduct.getQuantity() + ").");
                return;
            }

            // Record the sale
            boolean success = DatabaseClass.recordSale(selectedProduct.getProductId(), selectedQuantity, selectedAmount);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Sale recorded successfully!");
                clearForm();
                // Refresh product list after sale
                products = DatabaseClass.fetchProductsForSale();
                productName.getItems().clear();
                populateProductMenu();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to record sale.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity must be an integer and Amount must be a number.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void clearForm() {
        productName.setText("Select Product");
        salesProductQty.setText("");
        salesProductAmnt.setText("");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static String formatDateWithSuffix(LocalDate date) {
        int day = date.getDayOfMonth();
        String suffix = getDaySuffix(day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d'" + suffix + "' MMM, yyyy");
        return date.format(formatter);
    }

    private static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }
}


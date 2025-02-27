package com.exams.farmingsystem.examsproject;

import com.exams.farmingsystem.examsproject.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProductController {

    @FXML
    private TextField productNameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockField;

    @FXML
    private TextField unitField;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        productNameField.setText(product.getProductName());
        priceField.setText(String.valueOf(product.getProductPrice()));
        stockField.setText(String.valueOf(product.getQuantity()));
        unitField.setText(product.getUnit());
    }

    @FXML
    private void saveProduct() {
        try {
            String name = productNameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            String unit = unitField.getText().trim();

            if (name.isEmpty() || unit.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Name and Unit cannot be empty.");
                return;
            }
            if (price < 0 || stock < 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Price and Stock cannot be negative.");
                return;
            }

            // Update the product in the database
            DatabaseClass.updateProduct(product.getProductId(), name, price, stock, unit);

            // Close the window
            Stage stage = (Stage) productNameField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Price must be a number and Stock must be an integer.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save product: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
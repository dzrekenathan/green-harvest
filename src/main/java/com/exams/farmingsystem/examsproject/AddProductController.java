package com.exams.farmingsystem.examsproject;

import com.exams.farmingsystem.examsproject.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProductController {

    @FXML
    private TextField productNameTF;

    @FXML
    private TextField unitPriceTF;

    @FXML
    private TextField quantityTF;
    @FXML
    private SplitMenuButton unitMenuBtn;
    @FXML
    private Button saveBtn;

    private Product product;


    @FXML
    private void handleSaveButtonAction() {
        String productName = productNameTF.getText();
        double unitPrice = Double.parseDouble(unitPriceTF.getText());
        int quantity = Integer.parseInt(quantityTF.getText());
        String unit = unitMenuBtn.getText();


        if (product == null) {
            // Add new product
            DatabaseClass.addProduct(productName, unitPrice, quantity, unit);
        } else {
            // Update existing product
            DatabaseClass.updateProduct(product.getProductId(), productName, unitPrice, quantity, unit);
        }

        // Close the popup
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }




    @FXML
    private void handleCancelButtonAction() {
        // Close the popup without saving
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }


    public void setProductData(Product product) {
        this.product = product;
        productNameTF.setText(product.getProductName());
        unitPriceTF.setText(String.valueOf(product.getProductPrice()));
        quantityTF.setText(String.valueOf(product.getProductStock()));
        unitMenuBtn.setText(product.getUnit());
//        statusMenuBtn.setText(product.getStatus());
    }



}

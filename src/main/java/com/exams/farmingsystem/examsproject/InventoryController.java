package com.exams.farmingsystem.examsproject;

import com.exams.farmingsystem.examsproject.models.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Integer> productId;

    @FXML
    private TableColumn<Product, Double> productPrice;

    @FXML
    private TableColumn<Product, Integer> productStock;

    @FXML
    private TableColumn<Product, String> unit;

    @FXML
    private TableColumn<Product, Void> actions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind columns to Product properties
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        unit.setCellValueFactory(new PropertyValueFactory<>("unit"));

        // Add buttons to the "Action" column
        actions.setCellFactory(createActionCellFactory());

        // Fetch data from the database
        refreshProductTable();
    }

    private void refreshProductTable() {
        ObservableList<Product> products = DatabaseClass.fetchProducts();
        productTable.setItems(products);
    }

    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                // Style buttons
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                // Handle Edit Button Action
                editButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleEditAction(product);
                });

                // Handle Delete Button Action
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleDeleteAction(product);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton));
                }
            }
        };
    }

    private void handleEditAction(Product product) {
        try {
            // Load the edit form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exams/farmingsystem/examsproject/editProduct.fxml"));
            Scene scene = new Scene(loader.load());
            EditProductController controller = loader.getController();
            controller.setProduct(product);

            // Show the edit dialog
            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh table after edit
            refreshProductTable();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load edit form: " + e.getMessage());
        }
    }

    private void handleDeleteAction(Product product) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete " + product.getProductName() + "?");
        confirmation.setContentText("Are you sure you want to delete this product? This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete from database
            boolean success = DatabaseClass.deleteProduct(product.getProductId());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully!");
                refreshProductTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete product.");
            }
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
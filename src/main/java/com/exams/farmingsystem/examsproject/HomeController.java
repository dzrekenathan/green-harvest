package com.exams.farmingsystem.examsproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private Button addProductBtn;
    @FXML
    private Button sellProductBtn;
    @FXML
    private Button inventoryBtn;
    @FXML
    private Button weatherBtn;

    @FXML
    private void openAddProductPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-product.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(scene);
            stage.show();

            // Optionally close the current signup window
//            Stage currentStage = (Stage) addProductBtn.getScene().getWindow();
//            currentStage.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load login screen.");
        }
    }

    @FXML
    private void openSalesPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sales.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Sell a Product");
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load login screen.");
        }
    }


    @FXML
    private void openInventory() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inventory.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Product Inventory");
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load Inventory screen.");
        }
    }


    @FXML
    private void openWeatherPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("weather.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Sell a Product");
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load login screen.");
        }
    }


    @FXML
    private void openAllSalesPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sales-table.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("All Sales");
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load login screen.");
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

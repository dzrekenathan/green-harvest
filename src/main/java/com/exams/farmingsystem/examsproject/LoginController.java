package com.exams.farmingsystem.examsproject;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button signInBTN;
    @FXML
    private TextField loginEmailTF;
    @FXML
    private TextField loginPasswordTF;
    @FXML
    private Label signUpLBL;
    @FXML
    private ImageView loginBrandingView;
    @FXML
    private ImageView loginLockImage;
    @FXML
    private Label errorMessageLabel;

    public void loginUser(ActionEvent actionEvent) {
        String email = loginEmailTF.getText();
        String password = loginPasswordTF.getText();

        // Clear previous error messages
        errorMessageLabel.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please fill in all fields.");
        } else {
            boolean success = DatabaseClass.loginUser(email, password);
            if (success) {
                showSuccessMessage("Login successful!");
                openHomePage();
            } else {
                showErrorMessage("Invalid email or password.");
            }
        }
    }

    public void openSignUpPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) signUpLBL.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the sign-up page.");
        }
    }

    private void showSuccessMessage(String message) {
        errorMessageLabel.setStyle("-fx-text-fill: green;");
        errorMessageLabel.setText(message);
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setStyle("-fx-text-fill: red;");
        errorMessageLabel.setText(message);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openHomePage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("HomePage");
            stage.setScene(scene);
            stage.show();

            // Optionally close the login window
            Stage currentStage = (Stage) signUpLBL.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Dashboard page.");
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //URL imageUrl = getClass().getResource("/images/popcorn-155602_1920.png");
        URL imageUrl = getClass().getResource("/images/resturant.jpg");
        URL lockUrl = getClass().getResource("/images/lock.png");
        if (imageUrl != null ) {
            Image brandingImage = new Image(imageUrl.toExternalForm());
            loginBrandingView.setImage(brandingImage);
            Image lock = new Image(lockUrl.toExternalForm());
            loginLockImage.setImage(lock);
        }
    }
}
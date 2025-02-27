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


public class SignupController implements Initializable {

    @FXML
    private Button createAccountBtn;
    @FXML
    private TextField firstnameTF;
    @FXML
    private TextField lastnameTF;
    @FXML
    private TextField emailTF;
    @FXML
    private TextField passwordTF;
    @FXML
    private TextField confirmPasswordTF;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Label loginLBL;

    public void signUpUser(ActionEvent actionEvent) {
        String firstname = firstnameTF.getText();
        String lastname = lastnameTF.getText();
        String email = emailTF.getText();
        String password = passwordTF.getText();
        String confirmPassword = confirmPasswordTF.getText();

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
        } else if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Password Error", "Passwords do not match.");
        } else {
            DatabaseClass.writeToDatabase(firstname, lastname, email, password);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
            openLoginPage(); // Navigate to login screen
        }
    }

    @FXML
    private void openLoginPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

            // Optionally close the current signup window
            Stage currentStage = (Stage) createAccountBtn.getScene().getWindow();
            currentStage.close();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL imageUrl = getClass().getResource("/images/popcorn-155602_1920.png");
        if (imageUrl != null) {
            Image brandingImage = new Image(imageUrl.toExternalForm());
            brandingImageView.setImage(brandingImage);
        } else {

        }

    }
}
package com.exams.farmingsystem.examsproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WeatherController implements Initializable {

    private static final String API_KEY = "afb7ee367edf4ec4be2222315252202";

    @FXML
    private TextField location;

    @FXML
    private Label weather;

    @FXML
    private Button weatherBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        location.setPromptText("Enter city name...");

        // Set button click event
        weatherBtn.setOnAction(event -> {
            String city = location.getText().trim();
            if (!city.isEmpty()) {
                fetchWeather(city);
                weather.setVisible(true);
            } else {
                weather.setText("Please enter a valid location.");
            }
        });
    }

    private void fetchWeather(String city) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://api.weatherapi.com/v1/current.json?key=" + API_KEY + "&q=" + city;
        Request request = new Request.Builder().url(url).get().build();

        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject current = jsonObject.getJSONObject("current");
                    String condition = current.getJSONObject("condition").getString("text");
                    double tempF = current.getDouble("temp_f");

                    String result = "Temperature: " + tempF + "°F and Condition: " + condition;
                    updateUI(result);
                } else {
                    updateUI("Error fetching weather data.");
                }
            } catch (IOException e) {
                updateUI("Failed to connect to API.");
                e.printStackTrace();
            }
        }).start();
    }

    private void updateUI(String message) {
        Platform.runLater(() -> weather.setText(message));
    }
}

package org.demoapplication.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HomeController {

    @FXML
    private ComboBox<String> customerSelectBox;

    @FXML
    private ListView<String> logListView;

    @FXML
    private VBox contentArea; // Artık VBox kullanıyoruz

    @FXML
    private Button flashButton;

    @FXML
    private Button clearListeningButton;

    @FXML
    private Button addFunction;

    @FXML
    private Button startListeningButton;

    @FXML
    private Button stopListeningButton;

    private SerialPortListener serialPortListener;

    @FXML
    private void initialize() {
        loadCustomersFromDatabase();

        SerialPortListener serialPortListener = new SerialPortListener(logListView);
        loadCustomersFromDatabase();

        logListView.getItems().addAll(
                "Log 1: Application started",
                "Log 2: User logged in",
                "Log 3: Data loaded"
        );

        // Flash butonuna tıklama olayı ekleme
        flashButton.setOnAction(event -> {
            logListView.getItems().add("Log: Flash button clicked");
        });
        clearListeningButton.setOnAction(event -> {
            logListView.getItems().clear();
        });
        // Start Listening butonu
        startListeningButton.setOnAction(event -> serialPortListener.startListening("COM8"));

        // Stop Listening butonu
        stopListeningButton.setOnAction(event -> serialPortListener.stopListening());

    }

    private void loadCustomersFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/obd";
        String user = "root"; // MySQL kullanıcı adı
        String password = ""; // Şifre

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            while (rs.next()) {
                String customerName = rs.getString("name");
                customerSelectBox.getItems().add(customerName);
            }

        } catch (Exception e) {
            showAlert("Database Error", "Could not load customers from database:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleSubmit() {
        String selectedCustomer = customerSelectBox.getValue();
        if (selectedCustomer == null || selectedCustomer.isEmpty()) {
            showAlert("Form Error", "Please select a customer.");
        } else {
            showAlert("Form Submitted", "You selected: " + selectedCustomer);
        }
    }

    @FXML
    private void handleCancel() {
        customerSelectBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

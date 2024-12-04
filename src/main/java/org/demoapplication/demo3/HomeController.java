package org.demoapplication.demo3;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HomeController {

    @FXML
    private ComboBox<String> customerSelectBox;

    @FXML
    private void initialize() {
        loadCustomersFromDatabase();
    }

    private void loadCustomersFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/obdportal";
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

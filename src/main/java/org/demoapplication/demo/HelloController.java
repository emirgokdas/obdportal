package org.demoapplication.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloController {

    @FXML
    private ComboBox<String> comboBoxItems;

    @FXML
    private TextField textFieldName;

    @FXML
    public void initialize() {
        loadItemsFromDatabase();
    }

    @FXML
    private void handleSubmit() {
        String name = textFieldName.getText();
        String selectedItem = comboBoxItems.getValue();

        if (name.isEmpty() || selectedItem == null) {
            showAlert("Error", "Please fill in all fields!");
        } else {
            showAlert("Success", "Name: " + name + "\nSelected Item: " + selectedItem);
        }
    }

    private void loadItemsFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/obdportal";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM customers")) {

            while (rs.next()) {
                comboBoxItems.getItems().add(rs.getString("name"));
            }

        } catch (Exception e) {
            showAlert("Database Error", "Failed to load items from database:\n" + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

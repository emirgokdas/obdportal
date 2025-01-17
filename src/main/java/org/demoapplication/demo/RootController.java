package org.demoapplication.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RootController {

    public ComboBox customerSelectBox;
    public ComboBox deviceSelectBox;
    public ComboBox functionSelectBox;
    public ComboBox vendorSelectBox;
    public ComboBox modelSelectBox;
    public TextField deviceNameField;
    public TextField ipAddressField;
    public TextField portField;
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


        loadView("pages/home.fxml");

    }


    @FXML
    private void loadSettings() {
        loadView("pages/settings.fxml");
    }
    @FXML
    private void addFunction() {
        loadView("pages/addfunctions.fxml");
    }
    @FXML
    private void loadHome(){
        loadView("pages/home.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().clear(); // Eski içeriği temizle
            contentArea.getChildren().add(view); // Yeni içeriği ekle
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


//    private void loadView(String fxmlFile) {
//        // Logları göstermek için SerialPortListener örneği oluştur
//
//        try {
//            // Yeni FXML dosyasını yükle
//            Node view = FXMLLoader.load(getClass().getResource(fxmlFile));
//            contentArea.getChildren().clear(); // Eski içeriği temizle
//            contentArea.getChildren().add(view); // Yeni içeriği ekle
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

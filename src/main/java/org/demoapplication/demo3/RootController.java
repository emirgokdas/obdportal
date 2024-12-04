package org.demoapplication.demo3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RootController {

    @FXML
    private ListView<String> logListView;

    @FXML
    private VBox contentArea; // Artık VBox kullanıyoruz

    @FXML
    private Button flashButton;

    @FXML
    private Button clearButton;

    @FXML
    private void initialize() {
        logListView.getItems().addAll(
                "Log 1: Application started",
                "Log 2: User logged in",
                "Log 3: Data loaded"
        );

        // Flash butonuna tıklama olayı ekleme
        flashButton.setOnAction(event -> {
            logListView.getItems().add("Log: Flash button clicked");
        });
        clearButton.setOnAction(event -> {
            logListView.getItems().clear();
        });
        loadView("pages/home.fxml");
    }

    @FXML
    private void loadHome() {
        loadView("pages/home.fxml");
    }

    @FXML
    private void loadAbout() {
        loadView("pages/about.fxml");
    }

    @FXML
    private void loadSettings() {
        loadView("pages/settings.fxml");
    }

    @FXML
    private Button startListeningButton;

    @FXML
    private Button stopListeningButton;

    private SerialPortListener serialPortListener;

    private void loadView(String fxmlFile) {
        // Logları göstermek için SerialPortListener örneği oluştur
        serialPortListener = new SerialPortListener(logListView);

        // Flash butonu
        flashButton.setOnAction(event -> logListView.getItems().add("Log: Flash button clicked"));

        // Clear butonu
        clearButton.setOnAction(event -> logListView.getItems().clear());

        // Start Listening butonu
        startListeningButton.setOnAction(event -> serialPortListener.startListening("COM3"));

        // Stop Listening butonu
        stopListeningButton.setOnAction(event -> serialPortListener.stopListening());

        try {
            // Yeni FXML dosyasını yükle
            Node view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().clear(); // Eski içeriği temizle
            contentArea.getChildren().add(view); // Yeni içeriği ekle
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

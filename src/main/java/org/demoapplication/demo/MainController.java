package org.demoapplication.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootLayout; // BorderPane'deki ana düzen

    @FXML
    private ListView<String> logListView;

    @FXML
    private Button flashButton;

    @FXML
    private void initialize() {
        // Başlangıçta birkaç örnek log ekleme

    }

    // Örnek buton işlemi
    @FXML
    private void handleButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Button Clicked");
        alert.setHeaderText("Button Action");
        alert.setContentText("You clicked a button!");
        alert.showAndWait();
    }

    @FXML
    private void loadAboutPage() {
        try {
            // about.fxml dosyasını yükle
            Parent aboutView = FXMLLoader.load(getClass().getResource("pages/about.fxml"));
            rootLayout.setCenter(aboutView); // Center bölgesine yükle
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

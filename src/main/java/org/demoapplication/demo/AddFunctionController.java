package org.demoapplication.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.*;


public class AddFunctionController {

    @FXML
    private TextField hexValueField;

    @FXML
    private void generateJson() {
        String hexValue = hexValueField.getText();
        if (hexValue.isEmpty()) {
            System.out.println("Please enter a HEX value.");
            return;
        }
        String json = String.format("{\"hex\":\"%s\"}", hexValue);
        System.out.println("Generated JSON: " + json);
    }

    @FXML
    private TableView<Function> functionsTable;

    @FXML
    private TableColumn<Function, String> functionNameColumn;

    @FXML
    private TableColumn<Function, Void> operationColumn;

    @FXML
    private TextArea hexTextArea;

    @FXML
    private TextField functionNameField;

    @FXML
    private Button addNewButton;

    private ObservableList<Function> functionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadFunctionsFromDatabase();
    }

    private void setupTableColumns() {
        functionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //functionsTable.setItems(functionList);

        operationColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            {
                updateButton.setOnAction(event -> {
                    Function function = getTableView().getItems().get(getIndex());
                    loadFunctionData(function.getId());
                });
                deleteButton.setOnAction(event -> {
                    Function function = getTableView().getItems().get(getIndex());
                    deleteFunctionFromDatabase(function.getId());
                    functionList.remove(function);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, updateButton, deleteButton));
                }
            }
        });
    }

    private void loadFunctionsFromDatabase() {
        String query = "SELECT id, function_name FROM functions";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/obd", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                functionList.add(new Function(rs.getInt("id"), rs.getString("function_name")));
            }
            functionsTable.setItems(functionList);

        } catch (SQLException e) {
            showAlert("Database Error", "Could not load functions:\n" + e.getMessage());
        }
    }

    private void loadFunctionData(int id) {
        String query = "SELECT * FROM functions WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/obd", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                hexTextArea.setText(rs.getString("data"));
                functionNameField.setText(rs.getString("function_name"));

            }

        } catch (SQLException e) {
            showAlert("Database Error", "Could not load function data:\n" + e.getMessage());
        }
    }

    private void deleteFunctionFromDatabase(int id) {
        String query = "DELETE FROM functions WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/obd", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            showAlert("Database Error", "Could not delete function:\n" + e.getMessage());
        }
    }

    private void addNewButton(){

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
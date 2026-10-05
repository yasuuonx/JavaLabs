package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.UUID;

/**
 * Головне вікно додатку JavaFX для взаємодії з колекцією Company та пошуку за UUID.
 */
public class MainApp extends Application {
    private static final String TEXT_FILE = "input.txt";
    private Company company;
    private ListView<String> listView;
    private TextArea detailsArea;

    @Override
    public void start(Stage primaryStage) {
        this.company = FileManager.loadCompanyFromFile(TEXT_FILE);

        primaryStage.setTitle("Управління персоналом: " + company.getName() + " (UUID + JavaFX)");

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));

        Label formTitle = new Label("Створення нового співробітника:");
        formTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(8);

        TextField nameField = new TextField();
        nameField.setPromptText("Ім'я");

        TextField positionField = new TextField();
        positionField.setPromptText("Посада");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Зарплата");

        TextField expField = new TextField();
        expField.setPromptText("Стаж (роки)");

        ComboBox<Department> deptBox = new ComboBox<Department>();
        deptBox.getItems().addAll(Department.IT, Department.HR, Department.FINANCE, Department.MARKETING);
        deptBox.setValue(Department.IT);

        ComboBox<String> typeBox = new ComboBox<String>();
        typeBox.getItems().addAll("FullTimeEmployee", "ContractEmployee", "Manager", "Freelancer");
        typeBox.setValue("FullTimeEmployee");

        TextField extraParamField = new TextField();
        extraParamField.setPromptText("Річний бонус (грн)");

        typeBox.setOnAction(e -> {
            String selected = typeBox.getValue();
            if ("FullTimeEmployee".equals(selected)) {
                extraParamField.setPromptText("Річний бонус (грн)");
            } else if ("ContractEmployee".equals(selected)) {
                extraParamField.setPromptText("Тривалість контракту (місяців)");
            } else if ("Manager".equals(selected)) {
                extraParamField.setPromptText("Річний бонус (грн)");
            } else if ("Freelancer".equals(selected)) {
                extraParamField.setPromptText("Погодинна ставка (грн/год)");
            }
        });

        Button addButton = new Button("Додати до колекції");
        addButton.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold;");

        formGrid.add(new Label("Ім'я:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Посада:"), 2, 0);
        formGrid.add(positionField, 3, 0);

        formGrid.add(new Label("Зарплата:"), 0, 1);
        formGrid.add(salaryField, 1, 1);
        formGrid.add(new Label("Стаж:"), 2, 1);
        formGrid.add(expField, 3, 1);

        formGrid.add(new Label("Відділ:"), 0, 2);
        formGrid.add(deptBox, 1, 2);
        formGrid.add(new Label("Тип:"), 2, 2);
        formGrid.add(typeBox, 3, 2);

        formGrid.add(new Label("Параметр:"), 0, 3);
        formGrid.add(extraParamField, 1, 3);
        formGrid.add(addButton, 3, 3);

        Label listTitle = new Label("Список співробітників (короткий вигляд: Назва/Посада + UUID):");
        listTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        listView = new ListView<String>();
        listView.setPrefHeight(160);
        updateListView();

        Label searchTitle = new Label("Пошук за UUID:");
        searchTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        TextField uuidSearchField = new TextField();
        uuidSearchField.setPromptText("Введіть повний UUID");
        uuidSearchField.setPrefWidth(380);

        Button searchButton = new Button("Знайти");
        searchButton.setStyle("-fx-background-color: #1565c0; -fx-text-fill: white; -fx-font-weight: bold;");

        searchBox.getChildren().addAll(new Label("UUID:"), uuidSearchField, searchButton);

        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefHeight(80);
        detailsArea.setPromptText("Повна інформація про знайдений об'єкт відображатиметься тут...");

        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String position = positionField.getText().trim();
                double salary = Double.parseDouble(salaryField.getText().trim());
                int experience = Integer.parseInt(expField.getText().trim());
                Department dept = deptBox.getValue();
                String type = typeBox.getValue();
                double extraVal = Double.parseDouble(extraParamField.getText().trim());

                Employee emp = null;
                if ("FullTimeEmployee".equals(type)) {
                    emp = new FullTimeEmployee(name, position, salary, experience, dept, extraVal);
                } else if ("ContractEmployee".equals(type)) {
                    emp = new ContractEmployee(name, position, salary, experience, dept, (int) extraVal);
                } else if ("Manager".equals(type)) {
                    emp = new Manager(name, position, salary, experience, dept, extraVal, 5);
                } else if ("Freelancer".equals(type)) {
                    emp = new Freelancer(name, position, salary, experience, dept, 6, extraVal);
                }

                company.addNewEmployee(emp, 1);
                FileManager.saveCompanyToFile(TEXT_FILE, company);
                updateListView();

                nameField.clear();
                positionField.clear();
                salaryField.clear();
                expField.clear();
                extraParamField.clear();

                detailsArea.setText("Створено новий об'єкт:\n" + emp.toString());
            } catch (Exception ex) {
                showAlert("Помилка додавання", "Перевірте правильність заповнення всіх полів: " + ex.getMessage());
            }
        });

        searchButton.setOnAction(e -> {
            String uuidInput = uuidSearchField.getText().trim();
            if (uuidInput.isEmpty()) {
                detailsArea.setText("Введіть UUID для пошуку.");
                return;
            }

            try {
                UUID targetUuid = UUID.fromString(uuidInput);
                Employee found = company.findByUuid(targetUuid);
                if (found != null) {
                    detailsArea.setText("ЗНАЙДЕНО ОБ'ЄКТ (Повна інформація):\n" + found.toString());
                } else {
                    detailsArea.setText("Об'єкт з UUID '" + uuidInput + "' не знайдено.");
                }
            } catch (IllegalArgumentException ex) {
                detailsArea.setText("Помилка: Некоректний формат UUID!\nПриклад коректного UUID: " + UUID.randomUUID());
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.contains("UUID: ")) {
                String selectedUuid = newVal.substring(newVal.indexOf("UUID: ") + 6).trim();
                uuidSearchField.setText(selectedUuid);
            }
        });

        root.getChildren().addAll(formTitle, formGrid, listTitle, listView, searchTitle, searchBox, detailsArea);

        Scene scene = new Scene(root, 720, 620);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateListView() {
        listView.getItems().clear();
        for (Employee emp : company.getEmployees()) {
            listView.getItems().add(emp.toShortString());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
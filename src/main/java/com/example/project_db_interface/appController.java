package com.example.project_db_interface;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class appController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exitButton;

    @FXML
    private Label gender;

    @FXML
    private Label greetingsName;

    @FXML
    private Label locationName;

    @FXML
    private Button selectBooksButton;

    @FXML
    private Button selectPublishingButton;

    @FXML
    private Button selectRequestsButton;

    User user = new User();

    @FXML
    void initialize() {
        exitButton.setOnAction(actionEvent -> exit());
        selectBooksButton.setOnAction(actionEvent -> selectBooks());
        selectRequestsButton.setOnAction(actionEvent -> selectRequests());
        selectPublishingButton.setOnAction(actionEvent -> selectPublishings());
    }

    private void selectBooks() {
        selectBooksButton.getScene().getWindow().hide();
        try {
            //Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("books.fxml")));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("books.fxml"));
            loader.load();

            BooksController booksController = loader.getController();
            booksController.setUser(user);

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectPublishings() {
        selectPublishingButton.getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("publishings.fxml"));
            loader.load();

            PublishingsController publishingsController = loader.getController();
            publishingsController.setUser(user);

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectRequests() {
        selectRequestsButton.getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("requests.fxml"));
            loader.load();

            RequestsController requestsController = loader.getController();
            requestsController.setUser(user);

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void exit() {
        exitButton.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setGreetings(User user) {
        greetingsName.setText("Добро пожаловать, " + user.getFirstName() + " " + user.getLastName() + "!");
        locationName.setText("Страна: " + user.getLocation());
        gender.setText("Пол: " + user.getGender());

        this.user.setGender(user.getGender());
        this.user.setUserName(user.getUserName());
        this.user.setFirstName(user.getFirstName());
        this.user.setLastName(user.getLastName());
        this.user.setPassword(user.getPassword());
        this.user.setLocation(user.getLocation());
    }
}


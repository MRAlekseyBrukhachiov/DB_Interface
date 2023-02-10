package com.example.project_db_interface;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private Button backButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private CheckBox signUpCheckBoxFemale;

    @FXML
    private CheckBox signUpCheckBoxMale;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpLocation;

    @FXML
    private TextField signUpName;

    @FXML
    void initialize() {
        signUpButton.setOnAction(actionEvent -> signUpNewUser());
        backButton.setOnAction(actionEvent -> openNewScene("hello-view.fxml"));
        signUpCheckBoxMale.setOnAction(actionEvent ->
                signUpCheckBoxFemale.setSelected(!signUpCheckBoxMale.isSelected()));
        signUpCheckBoxFemale.setOnAction(actionEvent ->
                signUpCheckBoxMale.setSelected(!signUpCheckBoxFemale.isSelected()));
    }

    public void openNewScene(String window) {
        signUpButton.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(window)));
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void signUpNewUser() {
        DataBaseHandler dbHandler = new DataBaseHandler();

        String firstName = signUpName.getText();
        String lastName = signUpLastName.getText();
        String userName = loginField.getText();
        String password = passwordField.getText();
        String location = signUpLocation.getText();
        String gender;

        if (signUpCheckBoxMale.isSelected() ^ signUpCheckBoxFemale.isSelected()) {
            gender = signUpCheckBoxMale.isSelected() ? "Мужской" : "Женский";
            User user = new User(firstName, lastName, userName, password, location, gender);
            try {
                dbHandler.signUpUser(user);
                openNewScene("hello-view.fxml");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Определитесь с полом!");
            alert.showAndWait();
        }
    }

}


package com.example.project_db_interface;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authSignInButton;

    @FXML
    private TextField loginField;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    void initialize() {
        authSignInButton.setOnAction(actionEvent -> {
            String loginText = loginField.getText().trim();
            String loginPassword = passwordField.getText().trim();

            if (!loginText.equals("") && !loginPassword.equals("")) {
                loginUser(loginText, loginPassword);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Введите логин и пароль");
                alert.showAndWait();
            }
        });

        loginSignUpButton.setOnAction(actionEvent -> openNewScene("signUp.fxml"));
    }

    private void loginUser(String loginText, String loginPassword) {
        DataBaseHandler dbHandler = new DataBaseHandler();
        User user = new User();
        user.setUserName(loginText);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);

        int counter = 0;

        try {
            while (result.next()) {
                counter++;
                user.setFirstName(result.getString(Const.USERS_FIRSTNAME));
                user.setLastName(result.getString(Const.USERS_LASTNAME));
                user.setLocation(result.getString(Const.USERS_LOCATION));
                user.setGender(result.getString(Const.USERS_GENDER));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (counter >= 1) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("app.fxml"));
                loader.load();

                appController appController = loader.getController();
                appController.setGreetings(user);

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Publishing");
                stage.setScene(new Scene(root));
                stage.show();
                loginSignUpButton.getScene().getWindow().hide();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            Shake userLoginAnim = new Shake(loginField);
            Shake userPassAnim = new Shake(passwordField);
            userLoginAnim.playAnim();
            userPassAnim.playAnim();
        }
    }

    public void openNewScene(String window) {
        loginSignUpButton.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(window));
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

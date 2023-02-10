package com.example.project_db_interface;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.textfield.TextFields;

public class AddBookController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField addBookAuthorId;

    @FXML
    private TextField addBookCount;

    @FXML
    private TextField addBookGenreId;

    @FXML
    private TextField addBookLanguage;

    @FXML
    private TextField addBookTitle;

    @FXML
    private TextField addBookWeight;

    @FXML
    private Button clearButton;

    @FXML
    private Button saveButton;

    @FXML
    private ComboBox<String> coverSelection;

    @FXML
    private ComboBox<String> formatSelection;

    @FXML
    private ComboBox<String> paperSelection;

    private final String[] languages = {
            "Русский", "Румынский", "Английский", "Польский",
            "Китайский", "Японский", "Арабский", "Испанский",
            "Немецкий", "Французский", "Украинский", "Итальянский"
    };
    private final ArrayList<String> authors = new ArrayList<>();
    private final ArrayList<String> genres = new ArrayList<>();

    private void setAuthors() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        ResultSet result = dbHandler.getAuthors();

        try {
            while (result.next()) {
                authors.add(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setGenres() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        ResultSet result = dbHandler.getGenres();

        try {
            while (result.next()) {
                genres.add(result.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clear() {
        addBookAuthorId.setText(null);
        addBookCount.setText(null);
        addBookGenreId.setText(null);
        addBookLanguage.setText(null);
        addBookTitle.setText(null);
        addBookWeight.setText(null);
    }

    @FXML
    void save(MouseEvent event) {
        String title = addBookTitle.getText();
        String count = addBookCount.getText();
        String weight = addBookWeight.getText();
        String language = addBookLanguage.getText();
        String author_id = addBookAuthorId.getText();
        String genre_id = addBookGenreId.getText();
        String format_id = String.valueOf(formatSelection.getSelectionModel().getSelectedIndex() + 1);
        String cover_id = String.valueOf(coverSelection.getSelectionModel().getSelectedIndex() + 1);
        String paper_id = String.valueOf(paperSelection.getSelectionModel().getSelectedIndex() + 1);

        for (String s: languages) {
            if (s.toLowerCase().contains(language.toLowerCase())) {
                language = s;
                break;
            }
        }

        DataBaseHandler dbHandler = new DataBaseHandler();
        dbHandler.addBook(title, count, weight, language, author_id, genre_id,
                    format_id, cover_id, paper_id);
        clear();
    }

    @FXML
    void initialize() {
        setAuthors();
        setGenres();

        TextFields.bindAutoCompletion(addBookLanguage, languages);
        TextFields.bindAutoCompletion(addBookAuthorId, authors);
        TextFields.bindAutoCompletion(addBookGenreId, genres);

        DataBaseHandler dbHandler = new DataBaseHandler();
        ResultSet resultSet = dbHandler.getPapers();

        try {
        while (resultSet.next()) {  // loop
            paperSelection.getItems().addAll(resultSet.getString("type"));
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resultSet = dbHandler.getCovers();

        try {
            while (resultSet.next()) {  // loop
                coverSelection.getItems().addAll(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resultSet = dbHandler.getFormats();

        try {
            while (resultSet.next()) {  // loop
                String str = "%d / %d".formatted(resultSet.getInt("height"),
                        resultSet.getInt("width"));
                formatSelection.getItems().addAll(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        formatSelection.getSelectionModel().select(0);
        paperSelection.getSelectionModel().select(0);
        coverSelection.getSelectionModel().select(0);
    }

}


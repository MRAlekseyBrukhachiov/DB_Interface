package com.example.project_db_interface;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;


public class BooksController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBookButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, String> countCol;

    @FXML
    private TableColumn<Book, String> editCol;

    @FXML
    private Button findButton;

    @FXML
    private TextField findField;

    @FXML
    private TableColumn<Book, String> idAuthorCol;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> idCoverCol;

    @FXML
    private TableColumn<Book, String> idFormatCol;

    @FXML
    private TableColumn<Book, String> idGenreCol;

    @FXML
    private TableColumn<Book, String> idPaperCol;

    @FXML
    private TableColumn<Book, String> languageCol;

    @FXML
    private Button refreshTableButton;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> weightCol;

    User user = new User();

    ObservableList<Book> BookList = FXCollections.observableArrayList();

    @FXML
    void back(MouseEvent event) {
        backButton.getScene().getWindow().hide();
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void find() {

    }

    @FXML
    void delete(MouseEvent event) {
        Book book = booksTable.getSelectionModel().getSelectedItem();
        DataBaseHandler dbHandler = new DataBaseHandler();
        dbHandler.deleteBook(book);
        refreshTable();
    }

    @FXML
    void edit() {
        Book book = booksTable.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editBook.fxml"));
            loader.load();

            DataBaseHandler dbHandler = new DataBaseHandler();
            String author = dbHandler.getAuthorFromId(book.getAuthor_id());
            String genre = dbHandler.getGenreFromId(book.getGenre_id());

            EditBookController editBookController = loader.getController();
            editBookController.setTextField(book.getTitle(), book.getCount(), book.getWeight(), book.getLanguage(),
                    author, genre, book.getFormat_id(), book.getCover_id(),
                    book.getPaper_id(), book.getBook_id());

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void getAddView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("addBook.fxml"));
            loader.load();

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshTable() {
        try {
            BookList.clear();

            DataBaseHandler dbHandler = new DataBaseHandler();
            ResultSet resultSet = dbHandler.getBooks();

            while (resultSet.next()){
                Book book = new Book();
                book.setBook_id(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setCount(resultSet.getInt("count"));
                book.setWeight(resultSet.getInt("weight"));
                book.setLanguage(resultSet.getString("language"));
                book.setAuthor_id(resultSet.getInt("author_id"));
                book.setGenre_id(resultSet.getInt("genre_id"));
                book.setFormat_id(resultSet.getInt("format_id"));
                book.setCover_id(resultSet.getInt("cover_id"));
                book.setPaper_id(resultSet.getInt("paper_id"));
                book.setAuthor_name(resultSet.getString("name"));
                book.setGenre_name(resultSet.getString("genre.title"));

                BookList.add(book);
                booksTable.setItems(BookList);

                readySearch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readySearch() {
        FilteredList<Book> filteredData = new FilteredList<>(BookList, b -> true);

        findField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getLanguage().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getAuthor_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getGenre_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(book.getCount()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            }));
        });

        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedData);

        final ComboBox<TableColumn<Book, ?>> sortCombo = new ComboBox<>(booksTable.getColumns());

        final Callback<ListView<TableColumn<Book, ?>>, ListCell<TableColumn<Book, ?>>> cellFactory
                = new Callback<ListView<TableColumn<Book, ?>>, ListCell<TableColumn<Book, ?>>>(){
            @Override
            public ListCell<TableColumn<Book, ?>> call(ListView<TableColumn<Book, ?>> listView) {
                return createListCell();
            }
        };

        sortCombo.setCellFactory(cellFactory);
        sortCombo.setButtonCell(createListCell());
        sortCombo.valueProperty().addListener(new ChangeListener<TableColumn<Book, ?>>() {
            @Override
            public void changed(ObservableValue<? extends TableColumn<Book, ?>> obs,
                                TableColumn<Book, ?> oldCol, TableColumn<Book, ?> newCol) {
                if (newCol != null) {
                    booksTable.getSortOrder().setAll(Collections.singletonList(newCol));
                    newCol.setSortType(TableColumn.SortType.ASCENDING);
                } else {
                    booksTable.getSortOrder().clear();
                }
            }
        });

        booksTable.getSortOrder().addListener(new ListChangeListener<TableColumn<Book, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<Book, ?>> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        sortCombo.setValue(booksTable.getSortOrder().get(0));
                    }
                }
            }
        });
    }

    @FXML
    void initialize() {
        loadData();
        readySearch();
    }

    private ListCell<TableColumn<Book, ?>> createListCell() {
        final ListCell<TableColumn<Book, ?>> cell = new ListCell<>();
        cell.itemProperty().addListener(new ChangeListener<TableColumn<Book, ?>>() {
            @Override
            public void changed(
                    ObservableValue<? extends TableColumn<Book, ?>> obs,
                    TableColumn<Book, ?> oldColumn, TableColumn<Book, ?> newColumn) {
                if (newColumn == null) {
                    cell.setText(null);
                } else {
                    cell.setText(newColumn.getText());
                }
            }
        });
        return cell ;
    }

    private void loadData() {
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        languageCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        idAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author_name"));
        idGenreCol.setCellValueFactory(new PropertyValueFactory<>("genre_name"));
        idFormatCol.setCellValueFactory(new PropertyValueFactory<>("format_id"));
        idCoverCol.setCellValueFactory(new PropertyValueFactory<>("cover_id"));
        idPaperCol.setCellValueFactory(new PropertyValueFactory<>("paper_id"));
    }

    public void setUser(User user) {
        this.user.setGender(user.getGender());
        this.user.setUserName(user.getUserName());
        this.user.setFirstName(user.getFirstName());
        this.user.setLastName(user.getLastName());
        this.user.setPassword(user.getPassword());
        this.user.setLocation(user.getLocation());
    }

}



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


public class RequestsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBookButton;

    @FXML
    private TableColumn<Request, String> authorCol;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Request, String> dateCol;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField findField;

    @FXML
    private TableColumn<Request, String> idCol;

    @FXML
    private TableColumn<Request, String> recommendLetterCol;

    @FXML
    private Button refreshTableButton;

    @FXML
    private TableView<Request> requestTable;

    @FXML
    private TableColumn<Request, String> statusCol;

    User user = new User();

    ObservableList<Request> RequestList = FXCollections.observableArrayList();

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
        Request request = requestTable.getSelectionModel().getSelectedItem();
        DataBaseHandler dbHandler = new DataBaseHandler();
        dbHandler.deleteRequest(request);
        refreshTable();
    }

    @FXML
    void edit() {
        Request request = requestTable.getSelectionModel().getSelectedItem();
        System.out.println(request);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editRequest.fxml"));
            loader.load();

            EditRequestController editRequestController = loader.getController();
            editRequestController.setTextField(request);

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addRequest.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Publishing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void refreshTable() {
        try {
            RequestList.clear();

            DataBaseHandler dbHandler = new DataBaseHandler();
            ResultSet resultSet = dbHandler.getRequests();

            while (resultSet.next()){
                Request request = new Request();
                request.setRequest_id(resultSet.getInt("id"));
                request.setStatus_id(resultSet.getInt("status_id"));
                request.setAuthor_id(resultSet.getInt("author_id"));
                request.setDate(resultSet.getDate("Дата"));
                request.setRecommendLetter(resultSet.getString("Требование"));
                request.setAuthor_name(resultSet.getString("Автор"));
                request.setStatus_name(resultSet.getString("Статус"));

                RequestList.add(request);
                requestTable.setItems(RequestList);

                readySearch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readySearch() {
        FilteredList<Request> filteredData = new FilteredList<>(RequestList, b -> true);

        findField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((request -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (request.getAuthor_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(request.getDate()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            }));
        });

        SortedList<Request> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(requestTable.comparatorProperty());
        requestTable.setItems(sortedData);

        final ComboBox<TableColumn<Request, ?>> sortCombo = new ComboBox<>(requestTable.getColumns());

        final Callback<ListView<TableColumn<Request, ?>>, ListCell<TableColumn<Request, ?>>> cellFactory
                = new Callback<ListView<TableColumn<Request, ?>>, ListCell<TableColumn<Request, ?>>>(){
            @Override
            public ListCell<TableColumn<Request, ?>> call(ListView<TableColumn<Request, ?>> listView) {
                return createListCell();
            }
        };

        sortCombo.setCellFactory(cellFactory);
        sortCombo.setButtonCell(createListCell());
        sortCombo.valueProperty().addListener(new ChangeListener<TableColumn<Request, ?>>() {
            @Override
            public void changed(ObservableValue<? extends TableColumn<Request, ?>> obs,
                                TableColumn<Request, ?> oldCol, TableColumn<Request, ?> newCol) {
                if (newCol != null) {
                    requestTable.getSortOrder().setAll(Collections.singletonList(newCol));
                    newCol.setSortType(TableColumn.SortType.ASCENDING);
                } else {
                    requestTable.getSortOrder().clear();
                }
            }
        });

        requestTable.getSortOrder().addListener(new ListChangeListener<TableColumn<Request, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<Request, ?>> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        sortCombo.setValue(requestTable.getSortOrder().get(0));
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

    private ListCell<TableColumn<Request, ?>> createListCell() {
        final ListCell<TableColumn<Request, ?>> cell = new ListCell<>();
        cell.itemProperty().addListener(new ChangeListener<TableColumn<Request, ?>>() {
            @Override
            public void changed(
                    ObservableValue<? extends TableColumn<Request, ?>> obs,
                    TableColumn<Request, ?> oldColumn, TableColumn<Request, ?> newColumn) {
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

        idCol.setCellValueFactory(new PropertyValueFactory<>("request_id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author_name"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status_name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        recommendLetterCol.setCellValueFactory(new PropertyValueFactory<>("recommendLetter"));
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


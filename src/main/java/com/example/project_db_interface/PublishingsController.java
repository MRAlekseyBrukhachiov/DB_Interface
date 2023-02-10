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


public class PublishingsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBookButton;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Publishing, String> dateCol;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Publishing, String> editsCol;

    @FXML
    private TextField findField;

    @FXML
    private TableColumn<Publishing, String> idCol;

    @FXML
    private TableColumn<Publishing, String> idRequestCol;

    @FXML
    private TableView<Publishing> publishingTable;

    @FXML
    private Button refreshTableButton;

    @FXML
    private TableColumn<Publishing, String> resultsCol;

    @FXML
    private TableColumn<Publishing, String> staffNameCol;

    User user = new User();

    ObservableList<Publishing> PublishingList = FXCollections.observableArrayList();

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
        Publishing publishing = publishingTable.getSelectionModel().getSelectedItem();
        DataBaseHandler dbHandler = new DataBaseHandler();
        dbHandler.deletePublishing(publishing);
        refreshTable();
    }

    @FXML
    void edit() {
        Publishing publishing = publishingTable.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editPublishing.fxml"));
            loader.load();

            EditPublishingController editPublishingController = loader.getController();
            editPublishingController.setTextField(publishing);

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addPublishing.fxml")));
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
            PublishingList.clear();

            DataBaseHandler dbHandler = new DataBaseHandler();
            ResultSet resultSet = dbHandler.getPublishings();

            while (resultSet.next()){
                Publishing publishing = new Publishing();
                publishing.setPublishing_id(resultSet.getInt("id"));
                publishing.setRequest_id(resultSet.getInt("request_id"));
                publishing.setStaff_id(resultSet.getInt("staff_id"));
                publishing.setEdition_id(resultSet.getInt("edition_id"));
                publishing.setDate(resultSet.getDate("Дата"));
                publishing.setResults(resultSet.getString("Результат"));
                publishing.setStaff_name(resultSet.getString("Редактор"));
                publishing.setEdition_edits(resultSet.getString("Правки"));

                PublishingList.add(publishing);
                publishingTable.setItems(PublishingList);

                readySearch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readySearch() {
        FilteredList<Publishing> filteredData = new FilteredList<>(PublishingList, b -> true);

        findField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((publishing -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (publishing.getStaff_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(publishing.getResults()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(publishing.getEdition_edits()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(publishing.getDate()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            }));
        });

        SortedList<Publishing> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(publishingTable.comparatorProperty());
        publishingTable.setItems(sortedData);

        final ComboBox<TableColumn<Publishing, ?>> sortCombo = new ComboBox<>(publishingTable.getColumns());

        final Callback<ListView<TableColumn<Publishing, ?>>, ListCell<TableColumn<Publishing, ?>>> cellFactory
                = new Callback<ListView<TableColumn<Publishing, ?>>, ListCell<TableColumn<Publishing, ?>>>(){
            @Override
            public ListCell<TableColumn<Publishing, ?>> call(ListView<TableColumn<Publishing, ?>> listView) {
                return createListCell();
            }
        };

        sortCombo.setCellFactory(cellFactory);
        sortCombo.setButtonCell(createListCell());
        sortCombo.valueProperty().addListener(new ChangeListener<TableColumn<Publishing, ?>>() {
            @Override
            public void changed(ObservableValue<? extends TableColumn<Publishing, ?>> obs,
                                TableColumn<Publishing, ?> oldCol, TableColumn<Publishing, ?> newCol) {
                if (newCol != null) {
                    publishingTable.getSortOrder().setAll(Collections.singletonList(newCol));
                    newCol.setSortType(TableColumn.SortType.ASCENDING);
                } else {
                    publishingTable.getSortOrder().clear();
                }
            }
        });

        publishingTable.getSortOrder().addListener(new ListChangeListener<TableColumn<Publishing, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<Publishing, ?>> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        sortCombo.setValue(publishingTable.getSortOrder().get(0));
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

    private ListCell<TableColumn<Publishing, ?>> createListCell() {
        final ListCell<TableColumn<Publishing, ?>> cell = new ListCell<>();
        cell.itemProperty().addListener(new ChangeListener<TableColumn<Publishing, ?>>() {
            @Override
            public void changed(
                    ObservableValue<? extends TableColumn<Publishing, ?>> obs,
                    TableColumn<Publishing, ?> oldColumn, TableColumn<Publishing, ?> newColumn) {
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

        idCol.setCellValueFactory(new PropertyValueFactory<>("publishing_id"));
        staffNameCol.setCellValueFactory(new PropertyValueFactory<>("staff_name"));
        resultsCol.setCellValueFactory(new PropertyValueFactory<>("results"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        editsCol.setCellValueFactory(new PropertyValueFactory<>("edition_edits"));
        idRequestCol.setCellValueFactory(new PropertyValueFactory<>("request_id"));
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


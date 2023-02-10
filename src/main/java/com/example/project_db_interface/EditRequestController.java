package com.example.project_db_interface;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.textfield.TextFields;

public class EditRequestController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField addBookAuthorId;

    @FXML
    private TextArea addBookRecommedLetter;

    @FXML
    private DatePicker addRequestDate;

    @FXML
    private TextField addRequestStatusId;

    @FXML
    private Button clearButton;

    @FXML
    private Button saveButton;

    private final ArrayList<String> authors = new ArrayList<>();

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

    @FXML
    void clear() {
        addBookAuthorId.setText(null);
        addRequestStatusId.setText(null);
        addBookRecommedLetter.setText(null);
    }

    private int request_id;

    @FXML
    void save(MouseEvent event) {
        String author = addBookAuthorId.getText();
        String status = addRequestStatusId.getText();
        Date date = Date.valueOf(addRequestDate.getValue());
        String recommendLetter = addBookRecommedLetter.getText();

        DataBaseHandler dbHandler = new DataBaseHandler();
        int author_id = dbHandler.getAuthorId(author);
        int status_id = dbHandler.getStatusId(status);

        Request request = new Request(request_id, author_id, status_id, status, author, date, recommendLetter);
        dbHandler.editRequest(request);
        clear();
    }

    @FXML
    void initialize() {
        setAuthors();
        TextFields.bindAutoCompletion(addBookAuthorId, authors);
    }

    public void setTextField(Request request) {
        request_id = request.getRequest_id();
        addBookAuthorId.setText(request.getAuthor_name());
        addRequestStatusId.setText(request.getStatus_name());
        addBookRecommedLetter.setText(request.getRecommendLetter());
        addRequestDate.setValue(request.getDate().toLocalDate());
    }
}

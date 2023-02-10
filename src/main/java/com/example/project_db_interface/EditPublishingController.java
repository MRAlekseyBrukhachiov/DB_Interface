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

public class EditPublishingController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField addBookAuthorId;

    @FXML
    private DatePicker addPublishingDate;

    @FXML
    private TextArea addPublishingEdits;

    @FXML
    private TextField addPublishingRequestId;

    @FXML
    private TextField addPublishingResults;

    @FXML
    private Button clearButton;

    @FXML
    private Button saveButton;

    private final ArrayList<String> staffs = new ArrayList<>();

    private void setStaffs() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        ResultSet result = dbHandler.getStaff();

        try {
            while (result.next()) {
                staffs.add(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clear() {
        addBookAuthorId.setText(null);
        addPublishingEdits.setText(null);
        addPublishingRequestId.setText(null);
        addPublishingResults.setText(null);
    }

    private int publishing_id;

    @FXML
    void save(MouseEvent event) {
        String staff = addBookAuthorId.getText();
        int request_id = Integer.parseInt(addPublishingRequestId.getText());
        Date date = Date.valueOf(addPublishingDate.getValue());
        String results = addPublishingResults.getText();
        String edits = addPublishingEdits.getText();

        DataBaseHandler dbHandler = new DataBaseHandler();
        int edition_id = dbHandler.getEditionId(edits);
        int staff_id = dbHandler.getStaffId(staff);

        Publishing publishing =
                new Publishing(publishing_id, edition_id, staff_id, request_id, staff, edits, date, results);
        dbHandler.editPublishing(publishing);
        clear();
    }

    @FXML
    void initialize() {
        setStaffs();
        TextFields.bindAutoCompletion(addBookAuthorId, staffs);
    }

    public void setTextField(Publishing publishing) {
        publishing_id = publishing.getPublishing_id();
        addBookAuthorId.setText(publishing.getStaff_name());
        addPublishingEdits.setText(publishing.getEdition_edits());
        addPublishingResults.setText(publishing.getResults());
        addPublishingRequestId.setText(String.valueOf(publishing.getRequest_id()));
        addPublishingDate.setValue(publishing.getDate().toLocalDate());
    }

}

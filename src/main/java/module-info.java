module com.example.project_db_interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.controlsfx.controls;


    opens com.example.project_db_interface to javafx.fxml;
    exports com.example.project_db_interface;
}
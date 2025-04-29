module code.learner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens gui to javafx.fxml;
    exports gui;
    exports model;
    exports db;
}

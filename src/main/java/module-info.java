module com.example.multisourceshortestpathfinder {
    requires javafx.controls;
    requires javafx.fxml;

    opens controllers to javafx.fxml;
    exports app;
}
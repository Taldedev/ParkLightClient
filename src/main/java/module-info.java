module parklight.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.parklight.client to javafx.fxml;
    opens com.parklight.client.controller to javafx.fxml;
    opens com.parklight.client.model to com.google.gson;

    exports com.parklight.client;
}

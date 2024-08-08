module com.example.rtmisq {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires AnimateFX;
    requires java.sql;
    requires sqlite.jdbc;
    requires javafx.media;


    opens com.example.rtmisq to javafx.fxml;
    exports com.example.rtmisq;
    exports com.example;
    opens com.example to javafx.fxml;
}
module com.example.GYMmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.GYMmanagementsystem to javafx.fxml;
    exports com.example.GYMmanagementsystem;
    exports com.example.GYMmanagementsystem.Utilities;
    opens com.example.GYMmanagementsystem.Utilities to javafx.fxml;
    exports com.example.GYMmanagementsystem.Dashboard;
    opens com.example.GYMmanagementsystem.Dashboard to javafx.fxml;
    exports Payment;
    opens Payment to javafx.fxml;
}
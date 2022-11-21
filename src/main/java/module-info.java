module app {
    requires javafx.controls;
    requires javafx.fxml;


    opens app.Controller;
    opens app;
    opens app.Logic;
}
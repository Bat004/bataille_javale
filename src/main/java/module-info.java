module school.coda.baptiste{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens school.coda.baptiste to javafx.fxml;
    exports school.coda.baptiste;
}
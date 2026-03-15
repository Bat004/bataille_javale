module bataille.javale {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;

    opens school.coda.baptiste to javafx.fxml, javafx.controls, javafx.media;
    opens images;
    opens sounds;
    exports school.coda.baptiste;
}
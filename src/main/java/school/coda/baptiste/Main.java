package school.coda.baptiste;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import school.coda.baptiste.ui.menu;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        menu vueMenu = new menu(stage);

        Scene scene = new Scene(vueMenu.creerContenu(), 1000, 700);

        stage.setTitle("Bataille javale");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

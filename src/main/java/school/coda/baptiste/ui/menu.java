package school.coda.baptiste.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class menu {

    private final Stage stage;

    public menu(Stage stage) {
        this.stage = stage;
    }

    public Parent creerContenu() {
        Label titre = new Label("Bataille Javale");
        titre.setStyle(
                "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1a1a2e;" +
                        "-fx-font-family: 'Georgia', serif;"
        );

        Label credits = new Label("Baptiste Josserand");
        credits.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #999999;"
        );

        Button boutonJouer = creerBouton("Nouvelle partie", true);
        Button boutonQuitter = creerBouton("Quitter", false);

        boutonJouer.setOnAction(e -> {
            placement vuePlacement = new placement(stage);
            stage.getScene().setRoot(vuePlacement.creerContenu());
        });
        boutonQuitter.setOnAction(e -> stage.close());

        VBox centre = new VBox(16, titre, credits, boutonJouer, boutonQuitter);
        centre.setAlignment(Pos.CENTER);
        VBox.setMargin(boutonJouer, new Insets(20, 0, 0, 0));

        BorderPane racine = new BorderPane();
        racine.setStyle("-fx-background-color: #f5f5f5;");
        racine.setCenter(centre);
        return racine;
    }

    private Button creerBouton(String texte, boolean principal) {
        Button btn = new Button(texte);
        btn.setPrefWidth(200);
        btn.setPrefHeight(42);
        if (principal) {
            btn.setStyle(
                    "-fx-background-color: #1a1a2e;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-radius: 6;" +
                            "-fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #999999;" +
                            "-fx-font-size: 13px;" +
                            "-fx-border-color: #cccccc;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-cursor: hand;"
            );
        }
        return btn;
    }
}
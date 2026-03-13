package school.coda.baptiste.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Georgia', serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 4, 0.0, 0, 2);"
        );

        Label credits = new Label("Baptiste Josserand");
        credits.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: rgba(255,255,255,0.8);"
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
        centre.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        centre.setPadding(new Insets(40));
        VBox.setMargin(boutonJouer, new Insets(20, 0, 0, 0));

        BorderPane racine = new BorderPane();

        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.jpg"));
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false)
            );
            racine.setBackground(new Background(bgImage));
        } catch (Exception e) {
            racine.setStyle("-fx-background-color: #1a3a52;");
        }

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
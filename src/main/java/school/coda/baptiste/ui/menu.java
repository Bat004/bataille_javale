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

// Conventions : Les noms de classe doivent être en PascalCase
public class menu {

    private final Stage stage;

    public menu(Stage stage) {
        this.stage = stage;
    }

    public Parent creerContenu() {
        Label titre = new Label("⚓ Bataille Javale");
        titre.setStyle(
                "-fx-font-size: 48px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #0066cc;" +
                        "-fx-font-family: 'Georgia', serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0.0, 0, 3);"
        );

        Label sousTitre = new Label("Un jeu de stratégie naval classique");
        sousTitre.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #0066cc;" +
                        "-fx-font-style: italic;"
        );

        Label credits = new Label("Créé par Baptiste Josserand");
        credits.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: rgba(0,102,204,0.7);"
        );

        Button boutonJouer = creerBouton("🎮 Mode Normal", true);
        Button boutonSalve = creerBouton("💥 Mode Salve", true);
        Button boutonQuitter = creerBouton("❌ Quitter", false);

        boutonJouer.setOnAction(e -> { // Architecture:   Le nom qualifié peut être remplacé par un import
            placement vuePlacement = new placement(stage, school.coda.baptiste.modele.ModeJeu.NORMAL);
            stage.getScene().setRoot(vuePlacement.creerContenu());
        });
        boutonSalve.setOnAction(e -> {
            placement vuePlacement = new placement(stage, school.coda.baptiste.modele.ModeJeu.SALVE);
            stage.getScene().setRoot(vuePlacement.creerContenu());
        });
        boutonQuitter.setOnAction(e -> stage.close());

        VBox centre = new VBox(16, titre, sousTitre, boutonJouer, boutonSalve, boutonQuitter, credits);
        centre.setAlignment(Pos.CENTER);
        centre.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-border-radius: 16;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.0, 0, 5);"
        );
        centre.setPadding(new Insets(60, 80, 60, 80));
        VBox.setMargin(boutonJouer, new Insets(20, 0, 0, 0));
        VBox.setMargin(credits, new Insets(30, 0, 0, 0));

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
            racine.setStyle("-fx-background: linear-gradient(to bottom, #e3f2fd, #bbdefb);");
        }

        VBox wrapper = new VBox(centre);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(40));
        racine.setCenter(wrapper);
        return racine;
    }

    private Button creerBouton(String texte, boolean principal) {
        Button btn = new Button(texte);
        btn.setPrefWidth(220);
        btn.setPrefHeight(48);
        if (principal) {
            btn.setStyle(
                    "-fx-background-color: #0066cc;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,102,204,0.4), 6, 0.0, 0, 2);"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-text-fill: #0066cc;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-color: #0066cc;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        }
        return btn;
    }
}

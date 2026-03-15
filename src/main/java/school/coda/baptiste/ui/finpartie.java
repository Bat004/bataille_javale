package school.coda.baptiste.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import school.coda.baptiste.service.jeu;

// Conventions : Les noms de classe doivent être en PascalCase
public class finpartie {

    private final Stage stage;
    private final jeu partieJeu;

    public finpartie(Stage stage, jeu partieJeu) {
        this.stage = stage;
        this.partieJeu = partieJeu;
    }

    public Parent creerContenu() {
        boolean joueurGagne = !"Ordinateur".equals(partieJeu.getGagnant());

        Label icone = new Label(joueurGagne ? "🎉" : "💔");
        icone.setStyle("-fx-font-size: 60px;");

        Label resultat = new Label(joueurGagne ? "Victoire !" : "Défaite !");
        resultat.setStyle(
                "-fx-font-size: 48px; -fx-font-weight: bold;" +
                        "-fx-text-fill: " + (joueurGagne ? "#0066cc" : "#ff3333") + ";" +
                        "-fx-font-family: 'Georgia', serif;"
        );

        Label sousTitre = new Label(joueurGagne
                ? "Vous avez coulé toute la flotte ennemie !"
                : "L'ordinateur a coulé toute votre flotte."
        );
        sousTitre.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555; -fx-font-weight: bold;");

        Label tours = new Label("Partie terminée en " + partieJeu.getNumeroTour() + " tours");
        tours.setStyle("-fx-font-size: 14px; -fx-text-fill: #0066cc;");

        Label gagnant = new Label("Gagnant : " + partieJeu.getGagnant());
        gagnant.setStyle("-fx-font-size: 13px; -fx-text-fill: #888888;");

        Button boutonMenu = new Button("🏠 Retour au menu");
        boutonMenu.setPrefWidth(200);
        boutonMenu.setPrefHeight(48);
        boutonMenu.setStyle(
                "-fx-background-color: #0066cc;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,102,204,0.4), 6, 0.0, 0, 2);"
        );
        boutonMenu.setOnAction(e -> {
            menu vueMenu = new menu(stage);
            stage.getScene().setRoot(vueMenu.creerContenu());
        });

        VBox centre = new VBox(16, icone, resultat, sousTitre, tours, gagnant, boutonMenu);
        centre.setAlignment(Pos.CENTER);
        centre.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 16;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.0, 0, 5);"
        );
        centre.setPadding(new Insets(60, 80, 60, 80));
        VBox.setMargin(boutonMenu, new Insets(20, 0, 0, 0));

        VBox wrapper = new VBox(centre);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(40));

        BorderPane racine = new BorderPane();
        racine.setStyle("-fx-background: linear-gradient(to bottom, #f8f9fa, #e8ecf1);");
        racine.setCenter(wrapper);
        return racine;
    }
}

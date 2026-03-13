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

public class finpartie {

    private final Stage stage;
    private final jeu partieJeu;

    public finpartie(Stage stage, jeu partieJeu) {
        this.stage = stage;
        this.partieJeu = partieJeu;
    }

    public Parent creerContenu() {
        boolean joueurGagne = !"Ordinateur".equals(partieJeu.getGagnant());

        Label resultat = new Label(joueurGagne ? "Victoire" : "Defaite");
        resultat.setStyle(
                "-fx-font-size: 40px; -fx-font-weight: bold;" +
                        "-fx-text-fill: " + (joueurGagne ? "#1a1a2e" : "#888888") + ";" +
                        "-fx-font-family: 'Georgia', serif;"
        );

        Label sousTitre = new Label(joueurGagne
                ? "Vous avez coule toute la flotte ennemie."
                : "L'ordinateur a coule toute votre flotte."
        );
        sousTitre.setStyle("-fx-font-size: 15px; -fx-text-fill: #888888;");

        Label tours = new Label("Partie terminee en " + partieJeu.getNumeroTour() + " tours.");
        tours.setStyle("-fx-font-size: 13px; -fx-text-fill: #aaaaaa;");

        Button boutonMenu = new Button("Retour au menu");
        boutonMenu.setPrefWidth(180);
        boutonMenu.setPrefHeight(40);
        boutonMenu.setStyle(
                "-fx-background-color: #1a1a2e;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        );
        boutonMenu.setOnAction(e -> {
            menu vueMenu = new menu(stage);
            stage.getScene().setRoot(vueMenu.creerContenu());
        });

        VBox centre = new VBox(14, resultat, sousTitre, tours, boutonMenu);
        centre.setAlignment(Pos.CENTER);
        VBox.setMargin(boutonMenu, new Insets(16, 0, 0, 0));

        BorderPane racine = new BorderPane();
        racine.setStyle("-fx-background-color: #f5f5f5;");
        racine.setCenter(centre);
        racine.setPadding(new Insets(40));
        return racine;
    }
}
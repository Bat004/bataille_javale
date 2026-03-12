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
        Label titre = new Label("Bataille javale");
        titre.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        Label sousTitre = new Label("Projet Java");
        sousTitre.setStyle("-fx-font-size: 18px;");

        Label credits = new Label("Baptiste, Josserand");
        credits.setStyle("-fx-font-size: 14px;");

        Button boutonNouvellePartie = new Button("Nouvelle partie");
        boutonNouvellePartie.setPrefWidth(220);
        boutonNouvellePartie.setPrefHeight(45);

        Button boutonQuitter = new Button("Quitter");
        boutonQuitter.setPrefWidth(220);
        boutonQuitter.setPrefHeight(45);

        boutonNouvellePartie.setOnAction(event -> {
            placement vuePlacement = new placement(stage);
            stage.getScene().setRoot(vuePlacement.creerContenu());
        });

        boutonQuitter.setOnAction(event -> stage.close());

        VBox boiteCentre = new VBox(20, titre, sousTitre, credits, boutonNouvellePartie, boutonQuitter);
        boiteCentre.setAlignment(Pos.CENTER);

        BorderPane racine = new BorderPane();
        racine.setCenter(boiteCentre);
        racine.setPadding(new Insets(20));

        return racine;
    }
}

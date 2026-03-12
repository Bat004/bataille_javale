package school.coda.baptiste.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class placement {
    private final Stage stage;

    public placement(Stage stage) {
        this.stage = stage;
    }

    public Parent creerContenu() {
        Label titre = new Label("Phase de placement");
        titre.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label message = new Label("Placez vos bateaux.");
        message.setStyle("-fx-font-size: 16px;");

        Button boutonRetour = new Button("Retour au menu");
        boutonRetour.setPrefWidth(220);
        boutonRetour.setPrefHeight(45);

        boutonRetour.setOnAction(event -> {
            menu vueMenu = new menu(stage);
            stage.getScene().setRoot(vueMenu.creerContenu());
        });

        VBox boiteCentre = new VBox(20, titre, message, boutonRetour);
        boiteCentre.setAlignment(Pos.CENTER);

        BorderPane racine = new BorderPane();
        racine.setCenter(boiteCentre);
        racine.setPadding(new Insets(20));

        return racine;
    }
}

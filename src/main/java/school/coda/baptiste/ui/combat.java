package school.coda.baptiste.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import school.coda.baptiste.modele.Grille;
import school.coda.baptiste.modele.position;
import school.coda.baptiste.modele.resultat;
import school.coda.baptiste.modele.typeresultat;
import school.coda.baptiste.service.jeu;
import school.coda.baptiste.service.sonManager;

// 🚨Conventions Java: Les noms de classe doivent être en PascalCase
public class combat {

    private static final int TAILLE_CELLULE = 40;

    private final Stage stage;
    private final jeu partieJeu;
    private final sonManager sons = sonManager.getInstance();

    private final Rectangle[][] cellulesRadar = new Rectangle[10][10];
    private final Rectangle[][] cellulesOcean = new Rectangle[10][10];

    private Label labelTour;
    private Label labelStatut;
    private VBox listeHistorique;
    private boolean tourJoueur = true;

    public combat(Stage stage, jeu partieJeu) {
        this.stage = stage;
        this.partieJeu = partieJeu;
    }

    public Parent creerContenu() {
        BorderPane racine = new BorderPane();
        racine.setStyle("-fx-background-color: #f5f5f5;");
        racine.setPadding(new Insets(24));

        racine.setTop(creerEntete());
        racine.setCenter(creerZoneGrilles());
        racine.setRight(creerPanneauDroit());
        racine.setBottom(creerBarreStatut());

        rafraichirGrilleOcean();

        Platform.runLater(() -> sons.demarrerMusique());

        return racine;
    }

    // ── Entête ───────────────────────────────────────────────────────────────

    private HBox creerEntete() {
        Label titre = new Label("Combat");
        titre.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;" +
                "-fx-text-fill: #1a1a2e; -fx-font-family: 'Georgia', serif;"
        );

        labelTour = new Label("Tour 1");
        labelTour.setStyle(
                "-fx-font-size: 13px; -fx-text-fill: #888888;" +
                "-fx-border-color: #dddddd; -fx-border-radius: 6;" +
                "-fx-background-color: white; -fx-background-radius: 6;" +
                "-fx-padding: 4 12 4 12;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox entete = new HBox(12, titre, spacer, labelTour);
        entete.setAlignment(Pos.CENTER_LEFT);
        entete.setPadding(new Insets(0, 0, 20, 0));
        return entete;
    }

    // ── Zone grilles ─────────────────────────────────────────────────────────

    private HBox creerZoneGrilles() {
        VBox zoneRadar = new VBox(8,
                labelGrille("Grille radar  —  Mes tirs"),
                creerGrilleRadar()
        );
        zoneRadar.setAlignment(Pos.TOP_LEFT);

        VBox zoneOcean = new VBox(8,
                labelGrille("Grille ocean  —  Mes vaisseaux"),
                creerGrilleOcean()
        );
        zoneOcean.setAlignment(Pos.TOP_LEFT);

        HBox zone = new HBox(30, zoneRadar, zoneOcean);
        zone.setAlignment(Pos.TOP_CENTER);
        return zone;
    }

    private Label labelGrille(String texte) {
        Label lbl = new Label(texte);
        lbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        return lbl;
    }

    // ── Grille radar ─────────────────────────────────────────────────────────

    private GridPane creerGrilleRadar() {
        GridPane grille = new GridPane();
        grille.setHgap(2);
        grille.setVgap(2);

        for (int col = 0; col < 10; col++) {
            grille.add(labelEntete(String.valueOf(col + 1)), col + 1, 0);
        }
        for (int ligne = 0; ligne < 10; ligne++) {
            grille.add(labelEntete(String.valueOf((char) ('A' + ligne))), 0, ligne + 1);
        }

        for (int ligne = 0; ligne < 10; ligne++) {
            for (int col = 0; col < 10; col++) {
                Rectangle rect = new Rectangle(TAILLE_CELLULE, TAILLE_CELLULE);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.web("#dddddd"));
                rect.setStrokeWidth(1);
                rect.setArcWidth(3);
                rect.setArcHeight(3);

                final int l = ligne;
                final int c = col;

                rect.setOnMouseEntered(e -> {
                    if (tourJoueur && !partieJeu.getJoueurHumain().aDejaTire(new position(l, c))) {
                        rect.setFill(Color.web("#e8f0fe"));
                    }
                });
                rect.setOnMouseExited(e -> {
                    if (!partieJeu.getJoueurHumain().aDejaTire(new position(l, c))) {
                        rect.setFill(Color.WHITE);
                    }
                });
                rect.setOnMouseClicked(e -> {
                    if (tourJoueur) {
                        jouerTourJoueur(l, c);
                    }
                });

                cellulesRadar[ligne][col] = rect;
                grille.add(rect, col + 1, ligne + 1);
            }
        }
        return grille;
    }

    // ── Grille ocean ─────────────────────────────────────────────────────────

    private GridPane creerGrilleOcean() {
        GridPane grille = new GridPane();
        grille.setHgap(2);
        grille.setVgap(2);

        for (int col = 0; col < 10; col++) {
            grille.add(labelEntete(String.valueOf(col + 1)), col + 1, 0);
        }
        for (int ligne = 0; ligne < 10; ligne++) {
            grille.add(labelEntete(String.valueOf((char) ('A' + ligne))), 0, ligne + 1);
        }

        for (int ligne = 0; ligne < 10; ligne++) {
            for (int col = 0; col < 10; col++) {
                Rectangle rect = new Rectangle(TAILLE_CELLULE, TAILLE_CELLULE);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.web("#dddddd"));
                rect.setStrokeWidth(1);
                rect.setArcWidth(3);
                rect.setArcHeight(3);
                cellulesOcean[ligne][col] = rect;
                grille.add(rect, col + 1, ligne + 1);
            }
        }
        return grille;
    }

    private Label labelEntete(String texte) {
        Label lbl = new Label(texte);
        lbl.setMinSize(TAILLE_CELLULE, TAILLE_CELLULE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");
        return lbl;
    }

    // ── Panneau droit ─────────────────────────────────────────────────────────

    private VBox creerPanneauDroit() {
        VBox panneau = new VBox(16,
                creerPanneauHistorique(),
                creerControleSons()
        );
        panneau.setPadding(new Insets(0, 0, 0, 24));
        panneau.setMinWidth(200);
        panneau.setMaxWidth(210);
        return panneau;
    }

    private VBox creerPanneauHistorique() {
        Label titre = new Label("Historique");
        titre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        listeHistorique = new VBox(4);
        listeHistorique.setPadding(new Insets(8));

        ScrollPane scroll = new ScrollPane(listeHistorique);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(340);
        scroll.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #eeeeee;" +
                "-fx-border-radius: 6; -fx-background-radius: 6;"
        );

        return new VBox(8, titre, scroll);
    }

    private VBox creerControleSons() {
        Label titre = new Label("Sons");
        titre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        CheckBox cbMusique = new CheckBox("Musique");
        cbMusique.setSelected(sons.isMusiqueActive());
        cbMusique.setStyle("-fx-text-fill: #555555; -fx-font-size: 12px;");
        cbMusique.setOnAction(e -> sons.setMusiqueActive(cbMusique.isSelected()));

        CheckBox cbSons = new CheckBox("Effets sonores");
        cbSons.setSelected(sons.isSonsActifs());
        cbSons.setStyle("-fx-text-fill: #555555; -fx-font-size: 12px;");
        cbSons.setOnAction(e -> sons.setSonsActifs(cbSons.isSelected()));

        VBox box = new VBox(6, titre, cbMusique, cbSons);
        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #eeeeee;" +
                "-fx-border-radius: 6; -fx-background-radius: 6;" +
                "-fx-padding: 10;"
        );
        return box;
    }

    // ── Barre de statut ───────────────────────────────────────────────────────

    private HBox creerBarreStatut() {
        labelStatut = new Label("A vous de jouer — cliquez sur la grille radar pour tirer.");
        labelStatut.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px; -fx-padding: 14 0 0 0;");
        return new HBox(labelStatut);
    }

    // ── Logique de jeu ────────────────────────────────────────────────────────

    private void jouerTourJoueur(int ligne, int col) {
        position cible = new position(ligne, col);

        if (partieJeu.getJoueurHumain().aDejaTire(cible)) {
            labelStatut.setText("Vous avez deja tire ici !");
            return;
        }

        tourJoueur = false;

        resultat res = partieJeu.tirerJoueur(cible);
        appliquerCouleurRadar(ligne, col, res);
        jouerSonResultat(res);

        labelStatut.setText("Votre tir en " + cible + " : " + res.getMessage());
        ajouterHistorique("Vous  " + cible + "  " + res.getMessage(), res.getTypeResultat());
        labelTour.setText("Tour " + partieJeu.getNumeroTour());

        if (partieJeu.partieTerminee()) {
            allerFinPartie();
            return;
        }

        Platform.runLater(() -> {
            resultat resBot = partieJeu.tirerOrdinateur();
            rafraichirGrilleOcean();
            ajouterHistorique("Ordi  " + resBot.getMessage(), resBot.getTypeResultat());
            labelTour.setText("Tour " + partieJeu.getNumeroTour());

            if (partieJeu.partieTerminee()) {
                allerFinPartie();
            } else {
                tourJoueur = true;
                labelStatut.setText("A vous de jouer !");
            }
        });
    }

    private void jouerSonResultat(resultat res) {
        switch (res.getTypeResultat()) {
            case RATE -> sons.jouerRate();
            case TOUCHE -> sons.jouerTouche();
            case COULE -> {
                if (partieJeu.partieTerminee()) {
                    sons.jouerVictoire();
                } else {
                    sons.jouerCoule();
                }
            }
            default -> {
            }
        }
    }

    private void appliquerCouleurRadar(int ligne, int col, resultat res) {
        Rectangle rect = cellulesRadar[ligne][col];
        typeresultat type = res.getTypeResultat();

        if (type == typeresultat.TOUCHE || type == typeresultat.COULE) {
            rect.setFill(Color.web("#e74c3c"));
            rect.setStroke(Color.web("#c0392b"));
        } else if (type == typeresultat.RATE) {
            rect.setFill(Color.web("#bdc3c7"));
            rect.setStroke(Color.web("#aaaaaa"));
        }

        rect.setOnMouseEntered(null);
        rect.setOnMouseExited(null);
    }

    private void rafraichirGrilleOcean() {
        Grille ocean = partieJeu.getJoueurHumain().getGrilleOcean();

        for (int ligne = 0; ligne < 10; ligne++) {
            for (int col = 0; col < 10; col++) {
                position pos = new position(ligne, col);
                Rectangle rect = cellulesOcean[ligne][col];

                if (ocean.caseTouchee(pos)) {
                    rect.setFill(Color.web("#e74c3c"));
                    rect.setStroke(Color.web("#c0392b"));
                } else if (ocean.caseRatee(pos)) {
                    rect.setFill(Color.web("#bdc3c7"));
                    rect.setStroke(Color.web("#aaaaaa"));
                } else if (ocean.aUnBateauSur(pos)) {
                    rect.setFill(Color.web("#4a90d9"));
                    rect.setStroke(Color.web("#2e70b8"));
                } else {
                    rect.setFill(Color.WHITE);
                    rect.setStroke(Color.web("#dddddd"));
                }
            }
        }
    }

    private void ajouterHistorique(String message, typeresultat type) {
        Label lbl = new Label(message);
        lbl.setWrapText(true);
        lbl.setMaxWidth(185);

        String couleur;
        if (type == typeresultat.COULE) {
            couleur = "#c0392b";
        } else if (type == typeresultat.TOUCHE) {
            couleur = "#e67e22";
        } else {
            couleur = "#888888";
        }

        lbl.setStyle("-fx-text-fill: " + couleur + "; -fx-font-size: 12px; -fx-padding: 2 0 2 0;");
        listeHistorique.getChildren().add(0, lbl);
    }

    private void allerFinPartie() {
        sons.arreterMusique();
        finpartie vueFinPartie = new finpartie(stage, partieJeu);
        stage.getScene().setRoot(vueFinPartie.creerContenu());
    }
}
package school.coda.baptiste.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import school.coda.baptiste.modele.Grille;
import school.coda.baptiste.modele.ModeJeu;
import school.coda.baptiste.modele.position;
import school.coda.baptiste.modele.resultat;
import school.coda.baptiste.modele.typeresultat;
import school.coda.baptiste.service.jeu;

public class combat {

    private static final int TAILLE_CELLULE = 40;
    private static final String COULEUR_MER = "#0066cc";
    private static final String COULEUR_BATEAU = "#808080";
    private static final String COULEUR_TOUCHE = "#ff3333";
    private static final String COULEUR_EAU = "#ffffff";

    private final Stage stage;
    private final jeu partieJeu;

    private final Rectangle[][] cellulesRadar = new Rectangle[10][10];
    private final Rectangle[][] cellulesOcean = new Rectangle[10][10];

    private Label labelTour;
    private Label labelStatut;
    private Label labelTirsRestants;
    private VBox listeHistorique;
    private boolean tourJoueur = true;

    public combat(Stage stage, jeu partieJeu) {
        this.stage = stage;
        this.partieJeu = partieJeu;
    }

    public Parent creerContenu() {
        BorderPane racine = new BorderPane();
        racine.setStyle("-fx-background: linear-gradient(to bottom, #f8f9fa, #e8ecf1);");
        racine.setPadding(new Insets(24));

        racine.setTop(creerTitre());
        racine.setCenter(creerZonePrincipale());
        racine.setBottom(creerBarreStatutCadre());

        rafraichirGrilleOcean();
        return racine;
    }

    private HBox creerTitre() {
        Label titre = new Label("⚔️ Combat Naval");
        titre.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #0066cc; -fx-font-family: 'Georgia', serif;"
        );

        Label modeLabel = new Label("(" + partieJeu.getModeJeu().getNom() + ")");
        modeLabel.setStyle(
                "-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-style: italic;"
        );

        HBox titreBox = new HBox(10, titre, modeLabel);
        titreBox.setAlignment(Pos.CENTER);
        titreBox.setPadding(new Insets(0, 0, 20, 0));
        return titreBox;
    }

    private HBox creerZonePrincipale() {
        VBox zoneOcean = new VBox(8,
                labelGrille("🌊 Mes vaisseaux — Grille océan"),
                creerGrilleOcean()
        );
        zoneOcean.setAlignment(Pos.TOP_LEFT);

        VBox zoneRadar = new VBox(8,
                labelGrille("🎯 Mes tirs — Grille radar"),
                creerGrilleRadar()
        );
        zoneRadar.setAlignment(Pos.TOP_LEFT);

        HBox grilles = new HBox(30, zoneOcean, zoneRadar);
        grilles.setAlignment(Pos.TOP_CENTER);

        HBox zonePrincipale = new HBox(30, grilles, creerPanneauHistorique());
        zonePrincipale.setAlignment(Pos.TOP_CENTER);
        return zonePrincipale;
    }

    private Label labelGrille(String texte) {
        Label lbl = new Label(texte);
        lbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        return lbl;
    }

    // Changé GridPane en Region pour accepter le VBox final
    private Region creerGrilleRadar() {
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
                rect.setFill(Color.web(COULEUR_MER));
                rect.setStroke(Color.web("#0052a3"));
                rect.setStrokeWidth(1);
                rect.setArcWidth(3);
                rect.setArcHeight(3);

                final int l = ligne;
                final int c = col;

                rect.setOnMouseEntered(e -> {
                    if (tourJoueur && !partieJeu.getJoueurHumain().aDejaTire(new position(l, c)) && partieJeu.aTirsDisponibles()) {
                        rect.setFill(Color.web("#4da6ff"));
                    }
                });
                rect.setOnMouseExited(e -> {
                    if (!partieJeu.getJoueurHumain().aDejaTire(new position(l, c))) {
                        rect.setFill(Color.web(COULEUR_MER));
                    }
                });
                rect.setOnMouseClicked(e -> {
                    if (tourJoueur && partieJeu.aTirsDisponibles()) {
                        jouerTourJoueur(l, c);
                    }
                });

                cellulesRadar[ligne][col] = rect;
                grille.add(rect, col + 1, ligne + 1);
            }
        }

        VBox box = new VBox(0, grille);
        box.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.0, 0, 2);");
        box.setPadding(new Insets(10));
        return box;
    }

    // Changé GridPane en Region pour accepter le VBox final
    private Region creerGrilleOcean() {
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
                rect.setFill(Color.web(COULEUR_MER));
                rect.setStroke(Color.web("#0052a3"));
                rect.setStrokeWidth(1);
                rect.setArcWidth(3);
                rect.setArcHeight(3);
                cellulesOcean[ligne][col] = rect;
                grille.add(rect, col + 1, ligne + 1);
            }
        }

        VBox box = new VBox(0, grille);
        box.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.0, 0, 2);");
        box.setPadding(new Insets(10));
        return box;
    }

    private Label labelEntete(String texte) {
        Label lbl = new Label(texte);
        lbl.setMinSize(TAILLE_CELLULE, TAILLE_CELLULE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-text-fill: #0066cc; -fx-font-size: 11px; -fx-font-weight: bold;");
        return lbl;
    }

    private VBox creerPanneauHistorique() {
        Label titre = new Label("📋 Historique");
        titre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");

        labelTour = new Label("Tour 1");
        labelTour.setStyle(
                "-fx-font-size: 13px; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-border-color: #0066cc; -fx-border-radius: 6;" +
                        "-fx-background-color: #0066cc; -fx-background-radius: 6;" +
                        "-fx-padding: 6 14 6 14;"
        );

        labelTirsRestants = new Label();
        labelTirsRestants.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666; -fx-padding: 4 0 0 0;");
        mettreAJourLabelTirsRestants();

        HBox enteteHistorique = new HBox(8, titre, labelTour);
        enteteHistorique.setAlignment(Pos.CENTER_LEFT);

        listeHistorique = new VBox(4);
        listeHistorique.setPadding(new Insets(8));

        ScrollPane scroll = new ScrollPane(listeHistorique);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(420);
        scroll.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 6; -fx-background-radius: 6;"
        );

        VBox panneau = new VBox(8, enteteHistorique, labelTirsRestants, scroll);
        panneau.setMinWidth(200);
        panneau.setMaxWidth(210);
        panneau.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.0, 0, 2); -fx-padding: 16;");
        return panneau;
    }

    private HBox creerBarreStatutCadre() {
        labelStatut = new Label("À vous de jouer — cliquez sur la grille radar pour tirer.");
        labelStatut.setStyle("-fx-text-fill: #0066cc; -fx-font-size: 13px; -fx-font-weight: bold;");
        labelStatut.setWrapText(true);
        labelStatut.setAlignment(Pos.CENTER);

        VBox cadreStatut = new VBox(labelStatut);
        cadreStatut.setAlignment(Pos.CENTER);
        cadreStatut.setPadding(new Insets(10, 20, 10, 20));
        cadreStatut.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0.0, 0, 1);"
        );

        HBox conteneurCadre = new HBox(cadreStatut);
        conteneurCadre.setAlignment(Pos.CENTER);
        conteneurCadre.setPadding(new Insets(20, 0, 0, 0));
        return conteneurCadre;
    }

    private void jouerTourJoueur(int ligne, int col) {
        position cible = new position(ligne, col);

        if (partieJeu.getJoueurHumain().aDejaTire(cible)) {
            labelStatut.setText("Vous avez déjà tiré ici !");
            return;
        }

        if (!partieJeu.aTirsDisponibles()) {
            labelStatut.setText("Vous avez épuisé vos tirs pour ce tour !");
            return;
        }

        resultat res = partieJeu.tirerJoueur(cible);
        appliquerCouleurRadar(ligne, col, res);
        labelStatut.setText("Votre tir en " + cible + " : " + res.getMessage());
        ajouterHistorique("Vous  " + cible + "  " + res.getMessage(), res.getTypeResultat());
        labelTour.setText("Tour " + partieJeu.getNumeroTour());
        mettreAJourLabelTirsRestants();

        if (partieJeu.partieTerminee()) {
            allerFinPartie();
            return;
        }

        if (partieJeu.getModeJeu() == ModeJeu.SALVE && partieJeu.aTirsDisponibles()) {
            labelStatut.setText("Tirs restants : " + partieJeu.getTirsRestantsCetTour() + " — Continuez à tirer !");
            return;
        }

        tourJoueur = false;

        javafx.application.Platform.runLater(() -> {
            int nombreTirsOrdinateur = partieJeu.getNombreTirsDisponiblesOrdinateur();

            for (int i = 0; i < nombreTirsOrdinateur; i++) {
                resultat resBot = partieJeu.tirerOrdinateur();
                rafraichirGrilleOcean();
                ajouterHistorique("Ordi  " + resBot.getMessage(), resBot.getTypeResultat());
                labelTour.setText("Tour " + partieJeu.getNumeroTour());
                mettreAJourLabelTirsRestants();

                if (partieJeu.partieTerminee()) {
                    allerFinPartie();
                    return;
                }
            }

            tourJoueur = true;
            labelStatut.setText("À vous de jouer !");
        });
    }

    private void appliquerCouleurRadar(int ligne, int col, resultat res) {
        Rectangle rect = cellulesRadar[ligne][col];
        typeresultat type = res.getTypeResultat();

        if (type == typeresultat.TOUCHE || type == typeresultat.COULE) {
            rect.setFill(Color.web(COULEUR_TOUCHE));
            rect.setStroke(Color.web("#cc0000"));
        } else if (type == typeresultat.RATE) {
            rect.setFill(Color.web(COULEUR_EAU));
            rect.setStroke(Color.web("#cccccc"));
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
                    rect.setFill(Color.web(COULEUR_TOUCHE));
                    rect.setStroke(Color.web("#cc0000"));
                } else if (ocean.caseRatee(pos)) {
                    rect.setFill(Color.web(COULEUR_EAU));
                    rect.setStroke(Color.web("#cccccc"));
                } else if (ocean.aUnBateauSur(pos)) {
                    rect.setFill(Color.web(COULEUR_BATEAU));
                    rect.setStroke(Color.web("#606060"));
                } else {
                    rect.setFill(Color.web(COULEUR_MER));
                    rect.setStroke(Color.web("#0052a3"));
                }
            }
        }
    }

    private void ajouterHistorique(String message, typeresultat type) {
        Label lbl = new Label("Tour " + partieJeu.getNumeroTour() + ": " + message);
        lbl.setWrapText(true);
        lbl.setMaxWidth(185);

        String couleur;
        if (type == typeresultat.COULE) {
            couleur = "#cc0000";
        } else if (type == typeresultat.TOUCHE) {
            couleur = "#ff6600";
        } else {
            couleur = "#888888";
        }

        lbl.setStyle("-fx-text-fill: " + couleur + "; -fx-font-size: 12px; -fx-padding: 2 0 2 0; -fx-font-weight: bold;");
        listeHistorique.getChildren().add(0, lbl);
    }

    private void mettreAJourLabelTirsRestants() {
        if (partieJeu.getModeJeu() == ModeJeu.SALVE) {
            int tirsRestants = partieJeu.getTirsRestantsCetTour();
            int bateauxRestants = partieJeu.getJoueurHumain().getGrilleOcean().getNombreBateauxRestants();
            labelTirsRestants.setText("Bateaux restants : " + bateauxRestants + " | Tirs restants ce tour : " + tirsRestants);
        } else {
            labelTirsRestants.setText("");
        }
    }

    private void allerFinPartie() {
        finpartie vueFinPartie = new finpartie(stage, partieJeu);
        stage.getScene().setRoot(vueFinPartie.creerContenu());
    }
}
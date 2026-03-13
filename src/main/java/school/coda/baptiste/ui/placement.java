package school.coda.baptiste.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import school.coda.baptiste.modele.Grille;
import school.coda.baptiste.modele.orientation;
import school.coda.baptiste.modele.position;
import school.coda.baptiste.modele.typebateau;
import school.coda.baptiste.service.jeu;

public class placement {

    private static final int TAILLE_CELLULE_BASE = 46;

    private final Stage stage;
    private final jeu partieJeu;

    private typebateau bateauSelectionne;
    private orientation orientationSelectionnee = orientation.HORIZONTALE;
    private int tailleGrille = 10;
    private int tailleCell = TAILLE_CELLULE_BASE;

    private Rectangle[][] cellules;
    private Label labelStatut;
    private Button boutonDemarrer;
    private VBox listeBateaux;
    private VBox zoneGrille;

    public placement(Stage stage) {
        this(stage, new jeu("Joueur"));
    }

    public placement(Stage stage, jeu partieJeu) {
        this.stage = stage;
        this.partieJeu = partieJeu;
    }

    public Parent creerContenu() {
        BorderPane racine = new BorderPane();
        racine.setStyle("-fx-background-color: #f5f5f5;");
        racine.setPadding(new Insets(24));

        racine.setTop(creerEntete());
        racine.setCenter(creerZoneCentrale());
        racine.setBottom(creerBarreStatut());

        selectionnerProchainBateau();
        return racine;
    }

    // ── Entête ───────────────────────────────────────────────────────────────

    private HBox creerEntete() {
        Label titre = new Label("Placement des vaisseaux");
        titre.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #1a1a2e; -fx-font-family: 'Georgia', serif;"
        );

        Button retour = new Button("← Menu");
        retour.setStyle(styleBoutonSecondaire());
        retour.setOnAction(e -> {
            menu vueMenu = new menu(stage);
            stage.getScene().setRoot(vueMenu.creerContenu());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox entete = new HBox(12, titre, spacer, retour);
        entete.setAlignment(Pos.CENTER_LEFT);
        entete.setPadding(new Insets(0, 0, 20, 0));
        return entete;
    }

    // ── Zone centrale (grille + panneau) ─────────────────────────────────────

    private HBox creerZoneCentrale() {
        zoneGrille = creerGrille();
        HBox zone = new HBox(30, zoneGrille, creerPanneau());
        zone.setAlignment(Pos.TOP_CENTER);
        return zone;
    }

    // ── Grille ───────────────────────────────────────────────────────────────

    private VBox creerGrille() {
        cellules = new Rectangle[tailleGrille][tailleGrille];
        return genererGrille();
    }

    private VBox genererGrille() {
        GridPane grille = new GridPane();
        grille.setHgap(2);
        grille.setVgap(2);

        for (int col = 0; col < tailleGrille; col++) {
            grille.add(labelEntete(String.valueOf(col + 1)), col + 1, 0);
        }
        for (int ligne = 0; ligne < tailleGrille; ligne++) {
            grille.add(labelEntete(String.valueOf((char) ('A' + ligne))), 0, ligne + 1);
        }

        for (int ligne = 0; ligne < tailleGrille; ligne++) {
            for (int col = 0; col < tailleGrille; col++) {
                Rectangle rect = new Rectangle(tailleCell, tailleCell);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.web("#dddddd"));
                rect.setStrokeWidth(1);
                rect.setArcWidth(4);
                rect.setArcHeight(4);

                final int l = ligne, c = col;
                rect.setOnMouseEntered(e -> survollerCellule(l, c));
                rect.setOnMouseExited(e -> reinitialiserSurvol(l, c));
                rect.setOnMouseClicked(e -> clicCellule(l, c));

                cellules[ligne][col] = rect;
                grille.add(rect, col + 1, ligne + 1);
            }
        }

        VBox box = new VBox(0, grille);
        box.setAlignment(Pos.TOP_LEFT);
        return box;
    }

    private void rafraichirGrille() {
        zoneGrille.getChildren().clear();
        zoneGrille.getChildren().add(genererGrille());
    }

    private Label labelEntete(String texte) {
        Label lbl = new Label(texte);
        lbl.setMinSize(tailleCell, tailleCell);
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");
        return lbl;
    }

    // ── Panneau droit ─────────────────────────────────────────────────────────

    private VBox creerPanneau() {
        Label titreTaille = new Label("Taille de la grille");
        titreTaille.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");

        Spinner<Integer> spinnerTaille = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 15, 10);
        spinnerTaille.setValueFactory(valueFactory);
        spinnerTaille.setPrefWidth(160);
        spinnerTaille.setStyle("-fx-font-size: 12px;");
        spinnerTaille.valueProperty().addListener((obs, oldVal, newVal) -> {
            tailleGrille = newVal;
            tailleCell = Math.max(30, TAILLE_CELLULE_BASE - (newVal - 10) * 2);
            cellules = new Rectangle[tailleGrille][tailleGrille];
            rafraichirGrille();
            partieJeu.getJoueurHumain().getGrilleOcean().reinitialiser(tailleGrille);
            mettreAJourListeBateaux();
        });

        Label titreBateaux = new Label("Flotte");
        titreBateaux.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e; -fx-padding: 12 0 0 0;");

        listeBateaux = new VBox(6);
        mettreAJourListeBateaux();

        Label titreOrientation = new Label("Orientation");
        titreOrientation.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e; -fx-padding: 12 0 0 0;");

        ToggleGroup groupe = new ToggleGroup();

        ToggleButton btnH = new ToggleButton("Horizontal");
        btnH.setToggleGroup(groupe);
        btnH.setSelected(true);
        btnH.setPrefWidth(160);
        btnH.setStyle(styleToggle(true));

        ToggleButton btnV = new ToggleButton("Vertical");
        btnV.setToggleGroup(groupe);
        btnV.setPrefWidth(160);
        btnV.setStyle(styleToggle(false));

        btnH.setOnAction(e -> {
            orientationSelectionnee = orientation.HORIZONTALE;
            btnH.setStyle(styleToggle(true));
            btnV.setStyle(styleToggle(false));
        });
        btnV.setOnAction(e -> {
            orientationSelectionnee = orientation.VERTICALE;
            btnV.setStyle(styleToggle(true));
            btnH.setStyle(styleToggle(false));
        });
        groupe.selectedToggleProperty().addListener((obs, o, n) -> {
            if (n == null) o.setSelected(true);
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        boutonDemarrer = new Button("Demarrer le combat");
        boutonDemarrer.setPrefWidth(160);
        boutonDemarrer.setPrefHeight(40);
        boutonDemarrer.setDisable(true);
        boutonDemarrer.setStyle(styleBoutonPrimaire(false));
        boutonDemarrer.setOnAction(e -> demarrerCombat());

        VBox panneau = new VBox(8,
                titreTaille, spinnerTaille,
                separateur(),
                titreBateaux, listeBateaux,
                separateur(),
                titreOrientation, btnH, btnV,
                spacer,
                boutonDemarrer
        );
        panneau.setMinWidth(180);
        panneau.setMaxWidth(200);
        return panneau;
    }

    private void mettreAJourListeBateaux() {
        listeBateaux.getChildren().clear();
        Grille grille = partieJeu.getJoueurHumain().getGrilleOcean();

        for (typebateau type : typebateau.values()) {
            boolean place = grille.contientDejaType(type);
            boolean selectionne = type == bateauSelectionne;

            HBox ligne = new HBox(8);
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setPadding(new Insets(6, 10, 6, 10));
            ligne.setPrefWidth(160);

            String fond = selectionne ? "#e8f0fe" : (place ? "#f0faf0" : "white");
            String bordure = selectionne ? "#4a90d9" : (place ? "#81c784" : "#dddddd");

            ligne.setStyle(
                    "-fx-background-color: " + fond + ";" +
                            "-fx-border-color: " + bordure + ";" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;"
            );

            Label icone = new Label(place ? "✓" : (selectionne ? "▶" : " "));
            icone.setStyle("-fx-text-fill: " + (place ? "#4caf50" : (selectionne ? "#4a90d9" : "#cccccc")) + "; -fx-font-size: 12px;");
            icone.setMinWidth(14);

            Label nom = new Label(type.getNomAffiche());
            nom.setStyle("-fx-text-fill: " + (place ? "#888888" : "#1a1a2e") + "; -fx-font-size: 13px;");

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Label taille = new Label(type.getTaille() + " cases");
            taille.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

            ligne.getChildren().addAll(icone, nom, sp, taille);

            if (!place && !selectionne) {
                ligne.setOnMouseClicked(e -> {
                    bateauSelectionne = type;
                    mettreAJourListeBateaux();
                });
                ligne.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: #dddddd;" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 6;" +
                                "-fx-background-radius: 6;" +
                                "-fx-cursor: hand;"
                );
            }

            listeBateaux.getChildren().add(ligne);
        }
    }

    // ── Barre de statut ───────────────────────────────────────────────────────

    private HBox creerBarreStatut() {
        labelStatut = new Label("Selectionnez un vaisseau et cliquez sur la grille.");
        labelStatut.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px; -fx-padding: 14 0 0 0;");
        return new HBox(labelStatut);
    }

    // ── Logique survol ────────────────────────────────────────────────────────

    private void survollerCellule(int ligne, int col) {
        if (bateauSelectionne == null || ligne >= tailleGrille || col >= tailleGrille) return;
        Grille grille = partieJeu.getJoueurHumain().getGrilleOcean();
        position depart = new position(ligne, col);
        boolean valide = grille.peutPlacerBateau(bateauSelectionne, depart, orientationSelectionnee);

        for (position p : grille.construirePositions(depart, orientationSelectionnee, bateauSelectionne.getTaille())) {
            if (p.estDansLaGrille() && p.getLigne() < tailleGrille && p.getColonne() < tailleGrille && grille.getBateauSurPosition(p).isEmpty()) {
                cellules[p.getLigne()][p.getColonne()].setFill(
                        Color.web(valide ? "#cce5ff" : "#ffd6d6")
                );
            }
        }
    }

    private void reinitialiserSurvol(int ligne, int col) {
        if (bateauSelectionne == null || ligne >= tailleGrille || col >= tailleGrille) return;
        Grille grille = partieJeu.getJoueurHumain().getGrilleOcean();
        position depart = new position(ligne, col);

        for (position p : grille.construirePositions(depart, orientationSelectionnee, bateauSelectionne.getTaille())) {
            if (p.estDansLaGrille() && p.getLigne() < tailleGrille && p.getColonne() < tailleGrille && grille.getBateauSurPosition(p).isEmpty()) {
                cellules[p.getLigne()][p.getColonne()].setFill(Color.WHITE);
            }
        }
    }

    // ── Logique clic ──────────────────────────────────────────────────────────

    private void clicCellule(int ligne, int col) {
        if (bateauSelectionne == null || ligne >= tailleGrille || col >= tailleGrille) {
            labelStatut.setText("Selectionnez d'abord un vaisseau.");
            return;
        }

        boolean place = partieJeu.placerBateauJoueur(bateauSelectionne, new position(ligne, col), orientationSelectionnee);

        if (place) {
            colorierBateau(new position(ligne, col));
            labelStatut.setText(bateauSelectionne.getNomAffiche() + " place.");
            bateauSelectionne = null;
            mettreAJourListeBateaux();
            selectionnerProchainBateau();
            verifierFlotteComplete();
        } else {
            labelStatut.setText("Placement invalide, essayez une autre position.");
        }
    }

    private void colorierBateau(position depart) {
        partieJeu.getJoueurHumain().getGrilleOcean().getBateaux().stream()
                .filter(b -> b.contientPosition(depart))
                .findFirst()
                .ifPresent(b -> {
                    for (position p : b.getPositions()) {
                        Rectangle rect = cellules[p.getLigne()][p.getColonne()];
                        rect.setFill(Color.web("#4a90d9"));
                        rect.setStroke(Color.web("#2e70b8"));
                        rect.setOnMouseEntered(null);
                        rect.setOnMouseExited(null);
                    }
                });
    }

    private void selectionnerProchainBateau() {
        Grille grille = partieJeu.getJoueurHumain().getGrilleOcean();
        for (typebateau type : typebateau.values()) {
            if (!grille.contientDejaType(type)) {
                bateauSelectionne = type;
                labelStatut.setText("Placez votre " + type.getNomAffiche() + " (" + type.getTaille() + " cases).");
                mettreAJourListeBateaux();
                return;
            }
        }
        bateauSelectionne = null;
    }

    private void verifierFlotteComplete() {
        if (partieJeu.joueurPretPourCombat()) {
            labelStatut.setText("Flotte complete ! Cliquez sur Demarrer.");
            boutonDemarrer.setDisable(false);
            boutonDemarrer.setStyle(styleBoutonPrimaire(true));
        }
    }

    private void demarrerCombat() {
        partieJeu.demarrerCombat();
        combat vueCombat = new combat(stage, partieJeu);
        stage.getScene().setRoot(vueCombat.creerContenu());
    }

    // ── Styles ────────────────────────────────────────────────────────────────

    private String styleBoutonSecondaire() {
        return "-fx-background-color: white;" +
                "-fx-text-fill: #555555;" +
                "-fx-border-color: #cccccc;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private String styleBoutonPrimaire(boolean actif) {
        return "-fx-background-color: " + (actif ? "#1a1a2e" : "#cccccc") + ";" +
                "-fx-text-fill: white;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: " + (actif ? "hand" : "default") + ";";
    }

    private String styleToggle(boolean actif) {
        return "-fx-background-color: " + (actif ? "#1a1a2e" : "white") + ";" +
                "-fx-text-fill: " + (actif ? "white" : "#555555") + ";" +
                "-fx-border-color: " + (actif ? "#1a1a2e" : "#cccccc") + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private javafx.scene.shape.Line separateur() {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, 0, 160, 0);
        line.setStroke(Color.web("#eeeeee"));
        VBox.setMargin(line, new Insets(6, 0, 6, 0));
        return line;
    }
}
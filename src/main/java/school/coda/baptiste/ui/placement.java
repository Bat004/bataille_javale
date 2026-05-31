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
import school.coda.baptiste.modele.ModeJeu;
import school.coda.baptiste.modele.orientation;
import school.coda.baptiste.modele.position;
import school.coda.baptiste.modele.typebateau;
import school.coda.baptiste.service.jeu;

// 🚨Conventions Java: Les noms de classe doivent être en PascalCase
public class placement {

    private static final int TAILLE_CELLULE_BASE = 46;
    private static final String COULEUR_MER = "#0066cc";
    private static final String COULEUR_BATEAU = "#808080";
    // Architecture : Constante non utilisée
    private static final String COULEUR_TOUCHE = "#ff3333";
    // Architecture : Constante non utilisée
    private static final String COULEUR_EAU = "#ffffff";

    private final Stage stage;
    private final jeu partieJeu;
    private final ModeJeu modeJeu;

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
        this(stage, ModeJeu.NORMAL);
    }

    public placement(Stage stage, ModeJeu modeJeu) {
        this.stage = stage;
        this.modeJeu = modeJeu;
        this.partieJeu = new jeu("Joueur");
        this.partieJeu.setModeJeu(modeJeu);
    }

    public placement(Stage stage, jeu partieJeu) {
        this.stage = stage;
        this.partieJeu = partieJeu;
        this.modeJeu = ModeJeu.NORMAL;
    }

    public Parent creerContenu() {
        BorderPane racine = new BorderPane();
        racine.setStyle("-fx-background: linear-gradient(to bottom, #f8f9fa, #e8ecf1);");
        racine.setPadding(new Insets(24));

        racine.setTop(creerTitre());
        racine.setCenter(creerZoneCentrale());
        racine.setBottom(creerBarreInferieure());

        selectionnerProchainBateau();
        return racine;
    }

    private HBox creerTitre() {
        Label titre = new Label("⚓ Placement des vaisseaux");
        titre.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold;" +
                "-fx-text-fill: #0066cc; -fx-font-family: 'Georgia', serif;"
        );

        Label modeLabel = new Label("(" + modeJeu.getNom() + ")");
        modeLabel.setStyle(
                "-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-style: italic;"
        );

        HBox titreBox = new HBox(10, titre, modeLabel);
        titreBox.setAlignment(Pos.CENTER);
        titreBox.setPadding(new Insets(0, 0, 20, 0));
        return titreBox;
    }

    private HBox creerZoneCentrale() {
        zoneGrille = creerGrille();
        HBox zone = new HBox(30, zoneGrille, creerPanneau());
        zone.setAlignment(Pos.TOP_CENTER);
        return zone;
    }

    private VBox creerGrille() {
        cellules = new Rectangle[tailleGrille][tailleGrille];
        return genererGrille();
    }

    private VBox genererGrille() {
        GridPane grille = new GridPane();
        grille.setHgap(2);
        grille.setVgap(2);
        grille.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8;");

        for (int col = 0; col < tailleGrille; col++) {
            grille.add(labelEntete(String.valueOf(col + 1)), col + 1, 0);
        }
        for (int ligne = 0; ligne < tailleGrille; ligne++) {
            grille.add(labelEntete(String.valueOf((char) ('A' + ligne))), 0, ligne + 1);
        }

        for (int ligne = 0; ligne < tailleGrille; ligne++) {
            for (int col = 0; col < tailleGrille; col++) {
                Rectangle rect = new Rectangle(tailleCell, tailleCell);
                rect.setFill(Color.web(COULEUR_MER));
                rect.setStroke(Color.web("#0052a3"));
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
        box.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.0, 0, 2);");
        box.setPadding(new Insets(0));
        return box;
    }

    private void rafraichirGrille() {
        position.setTailleGrille(tailleGrille);
        zoneGrille.getChildren().clear();
        zoneGrille.getChildren().add(genererGrille());
    }

    private Label labelEntete(String texte) {
        Label lbl = new Label(texte);
        lbl.setMinSize(tailleCell, tailleCell);
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-text-fill: #0066cc; -fx-font-size: 12px; -fx-font-weight: bold;");
        return lbl;
    }

    private VBox creerPanneau() {
        Label titreTaille = new Label("Taille de la grille");
        titreTaille.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");

        Spinner<Integer> spinnerTaille = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 15, 10);
        spinnerTaille.setValueFactory(valueFactory);
        spinnerTaille.setPrefWidth(160);
        spinnerTaille.setStyle("-fx-font-size: 12px; -fx-control-inner-background: #f0f4f8;");
        spinnerTaille.valueProperty().addListener((obs, oldVal, newVal) -> {
            tailleGrille = newVal;
            tailleCell = Math.max(30, TAILLE_CELLULE_BASE - (newVal - 10) * 2);
            cellules = new Rectangle[tailleGrille][tailleGrille];
            rafraichirGrille();
            partieJeu.getJoueurHumain().getGrilleOcean().reinitialiser(tailleGrille);
            mettreAJourListeBateaux();
        });

        Label titreBateaux = new Label("⛵ Flotte");
        titreBateaux.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0066cc; -fx-padding: 12 0 0 0;");

        listeBateaux = new VBox(6);
        mettreAJourListeBateaux();

        Label titreOrientation = new Label("🧭 Orientation");
        titreOrientation.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0066cc; -fx-padding: 12 0 0 0;");

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

        boutonDemarrer = new Button("🎮 Démarrer le combat");
        boutonDemarrer.setPrefWidth(160);
        boutonDemarrer.setPrefHeight(40);
        boutonDemarrer.setDisable(true);
        boutonDemarrer.setStyle(styleBoutonPrimaire(false));
        boutonDemarrer.setOnAction(e -> demarrerCombat());

        // Ajouter une description pour le mode Salve
        Label descriptionSalve = new Label();
        if (modeJeu == ModeJeu.SALVE) {
            descriptionSalve.setText("💥 Mode Salve : Vous tirez autant de fois qu'il vous reste de bateaux !");
            descriptionSalve.setStyle("-fx-font-size: 11px; -fx-text-fill: #ff6600; -fx-padding: 12 0 0 0; -fx-font-weight: bold;");
        }

        VBox panneau = new VBox(8,
                titreTaille, spinnerTaille,
                separateur(),
                titreBateaux, listeBateaux,
                separateur(),
                titreOrientation, btnH, btnV,
                descriptionSalve,
                spacer,
                boutonDemarrer
        );
        panneau.setMinWidth(180);
        panneau.setMaxWidth(200);
        panneau.setStyle("-fx-background-color: white; -fx-padding: 16; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.0, 0, 2);");
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
            ligne.setPadding(new Insets(8, 10, 8, 10));
            ligne.setPrefWidth(160);

            String fond = selectionne ? "#e3f2fd" : (place ? "#e8f5e9" : "white");
            String bordure = selectionne ? "#0066cc" : (place ? "#4caf50" : "#e0e0e0");

            ligne.setStyle(
                    "-fx-background-color: " + fond + ";" +
                    "-fx-border-color: " + bordure + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;"
            );

            Label icone = new Label(place ? "✓" : (selectionne ? "▶" : " "));
            icone.setStyle("-fx-text-fill: " + (place ? "#4caf50" : (selectionne ? "#0066cc" : "#cccccc")) + "; -fx-font-size: 12px;");
            icone.setMinWidth(14);

            Label nom = new Label(type.getNomAffiche());
            nom.setStyle("-fx-text-fill: " + (place ? "#999999" : "#0066cc") + "; -fx-font-size: 13px; -fx-font-weight: bold;");

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
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
                );
            }

            listeBateaux.getChildren().add(ligne);
        }
    }

    private VBox creerBarreInferieure() {
        labelStatut = new Label("Sélectionnez un vaisseau et cliquez sur la grille.");
        labelStatut.setStyle("-fx-text-fill: #0066cc; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 14 0 10 0;");
        labelStatut.setWrapText(true);
        labelStatut.setAlignment(Pos.CENTER);

        Button retour = new Button("← Menu");
        retour.setStyle(styleBoutonSecondaire());
        retour.setOnAction(e -> {
            menu vueMenu = new menu(stage);
            stage.getScene().setRoot(vueMenu.creerContenu());
        });

        HBox boutonBox = new HBox(retour);
        boutonBox.setAlignment(Pos.CENTER);
        boutonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox barreInf = new VBox(8, labelStatut, boutonBox);
        barreInf.setAlignment(Pos.CENTER);
        barreInf.setPadding(new Insets(20, 0, 0, 0));
        barreInf.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");
        return barreInf;
    }

    private void survollerCellule(int ligne, int col) {
        if (bateauSelectionne == null || ligne >= tailleGrille || col >= tailleGrille) return;
        Grille grille = partieJeu.getJoueurHumain().getGrilleOcean();
        position depart = new position(ligne, col);
        boolean valide = grille.peutPlacerBateau(bateauSelectionne, depart, orientationSelectionnee);

        for (position p : grille.construirePositions(depart, orientationSelectionnee, bateauSelectionne.getTaille())) {
            if (p.estDansLaGrille() && p.getLigne() < tailleGrille && p.getColonne() < tailleGrille && grille.getBateauSurPosition(p).isEmpty()) {
                cellules[p.getLigne()][p.getColonne()].setFill(
                        Color.web(valide ? "#4da6ff" : "#ff6666")
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
                cellules[p.getLigne()][p.getColonne()].setFill(Color.web(COULEUR_MER));
            }
        }
    }

    private void clicCellule(int ligne, int col) {
        if (bateauSelectionne == null || ligne >= tailleGrille || col >= tailleGrille) {
            labelStatut.setText("Sélectionnez d'abord un vaisseau.");
            return;
        }

        boolean place = partieJeu.placerBateauJoueur(bateauSelectionne, new position(ligne, col), orientationSelectionnee);

        if (place) {
            colorierBateau(new position(ligne, col));
            labelStatut.setText("✓ " + bateauSelectionne.getNomAffiche() + " placé avec succès !");
            bateauSelectionne = null;
            mettreAJourListeBateaux();
            selectionnerProchainBateau();
            verifierFlotteComplete();
        } else {
            labelStatut.setText("✗ Placement invalide, essayez une autre position.");
        }
    }

    private void colorierBateau(position depart) {
        partieJeu.getJoueurHumain().getGrilleOcean().getBateaux().stream()
                .filter(b -> b.contientPosition(depart))
                .findFirst()
                .ifPresent(b -> {
                    for (position p : b.getPositions()) {
                        Rectangle rect = cellules[p.getLigne()][p.getColonne()];
                        rect.setFill(Color.web(COULEUR_BATEAU));
                        rect.setStroke(Color.web("#606060"));
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
            labelStatut.setText("✓ Flotte complète ! Cliquez sur Démarrer le combat.");
            boutonDemarrer.setDisable(false);
            boutonDemarrer.setStyle(styleBoutonPrimaire(true));
        }
    }

    private void demarrerCombat() {
        partieJeu.demarrerCombat();
        combat vueCombat = new combat(stage, partieJeu);
        stage.getScene().setRoot(vueCombat.creerContenu());
    }

    private String styleBoutonSecondaire() {
        return "-fx-background-color: white;" +
               "-fx-text-fill: #0066cc;" +
               "-fx-border-color: #0066cc;" +
               "-fx-border-width: 2;" +
               "-fx-border-radius: 6;" +
               "-fx-background-radius: 6;" +
               "-fx-font-size: 13px;" +
               "-fx-font-weight: bold;" +
               "-fx-cursor: hand;";
    }

    private String styleBoutonPrimaire(boolean actif) {
        return "-fx-background-color: " + (actif ? "#0066cc" : "#cccccc") + ";" +
               "-fx-text-fill: white;" +
               "-fx-border-radius: 6;" +
               "-fx-background-radius: 6;" +
               "-fx-font-size: 13px;" +
               "-fx-font-weight: bold;" +
               "-fx-cursor: " + (actif ? "hand" : "default") + ";";
    }

    private String styleToggle(boolean actif) {
        return "-fx-background-color: " + (actif ? "#0066cc" : "white") + ";" +
                "-fx-text-fill: " + (actif ? "white" : "#0066cc") + ";" +
                "-fx-border-color: " + (actif ? "#0066cc" : "#0066cc") + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;";
    }

    private javafx.scene.shape.Line separateur() {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, 0, 160, 0);
        line.setStroke(Color.web("#e0e0e0"));
        VBox.setMargin(line, new Insets(6, 0, 6, 0));
        return line;
    }
}

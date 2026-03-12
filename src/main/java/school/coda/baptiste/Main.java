package school.coda.baptiste;

import school.coda.baptiste.modele.Grille;
import school.coda.baptiste.modele.orientation;
import school.coda.baptiste.modele.position;
import school.coda.baptiste.modele.resultat;
import school.coda.baptiste.modele.typebateau;
import school.coda.baptiste.service.jeu;

public class Main {
    public static void main(String[] args) {
        testerGrille();
        testerJeu();
    }

    private static void testerGrille() {
        System.out.println("=== TEST GRILLE ===");

        Grille grille = new Grille();

        boolean placePatrouilleur = grille.placerBateau(
                typebateau.PATROUILLEUR,
                new position(0, 0),
                orientation.HORIZONTALE
        );
        System.out.println("Placement patrouilleur en A-1 horizontal : " + placePatrouilleur);

        boolean placeChevauchement = grille.placerBateau(
                typebateau.DESTROYER,
                new position(0, 1),
                orientation.VERTICALE
        );
        System.out.println("Placement destroyer qui chevauche : " + placeChevauchement);

        boolean placeHorsGrille = grille.placerBateau(
                typebateau.CUIRASSE,
                new position(9, 8),
                orientation.HORIZONTALE
        );
        System.out.println("Placement cuirassé hors grille : " + placeHorsGrille);

        resultat tir1 = grille.recevoirTir(new position(5, 5));
        System.out.println("Tir en F-6 : " + tir1.getMessage());

        resultat tir2 = grille.recevoirTir(new position(0, 0));
        System.out.println("Tir en A-1 : " + tir2.getMessage());

        resultat tir3 = grille.recevoirTir(new position(0, 1));
        System.out.println("Tir en A-2 : " + tir3.getMessage());

        resultat tir4 = grille.recevoirTir(new position(0, 1));
        System.out.println("Deuxième tir en A-2 : " + tir4.getMessage());

        System.out.println();
    }

    private static void testerJeu() {
        System.out.println("=== TEST JEU ===");

        jeu partie = new jeu("Baptiste");

        partie.placerBateauJoueur(typebateau.PORTE_AVIONS, new position(0, 0), orientation.HORIZONTALE);
        partie.placerBateauJoueur(typebateau.CUIRASSE, new position(1, 0), orientation.HORIZONTALE);
        partie.placerBateauJoueur(typebateau.DESTROYER, new position(2, 0), orientation.HORIZONTALE);
        partie.placerBateauJoueur(typebateau.SOUS_MARIN, new position(3, 0), orientation.HORIZONTALE);
        partie.placerBateauJoueur(typebateau.PATROUILLEUR, new position(4, 0), orientation.HORIZONTALE);

        boolean combatDemarre = partie.demarrerCombat();
        System.out.println("Combat démarré : " + combatDemarre);

        resultat resultatJoueur = partie.tirerJoueur(new position(0, 0));
        System.out.println("Tir joueur en A-1 : " + resultatJoueur.getMessage());

        resultat resultatBot = partie.tirerOrdinateur();
        System.out.println("Tir ordinateur : " + resultatBot.getMessage());

        System.out.println("Tour courant : " + partie.getNumeroTour());
        System.out.println("Historique :");
        for (String ligneHistorique : partie.getHistorique()) {
            System.out.println("- " + ligneHistorique);
        }
    }
}

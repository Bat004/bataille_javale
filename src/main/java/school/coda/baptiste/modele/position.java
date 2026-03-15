package school.coda.baptiste.modele;

import java.util.Objects;

// Conventions : Les noms de classe doivent être en PascalCase
public class position {
    private final int ligne;
    private final int colonne;
    private static int tailleGrille = 10;

    public position(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public static void setTailleGrille(int taille) {
        tailleGrille = taille;
    }

    public boolean estDansLaGrille() {
        return ligne >= 0 && ligne < tailleGrille && colonne >= 0 && colonne < tailleGrille;
    }

    public String toCaseLisible() {
        char lettre = (char) ('A' + ligne);
        return lettre + "-" + (colonne + 1);
    }

    @Override
    public String toString() {
        return toCaseLisible();
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (objet == null || getClass() != objet.getClass()) {
            return false;
        }
        position position = (position) objet;
        return ligne == position.ligne && colonne == position.colonne;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ligne, colonne);
    }
}

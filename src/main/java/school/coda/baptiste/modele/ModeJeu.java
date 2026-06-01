package school.coda.baptiste.modele;

/**
 * Énumération représentant les différents modes de jeu disponibles.
 */
public enum ModeJeu {
    // 👍 Bonne utilisation des champs des enum en Java
    NORMAL("Mode Normal", "Un tir par tour"),
    SALVE("Mode Salve", "Plusieurs tirs par tour");

    private final String nom;
    private final String description;

    ModeJeu(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    // 🚨 Code mort (jamais utilisé)
    public String getDescription() {
        return description;
    }
}

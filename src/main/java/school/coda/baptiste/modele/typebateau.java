package school.coda.baptiste.modele;

public enum typebateau {
    PORTE_AVIONS("Porte-avions", 5),
    CUIRASSE("Cuirassé", 4),
    DESTROYER("Destroyer", 3),
    SOUS_MARIN("Sous-marin", 3),
    PATROUILLEUR("Patrouilleur", 2);

    private final String nomAffiche;
    private final int taille;

    typebateau(String nomAffiche, int taille) {
        this.nomAffiche = nomAffiche;
        this.taille = taille;
    }

    public String getNomAffiche() {
        return nomAffiche;
    }

    public int getTaille() {
        return taille;
    }
}
package school.coda.baptiste.modele;

public class Bateau {
    private final TypeBateau typeBateau;

    public Bateau(TypeBateau typeBateau) {
        this.typeBateau = typeBateau;
    }

    public TypeBateau getTypeBateau() {
        return typeBateau;
    }

    public String getNom() {
        return typeBateau.getNomAffiche();
    }

    public int getTaille() {
        return typeBateau.getTaille();
    }
}

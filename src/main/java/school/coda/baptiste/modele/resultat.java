package school.coda.baptiste.modele;

public class resultat {
    private final typeresultat typeResultat;
    private final typebateau bateauCoule;

    public resultat(typeresultat typeResultat, typebateau bateauCoule) {
        this.typeResultat = typeResultat;
        this.bateauCoule = bateauCoule;
    }

    public typeresultat getTypeResultat() {
        return typeResultat;
    }

    public typebateau getBateauCoule() {
        return bateauCoule;
    }

    public boolean estCoule() {
        return typeResultat == typeresultat.COULE;
    }

    public String getMessage() {
        return switch (typeResultat) {
            case RATE -> "Manqué";
            case TOUCHE -> "Touché";
            case COULE -> "Touché-coulé : " + (bateauCoule != null ? bateauCoule.getNomAffiche() : "");
            case DEJA_TIRE -> "Case déjà visée";
            case INVALIDE -> "Position invalide";
        };
    }

    public static resultat rate() {
        return new resultat(typeresultat.RATE, null);
    }

    public static resultat touche() {
        return new resultat(typeresultat.TOUCHE, null);
    }

    public static resultat coule(typebateau bateauCoule) {
        return new resultat(typeresultat.COULE, bateauCoule);
    }

    public static resultat dejaTire() {
        return new resultat(typeresultat.DEJA_TIRE, null);
    }

    public static resultat invalide() {
        return new resultat(typeresultat.INVALIDE, null);
    }
}

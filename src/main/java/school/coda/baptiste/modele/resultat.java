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
        if (typeResultat == typeresultat.RATE) {
            return "Manqué";
        } else if (typeResultat == typeresultat.TOUCHE) {
            return "Touché";
        } else if (typeResultat == typeresultat.COULE) {
            return "Touché-coulé : " + (bateauCoule != null ? bateauCoule.getNomAffiche() : "");
        } else if (typeResultat == typeresultat.DEJA_TIRE) {
            return "Case déjà visée";
        } else if (typeResultat == typeresultat.INVALIDE) {
            return "Position invalide";
        }
        return "";
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

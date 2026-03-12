package school.coda.baptiste.modele;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Grille {
    public static final int TAILLE = 10;

    private final List<bateau> bateaux;
    private final Set<position> tirsRecus;

    public Grille() {
        this.bateaux = new ArrayList<>();
        this.tirsRecus = new HashSet<>();
    }

    public List<bateau> getBateaux() {
        return new ArrayList<>(bateaux);
    }

    public Set<position> getTirsRecus() {
        return new HashSet<>(tirsRecus);
    }

    public boolean flotteComplete() {
        return bateaux.size() == typebateau.values().length;
    }

    public boolean contientDejaType(typebateau typeBateau) {
        for (bateau bateau : bateaux) {
            if (bateau.getType() == typeBateau) {
                return true;
            }
        }
        return false;
    }

    public List<position> construirePositions(position depart, orientation orientation, int taille) {
        List<position> positions = new ArrayList<>();

        for (int i = 0; i < taille; i++) {
            int ligne = depart.getLigne();
            int colonne = depart.getColonne();

            if (orientation == school.coda.baptiste.modele.orientation.HORIZONTALE) {
                colonne += i;
            } else {
                ligne += i;
            }

            positions.add(new position(ligne, colonne));
        }

        return positions;
    }

    public boolean peutPlacerBateau(typebateau typeBateau, position depart, orientation orientation) {
        if (typeBateau == null || depart == null || orientation == null) {
            return false;
        }

        if (contientDejaType(typeBateau)) {
            return false;
        }

        List<position> positions = construirePositions(depart, orientation, typeBateau.getTaille());

        for (position position : positions) {
            if (!position.estDansLaGrille()) {
                return false;
            }

            if (getBateauSurPosition(position).isPresent()) {
                return false;
            }
        }

        return true;
    }

    public boolean placerBateau(typebateau typeBateau, position depart, orientation orientation) {
        if (!peutPlacerBateau(typeBateau, depart, orientation)) {
            return false;
        }

        List<position> positions = construirePositions(depart, orientation, typeBateau.getTaille());
        bateaux.add(new bateau(typeBateau, positions));
        return true;
    }

    public Optional<bateau> getBateauSurPosition(position position) {
        for (bateau bateau : bateaux) {
            if (bateau.contientPosition(position)) {
                return Optional.of(bateau);
            }
        }
        return Optional.empty();
    }

    public boolean aDejaEteVisee(position position) {
        return tirsRecus.contains(position);
    }

    public resultat recevoirTir(position position) {
        if (position == null || !position.estDansLaGrille()) {
            return resultat.invalide();
        }

        if (aDejaEteVisee(position)) {
            return resultat.dejaTire();
        }

        tirsRecus.add(position);

        Optional<bateau> bateauTouche = getBateauSurPosition(position);
        if (bateauTouche.isEmpty()) {
            return resultat.rate();
        }

        bateau bateau = bateauTouche.get();
        bateau.toucher(position);

        if (bateau.estCoule()) {
            return resultat.coule(bateau.getType());
        }

        return resultat.touche();
    }

    public boolean aUnBateauSur(position position) {
        return getBateauSurPosition(position).isPresent();
    }

    public boolean caseRatee(position position) {
        return tirsRecus.contains(position) && getBateauSurPosition(position).isEmpty();
    }

    public boolean caseTouchee(position position) {
        Optional<bateau> bateau = getBateauSurPosition(position);
        return bateau.isPresent() && bateau.get().estToucheA(position);
    }

    public boolean tousLesBateauxSontCoules() {
        if (bateaux.isEmpty()) {
            return false;
        }

        for (bateau bateau : bateaux) {
            if (!bateau.estCoule()) {
                return false;
            }
        }

        return true;
    }
}

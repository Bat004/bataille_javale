package school.coda.baptiste.modele;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 🚨Conventions Java: Les noms de classe doivent être en PascalCase
public class bateau {
    private final typebateau type;
    private final List<position> positions;
    private final Set<position> positionsTouchees;

    public bateau(typebateau type, List<position> positions) {
        this.type = type;
        this.positions = new ArrayList<>(positions);
        this.positionsTouchees = new HashSet<>();
    }

    public typebateau getType() {
        return type;
    }

    // Architecture : Méthode non utilisée
    public String getNom() {
        return type.getNomAffiche();
    }

    // Architecture : Méthode non utilisée
    public int getTaille() {
        return type.getTaille();
    }

    public List<position> getPositions() {
        return new ArrayList<>(positions);
    }

    public boolean contientPosition(position position) {
        return positions.contains(position);
    }

    public boolean estToucheA(position position) {
        return positionsTouchees.contains(position);
    }

    public void toucher(position position) {
        if (contientPosition(position)) {
            positionsTouchees.add(position);
        }
    }

    public boolean estCoule() {
        return positionsTouchees.size() == positions.size();
    }
}

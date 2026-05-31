package school.coda.baptiste.modele;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// 🚨Conventions Java: Les noms de classe doivent être en PascalCase
public class joueur {
    private final String nom;
    private final Grille grilleOcean;
    private final Set<position> tirsEnvoyes;
    private final Map<position, resultat> radar;

    public joueur(String nom) {
        this.nom = nom;
        this.grilleOcean = new Grille();
        this.tirsEnvoyes = new HashSet<>();
        this.radar = new HashMap<>();
    }

    public String getNom() {
        return nom;
    }

    public Grille getGrilleOcean() {
        return grilleOcean;
    }

    // 🚨 Code mort (jamais utilisé)
    public Set<position> getTirsEnvoyes() {
        return new HashSet<>(tirsEnvoyes);
    }

    // 🚨 Code mort (jamais utilisé)
    public Map<position, resultat> getRadar() {
        return new HashMap<>(radar);
    }

    public boolean aDejaTire(position position) {
        return tirsEnvoyes.contains(position);
    }

    public void enregistrerTir(position position, resultat resultat) {
        tirsEnvoyes.add(position);
        radar.put(position, resultat);
    }

    public boolean flotteComplete() {
        return grilleOcean.flotteComplete();
    }

    public boolean aPerdu() {
        return grilleOcean.tousLesBateauxSontCoules();
    }
}

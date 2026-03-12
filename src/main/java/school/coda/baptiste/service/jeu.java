package school.coda.baptiste.service;

import school.coda.baptiste.modele.joueur;
import school.coda.baptiste.modele.orientation;
import school.coda.baptiste.modele.position;
import school.coda.baptiste.modele.resultat;
import school.coda.baptiste.modele.typebateau;

import java.util.ArrayList;
import java.util.List;

public class jeu {
    private final joueur joueurHumain;
    private final joueur joueurOrdinateur;
    private final bot bot;
    private school.coda.baptiste.modele.jeu phase;
    private int numeroTour;
    private final List<String> historique;
    private String gagnant;

    public jeu(String nomJoueur) {
        this.joueurHumain = new joueur(nomJoueur);
        this.joueurOrdinateur = new joueur("Ordinateur");
        this.bot = new bot();
        this.phase = school.coda.baptiste.modele.jeu.PLACEMENT;
        this.numeroTour = 1;
        this.historique = new ArrayList<>();
        this.gagnant = null;
    }

    public joueur getJoueurHumain() {
        return joueurHumain;
    }

    public joueur getJoueurOrdinateur() {
        return joueurOrdinateur;
    }

    public school.coda.baptiste.modele.jeu getPhase() {
        return phase;
    }

    public int getNumeroTour() {
        return numeroTour;
    }

    public List<String> getHistorique() {
        return new ArrayList<>(historique);
    }

    public String getGagnant() {
        return gagnant;
    }

    public boolean placerBateauJoueur(typebateau typeBateau, position positionDepart, orientation orientation) {
        if (phase != school.coda.baptiste.modele.jeu.PLACEMENT) {
            return false;
        }

        return joueurHumain.getGrilleOcean().placerBateau(typeBateau, positionDepart, orientation);
    }

    public boolean joueurPretPourCombat() {
        return joueurHumain.flotteComplete();
    }

    public boolean demarrerCombat() {
        if (!joueurHumain.flotteComplete()) {
            return false;
        }

        if (!joueurOrdinateur.flotteComplete()) {
            bot.placerBateauxAleatoirement(joueurOrdinateur);
        }

        phase = school.coda.baptiste.modele.jeu.COMBAT;
        historique.add("Le combat commence.");
        return true;
    }

    public resultat tirerJoueur(position cible) {
        if (phase != school.coda.baptiste.modele.jeu.COMBAT) {
            return resultat.invalide();
        }

        if (joueurHumain.aDejaTire(cible)) {
            return resultat.dejaTire();
        }

        resultat resultatTir = joueurOrdinateur.getGrilleOcean().recevoirTir(cible);
        joueurHumain.enregistrerTir(cible, resultatTir);

        historique.add("Tour " + numeroTour + " - " + joueurHumain.getNom() + " tire en " + cible + " : " + resultatTir.getMessage());

        if (joueurOrdinateur.aPerdu()) {
            phase = school.coda.baptiste.modele.jeu.TERMINE;
            gagnant = joueurHumain.getNom();
            historique.add("Victoire de " + gagnant + ".");
        }

        return resultatTir;
    }

    public resultat tirerOrdinateur() {
        if (phase != school.coda.baptiste.modele.jeu.COMBAT) {
            return resultat.invalide();
        }

        position cible = bot.choisirTir(joueurHumain);
        resultat resultatTir = joueurHumain.getGrilleOcean().recevoirTir(cible);
        joueurOrdinateur.enregistrerTir(cible, resultatTir);

        historique.add("Tour " + numeroTour + " - Ordinateur tire en " + cible + " : " + resultatTir.getMessage());

        if (joueurHumain.aPerdu()) {
            phase = school.coda.baptiste.modele.jeu.TERMINE;
            gagnant = joueurOrdinateur.getNom();
            historique.add("Victoire de " + gagnant + ".");
        } else {
            numeroTour++;
        }

        return resultatTir;
    }

    public boolean partieTerminee() {
        return phase == school.coda.baptiste.modele.jeu.TERMINE;
    }
}

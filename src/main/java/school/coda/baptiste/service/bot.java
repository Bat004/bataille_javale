package school.coda.baptiste.service;

import school.coda.baptiste.modele.joueur;
import school.coda.baptiste.modele.orientation;
import school.coda.baptiste.modele.position;
import school.coda.baptiste.modele.typebateau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class bot {
    private final Random random;

    public bot() {
        this.random = new Random();
    }

    public void placerBateauxAleatoirement(joueur joueur) {
        for (typebateau typeBateau : typebateau.values()) {
            boolean place = false;

            while (!place) {
                int ligne = random.nextInt(10);
                int colonne = random.nextInt(10);
                orientation orientationChoisie = random.nextBoolean()
                        ? orientation.HORIZONTALE
                        : orientation.VERTICALE;

                place = joueur.getGrilleOcean().placerBateau(
                        typeBateau,
                        new position(ligne, colonne),
                        orientationChoisie
                );
            }
        }
    }

    public position choisirTir(joueur joueurHumain) {
        List<position> positionsDisponibles = new ArrayList<>();

        for (int ligne = 0; ligne < 10; ligne++) {
            for (int colonne = 0; colonne < 10; colonne++) {
                position position = new position(ligne, colonne);
                if (!joueurHumain.getGrilleOcean().aDejaEteVisee(position)) {
                    positionsDisponibles.add(position);
                }
            }
        }

        Collections.shuffle(positionsDisponibles, random);
        return positionsDisponibles.get(0);
    }
}

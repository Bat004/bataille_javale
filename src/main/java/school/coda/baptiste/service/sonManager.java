package school.coda.baptiste.service;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

// 🚨Conventions Java: Les noms de classe doivent être en PascalCase
public class sonManager {

    private static sonManager instance;

    private MediaPlayer musiquePlayer;
    private boolean musiqueActive = true;
    private boolean sonsActifs = true;

    private AudioClip sonTouche;
    private AudioClip sonRate;
    private AudioClip sonCoule;
    private AudioClip sonVictoire;

    private sonManager() {
        chargerSons();
    }

    public static sonManager getInstance() {
        if (instance == null) {
            instance = new sonManager();
        }
        return instance;
    }

    private void chargerSons() {
        sonTouche = chargerClip("sounds/tir.wav");
        sonRate = chargerClip("sounds/rate.wav");
        sonCoule = chargerClip("sounds/coule.wav");
        sonVictoire = chargerClip("sounds/victoire.wav");
        chargerMusique("sounds/musique.mp3");
    }

    // Architecture : Bonne réutilisation de code
    // Tips : On pourrait gérer le cas où le clip n'est pas trouvé avec un Optional
    private AudioClip chargerClip(String chemin) {
        try {
            URL url = getClass().getClassLoader().getResource(chemin);
            if (url != null) {
                return new AudioClip(url.toExternalForm());
            }
            System.err.println("Son introuvable : " + chemin);
        } catch (Exception e) {
            // 👍 C'est une bonne idée de tracer l'erreur plutôt que de planter l'application
            // Cependant, cela aurait été préférable de la remonter sous la forme
            // d'une exception custom unchecked plutôt que de la tracer dans ce catch
            // Cette exception custom pourrait être attrapée et tracée dans le Main
            System.err.println("Erreur son : " + chemin + " — " + e.getMessage());
        }
        return null;
    }

    private void chargerMusique(String chemin) {
        try {
            URL url = getClass().getClassLoader().getResource(chemin);
            if (url == null) {
                System.err.println("Musique introuvable : " + chemin);
                return;
            }
            Media media = new Media(url.toExternalForm());
            musiquePlayer = new MediaPlayer(media);
            musiquePlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musiquePlayer.setVolume(0.3);
            musiquePlayer.setOnError(() ->
                    System.err.println("Erreur musique : " + musiquePlayer.getError())
            );
        } catch (Exception e) {
            System.err.println("Erreur musique : " + chemin + " — " + e.getMessage());
            // 👍 C'est une bonne idée de tracer l'erreur plutôt que de planter l'application
            // Cependant, cela aurait été préférable de la remonter sous la forme
            // d'une exception custom unchecked plutôt que de la tracer dans ce catch
            // Cette exception custom pourrait être attrapée et tracée dans le Main
        }
    }

    public void jouerTouche() {
        jouerClip(sonTouche);
    }

    public void jouerRate() {
        jouerClip(sonRate);
    }

    public void jouerCoule() {
        jouerClip(sonCoule);
    }

    public void jouerVictoire() {
        arreterMusique();
        jouerClip(sonVictoire);
    }

    public void demarrerMusique() {
        if (musiqueActive && musiquePlayer != null) {
            musiquePlayer.setOnReady(() -> musiquePlayer.play());
            if (musiquePlayer.getStatus() == MediaPlayer.Status.READY) {
                musiquePlayer.play();
            }
        }
    }

    public void arreterMusique() {
        if (musiquePlayer != null) {
            musiquePlayer.stop();
        }
    }

    private void jouerClip(AudioClip clip) {
        if (sonsActifs && clip != null) {
            clip.play();
        }
    }

    public void setMusiqueActive(boolean actif) {
        this.musiqueActive = actif;
        if (!actif) {
            arreterMusique();
        } else {
            demarrerMusique();
        }
    }

    public void setSonsActifs(boolean actif) {
        this.sonsActifs = actif;
    }

    public boolean isMusiqueActive() {
        return musiqueActive;
    }

    public boolean isSonsActifs() {
        return sonsActifs;
    }
}
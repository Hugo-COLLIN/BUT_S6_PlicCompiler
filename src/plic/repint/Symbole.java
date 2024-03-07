package plic.repint;

public class Symbole {
    private String type;
    private int deplacement;
    private int taille;

    public Symbole(String type) {
        this.type = type;
        this.taille = 1;
    }

    // Constructeur pour les tableaux
    public Symbole(String type, int taille) {
        this.type = type;
        this.taille = taille;
    }

    // Getters et setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDeplacement() {
        return deplacement;
    }

    public void setDeplacement(int deplacement) {
        this.deplacement = deplacement;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }
}

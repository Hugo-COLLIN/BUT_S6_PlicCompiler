package plic.analyse;

public class Token {
    private String texte;
    private String erreur;
    private int ligne;

    public Token(String texte, int ligne) {
        this.texte = texte;
        this.ligne = ligne;
        this.erreur = null; // Pas d'erreur par défaut
    }

    public Token(String erreur, int ligne, boolean estErreur) {
        if (estErreur) {
            this.erreur = erreur;
            this.texte = null;
        } else {
            this.texte = erreur; // Si ce n'est pas une erreur, considérer comme texte
        }
        this.ligne = ligne;
    }

    // Getters et setters
    public String getTexte() {
        return texte;
    }

    public String getErreur() {
        return erreur;
    }

    public int getLigne() {
        return ligne;
    }
}

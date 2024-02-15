package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;

public class AnalyseurSyntaxique
{
    private final AnalyseurLexical analex;
    private String uniteCourante;

    public AnalyseurSyntaxique(File file) throws FileNotFoundException {
        this.analex = new AnalyseurLexical(file);
    }

    public void analyse() throws ErreurSyntaxique {
        // Demander la construction de la 1re unité lexicale
        this.uniteCourante = this.analex.next();
        //Analyser le texte
        this.analyseProg();
        // Vérifier la fin du fichier
        if (!this.uniteCourante.equals("EOF")) {
            throw new ErreurSyntaxique("Erreur : fin de fichier attendue");
        }
        // Fermer le fichier
        this.analex.close();
    }

    private void analyseProg() throws ErreurSyntaxique {
        if (!this.uniteCourante.equals("programme")) {
            throw new ErreurSyntaxique("Erreur : programme attendu");
        }
        this.uniteCourante = this.analex.next();

        if (!this.estIdf()) {
            throw new ErreurSyntaxique("Erreur : identificateur attendu (only letters)");
        }
        this.uniteCourante = this.analex.next();
    }

    private boolean estIdf() {
        return this.uniteCourante.matches("[a-zA-Z]+") && !this.uniteCourante.equals("programme") && !this.uniteCourante.equals("EOF");
    }
}

package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;

public class AnalyseurSyntaxique
{
    private AnalyseurLexical analex;
    private String uniteCourante;

    public AnalyseurSyntaxique(File file) throws FileNotFoundException {
        this.analex = new AnalyseurLexical(file);
    }

    public void analyse() throws ErreurSyntaxique {
        // Demander la construction de la 1re unité lexicale
        this.uniteCourante = this.analex.next();
        //Analyser le texte
        this.analyseProg();
    }

    private void analyseProg() throws ErreurSyntaxique {
        if (!this.uniteCourante.equals("programme")) {
            throw new ErreurSyntaxique("Erreur : programme attendu");
        }
        this.uniteCourante = this.analex.next();
    }
}
package plic;

import java.io.File;
import java.io.FileNotFoundException;

import plic.analyse.AnalyseurSyntaxique;

public class Plic {
    public static void main(String[] args) throws FileNotFoundException {
        new Plic(args[0]);
    }

    public Plic(String nomFichier) throws FileNotFoundException {
        File file = new File(nomFichier);
        // Créer un analyseur syntaxique
        AnalyseurSyntaxique anasynt = new AnalyseurSyntaxique(file);

        // Analyser le fichier
        try {
            anasynt.analyse();
            System.out.println("Analyse syntaxique réussie");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

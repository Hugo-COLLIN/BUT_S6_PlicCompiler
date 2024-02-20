package plic;

import java.io.File;
import java.io.FileNotFoundException;

import plic.analyse.AnalyseurSyntaxique;

public class Plic {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1) {
            System.err.println("ERREUR: Chemin du fichier source absent");
            System.exit(1);
        }
        if (args.length > 1) {
            System.err.println("ERREUR: Un seul paramètre admis (chemin du fichier source)");
            System.exit(1);
        }
        if (!args[0].endsWith(".plic")) {
            System.err.println("ERREUR: Suffixe incorrect (fichier .plic attendu)");
            System.exit(1);
        }

        new Plic(args[0]);
    }

    public Plic(String nomFichier) throws FileNotFoundException {
        File file = new File(nomFichier);
        // Créer un analyseur syntaxique
        AnalyseurSyntaxique anasynt = new AnalyseurSyntaxique(file);

        // Analyser le fichier
        try {
            anasynt.analyse();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

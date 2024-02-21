package plic;

import java.io.File;
import java.io.FileNotFoundException;

import plic.analyse.AnalyseurSyntaxique;
import plic.repint.Bloc;

public class Plic {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("ERREUR: Chemin du fichier source attendu");
            System.exit(1);
        }
        if (args.length > 1) {
            System.err.println("ERREUR: Un seul paramètre admis (chemin du fichier source)");
            System.exit(1);
        }
        if (!args[0].endsWith(".plic")) {
            System.err.println("ERREUR: Chemin de fichier suffixé .plic attendu");
            System.exit(1);
        }

        try {
            new Plic(args[0]);
        }
        catch (FileNotFoundException e) {
            System.err.println("ERREUR: Fichier spécifié introuvable");
            System.exit(1);
        }
    }

    public Plic(String nomFichier) throws FileNotFoundException {
        File file = new File(nomFichier);
        // Créer un analyseur syntaxique
        AnalyseurSyntaxique anasynt = new AnalyseurSyntaxique(file);

        // Analyser le fichier
        try {
            Bloc bloc = anasynt.analyse();
            System.out.println(bloc);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

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
        } catch (FileNotFoundException e) {
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
            bloc.verifier();
            System.out.println(bloc);
            String code = generateMipsHeader() + bloc.toMips() + generateMipsFooter();
            System.out.println(code);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static String generateMipsHeader() {
        return """
                .data
                linebreak: \t.asciiz "\\n"

                .text
                main :
                    move $s7, $sp
                    add $sp, $sp, -16

                """;
    }

    public static String generateMipsFooter() {
        return """
                    
                    # syscall attend un argument dans $v0 qui est 10 ; syscall récupère le contenu de $v0, 10 pour fin de programme
                    end :
                        li $v0, 10
                        syscall
                """;
    }

}

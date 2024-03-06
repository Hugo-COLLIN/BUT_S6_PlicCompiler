package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Modify the behavior of Scanner and ignore comments
 */
public class AnalyseurLexical {
    Scanner scanner;
    int ligneActuelle = 1; // Commence à la ligne 1

    public AnalyseurLexical(File file) throws FileNotFoundException {
        scanner = new Scanner(file);
//        scanner.useDelimiter(" ");
//        scanner.useDelimiter("\\s+|(?=//)|(?<=//)");
        scanner.useDelimiter("\\s+");
    }

//    public String next() {
//        if (!scanner.hasNext()) { // Check if there are more tokens available
//            return "EOF";
//        }
//        String token = scanner.next();
//
//        // Ignorer les commentaires
//        if (token.equals("//")) {
//            scanner.nextLine(); // Passer le reste de la ligne de commentaire
//            ligneActuelle++;
//            return next(); // Récupérer le prochain token valide
//        }
//
//        // Compter les sauts de ligne dans les tokens (utile si on change le délimiteur pour inclure les sauts de ligne)
////        ligneActuelle += token.length() - token.replace("\n", "").length();
//
//        // Afficher le token et le numéro de ligne actuel pour le débogage
//        System.out.println(token);
//        System.out.println(ligneActuelle);
//
//        return token;
//    }

    public String next() {
        if (!scanner.hasNext()) { // Check if there are more tokens available
            return "EOF";
        }
        String token = scanner.next();

        if (token.equals("//")) {
            scanner.nextLine();
            ligneActuelle++;
            return next();
        }

        if (token.contains("\n")) {
//            scanner.nextLine();
            ligneActuelle++;
            return next();
        }

        System.out.println(token);
        System.out.println(ligneActuelle);

//        System.out.println(token);
        return token;
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public void close() {
        scanner.close();
    }
}

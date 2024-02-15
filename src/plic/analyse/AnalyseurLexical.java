package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Adapte le comportement de Scanner et ignore les commentaires
 */
public class AnalyseurLexical {
    Scanner scanner;
    public AnalyseurLexical(File file) throws FileNotFoundException {
        scanner = new Scanner(file);
    }

    public String next() {
        if (!scanner.hasNext()) { // Check if there are more tokens available
            return "EOF";
        }
        String token = scanner.next();
        if (token.equals("//")) {
            scanner.nextLine();
            return next();
        }
        return token;
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public void close() {
        scanner.close();
    }
}

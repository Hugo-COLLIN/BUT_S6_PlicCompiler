package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class AnalyseurSyntaxique
{
    private final AnalyseurLexical analex;
    private String uniteCourante;
    private static final List<String> motsReserves = List.of("programme", "entier", "{", "}", "EOF");

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
        analyseTerminal("programme");

        analyseIdentificateur();
        this.analyseBloc();
    }

    private void analyseIdentificateur() throws ErreurSyntaxique {
        if (!this.estIdf()) {
            throw new ErreurSyntaxique("Erreur : identificateur attendu (lettres uniquement)");
        }
        this.uniteCourante = this.analex.next();
    }

    private void analyseTerminal(String terminal) throws ErreurSyntaxique {
        if (!this.uniteCourante.equals(terminal)) {
            throw new ErreurSyntaxique("Erreur : " + terminal + " attendu");
        }
        this.uniteCourante = this.analex.next();
    }

    private boolean estIdf() {
        return this.uniteCourante.matches("[a-zA-Z]+") && !motsReserves.contains(this.uniteCourante);
    }

    private void analyseBloc() throws ErreurSyntaxique {
        analyseTerminal("{");
        // Itérer sur analyseDeclaration tant qu’il y a des déclarations (Tant que l’unité courante est entier)
        while (this.uniteCourante.equals("entier")) {
            analyseDeclaration();
        }
        // Itérer sur analyseInstruction tant qu’il y a des instructions (Tant que l’unité courante est un idf ou bien ecrire)
        while (estIdf() || this.uniteCourante.equals("ecrire")) {
            analyseInstruction();
        }
        analyseTerminal("}");
    }

    private void analyseInstruction() throws ErreurSyntaxique {
        if (this.uniteCourante.equals("entier")) {
            this.uniteCourante = this.analex.next();
            analyseIdentificateur();
            analyseTerminal(";");
        }
    }

    private void analyseDeclaration() throws ErreurSyntaxique {
        analyseTerminal("entier");
        analyseIdentificateur();
        analyseTerminal(";");
    }
}

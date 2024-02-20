package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class AnalyseurSyntaxique
{
    private final AnalyseurLexical analex;
    private String uniteCourante;
    private static final List<String> motsReserves = List.of("programme", "entier", "{", "}", "EOF", "ecrire", ":=");

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
            throw new ErreurSyntaxique("fin de fichier attendue");
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
            throw new ErreurSyntaxique("identificateur attendu (lettres uniquement)");
        }
        this.uniteCourante = this.analex.next();
    }

    private void analyseTerminal(String terminal) throws ErreurSyntaxique {
        if (!this.uniteCourante.equals(terminal)) {
            throw new ErreurSyntaxique(terminal + " attendu");
        }
        this.uniteCourante = this.analex.next();
    }

    private void analyseBloc() throws ErreurSyntaxique {
        analyseTerminal("{");
        while (!this.uniteCourante.equals("}")) {
            if (this.uniteCourante.equals("entier")) {
                analyseDeclaration();
            } else if (estIdf() || this.uniteCourante.equals("ecrire")) {
                analyseInstruction();
            } else {
                throw new ErreurSyntaxique("Instruction ou déclaration inattendue");
            }
        }
        analyseTerminal("}");
    }

    private void analyseDeclaration() throws ErreurSyntaxique {
        analyseTerminal("entier");
        analyseIdentificateur();
        analyseTerminal(";");
    }

    private void analyseInstruction() throws ErreurSyntaxique {
        if (estIdf()) {
            analyseAffectation();
        } else if (this.uniteCourante.equals("ecrire")) {
            analyseEcrire();
        } else {
            throw new ErreurSyntaxique("Instruction inattendue");
        }
    }

    private void analyseEcrire() throws ErreurSyntaxique {
        analyseTerminal("ecrire");
        analyseExpression();
        analyseTerminal(";");
    }

    private void analyseAffectation() throws ErreurSyntaxique {
        analyseIdentificateur();
        analyseTerminal(":=");
        analyseExpression();
        analyseTerminal(";");
    }

    private void analyseExpression() throws ErreurSyntaxique {
        analyseOperande();
    }

    private void analyseOperande() throws ErreurSyntaxique {
        if (estCsteEntiere()) {
            this.uniteCourante = this.analex.next();
        } else if (estIdf()) {
            analyseAcces();
        } else {
            throw new ErreurSyntaxique("Opérande inattendu");
        }
    }

    private void analyseAcces() throws ErreurSyntaxique {
        analyseIdentificateur();
    }

    private boolean estIdf() {
        return this.uniteCourante.matches("[a-zA-Z]+") && !motsReserves.contains(this.uniteCourante);
    }

    private boolean estCsteEntiere() {
        return this.uniteCourante.matches("\\d+");
    }
}

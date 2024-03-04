package plic.analyse;

import plic.exceptions.DoubleDeclaration;
import plic.exceptions.ErreurSyntaxique;
import plic.repint.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class AnalyseurSyntaxique
{
    private final AnalyseurLexical analex;
    private String uniteCourante;
    private static final List<String> motsReserves = List.of("programme", "entier", "{", "}", "EOF", "ecrire", ":=");
    private TDS tds;

    public AnalyseurSyntaxique(File file) throws FileNotFoundException {
        this.analex = new AnalyseurLexical(file);
        this.tds = TDS.getInstance();
    }

    public Bloc analyse() throws ErreurSyntaxique, DoubleDeclaration {
        // Demander la construction de la 1re unité lexicale
        this.uniteCourante = this.analex.next();
        //Analyser le texte
        var bloc = this.analyseProg();
        // Vérifier la fin du fichier
        if (!this.uniteCourante.equals("EOF")) {
            throw new ErreurSyntaxique("fin de fichier attendue");
        }
        // Fermer le fichier
        this.analex.close();
        return bloc;
    }

    private Bloc analyseProg() throws ErreurSyntaxique, DoubleDeclaration {
        analyseTerminal("programme");
        analyseIdentificateur();
        return this.analyseBloc();
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

    private Bloc analyseBloc() throws ErreurSyntaxique, DoubleDeclaration {
        Bloc bloc = new Bloc();
        analyseTerminal("{");
        while (!this.uniteCourante.equals("}")) {
            if (this.uniteCourante.equals("entier")) {
                analyseDeclaration();
            } else if (estIdf() || this.uniteCourante.equals("ecrire")) {
                bloc.ajouter(analyseInstruction());
            } else {
                throw new ErreurSyntaxique("Instruction ou déclaration inattendue");
            }
        }
        analyseTerminal("}");
        return bloc;
    }

    private void analyseDeclaration() throws ErreurSyntaxique, DoubleDeclaration {
        analyseTerminal("entier");
        var tempIdentificateur = this.uniteCourante;
        System.out.println(tempIdentificateur);
        analyseIdentificateur();
        analyseTerminal(";");

        tds.ajouter(new Entree(tempIdentificateur), new Symbole("entier"));
    }

    private Instruction analyseInstruction() throws ErreurSyntaxique {
        if (estIdf()) {
            return analyseAffectation();
        } else if (this.uniteCourante.equals("ecrire")) {
            return analyseEcrire();
        } else {
            throw new ErreurSyntaxique("Instruction inattendue");
        }
    }

    private Ecrire analyseEcrire() throws ErreurSyntaxique {
        analyseTerminal("ecrire");
        Expression expression = analyseExpression();
        analyseTerminal(";");
        return new Ecrire(expression);
    }

    private Affectation analyseAffectation() throws ErreurSyntaxique {
        String idfNom = this.uniteCourante;
        analyseIdentificateur();
        analyseTerminal(":=");
        Expression expression = analyseExpression();
        analyseTerminal(";");
        return new Affectation(expression, new Idf(idfNom));
    }

    private Expression analyseExpression() throws ErreurSyntaxique {
        return analyseOperande();
    }

    private Expression analyseOperande() throws ErreurSyntaxique {
        if (estCsteEntiere()) {
            int valeur = Integer.parseInt(this.uniteCourante);
            this.uniteCourante = this.analex.next();
            return new Nombre(valeur);
        } else if (estIdf()) {
            String nom = this.uniteCourante;
            analyseAcces();
            return new Idf(nom);
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

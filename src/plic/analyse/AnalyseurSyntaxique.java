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
    private static final List<String> motsReserves = List.of("programme", "entier", "tableau", "[", "]", "{", "}", "EOF", "ecrire", ":=");

    private static final List<String> types = List.of("entier", "tableau");

    private final TDS tds;

    public AnalyseurSyntaxique(File file) throws FileNotFoundException {
        this.analex = new AnalyseurLexical(file);
        this.tds = TDS.getInstance();
    }

    public Bloc analyse() throws ErreurSyntaxique, DoubleDeclaration {
        // Demander la construction de la 1re unité lexicale
        nextToken();
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

    private void nextToken() {
        this.uniteCourante = this.analex.next();
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
        nextToken();
    }

    private void analyseTerminal(String terminal) throws ErreurSyntaxique {
        if (!this.uniteCourante.equals(terminal)) {
            throw new ErreurSyntaxique(terminal + " attendu");
        }
        nextToken();
    }

    private String analyseTerminal(List<String> terminal) throws ErreurSyntaxique {
        for (String t : terminal) {
            if (this.uniteCourante.equals(t)) {
                nextToken();
                return t;
            }
        }
        throw new ErreurSyntaxique("L'un des terminaux " + terminal + " attendu");
    }

    private Bloc analyseBloc() throws ErreurSyntaxique, DoubleDeclaration {
        Bloc bloc = new Bloc();
        analyseTerminal("{");

        // Phase de déclaration
        while (analyseType()) {
            analyseDeclaration();
        }

        // Phase d'instruction
        while (!this.uniteCourante.equals("}")) {
            bloc.ajouter(analyseInstruction());
        }

        analyseTerminal("}");
        return bloc;
    }

    private boolean analyseType() {
//        for (String t : types) {
//            if (t.equals(type))
//                return t;
//        }
        return types.contains(this.uniteCourante);
    }


    private void analyseDeclaration() throws ErreurSyntaxique, DoubleDeclaration {
        String identificateur = analyseTerminal(List.of("entier", "tableau"));

        if (identificateur.equals("entier")) {
            var tempIdentificateur = this.uniteCourante;
            analyseIdentificateur();
            analyseTerminal(";");

            tds.ajouter(new Entree(tempIdentificateur), new Symbole("entier"));
        }
        else if (identificateur.equals("tableau")) {
            analyseTerminal("[");
            if (!estCsteEntiere()) {
                throw new ErreurSyntaxique("La taille du tableau doit être une constante entière");
            }
            int taille = Integer.parseInt(this.uniteCourante);
            if (taille <= 0) {
                throw new ErreurSyntaxique("La taille du tableau doit être supérieure à 0");
            }
            nextToken(); // Consomme la taille
            analyseTerminal("]");
            String idf = this.uniteCourante;
            analyseIdentificateur();
            analyseTerminal(";");
            tds.ajouter(new Entree(idf), new Symbole("tableau", taille));
        }
    }

    private Instruction analyseInstruction() throws ErreurSyntaxique {
        if (estIdf()) {
            return analyseAffectation();
        } else if (this.uniteCourante.equals("ecrire")) {
            return analyseEcrire();
        } else {
            throw new ErreurSyntaxique("Instruction inattendue: " + this.uniteCourante);
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
            nextToken();
            return new Nombre(valeur);
        } else if (estIdf()) {
            String nom = this.uniteCourante;
            analyseAcces(); // Utilise analyseAcces pour gérer correctement les identifiants et les accès aux tableaux
            if (this.uniteCourante.equals("[")) {
                analyseTerminal("[");
                Expression indice = analyseExpression();
                analyseTerminal("]");
            }
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

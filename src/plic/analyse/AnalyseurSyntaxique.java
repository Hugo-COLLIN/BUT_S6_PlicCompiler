package plic.analyse;

import plic.exceptions.DoubleDeclaration;
import plic.exceptions.ErreurSyntaxique;
import plic.repint.*;
import plic.repint.expression.AccesTableau;
import plic.repint.expression.Expression;
import plic.repint.expression.Idf;
import plic.repint.expression.Nombre;
import plic.repint.expression.operateurs.arithmetique.Difference;
import plic.repint.expression.operateurs.arithmetique.Oppose;
import plic.repint.expression.operateurs.arithmetique.Produit;
import plic.repint.expression.operateurs.arithmetique.Somme;
import plic.repint.expression.operateurs.booleen.Et;
import plic.repint.expression.operateurs.booleen.Non;
import plic.repint.expression.operateurs.booleen.Ou;
import plic.repint.expression.operateurs.comparaison.*;
import plic.repint.instruction.Affectation;
import plic.repint.instruction.AffectationTableau;
import plic.repint.instruction.Ecrire;
import plic.repint.instruction.Instruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class AnalyseurSyntaxique
{
    private final AnalyseurLexical analex;
    private String uniteCourante;
    private static final List<String> motsReserves = List.of("programme", "entier", "tableau", "[", "]", "{", "}", "(", ")", "EOF", "ecrire", ":=");

    private static final List<String> types = List.of("entier", "tableau");

    private static final List<String> operateurs = List.of("+", "-", "*", "<", ">", "<=", ">=", "=", "#", "et", "ou");

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
            throw new ErreurSyntaxique("identificateur attendu (lettres uniquement, n'est pas un mot réservé)");
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
        return types.contains(this.uniteCourante);
    }


    private void analyseDeclaration() throws ErreurSyntaxique, DoubleDeclaration {
        String identificateur = analyseTerminal(List.of("entier", "tableau"));

        int taille = 1;

        if (identificateur.equals("tableau")) {
            analyseTerminal("[");
            if (!estCsteEntiere()) {
                throw new ErreurSyntaxique("La taille du tableau doit être une constante entière");
            }
            taille = Integer.parseInt(this.uniteCourante);
            if (taille <= 0) {
                throw new ErreurSyntaxique("La taille du tableau doit être supérieure à 0");
            }
            nextToken(); // Consomme la taille
            analyseTerminal("]");
        }

        var tempIdentificateur = this.uniteCourante;
        analyseIdentificateur();
        analyseTerminal(";");

        tds.ajouter(new Entree(tempIdentificateur), new Symbole(identificateur, taille));
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

    private Instruction analyseAffectation() throws ErreurSyntaxique {
        String idfNom = this.uniteCourante;
        analyseIdentificateur();

        // Vérifier si l'affectation concerne un tableau
        if (this.uniteCourante.equals("[")) {
            analyseTerminal("[");
            Expression indice = analyseExpression(); // Analyser l'indice du tableau
            analyseTerminal("]");
            analyseTerminal(":=");
            Expression expression = analyseExpression(); // Analyser l'expression à affecter
            analyseTerminal(";");
            return new AffectationTableau(new Idf(idfNom), indice, expression);
        } else {
            analyseTerminal(":=");
            Expression expression = analyseExpression(); // Analyser l'expression à affecter
            analyseTerminal(";");
            return new Affectation(expression, new Idf(idfNom));
        }
    }

    private Expression analyseExpression() throws ErreurSyntaxique {
        Expression expr;
        // Gestion des expressions unaires négatives
        if (this.uniteCourante.equals("-")) {
            nextToken();
            expr = analyseExpression(); // Applique l'opération d'opposition immédiatement à l'expression analysée
            return new Oppose(expr); // Assure que l'effet de la négation est appliqué directement
        } else if (this.uniteCourante.equals("non")) {
            nextToken();
            expr = analyseExpression(); // Applique l'opération de négation logique immédiatement
            return new Non(expr);
        } else {
            expr = analyseTerme();
            return analyseSuiteExp(expr);
        }
    }

    private Expression analyseSuiteExp(Expression gauche) throws ErreurSyntaxique {
        if (this.uniteCourante.equals("+") || this.uniteCourante.equals("-") || this.uniteCourante.equals("ou")) {
            String op = analyseTerminal(operateurs);
            Expression terme = analyseTerme();
            Expression droite = switch (op) {
                case "+" -> new Somme(gauche, terme);
                case "-" -> new Difference(gauche, terme);
                case "ou" -> new Ou(gauche, terme);
                default -> throw new ErreurSyntaxique("Opérateur inattendu: " + op);
            };
            return analyseSuiteExp(droite);
        }
        return gauche;
    }

    private Expression analyseTerme() throws ErreurSyntaxique {
        Expression facteur = analyseFacteur();
        return analyseSuiteTerme(facteur);
    }

    private Expression analyseSuiteTerme(Expression gauche) throws ErreurSyntaxique {
        if (this.uniteCourante.equals("*") || this.uniteCourante.equals("/") || this.uniteCourante.equals("et")) {
            String op = analyseTerminal(operateurs);
            Expression facteur = analyseFacteur();
            Expression droite = switch (op) {
                case "*" -> new Produit(gauche, facteur);
//                case "/" -> new Division(gauche, facteur);
                case "et" -> new Et(gauche, facteur);
                default -> throw new ErreurSyntaxique("Opérateur inattendu: " + op);
            };
            return analyseSuiteTerme(droite);
        }
        return gauche;
    }

    private Expression analyseFacteur() throws ErreurSyntaxique {
        Expression expr = analyseExpressionPrimaire();
        if (this.uniteCourante.equals(">") || this.uniteCourante.equals("<") || this.uniteCourante.equals("<=") || this.uniteCourante.equals(">=") || this.uniteCourante.equals("=") || this.uniteCourante.equals("#")) {
            String op = analyseTerminal(operateurs);
            Expression droite = analyseExpressionPrimaire();
            return switch (op) {
                case ">" -> new Superieur(expr, droite);
                case "<" -> new Inferieur(expr, droite);
                case "<=" -> new InferieurOuEgal(expr, droite);
                case ">=" -> new SuperieurOuEgal(expr, droite);
                case "=" -> new Egal(expr, droite);
                case "#" -> new Different(expr, droite);
                default -> throw new ErreurSyntaxique("Opérateur de comparaison inattendu: " + op);
            };
        }
        return expr;
    }

    private Expression analyseExpressionPrimaire() throws ErreurSyntaxique {
        if (this.uniteCourante.equals("(")) {
            return analyseExpressionParenthesee();
        } else if (this.uniteCourante.equals("-")) {
            nextToken();
            Expression expr = analyseExpressionPrimaire(); // Applique récursivement pour gérer les cas unaires multiples
            return new Oppose(expr);
        } else {
            return analyseOperande();
        }
    }


    private Expression analyseExpressionParenthesee() throws ErreurSyntaxique {
        analyseTerminal("("); // Vérifie et consomme la parenthèse ouvrante
        Expression expr = analyseExpression(); // Analyse l'expression interne
        analyseTerminal(")"); // Vérifie et consomme la parenthèse fermante
        return expr;
    }


    private Expression analyseOperande() throws ErreurSyntaxique {
        if (estCsteEntiere()) {
            int valeur = Integer.parseInt(this.uniteCourante);
            nextToken();
            return new Nombre(valeur);
        } else if (estIdf()) {
            String nom = this.uniteCourante;
            analyseIdentificateur();
            if (this.uniteCourante.equals("[")) {
                analyseTerminal("[");
                Expression indice = analyseExpression();
                analyseTerminal("]");
                return new AccesTableau(new Idf(nom), indice);
            }
            return new Idf(nom);
        } else {
            throw new ErreurSyntaxique("Opérande inattendu: " + this.uniteCourante);
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

package plic.analyse;

import plic.exceptions.DoubleDeclaration;
import plic.exceptions.ErreurNonImplemente;
import plic.exceptions.ErreurSyntaxique;
import plic.repint.*;
import plic.repint.expression.*;
import plic.repint.expression.operateurs.arithmetique.*;
import plic.repint.expression.operateurs.booleen.*;
import plic.repint.expression.operateurs.comparaison.*;
import plic.repint.instruction.*;
import plic.repint.instruction.controle.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class AnalyseurSyntaxique
{
    private final AnalyseurLexical analex;
    private String uniteCourante;

    private static final List<String> motsReserves = List.of("programme", "entier", "tableau", "[", "]", "{", "}", "(", ")", "EOF", "ecrire", "lire", ":=");

    private static final List<String> types = List.of("entier", "tableau");

    private static final List<String> operateurs = List.of("+", "-", "*", "<", ">", "<=", ">=", "=", "#", "et", "ou");

    private final TDS tds;

    public AnalyseurSyntaxique(File file) throws FileNotFoundException {
        this.analex = new AnalyseurLexical(file);
        this.tds = TDS.getInstance();
    }

    public Bloc analyse() throws ErreurSyntaxique, DoubleDeclaration, ErreurNonImplemente {
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

    private Bloc analyseProg() throws ErreurSyntaxique, DoubleDeclaration, ErreurNonImplemente {
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
//        System.out.println(this.uniteCourante);
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

    private Bloc analyseBloc() throws ErreurSyntaxique, DoubleDeclaration, ErreurNonImplemente {
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

    private Instruction analyseInstruction() throws ErreurSyntaxique, DoubleDeclaration, ErreurNonImplemente {
        switch (this.uniteCourante) {
            case "lire":
                return analyseLire();
            case "ecrire":
                return analyseEcrire();
            case "si":
                return analyseCondition();
            case "pour":
            case "tantque":
                return analyseIteration();
            default:
                if (estIdf()) {
                    return analyseAffectation();
                } else {
                    throw new ErreurSyntaxique("Instruction inattendue: " + this.uniteCourante);
                }
        }
    }

    private Instruction analyseCondition() throws ErreurSyntaxique, DoubleDeclaration, ErreurNonImplemente {
        analyseTerminal("si");
        analyseTerminal("(");
        Expression condition = analyseExpression();
        analyseTerminal(")");
        analyseTerminal("alors");
        Bloc alorsBloc = analyseBloc();
        Bloc sinonBloc = null;
        if (this.uniteCourante.equals("sinon")) {
            nextToken(); // Consomme "sinon"
            sinonBloc = analyseBloc();
        }
        return new Condition(condition, alorsBloc, sinonBloc);
    }

    private Instruction analyseIteration() throws ErreurSyntaxique, DoubleDeclaration, ErreurNonImplemente {
        if (this.uniteCourante.equals("pour")) {
            nextToken(); // Consomme "pour"
            String idfNom = this.uniteCourante;
            analyseIdentificateur();
            analyseTerminal("dans");
            Expression debut = analyseExpression();
            analyseTerminal("..");
            Expression fin = analyseExpression();
            analyseTerminal("repeter");
            Bloc bloc = analyseBloc();
            return new Pour(new Idf(idfNom), debut, fin, bloc);
        }
        else
        if (this.uniteCourante.equals("tantque")) {
            nextToken();
            Expression condition = analyseExpressionParenthesee();
            analyseTerminal("repeter");
            Bloc bloc = analyseBloc();
            return new TantQue(condition, bloc);
        }
        else {
            throw new ErreurSyntaxique("Instruction d'itération inattendue: " + this.uniteCourante);
        }
    }


    private Ecrire analyseEcrire() throws ErreurSyntaxique {
        analyseTerminal("ecrire");
        Expression expression = analyseExpression();
        analyseTerminal(";");
        return new Ecrire(expression);
    }

    private Instruction analyseLire() throws ErreurSyntaxique, ErreurNonImplemente {
        analyseTerminal("lire");
        Idf idf = new Idf(this.uniteCourante);
        analyseIdentificateur(); // Consomme l'identificateur

        Expression indice = null;
        if (this.uniteCourante.equals("[")) {
            analyseTerminal("[");
            indice = analyseExpression(); // Analyser l'indice du tableau
            analyseTerminal("]");
        }

        analyseTerminal(";");

        if (indice == null) {
            if (idf.getType().equals("tableau")) {
                throw new ErreurNonImplemente("Affectation de tableaux par lecture");
            }

            return new Lire(idf);
        } else {
            return new Lire(new AccesTableau(idf, indice));
        }
    }

    private Instruction analyseAffectation() throws ErreurSyntaxique, ErreurNonImplemente {
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
        Expression terme = analyseTerme();
        return analyseSuiteExp(terme);
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
        // Si aucun opérateur n'est trouvé, retourne l'expression gauche (epsilon)
        return gauche;
    }

    private Expression analyseTerme() throws ErreurSyntaxique {
        Expression facteur = analyseFacteur();
        return analyseSuiteTerme(facteur);
    }

    private Expression analyseSuiteTerme(Expression gauche) throws ErreurSyntaxique {
        if (this.uniteCourante.equals("*") || /*this.uniteCourante.equals("/") ||*/ this.uniteCourante.equals("et")) {
            String op = analyseTerminal(operateurs);
            Expression facteur = analyseFacteur();
            Expression droite = switch (op) {
                case "*" -> new Produit(gauche, facteur);
                // case "/" -> new Division(gauche, facteur);
                case "et" -> new Et(gauche, facteur);
                default -> throw new ErreurSyntaxique("Opérateur inattendu: " + op);
            };
            return analyseSuiteTerme(droite);
        }
        // Si aucun opérateur n'est trouvé, retourne l'expression gauche (epsilon)
        return gauche;
    }

    private Expression analyseFacteur() throws ErreurSyntaxique {
        switch (this.uniteCourante) {
            case "(" -> {
                return analyseExpressionParenthesee();
            }
            case "-" -> {
                nextToken();
                if (this.uniteCourante.equals("(")) {
                    Expression expr = analyseExpressionParenthesee();
                    return new Oppose(expr);
                }
                throw new ErreurSyntaxique("Expression parenthésée attendue après le signe moins", this.uniteCourante);
            }
            case "non" -> {
                nextToken();
                Expression expr = analyseExpression();
                return new Non(expr);
            }
            default -> {
                Expression operande = analyseOperande();
                if (this.uniteCourante.equals(">") || this.uniteCourante.equals("<") || this.uniteCourante.equals("<=") || this.uniteCourante.equals(">=") || this.uniteCourante.equals("=") || this.uniteCourante.equals("#")) {
                    String op = analyseTerminal(operateurs);
                    Expression droite = analyseOperande();
                    return switch (op) {
                        case ">" -> new Superieur(operande, droite);
                        case "<" -> new Inferieur(operande, droite);
                        case "<=" -> new InferieurOuEgal(operande, droite);
                        case ">=" -> new SuperieurOuEgal(operande, droite);
                        case "=" -> new Egal(operande, droite);
                        case "#" -> new Different(operande, droite);
                        default -> throw new ErreurSyntaxique("Opérateur de comparaison inattendu: " + op);
                    };
                }
                return operande;
            }
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

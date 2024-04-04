package plic.repint.instruction;

import plic.exceptions.ErreurSemantique;
import plic.repint.GenererLecture;
import plic.repint.expression.Expression;

public class Lire extends Instruction {

    public Lire(Expression e) {
        super(e);
    }

    @Override
    public void verifier() throws ErreurSemantique {
        // Vérifier que l'identificateur est déclaré
        expression.verifier();
    }

    @Override
    public String toMips() {
        GenererLecture expression = (GenererLecture) this.expression;
        return expression.genererCodeLecture();
    }

//    @Override
//    public String toMips() {
//        StringBuilder sb = new StringBuilder();
//        Idf idf = (Idf) expression;
//        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
//        int deplacement = symbole.getDeplacement();
//
//        // Lire un entier depuis l'entrée standard et le stocker dans l'identificateur
//        sb.append("li $v0, 5\n"); // Code système pour lire un entier
//        sb.append("syscall\n"); // Lire l'entier
//        sb.append("sw $v0, ").append(deplacement).append("($s7)\n"); // Stocker l'entier lu dans l'identificateur
//
//        return sb.toString();
//    }

//    @Override
//    public String toMips() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("li $v0, 5\n"); // Code système pour lire un entier
//        sb.append("syscall\n"); // Lire l'entier
//
//        // Utiliser directement l'expression pour générer le code MIPS
//        // sans avoir besoin de faire un casting explicite
//        sb.append(expression.toMips());
//
//        if (expression.getType().equals("entier")) {
//            // Si l'expression est un identificateur simple, stocker l'entier lu
//            sb.append("sw $v0, 0($t2)\n"); // Stocker l'entier lu à l'adresse calculée
//        }
//        // Pas besoin d'un else, car AccesTableau.toMips() s'occupe déjà de stocker la valeur
//
//        return sb.toString();
//    }

//    @Override
//    public String toMips() {
//        StringBuilder sb = new StringBuilder();
//        String expressionType = expression.getType();
//        if (expressionType.equals("entier"))
//        {
//            Idf idf = (Idf) expression;
//            Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
//            int deplacement = symbole.getDeplacement();
//        }
//        sb.append("li $v0, 5\n"); // Code système pour lire un entier
//        sb.append("syscall\n"); // Lire l'entier
//
//        sb.append(expression.toMips());
//
//
//
//
////        if (expressionType.equals("tableau")) {
////            AccesTableau accesTableau = (AccesTableau) expression;
////            sb.append(accesTableau.toMips()); // Générer le code MIPS pour accéder à l'indice du tableau
////            sb.append("sw $v0, 0($t2)\n"); // Stocker l'entier lu à l'adresse calculée
////        } else {
////            Idf idf = (Idf) expression;
////            Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
////            int deplacement = symbole.getDeplacement();
////            sb.append("sw $v0, ").append(deplacement).append("($s7)\n"); // Stocker l'entier lu dans l'identificateur
////        }
//
//        return sb.toString();
//    }

//    @Override
//    public String toMips() {
//        StringBuilder sb = new StringBuilder();
//        String expressionType = expression.getType();
//
//        if (expressionType.equals("tableau")) {
//            // Ici, nous savons que l'expression est un AccesTableau, donc pas besoin de casting en Idf
//            AccesTableau accesTableau = (AccesTableau) expression;
//            Idf idf = accesTableau.getIdf();
//            Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
//            int deplacement = symbole.getDeplacement();
//
//            // Générer le code MIPS pour lire un entier et le stocker dans l'élément de tableau
//            sb.append("li $v0, 5\n"); // Code système pour lire un entier
//            sb.append("syscall\n"); // Lire l'entier
//            sb.append(accesTableau.getIndice().toMips()); // Calculer l'indice
//            sb.append("li $t1, 4\n");
//            sb.append("mul $t2, $v0, $t1\n"); // Multiplier l'indice par 4 (taille d'un entier)
//            sb.append("add $t2, $t2, ").append(deplacement).append("\n");
//            sb.append("add $t2, $t2, $s7\n"); // Adresse de l'élément de tableau
//            sb.append("sw $v0, 0($t2)\n"); // Stocker l'entier lu dans l'élément de tableau
//        } else {
//            // Traitement pour une variable simple (Idf)
//            Idf idf = (Idf) expression; // Ici, le casting est sûr car getType() n'est pas "tableau"
//            Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
//            int deplacement = symbole.getDeplacement();
//
//            sb.append("li $v0, 5\n"); // Code système pour lire un entier
//            sb.append("syscall\n"); // Lire l'entier
//            sb.append("sw $v0, ").append(deplacement).append("($s7)\n"); // Stocker l'entier lu dans la variable
//        }
//
//        return sb.toString();
//    }

//    @Override
//    public String toMips() throws ErreurSemantique {
//        StringBuilder sb = new StringBuilder();
//        sb.append("li $v0, 5\n"); // Code système pour lire un entier
//        sb.append("syscall\n"); // Lire l'entier
//
//        String expressionType = expression.getType();
//
//        System.out.println(expressionType);
//
//        if (expressionType.equals("tableau")) {
//            AccesTableau accesTableau = expression.asAccesTableau();
//            sb.append(accesTableau.toMips()); // Générer le code MIPS pour accéder à l'indice du tableau
//            sb.append("sw $v0, 0($t2)\n"); // Stocker l'entier lu à l'adresse calculée
//        } else {
//            Idf idf = expression.asIdf();
//            Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
//            int deplacement = symbole.getDeplacement();
//            sb.append("sw $v0, ").append(deplacement).append("($s7)\n"); // Stocker l'entier lu dans l'identificateur
//        }
//
//        return sb.toString();
//    }

}

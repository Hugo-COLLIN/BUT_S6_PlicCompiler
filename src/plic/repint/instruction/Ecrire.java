package plic.repint.instruction;

import plic.exceptions.ErreurSemantique;
import plic.repint.*;
import plic.repint.expression.Expression;
import plic.repint.expression.Idf;

public class Ecrire extends Instruction {

    // Compteur statique pour suivre le nombre d'écritures de tableau
    private static int tableauEcritureCompteur = 0;

    public Ecrire(Expression e) {
        super(e);
        tableauEcritureCompteur = 0; // Réinitialiser le compteur entre chaque exécution du compilateur
    }

    @Override
    public void verifier() throws ErreurSemantique {
        // Vérifier que l'expression est valide
        expression.verifier();
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        String expressionType = expression.getType();

        if (expressionType.equals("tableau")) {
            // Incrémenter le compteur et générer les noms de labels basés sur ce compteur
            tableauEcritureCompteur++;
            String loopLabel = "loopTab" + tableauEcritureCompteur;
            String endLoopLabel = "endLoopTab" + tableauEcritureCompteur;

            Idf idf = (Idf) expression;
            Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));

            int taille = symbole.getTaille();
            int deplacement = symbole.getDeplacement();

            sb.append("li $t4, 0\n"); // Index de boucle
            sb.append("li $t5, ").append(taille).append("\n"); // Taille du tableau
            sb.append(loopLabel).append(":\n");
            sb.append("bge $t4, $t5, ").append(endLoopLabel).append("\n"); // Si $t4 >= taille, fin de la boucle
            sb.append("li $t1, 4\n");
            sb.append("mul $t2, $t4, $t1\n"); // Calculer le déplacement pour l'élément courant
            sb.append("add $t2, $t2, ").append(deplacement).append("\n");
            sb.append("add $t2, $t2, $s7\n"); // Adresse de l'élément courant
            sb.append("lw $a0, 0($t2)\n"); // Charger l'élément courant dans $a0
            sb.append("li $v0, 1\n");
            sb.append("syscall\n"); // Afficher l'élément courant
            sb.append("li $v0, 4\n"); // Ne doit pas être passée pour afficher correctement le dernier élément

            // Vérifier si l'élément courant n'est pas le dernier avant d'afficher le séparateur
            sb.append("addi $t6, $t4, 1\n"); // Calculer l'index du prochain élément
            sb.append("bge $t6, $t5, ").append(endLoopLabel).append("\n"); // Si c'est le dernier élément, ne pas afficher le séparateur

            sb.append("la $a0, separator\n");
            sb.append("syscall\n"); // Séparation des éléments

            sb.append("addi $t4, $t4, 1\n"); // Incrémenter l'index de boucle
            sb.append("j ").append(loopLabel).append("\n\n"); // Retour au début de la boucle
            sb.append(endLoopLabel).append(":\n");
        } else {
            // Générer le code MIPS pour afficher une valeur simple ou un élément de tableau
            sb.append(expression.toMips());
            sb.append("move $a0, $v0\n");
            sb.append("li $v0, 1\n");
            sb.append("syscall\n");
            sb.append("li $v0, 4\n");
        }
        // Saut de ligne
        sb.append("la $a0, linebreak\n");
        sb.append("syscall\n");

        return sb.toString();
    }
}

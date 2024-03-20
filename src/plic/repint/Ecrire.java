package plic.repint;

public class Ecrire extends Instruction
{
    public Ecrire(Expression e) {
        super(e);
    }

    @Override
    public void verifier() {
        // Vérifier que l'expression est valide
        expression.verifier();
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();

        Idf idf = (Idf) expression;
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));

        if (symbole != null && symbole.getTaille() > 1) { // C'est un tableau
            int taille = symbole.getTaille();
            int deplacement = symbole.getDeplacement();

            sb.append("li $t4, 0\n"); // Index de boucle
            sb.append("li $t5, ").append(taille).append("\n"); // Taille du tableau
            String loopLabel = "loop" + System.identityHashCode(this);
            String endLoopLabel = "endLoop" + System.identityHashCode(this);
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
            sb.append("la $a0, linebreak\n");
            sb.append("syscall\n"); // Saut de ligne après le dernier élément
        } else {
            // Générer le code MIPS pour afficher une valeur simple ou un élément de tableau
            sb.append(expression.toMips());
            sb.append("move $a0, $v0\n");
            sb.append("li $v0, 1\n");
            sb.append("syscall\n");
            sb.append("li $v0, 4\n");
            sb.append("la $a0, linebreak\n");
            sb.append("syscall\n"); // Saut de ligne
        }

        return sb.toString();
    }

//    @Override
//    public String toMips() {
//        StringBuilder sb = new StringBuilder();
//
//        Idf idf = (Idf) expression;
//        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
//
//        if (symbole != null && symbole.getTaille() > 1) { // C'est un tableau
//            int taille = symbole.getTaille();
//            int deplacement = symbole.getDeplacement();
//
//            sb.append("li $t4, 0\n"); // Index de boucle initialisé à 0
//            sb.append("li $t5, ").append(taille).append("\n"); // Taille du tableau
//            String loopLabel = "loop" + System.identityHashCode(this);
//            String endLoopLabel = "endLoop" + System.identityHashCode(this);
//            sb.append(loopLabel).append(":\n");
//            sb.append("bge $t4, $t5, ").append(endLoopLabel).append("\n"); // Si $t4 >= taille, fin de la boucle
//
//            // Charger et afficher l'élément courant
//            sb.append("li $t1, 4\n");
//            sb.append("mul $t2, $t4, $t1\n"); // Calculer le déplacement pour l'élément courant
//            sb.append("add $t2, $t2, ").append(deplacement).append("\n");
//            sb.append("add $t2, $t2, $s7\n"); // Adresse de l'élément courant
//            sb.append("lw $a0, 0($t2)\n"); // Charger l'élément courant dans $a0
//            sb.append("li $v0, 1\n");
//            sb.append("syscall\n"); // Afficher l'élément courant
//
//            // Vérifier si on doit imprimer un séparateur (si ce n'est pas le dernier élément)
//            sb.append("addi $t6, $t4, 1\n"); // Calculer l'index du prochain élément
//            sb.append("blt $t6, $t5, printSeparator").append(System.identityHashCode(this)).append("\n");
//
//            sb.append("j skipSeparator").append(System.identityHashCode(this)).append("\n");
//
//            sb.append("printSeparator").append(System.identityHashCode(this)).append(":\n");
//            sb.append("li $v0, 4\n");
//            sb.append("la $a0, separator\n");
//            sb.append("syscall\n"); // Imprimer le séparateur
//
//            sb.append("skipSeparator").append(System.identityHashCode(this)).append(":\n");
//            sb.append("addi $t4, $t4, 1\n"); // Incrémenter l'index de boucle
//            sb.append("j ").append(loopLabel).append("\n"); // Retour au début de la boucle
//
//            sb.append(endLoopLabel).append(":\n");
//            sb.append("la $a0, linebreak\n");
//            sb.append("syscall\n"); // Saut de ligne après le dernier élément
//        } else {
//            // Générer le code MIPS pour afficher une valeur simple ou un élément de tableau
//            sb.append(expression.toMips());
//            sb.append("move $a0, $v0\n");
//            sb.append("li $v0, 1\n");
//            sb.append("syscall\n");
//            sb.append("li $v0, 4\n");
//            sb.append("la $a0, linebreak\n");
//            sb.append("syscall\n"); // Saut de ligne
//        }
//
//        return sb.toString();
//    }

}

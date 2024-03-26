// Inferieur.java
package plic.repint.expression.operateurs.comparaison;

import plic.repint.expression.Expression;

public class SuperieurOuEgal extends Comparaison {
    public SuperieurOuEgal(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public String toMips() {
        return opGauche.toMips() +       // Générer le code MIPS pour évaluer l'opérande gauche et le stocker dans $v0
                "sw $v0, 0($sp)\n" +     // Sauvegarder le résultat de l'opérande gauche sur la pile
                "addiu $sp, $sp, -4\n" + // Décrémenter le pointeur de pile pour faire de la place pour le résultat de l'opérande droit
                opDroit.toMips() +       // Générer le code MIPS pour évaluer l'opérande droit et le stocker dans $v0
                "lw $t0, 4($sp)\n" +     // Charger le résultat de l'opérande gauche de la pile dans $t0
                "addiu $sp, $sp, 4\n" +  // Incrémenter le pointeur de pile pour nettoyer la pile
                "slt $v0, $t0, $v0\n" +  // Utiliser slt pour comparer $v0 (opDroit) et $t0 (opGauche). Si $v0 < $t0, mettre 1 dans $v0
                "xori $v0, $v0, 1\n";    // Utiliser xori pour inverser le bit de résultat de slt, car slt donne 1 si $v0 < $t0, mais nous voulons 1 aussi si $v0 == $t0
    }
}

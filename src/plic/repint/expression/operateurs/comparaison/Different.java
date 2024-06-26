// Copyright © 2024 Hugo COLLIN
package plic.repint.expression.operateurs.comparaison;

import plic.repint.expression.Expression;

public class Different extends Comparaison {
    public Different(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public String toMips() {
        return opGauche.toMips() +
                "sw $v0, 0($sp)\n" +
                "addiu $sp, $sp, -4\n" +
                opDroit.toMips() +
                "lw $t0, 4($sp)\n" +
                "addiu $sp, $sp, 4\n" +
                "sub $v0, $v0, $t0\n" +  // Soustraire les valeurs des opérandes
                "sltiu $v0, $v0, 1\n" +  // Si le résultat de la soustraction est 0 (égalité), mettre 1 dans $v0, sinon mettre 0
                "xori $v0, $v0, 1\n";    // Inverser le résultat pour obtenir le comportement de 'différent'
    }
}

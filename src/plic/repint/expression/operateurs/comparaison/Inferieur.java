// Inferieur.java
package plic.repint.expression.operateurs.comparaison;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;
import plic.repint.expression.operateurs.Binaire;

public class Inferieur extends Comparaison {
    public Inferieur(Expression opGauche, Expression opDroit) {
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
                "slt $v0, $t0, $v0\n";
    }
}

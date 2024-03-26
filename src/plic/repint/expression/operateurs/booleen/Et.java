package plic.repint.expression.operateurs.booleen;

import plic.repint.expression.Expression;
import plic.repint.expression.operateurs.arithmetique.Arithmetique;

public class Et extends Booleen {
    public Et(Expression opGauche, Expression opDroit) {
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
                "and $v0, $t0, $v0\n";
    }
}

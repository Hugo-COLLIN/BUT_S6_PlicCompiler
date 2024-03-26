package plic.repint.expression.operateurs.booleen;

import plic.repint.expression.Expression;

public class Ou extends BooleenBinaire {
    public Ou(Expression opGauche, Expression opDroit) {
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
                "or $v0, $t0, $v0\n";
    }
}

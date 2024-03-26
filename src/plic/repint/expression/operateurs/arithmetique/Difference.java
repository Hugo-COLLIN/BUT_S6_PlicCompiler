package plic.repint.expression.operateurs.arithmetique;

import plic.repint.expression.Expression;
import plic.repint.expression.operateurs.Binaire;

public class Difference extends Arithmetique {
    public Difference(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public String toMips() {
        return opGauche.toMips() +
                "sw $v0, 0($sp)\n" +
                "addi $sp, $sp, -4\n" +
                opDroit.toMips() +
                "addi $sp, $sp, 4\n" +
                "lw $t0, 0($sp)\n" +
                "sub $v0, $t0, $v0\n";
    }
}

package plic.repint.expression.operateurs.arithmetique;

import plic.repint.expression.Expression;

public class Somme extends ArithmetiqueBinaire {
    public Somme(Expression opGauche, Expression opDroit) {
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
                "add $v0, $t0, $v0\n";
    }
}

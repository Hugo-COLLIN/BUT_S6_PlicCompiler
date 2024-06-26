// Copyright © 2024 Hugo COLLIN
package plic.repint.expression.operateurs.arithmetique;

import plic.repint.expression.Expression;

public class Produit extends ArithmetiqueBinaire {
    public Produit(Expression opGauche, Expression opDroit) {
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
                "mul $v0, $t0, $v0\n";
    }
}

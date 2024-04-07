// Copyright © 2024 Hugo COLLIN
package plic.repint.expression.operateurs.arithmetique;

import plic.repint.expression.Expression;

public class Difference extends ArithmetiqueBinaire {
    public Difference(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public String toMips() {
        return opGauche.toMips() +
                "move $t1, $v0\n" + // Stocker temporairement le résultat de opGauche
                opDroit.toMips() +
                "sub $v0, $t1, $v0\n"; // Utiliser le résultat temporaire pour la soustraction
    }
}

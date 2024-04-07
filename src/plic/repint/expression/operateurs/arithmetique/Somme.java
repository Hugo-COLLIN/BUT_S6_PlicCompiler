// Copyright © 2024 Hugo COLLIN
package plic.repint.expression.operateurs.arithmetique;

import plic.repint.expression.Expression;

public class Somme extends ArithmetiqueBinaire {
    public Somme(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public String toMips() {
        return opGauche.toMips() +
                "move $t1, $v0\n" + // Stocker temporairement le résultat de opGauche
                opDroit.toMips() +
                "add $v0, $t1, $v0\n"; // Utiliser le résultat temporaire pour l'addition
    }
}

// Copyright © 2024 Hugo COLLIN
package plic.repint.expression.operateurs.arithmetique;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;
import plic.repint.expression.operateurs.Binaire;

public abstract class ArithmetiqueBinaire extends Binaire {

    public ArithmetiqueBinaire(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public void verifier() throws ErreurSemantique {
        this.opDroit.verifier();
        this.opGauche.verifier();
        if (!(this.opDroit.getType().equals("entier") && this.opGauche.getType().equals("entier")))
            throw new ErreurSemantique("Les deux operateurs doivent être de type entier");
    }

    @Override
    public abstract String toMips();

    @Override
    public String getType() {
        return "entier";
    }
}

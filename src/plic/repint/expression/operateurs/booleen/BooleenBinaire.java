// Copyright © 2024 Hugo COLLIN
package plic.repint.expression.operateurs.booleen;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;
import plic.repint.expression.operateurs.Binaire;

public abstract class BooleenBinaire extends Binaire {

    public BooleenBinaire(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public void verifier() throws ErreurSemantique {
        this.opDroit.verifier();
        this.opGauche.verifier();
        if (!(this.opDroit.getType().equals("booleen") && this.opGauche.getType().equals("booleen")))
            throw new ErreurSemantique("Les deux operateurs doivent être de type booleen");
    }

    @Override
    public abstract String toMips();

    @Override
    public String getType() {
        return "booleen";
    }
}

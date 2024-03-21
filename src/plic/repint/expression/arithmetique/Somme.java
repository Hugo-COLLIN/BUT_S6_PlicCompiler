package plic.repint.expression.arithmetique;

import plic.repint.expression.Expression;

public class Somme extends Binaire {
    public Somme(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public void verifier() {

    }

    @Override
    public String toMips() {
        return "";
    }

    @Override
    public String getType() {
        return "entier";
    }
}

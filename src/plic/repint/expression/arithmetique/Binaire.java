package plic.repint.expression.arithmetique;

import plic.repint.expression.Expression;

public abstract class Binaire extends Expression {
    protected Expression opGauche;
    protected Expression opDroit;

    public Binaire(Expression opGauche, Expression opDroit) {
        this.opGauche = opGauche;
        this.opDroit = opDroit;
    }

    @Override
    public void verifier() {
        this.opDroit.verifier();
//        this.opDroit.getType().equals("entier");
        this.opGauche.verifier();
    }

    @Override
    public abstract String toMips();

    @Override
    public abstract String getType();
}

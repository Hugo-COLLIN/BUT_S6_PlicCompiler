package plic.repint.expression.arithmetique;

import plic.repint.expression.Expression;

public abstract class Binaire extends Expression {
    private Expression opGauche;
    private Expression opDroit;

    public Binaire(Expression opGauche, Expression opDroit) {
        this.opGauche = opGauche;
        this.opDroit = opDroit;
    }

    @Override
    public abstract void verifier();

    @Override
    public abstract String toMips();

    @Override
    public abstract String getType();
}

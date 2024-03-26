package plic.repint.expression.operateurs;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;

public abstract class Binaire extends Expression {
    protected Expression opGauche;
    protected Expression opDroit;

    public Binaire(Expression opGauche, Expression opDroit) {
        this.opGauche = opGauche;
        this.opDroit = opDroit;
    }

    @Override
    public abstract void verifier() throws ErreurSemantique;

    @Override
    public abstract String toMips();

    @Override
    public abstract String getType();
}

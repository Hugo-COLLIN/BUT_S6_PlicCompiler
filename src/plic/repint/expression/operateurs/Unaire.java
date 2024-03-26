package plic.repint.expression.operateurs;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;

public abstract class Unaire extends Expression {
    protected Expression op;

    public Unaire(Expression op) {
        this.op = op;
        // TODO verif si de type entier (en combinant avec arithmetique ?)
    }

    @Override
    public abstract void verifier() throws ErreurSemantique;

    @Override
    public abstract String toMips();

    @Override
    public abstract String getType();
}

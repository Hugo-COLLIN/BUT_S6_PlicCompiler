package plic.repint.expression;

import plic.exceptions.ErreurSemantique;

public abstract class Expression
{
    public abstract void verifier() throws ErreurSemantique;

    @Override
    public String toString() {
        return "Bloc{}";
    }

    public abstract String toMips();

    public abstract String getType();
}

package plic.repint;

import plic.exceptions.ErreurSemantique;

public abstract class Instruction
{
    Expression expression;

    Instruction(Expression e) {
        this.expression = e;
    }

    public abstract void verifier() throws ErreurSemantique;

    @Override
    public String toString() {
        return "Instruction{" +
                "expression=" + expression +
                '}';
    }

    public abstract String toMips();
}

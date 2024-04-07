// Copyright © 2024 Hugo COLLIN
package plic.repint.instruction;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;

public abstract class Instruction
{
    protected Expression expression;

    protected Instruction(Expression e) {
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

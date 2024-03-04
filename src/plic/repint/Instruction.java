package plic.repint;

public abstract class Instruction
{
    Expression expression;

    Instruction(Expression e) {
        this.expression = e;
    }

    public abstract void verifier();

    @Override
    public String toString() {
        return "Instruction{" +
                "expression=" + expression +
                '}';
    }

    public abstract String toMips();
}

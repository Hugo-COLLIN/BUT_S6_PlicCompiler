package plic.repint;

public abstract class Instruction
{
    Expression expression;

    Instruction(Expression e) {
        this.expression = e;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "expression=" + expression +
                '}';
    }
}

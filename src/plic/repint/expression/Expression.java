package plic.repint.expression;

public abstract class Expression
{
    public abstract void verifier();

    @Override
    public String toString() {
        return "Bloc{}";
    }

    public abstract String toMips();

    public abstract String getType();
}

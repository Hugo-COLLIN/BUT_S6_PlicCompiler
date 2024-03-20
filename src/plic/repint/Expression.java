package plic.repint;

public abstract class Expression
{
    Expression() {

    }

    public abstract void verifier();

    @Override
    public String toString() {
        return "Bloc{}";
    }

    public abstract String toMips();

    public abstract String getType();
}

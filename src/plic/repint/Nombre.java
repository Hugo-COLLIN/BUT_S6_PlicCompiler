package plic.repint;

public class Nombre extends Expression
{
    int val;

    public Nombre(int v) {
        super();
        this.val = v;
    }

    @Override
    public String toString() {
        return "Nombre{" +
                "val=" + val +
                '}';
    }
}

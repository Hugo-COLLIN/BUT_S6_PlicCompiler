package plic.repint;

public class Affectation extends Instruction
{
    Idf idf;

    Affectation(Expression e, Idf i) {
        super(e);
        this.idf = i;
    }

}

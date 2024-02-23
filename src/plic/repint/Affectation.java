package plic.repint;

public class Affectation extends Instruction
{
    Idf idf;

    public Affectation(Expression e, Idf i) {
        super(e);
        this.idf = i;
    }

    @Override
    public void verifier() {
        // Vérifier l'identificateur de destination
        idf.verifier();
        // Vérifier l'expression à affecter
        expression.verifier();
    }


}

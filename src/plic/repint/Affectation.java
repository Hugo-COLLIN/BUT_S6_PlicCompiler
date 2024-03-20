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

    @Override
    public String toMips() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
        int deplacement = symbole.getDeplacement();
        return expression.toMips() +
                "sw $v0, " + deplacement + "($s7)\n\n";
    }
}

package plic.repint;

public class Ecrire extends Instruction
{
    public Ecrire(Expression e) {
        super(e);
    }

    @Override
    public void verifier() {
        // Vérifier que l'expression est valide
        expression.verifier();
    }


}

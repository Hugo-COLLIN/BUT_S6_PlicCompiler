package plic.repint.instruction;

import plic.exceptions.ErreurSemantique;
import plic.repint.GenererLecture;
import plic.repint.expression.Expression;

public class Lire extends Instruction {

    public Lire(Expression e) {
        super(e);
    }

    @Override
    public void verifier() throws ErreurSemantique {
        // Vérifier que l'identificateur est déclaré
        expression.verifier();
    }

    @Override
    public String toMips() {
        GenererLecture expression = (GenererLecture) this.expression;
        return expression.genererCodeLecture();
    }
}

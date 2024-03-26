package plic.repint.expression.operateurs.booleen;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;
import plic.repint.expression.operateurs.Unaire;

public class Non extends Unaire {
    public Non(Expression op) {
        super(op);
    }

    @Override
    public void verifier() throws ErreurSemantique {
        op.verifier();
        if (!this.op.getType().equals("booleen"))
            throw new ErreurSemantique("L'expression doit être de type booleen");
    }

    @Override
    public String toMips() {
        return op.toMips() +
                "xori $v0, $v0, 1\n"; // Négation de l'expression
    }

    @Override
    public String getType() {
        return "booleen";
    }
}

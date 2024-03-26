package plic.repint.expression.operateurs.arithmetique;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;
import plic.repint.expression.operateurs.Unaire;

public class Oppose extends Unaire {
    public Oppose(Expression op) {
        super(op);
    }

    @Override
    public void verifier() throws ErreurSemantique {
        op.verifier();
    }

    @Override
    public String toMips() {
        return op.toMips() +
                "neg $v0, $v0\n"; // Inverser le signe de l'expression
    }

    @Override
    public String getType() {
        return "entier";
    }
}

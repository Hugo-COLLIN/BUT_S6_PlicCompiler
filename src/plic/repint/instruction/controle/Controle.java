// Copyright © 2024 Hugo COLLIN
package plic.repint.instruction.controle;

import plic.exceptions.ErreurSemantique;
import plic.repint.expression.Expression;
import plic.repint.instruction.Instruction;

public abstract class Controle extends Instruction {

    // Compteur statique pour différencier les pointeurs via un nom unique
    protected static int compteurPointeurs = 0;

    public Controle(Expression expression) {
        super(expression);
        compteurPointeurs = 0;
    }

    @Override
    public abstract void verifier() throws ErreurSemantique;

    @Override
    public String toMips() {
        compteurPointeurs++;
        return "";
    }
}

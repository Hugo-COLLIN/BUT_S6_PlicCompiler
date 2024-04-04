package plic.repint.instruction;

import plic.exceptions.ErreurSemantique;
import plic.repint.Entree;
import plic.repint.Symbole;
import plic.repint.TDS;
import plic.repint.expression.Idf;

public class Lire extends Instruction {

    public Lire(Idf idf) {
        super(idf);
    }

    @Override
    public void verifier() throws ErreurSemantique {
        // Vérifier que l'identificateur est déclaré
        expression.verifier();
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        Idf idf = (Idf) expression;
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
        int deplacement = symbole.getDeplacement();

        // Lire un entier depuis l'entrée standard et le stocker dans l'identificateur
        sb.append("li $v0, 5\n"); // Code système pour lire un entier
        sb.append("syscall\n"); // Lire l'entier
        sb.append("sw $v0, ").append(deplacement).append("($s7)\n"); // Stocker l'entier lu dans l'identificateur

        return sb.toString();
    }
}

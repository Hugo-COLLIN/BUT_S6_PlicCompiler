package plic.repint.instruction.controle;

import plic.exceptions.ErreurSemantique;
import plic.repint.Bloc;
import plic.repint.expression.Expression;
import plic.repint.expression.Idf;
import plic.repint.instruction.Instruction;

public class Pour extends Controle {
    private Idf idf;
    private Expression debut;
    private Expression fin;
    private Bloc bloc;

    public Pour(Idf idf, Expression debut, Expression fin, Bloc bloc) {
        super(null);
        this.idf = idf;
        this.debut = debut;
        this.fin = fin;
        this.bloc = bloc;
    }

    @Override
    public void verifier() throws ErreurSemantique {
        idf.verifier();
        debut.verifier();
        fin.verifier();
        bloc.verifier();
    }

    @Override
    public String toMips() {
        super.toMips();
        String debutLabel = "debutPour" + compteurPointeurs;
        String finLabel = "finPour" + compteurPointeurs;

        return debut.toMips() +
                "sw $v0, " + idf.getDeplacement() + "($s7)\n" +
                debutLabel + ":\n" +
                fin.toMips() +
                "lw $t1, " + idf.getDeplacement() + "($s7)\n" +
                "ble $t1, $v0, " + finLabel + "\n" +
                bloc.toMips() +
                "addi $t1, $t1, 1\n" +
                "sw $t1, " + idf.getDeplacement() + "($s7)\n" +
                "j " + debutLabel + "\n" +
                finLabel + ":\n";
    }
}

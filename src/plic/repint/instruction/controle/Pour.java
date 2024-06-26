// Copyright © 2024 Hugo COLLIN
package plic.repint.instruction.controle;

import plic.exceptions.ErreurSemantique;
import plic.repint.Bloc;
import plic.repint.expression.Expression;
import plic.repint.expression.Idf;

public class Pour extends Controle {
    private final Idf idf;
    private final Expression debut;
    private final Expression fin;
    private final Bloc bloc;

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

        if (!idf.getType().equals("entier")) {
            throw new ErreurSemantique("L'identificateur de la boucle pour doit être de type entier");
        }

        if (!debut.getType().equals("entier") || !fin.getType().equals("entier")) {
            throw new ErreurSemantique("Les bornes de la boucle pour doivent être de type entier");
        }
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

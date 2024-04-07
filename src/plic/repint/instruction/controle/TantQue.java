package plic.repint.instruction.controle;

import plic.exceptions.ErreurSemantique;
import plic.repint.Bloc;
import plic.repint.expression.Expression;
import plic.repint.instruction.Instruction;

public class TantQue extends Instruction {
    private Bloc bloc;

    // Compteur statique pour différencier les pointeurs via un nom unique
    protected static int compteurPointeurs = 0;

    public TantQue(Expression condition, Bloc bloc) {
        super(condition);
        this.bloc = bloc;
        compteurPointeurs = 0;
    }

    @Override
    public void verifier() throws ErreurSemantique {
        expression.verifier();
        bloc.verifier();
        if (!expression.getType().equals("booleen")) {
            throw new ErreurSemantique("La condition de la boucle tant que doit être de type booleen");
        }
    }

    @Override
    public String toMips() {
        compteurPointeurs++;
        String debutLabel = "debutTantQue" + compteurPointeurs;
        String finLabel = "finTantQue" + compteurPointeurs;

        // Génère le code MIPS pour la boucle avec une réévaluation correcte de la condition
        return debutLabel + ":\n" +
                expression.toMips() + // Évalue la condition
                "beqz $v0, " + finLabel + "\n" + // Si la condition est fausse, sort de la boucle
                bloc.toMips() + // Exécute le bloc d'instructions
                "j " + debutLabel + "\n" + // Retourne au début pour réévaluer la condition
                finLabel + ":\n";
    }
}

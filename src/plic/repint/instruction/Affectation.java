package plic.repint.instruction;

import plic.exceptions.ErreurSemantique;
import plic.repint.*;
import plic.repint.expression.Expression;
import plic.repint.expression.Idf;

public class Affectation extends Instruction
{
    Idf idf;

    public Affectation(Expression e, Idf i) {
        super(e);
        this.idf = i;
    }

    @Override
    public void verifier() throws ErreurSemantique {
        // Vérifier l'identificateur de destination
        idf.verifier();
        // Vérifier l'expression à affecter
        expression.verifier();

        // Récupérer le type de la variable de destination
        Symbole symboleDest = TDS.getInstance().identifier(new Entree(idf.getNom()));
        String typeDest = symboleDest.getType();

        // Déterminer le type de l'expression à affecter
        // Cette partie dépend de la manière dont vous avez implémenté la classe Expression
        // Pour cet exemple, supposons que la méthode getType() retourne le type de l'expression
        String typeExpression = expression.getType();

        // Comparer les types et lancer une erreur si nécessaire
        if (!typeDest.equals(typeExpression)) {
            throw new ErreurSemantique("Affectation entre des types différents non autorisée ("
                            + typeDest + " := " + typeExpression + ")");
        }
    }


    @Override
    public String toMips() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
        int deplacement = symbole.getDeplacement();
        return expression.toMips() +
                "sw $v0, " + deplacement + "($s7)\n\n";
    }
}

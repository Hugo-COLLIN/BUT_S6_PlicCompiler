package plic.repint.expression.arithmetique;

import plic.repint.expression.Expression;

public class Binaire extends Expression {
    private Expression opGauche;
    private Expression opDroit;
    private String operateur;

    public Binaire(Expression opGauche, Expression opDroit, String operateur) {
        this.opGauche = opGauche;
        this.opDroit = opDroit;
        this.operateur = operateur;
    }

    @Override
    public void verifier() {

    }

    @Override
    public String toMips() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    // Méthodes pour évaluer ou afficher l'expression, selon vos besoins
}

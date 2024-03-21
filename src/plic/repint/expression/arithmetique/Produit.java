package plic.repint.expression.arithmetique;

import plic.repint.expression.Expression;

public class Produit extends Binaire {
    public Produit(Expression opGauche, Expression opDroit) {
        super(opGauche, opDroit);
    }

    @Override
    public String toMips() {
        return opGauche.toMips() +      // Générer le code MIPS pour évaluer l'opérande gauche et le stocker dans $t0. Le résultat de l'addition est maintenant dans $v0, prêt à être utilisé ou stocké
                "move $t0, $v0\n" +     // Sauvegarder le résultat de l'opérande gauche dans $t0
                opDroit.toMips() +      // Générer le code MIPS pour évaluer l'opérande droit. À ce stade, le résultat de l'opérande droit est dans $v0
                "mul $v0, $t0, $v0\n";  // Le résultat de la soustraction est maintenant dans $v0, prêt à être utilisé ou stocké
    }
    //TODO: bouger le registre $sp au lieu d'affceter à $v0

    @Override
    public String getType() {
        return "entier";
    }
}

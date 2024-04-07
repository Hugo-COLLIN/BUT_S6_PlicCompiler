package plic.repint.instruction;

import plic.exceptions.ErreurSemantique;
import plic.repint.*;
import plic.repint.expression.Expression;
import plic.repint.expression.Idf;

public class AffectationTableau extends Instruction {
    private final Idf idf;
    private final Expression indice;
    private final Expression valeur;

    public AffectationTableau(Idf idf, Expression indice, Expression valeur) {
        super(valeur);
        this.idf = idf;
        this.indice = indice;
        this.valeur = valeur;
    }

    @Override
    public void verifier() throws ErreurSemantique {
        // Vérifier l'identificateur de tableau, l'indice et la valeur
        idf.verifier();
        indice.verifier();
        valeur.verifier();
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        // Générer le code MIPS pour la valeur à affecter
        // Cela doit être fait en premier pour éviter de perdre la valeur lors du calcul de l'adresse de destination
        sb.append(valeur.toMips()); // Calculer la valeur et la stocker dans $v0

        // Sauvegarder la valeur de $v0 dans un registre temporaire pour éviter son écrasement
        sb.append("move $t0, $v0\n");

        // Identifier le symbole pour l'IDF du tableau destination
        Symbole symboleDest = TDS.getInstance().identifier(new Entree(idf.getNom()));
        int deplacementDest = symboleDest.getDeplacement();

        // Générer le code MIPS pour calculer l'adresse de l'élément du tableau destination
        sb.append(indice.toMips()); // Calculer l'indice pour le tableau destination
        sb.append("li $t1, 4\n");
        sb.append("mul $v0, $v0, $t1\n"); // Multiplier l'indice par 4 (taille d'un entier)
        sb.append("li $t2, ").append(deplacementDest).append("\n");
        sb.append("add $t2, $t2, $v0\n"); // Ajouter le déplacement de base du tableau destination
        sb.append("add $t2, $t2, $s7\n"); // Ajouter le déplacement relatif à $s7 pour obtenir l'adresse finale de destination

        // Restaurer la valeur à stocker depuis $t0 et la stocker à l'adresse calculée pour l'élément du tableau destination
        sb.append("move $v0, $t0\n");
        sb.append("sw $v0, 0($t2)\n\n");

        return sb.toString();
    }

}

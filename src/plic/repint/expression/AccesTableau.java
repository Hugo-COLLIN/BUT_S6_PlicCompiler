package plic.repint.expression;

import plic.repint.Entree;
import plic.repint.Symbole;
import plic.repint.TDS;

public class AccesTableau extends Expression {
    private Idf idf;
    private Expression indice;

    public AccesTableau(Idf idf, Expression indice) {
        this.idf = idf;
        this.indice = indice;
    }

    @Override
    public void verifier() {
        // Vérifier l'identificateur de tableau et l'indice
        idf.verifier();
        indice.verifier();
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
        int deplacement = symbole.getDeplacement();

        sb.append(indice.toMips());
        sb.append("li $t1, 4\n");
        sb.append("mul $v0, $v0, $t1\n"); // Multiplier l'indice par 4 (taille d'un entier)
        sb.append("li $t2, ").append(deplacement).append("\n");
        sb.append("add $t2, $t2, $v0\n"); // Ajouter le déplacement de base du tableau
        sb.append("add $t2, $t2, $s7\n"); // Ajouter le déplacement relatif à $s7
        sb.append("lw $v0, 0($t2)\n\n"); // Charger la valeur à l'adresse calculée

        return sb.toString();
    }

    @Override
    public String getType() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));
        if (symbole == null) {
            throw new RuntimeException("ERREUR: Identificateur non déclaré: " + idf.getNom());
        }
        // Supposons que le type du tableau est stocké dans la propriété 'type' du symbole
        // et que le type des éléments du tableau est 'entier'
        return "entier"; // Remplacez ceci par la logique réelle pour déterminer le type
    }

}
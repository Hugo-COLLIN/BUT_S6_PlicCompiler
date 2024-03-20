package plic.repint;

public class AffectationTableau extends Instruction {
    private Idf idf;
    private Expression indice;
    private Expression valeur;

    public AffectationTableau(Idf idf, Expression indice, Expression valeur) {
        super(valeur); // Utilisez la valeur comme expression de base pour l'instruction
        this.idf = idf;
        this.indice = indice;
        this.valeur = valeur;
    }

    @Override
    public void verifier() {
        // Vérifier l'identificateur de tableau, l'indice et la valeur
        idf.verifier();
        indice.verifier();
        valeur.verifier();
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
        sb.append(valeur.toMips());
        sb.append("sw $v0, 0($t2)\n\n"); // Stocker la valeur à l'adresse calculée

        return sb.toString();
    }


}

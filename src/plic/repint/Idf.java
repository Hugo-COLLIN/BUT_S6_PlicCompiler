package plic.repint;

public class Idf extends Expression
{
    String nom;
    public Idf(String n) {
        super();
        this.nom = n;
    }

    @Override
    public void verifier() {
        // Vérifier que l'identificateur est déclaré
        Symbole symbole = TDS.getInstance().identifier(new Entree(nom));
        if (symbole == null) {
            throw new RuntimeException("ERREUR: Identificateur non déclaré: " + nom);
        }
    }


    @Override
    public String toString() {
        return "Idf{" +
                "nom='" + nom + '\'' +
                '}';
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toMips() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(nom));
        int deplacement = symbole.getDeplacement();
        return "lw $v0, " + deplacement + "($s7)\n";
    }
}

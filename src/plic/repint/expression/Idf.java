// Copyright © 2024 Hugo COLLIN
package plic.repint.expression;

import plic.repint.Entree;
import plic.repint.GenererLecture;
import plic.repint.Symbole;
import plic.repint.TDS;

public class Idf extends Expression implements GenererLecture
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
        return "lw $v0, " + deplacement + "($s7)\n\n";
    }

    @Override
    public String getType() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(nom));
        if (symbole == null) {
            throw new RuntimeException("ERREUR: Identificateur non déclaré: " + nom);
        }
        return symbole.getType();
    }

    public int getDeplacement() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(nom));
        if (symbole == null) {
            throw new RuntimeException("ERREUR: Identificateur non déclaré: " + nom);
        }
        return symbole.getDeplacement();
    }

    // solve casting issue
    public String genererCodeLecture() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(nom));
        int deplacement = symbole.getDeplacement();

        return "li $v0, 5\n" + // Code système pour lire un entier
                "syscall\n" + // Lire l'entier
                "sw $v0, " + deplacement + "($s7)\n";
    }
}

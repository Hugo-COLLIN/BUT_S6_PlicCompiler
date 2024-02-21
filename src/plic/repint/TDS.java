package plic.repint;

import java.util.List;

public class TDS
{
    int cptDepl;

    private static TDS instance;

    private List<Entree> entree;

    private List<Symbole> symbole;

    private TDS() {
        this.cptDepl = 0;
        this.entree = List.of();
        this.symbole = List.of();
    }

    static TDS getInstance() {
        if (instance == null)
            instance = new TDS();
        return instance;
    }

    void ajouter(Entree e, Symbole s) throws DoubleDeclaration {
        if (this.entree.contains(e))
            throw new DoubleDeclaration("dans Entree");
        this.entree.add(e);

        if (this.symbole.contains(s))
            throw new DoubleDeclaration("dans Symbole");
        this.symbole.add(s);
    }
}

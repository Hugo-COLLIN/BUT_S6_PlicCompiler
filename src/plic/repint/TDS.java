package plic.repint;

import java.util.HashMap;
import java.util.Map;

public class TDS
{
    int cptDepl;

    private static TDS instance;

    private Map<Entree, Symbole> entreeVersSymbole;

    private TDS() {
        this.cptDepl = 0;
        this.entreeVersSymbole = new HashMap<>();
    }

    public static TDS getInstance() {
        if (instance == null)
            instance = new TDS();
        return instance;
    }

    public void ajouter(Entree e, Symbole s) throws DoubleDeclaration {
        if (this.entreeVersSymbole.containsKey(e))
            throw new DoubleDeclaration("dans Entree");

        this.entreeVersSymbole.put(e, s);
    }

    @Override
    public String toString() {
        return "TDS{" +
                "cptDepl=" + cptDepl +
                ", entreeVersSymbole=" + entreeVersSymbole +
                '}';
    }
}

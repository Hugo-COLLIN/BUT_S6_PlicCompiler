package plic.repint;

import plic.exceptions.DoubleDeclaration;

import java.util.HashMap;
import java.util.Map;

public class TDS
{
    int cptDepl;

    private static TDS instance;

    private final Map<Entree, Symbole> entreeVersSymbole;

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

        s.setDeplacement(-4 * (this.entreeVersSymbole.size() + 1)); // Calcule le déplacement pour la nouvelle variable
        this.entreeVersSymbole.put(e, s);
    }

    public Symbole identifier(Entree e) {
        // Vérifie si l'entrée existe dans la TDS
        // Retourne le symbole associé à l'entrée si elle existe
        // Retourne null si l'identificateur n'est pas déclaré
        return this.entreeVersSymbole.getOrDefault(e, null);
    }


    @Override
    public String toString() {
        return "TDS{" +
                "cptDepl=" + cptDepl +
                ", entreeVersSymbole=" + entreeVersSymbole +
                '}';
    }

    // Utilisé uniquement dans les tests :
    public void reinitialiser() {
        this.cptDepl = 0; // Réinitialise le compteur de déplacement
        this.entreeVersSymbole.clear(); // Vide la map
    }

}

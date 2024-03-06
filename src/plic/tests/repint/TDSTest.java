package plic.tests.repint;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plic.exceptions.DoubleDeclaration;
import plic.repint.Entree;
import plic.repint.Symbole;
import plic.repint.TDS;

import static org.junit.jupiter.api.Assertions.*;

class TDSTest {
    private TDS tds;
    private Entree entree;
    private Symbole symbole;

    @BeforeEach
    void setUp() {
        tds = TDS.getInstance();
        tds.reinitialiser();
        entree = new Entree("testEntree");
        symbole = new Symbole("testSymbole");
    }

    @Test
    void testSingleton() {
        TDS anotherInstance = TDS.getInstance();
        assertSame(tds, anotherInstance, "Les instances de TDS devraient être les mêmes");
    }

    @Test
    void testAjouter() {
        assertDoesNotThrow(() -> tds.ajouter(entree, symbole), "L'ajout d'une nouvelle entrée ne devrait pas lancer d'exception");
    }

    @Test
    void testDoubleDeclaration() {
        assertDoesNotThrow(() -> tds.ajouter(entree, symbole), "Premier ajout devrait réussir");
        assertThrows(DoubleDeclaration.class, () -> tds.ajouter(entree, symbole), "Le deuxième ajout de la même entrée devrait lancer une DoubleDeclaration");
    }

    @Test
    void testAjoutEtIdentification() throws DoubleDeclaration {
        tds.ajouter(entree, symbole);

        assertEquals(symbole, tds.identifier(entree), "Le symbole retourné doit correspondre à celui ajouté");
    }

    @Test
    void testDeplacement() throws DoubleDeclaration {
        tds.ajouter(entree, symbole);

        Entree entree2 = new Entree("variable2");
        Symbole symbole2 = new Symbole("entier");
        tds.ajouter(entree2, symbole2);

        assertEquals(-4, symbole.getDeplacement(), "Le déplacement de la première variable doit être -4");
        assertEquals(-8, symbole2.getDeplacement(), "Le déplacement de la seconde variable doit être -8");
    }
}

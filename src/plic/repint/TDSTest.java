package plic.repint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TDSTest {
    private TDS tds;
    private Entree entree;
    private Symbole symbole;

    @BeforeEach
    void setUp() {
        tds = TDS.getInstance();
        entree = new Entree("testEntree");
        symbole = new Symbole("testSymbole");
    }

    @Test
    void testSingleton() {
        TDS anotherInstance = TDS.getInstance();
        assertSame(tds, anotherInstance, "Les instances de TDS devraient être les mêmes");
    }

    @Test
    void testAjouter() throws DoubleDeclaration {
        assertDoesNotThrow(() -> tds.ajouter(entree, symbole), "L'ajout d'une nouvelle entrée ne devrait pas lancer d'exception");
    }

    @Test
    void testDoubleDeclaration() {
        assertDoesNotThrow(() -> tds.ajouter(entree, symbole), "Premier ajout devrait réussir");
        assertThrows(DoubleDeclaration.class, () -> tds.ajouter(entree, symbole), "Le deuxième ajout de la même entrée devrait lancer une DoubleDeclaration");
    }
}

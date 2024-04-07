// Copyright © 2024 Hugo COLLIN
package plic.analyse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plic.exceptions.DoubleDeclaration;
import plic.exceptions.ErreurSyntaxique;
import plic.repint.Bloc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class AnalyseurSyntaxiqueTest {
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = File.createTempFile("testSyntaxique", ".plic");
    }

    void writeFileContent(String content) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(tempFile);
        out.print(content);
        out.close();
    }

    @Test
    void testAnalyseSyntaxiqueValide() throws FileNotFoundException {
        String content = "programme monProgramme { entier a ; a := 5 ; }";
        writeFileContent(content);
        AnalyseurSyntaxique anasynt = new AnalyseurSyntaxique(tempFile);
        assertDoesNotThrow(() -> {
            Bloc bloc = anasynt.analyse();
            assertNotNull(bloc, "L'analyse syntaxique d'un fichier valide doit retourner un Bloc non null");
        });
    }

    @Test
    void testErreurSyntaxique() throws FileNotFoundException {
        String content = "programme monProgramme { entier a a := 5 ; }"; // Manque un ';'
        writeFileContent(content);
        AnalyseurSyntaxique anasynt = new AnalyseurSyntaxique(tempFile);
        assertThrows(ErreurSyntaxique.class, anasynt::analyse, "Une erreur syntaxique doit lancer une exception ErreurSyntaxique");
    }

    @Test
    void testDoubleDeclaration() throws FileNotFoundException {
        String content = "programme monProgramme { entier a ; entier a ; }"; // 'a' déclaré deux fois
        writeFileContent(content);
        AnalyseurSyntaxique anasynt = new AnalyseurSyntaxique(tempFile);
        assertThrows(DoubleDeclaration.class, anasynt::analyse, "Une double déclaration doit lancer une exception DoubleDeclaration");
    }

    @Test
    void testEOFUnexpected() throws FileNotFoundException {
        String content = "programme monProgramme { entier a ; a := 5 ;"; // Manque '}' à la fin
        writeFileContent(content);
        AnalyseurSyntaxique anasynt = new AnalyseurSyntaxique(tempFile);
        assertThrows(DoubleDeclaration.class, anasynt::analyse, "Une fin de fichier inattendue doit lancer une exception ErreurSyntaxique");
    }
}

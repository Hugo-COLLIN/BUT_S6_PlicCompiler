package plic.tests.analyse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plic.analyse.AnalyseurLexical;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class AnalyseurLexicalTest {
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".plic");
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    void writeFileContent(String content) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(tempFile);
        out.print(content);
        out.close();
    }

    @Test
    void testNextTokenSimple() throws FileNotFoundException {
        writeFileContent("token1 token2");
        AnalyseurLexical analex = new AnalyseurLexical(tempFile);
        assertEquals("token1", analex.next(), "Le premier token doit être 'token1'");
        assertEquals("token2", analex.next(), "Le deuxième token doit être 'token2'");
        analex.close();
    }

    @Test
    void testReadingWithComments() throws FileNotFoundException {
        writeFileContent("// Ceci est un commentaire\ntoken1");
        AnalyseurLexical analex = new AnalyseurLexical(tempFile);
        assertEquals("token1", analex.next(), "Le token après le commentaire doit être 'token1'");
        analex.close();
    }

    @Test
    void testEOF() throws FileNotFoundException {
        writeFileContent("token1");
        AnalyseurLexical analex = new AnalyseurLexical(tempFile);
        analex.next(); // Lire le premier token
        assertEquals("EOF", analex.next(), "Doit retourner 'EOF' après tous les tokens");
        analex.close();
    }

    @Test
    void testHasNext() throws FileNotFoundException {
        writeFileContent("token1");
        AnalyseurLexical analex = new AnalyseurLexical(tempFile);
        assertTrue(analex.hasNext(), "Doit retourner true s'il reste des tokens");
        analex.next(); // Lire le premier token
        assertFalse(analex.hasNext(), "Doit retourner false après avoir lu tous les tokens");
        analex.close();
    }
}

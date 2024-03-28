package plic;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import plic.repint.TDS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class MoulinettePlicTest {
    static String plicDirPath = "./files/plic/sources";

    @Test
    public void testPlicFiles() {
        File plicFilesDir = new File(plicDirPath);

        // Assurez-vous que le dossier existe et est un dossier
        assert plicFilesDir.exists() && plicFilesDir.isDirectory() : "Le dossier spécifié n'existe pas ou n'est pas un dossier.";

        // Récupérer tous les fichiers .plic dans le dossier
        File[] plicFiles = plicFilesDir.listFiles((dir, name) -> name.endsWith(".plic"));

        assert plicFiles != null && plicFiles.length > 0 : "Aucun fichier .plic trouvé dans le dossier spécifié.";

        for (File plicFile : plicFiles) {
            // Capturer la sortie standard et d'erreur
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            ByteArrayOutputStream errContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));

            // Exécuter le compilateur
            runPlicCompiler(plicFile.getAbsolutePath());

            // Restaurer les sorties standard et d'erreur
            System.setOut(originalOut);
            System.setErr(originalErr);

            // Vérifier la sortie avec ApprovalTests
            String testName = plicFile.getName().replace(".plic", "");
            Approvals.verify(outContent.toString() + "\n" + errContent.toString(), testName);
        }
    }

    private static void runPlicCompiler(String filePath) {
        // Réinitialiser l'état de TDS avant chaque compilation
        TDS.getInstance().reinitialiser();

        String[] args = {filePath};
        Plic.main(args);
    }
}

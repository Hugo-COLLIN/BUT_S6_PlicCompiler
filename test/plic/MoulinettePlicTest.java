package plic;

import org.approvaltests.Approvals;
import org.approvaltests.combinations.CombinationApprovals;
import org.junit.jupiter.api.Test;
import plic.repint.TDS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

public class MoulinettePlicTest {
    static String plicDirPath = "./files/plic/sources";

    @Test
    public void testPlicFiles() {
        File plicFilesDir = new File(plicDirPath);

        assert plicFilesDir.exists() && plicFilesDir.isDirectory() : "Le dossier spécifié n'existe pas ou n'est pas un dossier.";

        // Récupérer tous les fichiers .plic dans le dossier
        File[] plicFiles = plicFilesDir.listFiles((dir, name) -> name.endsWith(".plic"));

        assert plicFiles != null && plicFiles.length > 0 : "Aucun fichier .plic trouvé dans le dossier spécifié.";

        String[] plicContent = Arrays.stream(plicFiles)
                .map(File::getAbsolutePath).toArray(String[]::new);

//        System.out.println(plicContent);

        CombinationApprovals.verifyAllCombinations(
            this::runPlicCompiler,
            plicContent
        );
    }

    private String runPlicCompiler(String filePath) {
        // Réinitialiser l'état de TDS avant chaque compilation
        TDS.getInstance().reinitialiser();

        // --- Capturer la sortie standard de Plic.main ---
        // Sauvegarde de la sortie standard originale
        PrintStream originalStdout = System.out;

        // Création d'un flux de sortie pour capturer la sortie standard
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(outputStream);

        // Redirection de la sortie standard vers le flux de capture
        System.setOut(captureStream);

        // --- Exécution de Plic.main avec la sortie standard capturée ---
        // Exécution de Plic.main avec la sortie standard capturée
        String[] args = {filePath};
        Plic.main(args);

        // --- Restaurer les paramètres de sortie standard ---
        // Restauration de la sortie standard originale
        System.setOut(originalStdout);

        // Conversion de la sortie capturée en chaîne de caractères
        String capturedOutput = outputStream.toString();

        // Affichage ou utilisation de la sortie capturée
        System.out.println("Captured output for " + filePath + ": " + capturedOutput);
        return capturedOutput;
    }
}

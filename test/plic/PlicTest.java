package plic;

import org.approvaltests.combinations.CombinationApprovals;
import org.junit.jupiter.api.Test;
import plic.repint.TDS;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class PlicTest {
    static String plicDirPath = "./files/plic/sources";

    @Test
    public void testPlicFiles() throws Exception {
        Path plicFilesDirPath = Paths.get(plicDirPath);

        assert Files.exists(plicFilesDirPath) && Files.isDirectory(plicFilesDirPath) : "Le dossier spécifié n'existe pas ou n'est pas un dossier.";

        // Utiliser Files.walk pour récupérer tous les fichiers .plic dans le dossier et sous-dossiers
        List<Path> plicFilesPaths = Files.walk(plicFilesDirPath)
                .filter(path -> path.toString().endsWith(".plic"))
                .collect(Collectors.toList());

        assert plicFilesPaths != null && !plicFilesPaths.isEmpty() : "Aucun fichier .plic trouvé dans le dossier spécifié.";

        Path basePath = plicFilesDirPath.toAbsolutePath();

        String[] plicContent = plicFilesPaths.stream()
                .map(path -> basePath.relativize(path.toAbsolutePath()).toString())
                .toArray(String[]::new);

        CombinationApprovals.verifyAllCombinations(
                this::runPlicCompiler,
                plicContent
        );
    }

    private String runPlicCompiler(String relativeFilePath) {
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
        // Conversion du chemin relatif en chemin absolu et exécution de Plic.main avec la sortie standard capturée
        String absoluteFilePath = Paths.get(plicDirPath, relativeFilePath).toAbsolutePath().toString();
        String[] args = {absoluteFilePath};
        Plic.main(args);

        // --- Restaurer les paramètres de sortie standard ---
        // Restauration de la sortie standard originale
        System.setOut(originalStdout);

        // Conversion de la sortie capturée en chaîne de caractères
        String capturedOutput = outputStream.toString();

        return capturedOutput;
    }
}

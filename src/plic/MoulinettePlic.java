package plic;

import plic.repint.TDS;

import java.io.File;

public class MoulinettePlic {
    /**
     * Chemin vers le dossier contenant les fichiers PLIC
      */
    static String plicDirPath = "files/plic/sources";

    public static void main(String[] args) {
        File plicFilesDir = new File(args.length > 0 ? args[0] : plicDirPath);

        // Vérifier si le dossier existe
        if (!plicFilesDir.exists() || !plicFilesDir.isDirectory()) {
            System.err.println("Le dossier spécifié n'existe pas ou n'est pas un dossier.");
            return;
        }

        // Récupérer tous les fichiers .plic dans le dossier
        File[] plicFiles = plicFilesDir.listFiles((dir, name) -> name.endsWith(".plic"));

        if (plicFiles != null) {
            for (File plicFile : plicFiles) {
                System.out.println("Testing: " + plicFile.getName());
                runPlicCompiler(plicFile.getAbsolutePath());
                System.out.println("--------------------------------");
            }
        } else {
            System.err.println("Aucun fichier .plic trouvé dans le dossier spécifié.");
        }
    }

    private static void runPlicCompiler(String filePath) {
        // Réinitialiser l'état de TDS avant chaque compilation
        TDS.getInstance().reinitialiser();

        String[] args = {filePath};
        Plic.main(args);
    }
}

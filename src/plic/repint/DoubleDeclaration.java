package plic.repint;

public class DoubleDeclaration extends Exception {
    public DoubleDeclaration(String message) {
        super("ERREUR: Double déclaration " + message);
    }
}

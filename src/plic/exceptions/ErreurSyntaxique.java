package plic.exceptions;

public class ErreurSyntaxique extends Exception {
    public ErreurSyntaxique(String message) {
        super("ERREUR: " + message);
    }
}

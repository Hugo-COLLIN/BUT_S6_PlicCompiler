package plic.exceptions;

public class ErreurSyntaxique extends Exception {
    public ErreurSyntaxique(String message) {
        super("ERREUR: " + message);
    }

    public ErreurSyntaxique(String message, String token) {
        super("ERREUR: " + message + " - token \"" + token + "\"");
    }
}

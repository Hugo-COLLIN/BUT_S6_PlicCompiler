package plic.exceptions;

public class ErreurNonImplemente extends Exception {
    public ErreurNonImplemente(String token) {
        super("ERREUR: Pas encore implémenté: " + token);
    }
}

package plic.exceptions;

public class ErreurNonImplemente extends Exception {
    public ErreurNonImplemente() {
        super("ERREUR: Pas encore implémenté");
    }

    public ErreurNonImplemente(String token) {
        super("ERREUR: Pas encore implémenté:" + token);
    }
}

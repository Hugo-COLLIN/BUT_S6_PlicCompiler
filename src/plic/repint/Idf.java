package plic.repint;

public class Idf extends Expression
{
    String nom;
    Idf(String n) {
        super();
        this.nom = n;
    }

    @Override
    public String toString() {
        return "Idf{" +
                "nom='" + nom + '\'' +
                '}';
    }
}

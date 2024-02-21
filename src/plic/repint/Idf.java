package plic.repint;

public class Idf extends Expression
{
    String nom;
    public Idf(String n) {
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

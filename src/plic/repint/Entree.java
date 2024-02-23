package plic.repint;

import java.util.Objects;

public class Entree {
    private String idf;

    public Entree(String idf) {
        this.idf = idf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entree entree = (Entree) o;
        return Objects.equals(idf, entree.idf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idf);
    }

    public String getIdf() {
        return idf;
    }

    public void setIdf(String idf) {
        this.idf = idf;
    }
}

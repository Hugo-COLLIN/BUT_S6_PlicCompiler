package plic.repint;

import java.util.ArrayList;
import java.util.List;

public class Bloc
{
    List<Instruction> instructions;

    public Bloc() {
        this.instructions = new ArrayList<>();
    }

    public void ajouter(Instruction i) {
        this.instructions.add(i);
    }

    @Override
    public String toString() {
        return "Bloc{" +
                "instruction=" + instructions +
                '}';
    }
}

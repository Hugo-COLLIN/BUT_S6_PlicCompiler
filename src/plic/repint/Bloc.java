package plic.repint;

import java.util.ArrayList;
import java.util.List;

public class Bloc
{
    List<Instruction> instruction;

    public Bloc() {
        this.instruction = new ArrayList<>();
    }

    public void ajouter(Instruction i) {
        this.instruction.add(i);
    }

    @Override
    public String toString() {
        return "Bloc{" +
                "instruction=" + instruction +
                '}';
    }
}

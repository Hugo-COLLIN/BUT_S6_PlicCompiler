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

    public void verifier() {
        for (Instruction instruction : instructions) {
            instruction.verifier();
        }
    }


    @Override
    public String toString() {
        return "Bloc{" +
                "instruction=" + instructions +
                '}';
    }

    public String toMips() {
        StringBuilder sb = new StringBuilder();
        for (Instruction instruction : instructions) {
            sb.append(instruction.toMips());
        }
        return sb.toString();
    }

}

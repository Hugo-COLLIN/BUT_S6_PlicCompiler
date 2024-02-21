package plic.repint;

import java.util.List;

public class Bloc
{
    List<Instruction> instruction;

    public Bloc() {
        this.instruction = List.of();
    }

    void ajouter(Instruction i) {
        System.err.println("Not implemented");
    }

    @Override
    public String toString() {
        return "Bloc{" +
                "instruction=" + instruction +
                '}';
    }
}

package plic.repint;

import plic.exceptions.ErreurSemantique;
import plic.repint.instruction.Instruction;

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

    public void verifier() throws ErreurSemantique {
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

        // Obtenir la taille totale nécessaire pour les variables
        int tailleTotale = TDS.getInstance().calculerTailleTotale();

        // Initialiser $s7 avec la taille totale
        sb.append("    move $s7, $sp\n");
        sb.append("    add $sp, $sp, -").append(tailleTotale).append("\n");

        // Générer le code MIPS pour les instructions
        for (Instruction instruction : instructions) {
            String[] lines = instruction.toMips().split("\n");
            for (String line : lines) {
                sb.append("    ").append(line).append("\n");
            }
        }

        return sb.toString();
    }
}

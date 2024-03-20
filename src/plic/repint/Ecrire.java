package plic.repint;

public class Ecrire extends Instruction
{
    public Ecrire(Expression e) {
        super(e);
    }

    @Override
    public void verifier() {
        // Vérifier que l'expression est valide
        expression.verifier();
    }

    @Override
    public String toMips() {
        return expression.toMips() +
                "move $a0, $v0\n" +
                "li $v0, 1\n" +
                "syscall\n" +
                "la $a0, linebreak\n" +
                "li $v0, 4\n" +
                "syscall\n\n";
    }
}

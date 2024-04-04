package plic.repint.instruction.controle;

import plic.exceptions.ErreurSemantique;
import plic.repint.Bloc;
import plic.repint.expression.Expression;
import plic.repint.instruction.Instruction;

public class Condition extends Instruction {
    private Bloc blocAlors;
    private Bloc blocSinon;

    public Condition(Expression expression, Bloc blocAlors, Bloc blocSinon) {
        super(expression);
        this.blocAlors = blocAlors;
        this.blocSinon = blocSinon;
    }

    @Override
    public void verifier() throws ErreurSemantique {
        expression.verifier();
        blocAlors.verifier();
        if (blocSinon != null) {
            blocSinon.verifier();
        }
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        String finLabel = "finCondition" + this.hashCode();
        String sinonLabel = "sinonBloc" + this.hashCode();

        sb.append(expression.toMips());
        sb.append("beqz $v0, ").append(sinonLabel).append("\n");
        sb.append(blocAlors.toMips());
        sb.append("j ").append(finLabel).append("\n");
        sb.append(sinonLabel).append(":\n");
        if (blocSinon != null) {
            sb.append(blocSinon.toMips());
        }
        sb.append(finLabel).append(":\n");

        return sb.toString();
    }
}

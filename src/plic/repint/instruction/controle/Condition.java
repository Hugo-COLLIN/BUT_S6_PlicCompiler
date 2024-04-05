package plic.repint.instruction.controle;

import plic.exceptions.ErreurSemantique;
import plic.repint.Bloc;
import plic.repint.expression.Expression;
import plic.repint.instruction.Instruction;

public class Condition extends Instruction {
    private Bloc blocAlors;
    private Bloc blocSinon;

    // Compteur statique pour suivre le nombre d'écritures de tableau
    private static int conditionsCompteur = 0;

    public Condition(Expression expression, Bloc blocAlors, Bloc blocSinon) {
        super(expression);
        this.blocAlors = blocAlors;
        this.blocSinon = blocSinon;
        // Ne réinitialisez pas le compteur ici pour permettre l'unicité des labels
    }

    @Override
    public void verifier() throws ErreurSemantique {
        expression.verifier();
        blocAlors.verifier();
        if (blocSinon != null) {
            blocSinon.verifier();
        }
    }

    //COMPIL ERROR ::::
//    @Override
//    public String toMips() {
//        StringBuilder sb = new StringBuilder();
//
//        conditionsCompteur++;
//        String finLabel = "finCondition" + conditionsCompteur;
//        String sinonLabel = "sinonBloc" + conditionsCompteur;
//
//        sb.append(expression.toMips());
//        sb.append("beqz $v0, ").append(sinonLabel).append("\n");
//        sb.append(blocAlors.toMips());
//        if (blocSinon != null) {
//            sb.append("j ").append(finLabel).append("\n");
//            sb.append(sinonLabel).append(":\n");
//            sb.append(blocSinon.toMips());
//        }
//        sb.append(finLabel).append(":\n");
//
//        return sb.toString();
//    }

//    @Override
//    public String toMips() {
//        StringBuilder sb = new StringBuilder();
//
//        // Incrémenter le compteur et générer les noms de labels basés sur ce compteur
//        conditionsCompteur++;
//        String finLabel = "finCondition" + conditionsCompteur;
//        String sinonLabel = "sinonBloc" + conditionsCompteur;
//
//        sb.append(expression.toMips());
//        sb.append("beqz $v0, ").append(sinonLabel).append("\n");
//        sb.append(blocAlors.toMips());
//        if (blocSinon != null) {
//            sb.append("j ").append(finLabel).append("\n");
//            sb.append(sinonLabel).append(":\n");
//            sb.append(blocSinon.toMips());
//        }
//        else {
//            sb.append(sinonLabel).append(":\n");
//        }
//        sb.append(finLabel).append(":\n");
//
//        return sb.toString();
//    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();

        // Incrémenter le compteur et générer les noms de labels basés sur ce compteur
        conditionsCompteur++;
        String finLabel = "finCondition" + conditionsCompteur;
        String sinonLabel = "sinonBloc" + conditionsCompteur;

        sb.append(expression.toMips());
        sb.append("beqz $v0, ").append(sinonLabel).append("\n");
        sb.append(blocAlors.toMips());
        if (blocSinon != null) {
            sb.append("j ").append(finLabel).append("\n");
            sb.append(sinonLabel).append(":\n");
            sb.append(blocSinon.toMips());
            sb.append(finLabel).append(":\n");
        }
        else {
            sb.append(sinonLabel).append(":\n");
        }

        return sb.toString();
    }
}

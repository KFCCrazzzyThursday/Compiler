package arithmetic;

import token.*;
import exceptions.*;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

public class arithmetic_ternary_operator {
    private BooleanToken condition;
    private Token branch_1;
    private Token branch_2;

    public arithmetic_ternary_operator(Token condition_, Token branch_1_, Token branch_2_) throws ExpressionException{
        if (!condition_.getType().equals("boolean"))
            throw new TypeMismatchedException();
        condition = new BooleanToken(condition_);

        if ( branch_1_.getType().equals("decimal"))
            branch_1 = new DecimalToken( branch_1_);
        else if ( branch_1_.getType().equals("boolean"))
            branch_1 = new BooleanToken( branch_1_);
        else
            throw new MissingOperandException();

        if ( branch_2_.getType().equals("decimal"))
            branch_2 = new DecimalToken( branch_2_);
        else if ( branch_2_.getType().equals("boolean"))
            branch_2 = new BooleanToken( branch_2_);
        else
            throw new MissingOperandException();
    }


    public Token result() throws ExpressionException {
        boolean choose = condition.getBoolean();

        if (choose) {
            if (branch_1.getType().equals("decimal"))
                return new DecimalToken(branch_1, false);
            else
                return new BooleanToken(branch_1, false);
        }

        if (branch_2.getType().equals("decimal"))
            return new DecimalToken(branch_2, false);
        else
            return new BooleanToken(branch_2, false);
    }
}

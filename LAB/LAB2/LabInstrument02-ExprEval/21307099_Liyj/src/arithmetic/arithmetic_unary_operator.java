package arithmetic;

import token.*;
import exceptions.*;
import token.Value.BooleanToken;
import token.Value.DecimalToken;


public class arithmetic_unary_operator {
    private final Token token;


    public arithmetic_unary_operator(Token value) throws TypeMismatchedException {
        if (value.getType().equals("decimal")){
            token = new DecimalToken(-((DecimalToken)value).getDecimal(), false);}
        else if (value.getType().equals("boolean"))
            token = new BooleanToken(((BooleanToken)value).getBoolean(), false);
        else
            throw new TypeMismatchedException();
    }

    public Token result() {
        return token;
    }
}

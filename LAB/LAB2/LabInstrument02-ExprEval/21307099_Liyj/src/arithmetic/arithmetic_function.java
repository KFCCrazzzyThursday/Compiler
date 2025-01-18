package arithmetic;
import token.*;
import exceptions.*;
import token.Value.DecimalToken;
import token.Function.*;
import java.util.*;


public class arithmetic_function {
    
    private Token token;
    
    private final ArrayList <Token> args;
    
    private final int length;

    
    public arithmetic_function() {
        args = new ArrayList <Token>();
        length = 0;
    }

    
    public arithmetic_function(Token _func, ArrayList <Token> _args) {
        token = new Function(_func);
        args = new ArrayList <Token>(_args);
        length = args.size();
    }

    
    private void checkArgs() throws ExpressionException {
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                Token iValue = args.get(i);

                switch (iValue.getType()) {
                    case "boolean":
                        throw new TypeMismatchedException();
                    case "comma":
                        throw new MissingOperandException();
                    case "decimal":
                        break;
                    default:
                        throw new FunctionCallException();
                }
                DecimalToken iValue1 = (DecimalToken) iValue;
                double nowValue = iValue1.getDecimal();
            } else {
                if (!args.get(i).getType().equals("comma"))
                    throw new FunctionCallException();
            }
        }
        if (length % 2 == 0)
            throw new MissingOperandException();
    }

    
    private Token Sin_Cos() throws ExpressionException {
        if (length == 0)
            throw new MissingOperandException();
        if (length != 1)
            throw new FunctionCallException();

        DecimalToken value = (DecimalToken) args.getFirst();

        return switch (token.getValue()) {
            case "sin" -> new DecimalToken(Math.sin(value.getDecimal()), false);
            case "cos" -> new DecimalToken(Math.cos(value.getDecimal()), false);
            default -> throw new FunctionCallException();
        };
    }


    private Token Max_Min() throws ExpressionException {

        if (length == 0)
            throw new MissingOperandException();

        Token firstValue = args.getFirst();
        double maxValue = ((DecimalToken)firstValue).getDecimal();
        double minValue = maxValue;
        for (int i = 1; i < length; i++) {
            if (i % 2 == 0) {
                Token iValue = args.get(i);

                double nowValue = ((DecimalToken)iValue).getDecimal();
                maxValue = Math.max(nowValue, maxValue);
                minValue = Math.min(nowValue, minValue);
            } else {
                if (!args.get(i).getType().equals("comma"))
                    throw new FunctionCallException();
            }
        }

        if ((length + 1) / 2 <= 1)
            throw new MissingOperandException();

        switch (token.getValue()) {
            case "max":
                return new DecimalToken(maxValue, false);
            case "min":
                return new DecimalToken(minValue, false);
        }
        throw new FunctionCallException();
    }

    public Token result() throws ExpressionException {

        checkArgs();

        if (token.getValue().equals("sin") || token.getValue().equals("cos"))
            return Sin_Cos();
        else
            return Max_Min();
    }
}

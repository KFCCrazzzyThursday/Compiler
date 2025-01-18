package arithmetic;

import token.*;
import exceptions.*;
import token.Value.BooleanToken;
import token.Value.DecimalToken;


public class arithmetic_binary_operator {
    
    private Token token;
    
    private Token left;
    
    private Token right;

    
    public arithmetic_binary_operator(Token _operator, Token _left, Token _right) throws TypeMismatchedException {
        if (_left.getType().equals("decimal") && _right.getType().equals("decimal")) {
            left = new DecimalToken(_left);
            right = new DecimalToken(_right);
        } else if (_left.getType().equals("boolean") && _right.getType().equals("boolean")) {
            left = new BooleanToken(_left);
            right = new BooleanToken(_right);
        } else {
            throw new TypeMismatchedException();
        }
        token = _operator;
    }

    
    private Token Arithmetic() throws ExpressionException {
        double leftDecimal = ((DecimalToken) left).getDecimal();
        double rightDecimal = ((DecimalToken) right).getDecimal();
        switch (token.getValue()) {
            case "+":
                return new DecimalToken(leftDecimal + rightDecimal, false);
            case "-":
                return new DecimalToken(leftDecimal - rightDecimal, false);
            case "*":
                return new DecimalToken(leftDecimal * rightDecimal, false);
            case "/":
                if (rightDecimal == 0)
                    throw new DividedByZeroException();
                return new DecimalToken(leftDecimal / rightDecimal, false);
            case "^":
                return new DecimalToken(Math.pow(leftDecimal, rightDecimal), false);
        }
        throw new MissingOperatorException();
    }

    
    private Token Relation_Boolean() throws ExpressionException {
        boolean leftBoolean = ((BooleanToken) left).getBoolean();
        boolean rightBoolean = ((BooleanToken) right).getBoolean();
        switch (token.getValue()) {
            case "&":
                return new BooleanToken(leftBoolean && rightBoolean, false);
            case "|":
                return new BooleanToken(leftBoolean || rightBoolean, false);
        }
        throw new MissingOperatorException();
    }

    
    private Token Relation_Decimal() throws ExpressionException {
        double leftDecimal = ((DecimalToken) left).getDecimal();
        double rightDecimal = ((DecimalToken) right).getDecimal();
        switch (token.getValue()) {
            case "<":
                return new BooleanToken(leftDecimal < rightDecimal, false);
            case "<=":
                return new BooleanToken(leftDecimal <= rightDecimal, false);
            case ">":
                return new BooleanToken(leftDecimal > rightDecimal, false);
            case ">=":
                return new BooleanToken(leftDecimal >= rightDecimal, false);
            case "=":
                return new BooleanToken(leftDecimal == rightDecimal, false);
            case "<>":
                return new BooleanToken(leftDecimal != rightDecimal, false);
        }
        throw new MissingOperatorException();
    }

    
    public Token result() throws ExpressionException {
        if (token.getType().equals("arithmetic"))
            return Arithmetic();
        else if (token.getType().equals("boolean"))
            return Relation_Boolean();
        return Relation_Decimal();
    }
}

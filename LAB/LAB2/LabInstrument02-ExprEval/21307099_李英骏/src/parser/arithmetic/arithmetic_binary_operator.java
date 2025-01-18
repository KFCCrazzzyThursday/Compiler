package arithmetic;

import token.*;
import exceptions.*;
import token.Operator.BinaryOperatorToken;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

/**
 * 这个类表示一个算术二元运算符
 */
public class arithmetic_binary_operator {

    private final BinaryOperatorToken token;
    private final Token left;
    private final Token right;

    /**
     * 构造一个算术二元运算符对象
     *
     * @param _operator 二元运算符
     * @param _left     左操作数
     * @param _right    右操作数
     * @throws TypeMismatchedException 当左右操作数类型不匹配时抛出该异常
     */
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
        token = (BinaryOperatorToken) _operator;
    }

    /**
     * 执行算术运算
     *
     * @return 运算结果
     * @throws ExpressionException 当运算过程中发生错误时抛出该异常
     */
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

    /**
     * 执行布尔关系运算
     *
     * @return 运算结果
     * @throws ExpressionException 当运算过程中发生错误时抛出该异常
     */
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

    /**
     * 执行十进制关系运算
     *
     * @return 运算结果
     * @throws ExpressionException 当运算过程中发生错误时抛出该异常
     */
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
            case "|":
            case "&":
                throw new TypeMismatchedException();
        }
        throw new MissingOperatorException();
    }

    /**
     * 计算并返回运算结果
     *
     * @return 运算结果
     * @throws ExpressionException 当运算过程中发生错误时抛出该异常
     */
    public Token result() throws ExpressionException {
        if (token.getReturnType().equals("decimal"))
            return Arithmetic();
        else if (token.getReturnType().equals("boolean") && left.getType().equals("boolean") && right.getType().equals("boolean"))
            return Relation_Boolean();
        return Relation_Decimal();
    }
}

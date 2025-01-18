package parser;

import exceptions.*;
import scanner.Scanner;
import token.Function.Function;
import token.Operator.BinaryOperatorToken;
import token.Operator.TernaryOperatorToken;
import token.Operator.UnaryOperatorToken;
import token.OperatorPrecedenceTable;
import token.Symbol.DelimiterToken;
import token.Symbol.ParenthesisToken;
import token.Token;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

import java.util.ArrayList;

public class Parser {
    private ArrayList <Token> input_buffer;
    private ArrayList <Token> stack;
    private static int [][] operator_precedence_table;

    public Parser(String expression) throws ExpressionException {
        input_buffer = new ArrayList <Token>(new Scanner(expression).scan());
        stack = new ArrayList <Token>();
        input_buffer.add(new DelimiterToken("$"));
        operator_precedence_table = OperatorPrecedenceTable.getCodedPrecedenceTable();
    }

    private Token getStackTop() {
        int stackLength = stack.size();
        int i = stackLength - 1;
        for (; i >= 0; i--) {
            if (stack.get(i).getTerminal())
                break;
        }
        return stack.get(i);
    }

    private Token addInStack(Token lookahead) throws IllegalSymbolException {
        return switch (lookahead.getType()) {
            case "decimal" -> new DecimalToken(lookahead);
            case "boolean" -> new BooleanToken(lookahead);
            case "function" -> new Function(lookahead);
            case "arithmetic", "relation" -> new BinaryOperatorToken(lookahead);
            case "parenthesis" -> new ParenthesisToken(lookahead);
            case "ternary" -> new TernaryOperatorToken(lookahead);
            case "unary" -> new UnaryOperatorToken(lookahead);
            case "comma", "eoe" -> new DelimiterToken(lookahead);
            default -> throw new IllegalSymbolException();
        };
    }

    private double getAnswer() throws TypeMismatchedException {
        if (stack.size() == 2) {
            Token top = stack.getLast();
            if (top.getType().equals("decimal")) {
                DecimalToken decimal = (DecimalToken) top;
                return decimal.getDecimal();
            }
            else
                throw new TypeMismatchedException();
        }
        return 0;
    }

    private void reduce() throws ExpressionException {
        Token stackTop = getStackTop();
        Token lookahead = input_buffer.getFirst();
        int action = operator_precedence_table[stackTop.getPriority()][lookahead.getPriority()];
        while (action == 1) {
            stack = new Reducer(stack).calculate(stackTop.getType());
            stackTop = getStackTop();
            action = operator_precedence_table[stackTop.getPriority()][lookahead.getPriority()];
        }
    }

    private void shift(Token lookahead) throws IllegalSymbolException {
        stack.add(addInStack(lookahead));
        input_buffer.removeFirst();
    }

    public double opp() throws ExpressionException {
        stack.add(new DelimiterToken("$"));
        while (true) {
            Token stackTop = getStackTop();
            Token lookahead = input_buffer.getFirst();
            int action = operator_precedence_table[stackTop.getPriority()][lookahead.getPriority()];

            switch (action) {
                case 0:
                    shift(lookahead);
                    break;
                case 1:
                    reduce();
                    break;
                case 2:
                    return getAnswer();
                case -1:
                    throw new MissingOperatorException();
                case -2:
                    throw new MissingOperandException();
                case -3:
                    throw new MissingLeftParenthesisException();
                case -4:
                    throw new MissingRightParenthesisException();
                case -5:
                    throw new FunctionCallException();
                case -6:
                    throw new TrinaryOperationException();
            }
        }
    }
}

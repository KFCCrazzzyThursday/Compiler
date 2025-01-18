package parser;

import arithmetic.*;
import exceptions.*;
import token.Token;

import java.util.ArrayList;


public class Reducer {
    private ArrayList <Token> stack;
    private int length;

    public Reducer() {
        stack = new ArrayList <Token>();
        length = 0;
    }

    public Reducer(ArrayList <Token> _stack) {
        stack = new ArrayList <Token>(_stack);
        length = stack.size();
    }

    private int getTerminalLocation(int last) throws MissingOperatorException {
        for (int i = last; i >= 0; i--)
            if (stack.get(i).getTerminal())
                return i;
        throw new MissingOperatorException();
    }

    private int findLeftParenthesis() throws MissingLeftParenthesisException {
        for (int i = length - 1; i >= 0; i--)
            if (stack.get(i).getValue().equals("("))
                return i;
        throw new MissingLeftParenthesisException();
    }

    private void reduce(int location, int times, Token result) {
        for (int i = 0; i < times; i++)
            stack.remove(location);
        stack.add(location, result);
    }

    public ArrayList <Token> calculate(String type) throws ExpressionException {
        int i = getTerminalLocation(stack.size() - 1);

        if (type.equals("decimal") || type.equals("boolean")) {
            Token temp = stack.get(i);
            arithmetic_identity A_I = new arithmetic_identity(temp);
            Token result = A_I.result();

            reduce(i, 1, result);

        } else if (type.equals("arithmetic")) {

            if (i - 1 < 0 || i + 1 >= length)
                throw new MissingOperandException();
            if (stack.get(i - 1).getTerminal() || stack.get(i + 1).getTerminal())
                throw new MissingOperandException();
            Token result = new arithmetic_binary_operator(stack.get(i), stack.get(i - 1), stack.get(i + 1)).result();

            reduce(i - 1, 3, result);

        } else if (type.equals("unary")) {

            if (i + 1 >= length)
                throw new MissingOperandException();
            Token result = new arithmetic_unary_operator(stack.get(i + 1)).result();

            reduce(i, 2, result);

        } else if (type.equals("parenthesis")) {

            int left = findLeftParenthesis();
            ArrayList <Token> args = new ArrayList <Token> ();
            for (int j = left + 1; j < i; j++)
                args.add(stack.get(j));
            if (left > 0 && stack.get(left - 1).getType().equals("function")) {
                Token result = new arithmetic_function(stack.get(left - 1), args).result();
                reduce(left - 1, i - left + 2, result);
            }
            else {
                if (args.size() <= 0)
                    throw new MissingOperandException();
                if (args.size() > 1)
                    throw new SemanticException();
                if (args.get(0).getTerminal())
                    throw new MissingOperandException();

                Token result = new arithmetic_identity(args.get(0)).result();
                reduce(left, 3, result);
            }

        } else if (type.equals("relation")) {

            if (i - 1 < 0 || i + 1 >= length)
                throw new MissingOperandException();
            if (stack.get(i - 1).getTerminal() || stack.get(i + 1).getTerminal())
                throw new MissingOperandException();

            Token result = new arithmetic_binary_operator(stack.get(i), stack.get(i - 1), stack.get(i + 1)).result();
            reduce(i - 1, 3, result);

        } else if (type.equals("ternary")) {

            if (i - 1 < 0 || i + 1 >= length)
                throw new MissingOperandException();
            int j = getTerminalLocation(i - 1);
            if (j - 1 < 0 || i - j != 2)
                throw new MissingOperandException();
            if (stack.get(j - 1).getTerminal() || stack.get(j + 1).getTerminal()
                    || stack.get(i + 1).getTerminal())
                throw new MissingOperandException();

            Token result = new arithmetic_ternary_operator(stack.get(j - 1), stack.get(j + 1), stack.get(i + 1)).result();
            reduce(j - 1, i + 1 - (j - 1) + 1, result);

        } else
            throw new MissingOperatorException();

        return new ArrayList <Token>(stack);
    }
}

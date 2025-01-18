package parser;

import arithmetic.*;
import exceptions.*;
import token.Token;

import java.util.ArrayList;

/**
 * 这个类用于处理算术表达式的规约
 */
public class Reducer {
    private final ArrayList<Token> stack;
    private final int length;

    /**
     * 带参数的构造方法，初始化栈
     *
     * @param _stack 要初始化的栈
     */
    public Reducer(ArrayList<Token> _stack) {
        stack = new ArrayList<Token>(_stack);
        length = stack.size();
    }

    /**
     * 获取最近的终结符位置
     *
     * @param last 起始位置
     * @return 终结符位置
     * @throws MissingOperatorException 当缺少操作符时抛出异常
     */
    private int getTerminalLocation(int last) throws MissingOperatorException {
        for (int i = last; i >= 0; i--)
            if (stack.get(i).getTerminal())
                return i;
        throw new MissingOperatorException();
    }

    /**
     * 查找左括号的位置
     *
     * @return 左括号的位置
     * @throws MissingLeftParenthesisException 当缺少左括号时抛出异常
     */
    private int findLeftParenthesis() throws MissingLeftParenthesisException {
        for (int i = length - 1; i >= 0; i--)
            if (stack.get(i).getValue().equals("("))
                return i;
        throw new MissingLeftParenthesisException();
    }

    /**
     * 规约操作，将结果替换对应位置的令牌
     *
     * @param location 规约位置
     * @param times    规约次数
     * @param result   规约结果
     */
    private void reduce(int location, int times, Token result) {
        for (int i = 0; i < times; i++)
            stack.remove(location);
        stack.add(location, result);
    }

    /**
     * 根据类型进行规约计算
     *
     * @param type 类型
     * @return 规约后的栈
     * @throws ExpressionException 当表达式出错时抛出异常
     */
    public ArrayList<Token> calculate(String type) throws ExpressionException {
        int i = getTerminalLocation(stack.size() - 1);

        switch (type) {
            case "decimal", "boolean" -> {
                Token temp = stack.get(i);
                arithmetic_identity A_I = new arithmetic_identity(temp);
                Token result = A_I.result();

                reduce(i, 1, result);

            }
            case "arithmetic", "relation" -> {

                if (i - 1 < 0 || i + 1 >= length)
                    throw new MissingOperandException();
                if (stack.get(i - 1).getTerminal() || stack.get(i + 1).getTerminal())
                    throw new MissingOperandException();
                Token result = new arithmetic_binary_operator(stack.get(i), stack.get(i - 1), stack.get(i + 1)).result();

                reduce(i - 1, 3, result);

            }
            case "unary" -> {

                if (i + 1 >= length)
                    throw new MissingOperandException();
                Token result = new arithmetic_unary_operator(stack.get(i + 1)).result();

                reduce(i, 2, result);

            }
            case "parenthesis" -> {

                int left = findLeftParenthesis();
                ArrayList<Token> args = new ArrayList<Token>();
                for (int j = left + 1; j < i; j++)
                    args.add(stack.get(j));
                if (left > 0 && stack.get(left - 1).getType().equals("function")) {
                    Token result = new arithmetic_function(stack.get(left - 1), args).result();
                    reduce(left - 1, i - left + 2, result);
                } else {
                    if (args.isEmpty())
                        throw new MissingOperandException();
                    if (args.size() > 1)
                        throw new SemanticException();
                    if (args.getFirst().getTerminal())
                        throw new MissingOperandException();

                    Token result = new arithmetic_identity(args.getFirst()).result();
                    reduce(left, 3, result);
                }
            }
            case "ternary" -> {

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

            }
            default -> throw new MissingOperatorException();
        }

        return new ArrayList<Token>(stack);
    }
}

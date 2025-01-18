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

/**
 * 这个类表示一个算术表达式解析器
 */
public class Parser {
    private final ArrayList<Token> input_buffer;
    private ArrayList<Token> stack;
    private static int[][] operator_precedence_table;

    /**
     * 构造一个解析器对象，初始化输入缓冲区和操作符优先级表
     *
     * @param expression 要解析的算术表达式
     * @throws ExpressionException 当表达式解析出错时抛出异常
     */
    public Parser(String expression) throws ExpressionException {
        input_buffer = new ArrayList<Token>(new Scanner(expression).scan());
        stack = new ArrayList<Token>();
        input_buffer.add(new DelimiterToken("$"));
        operator_precedence_table = OperatorPrecedenceTable.getCodedPrecedenceTable();
    }

    /**
     * 获取栈顶的终结符
     *
     * @return 栈顶终结符
     */
    private Token getStackTop() {
        int stackLength = stack.size();
        int i = stackLength - 1;
        for (; i >= 0; i--) {
            if (stack.get(i).getTerminal())
                break;
        }
        return stack.get(i);
    }

    /**
     * 将令牌加入栈中
     *
     * @param lookahead 前瞻符号
     * @return 对应的令牌
     * @throws IllegalSymbolException 当符号非法时抛出异常
     */
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

    /**
     * 获取最终答案
     *
     * @return 算术表达式的结果
     * @throws TypeMismatchedException 当类型不匹配时抛出异常
     */
    private double getAnswer() throws TypeMismatchedException {
        if (stack.size() == 2) {
            Token top = stack.getLast();
            if (top.getType().equals("decimal")) {
                DecimalToken decimal = (DecimalToken) top;
                return decimal.getDecimal();
            } else
                throw new TypeMismatchedException();
        }
        return 0;
    }

    /**
     * 进行规约操作
     *
     * @throws ExpressionException 当表达式出错时抛出异常
     */
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

    /**
     * 进行移入操作
     *
     * @param lookahead 前瞻符号
     * @throws IllegalSymbolException 当符号非法时抛出异常
     */
    private void shift(Token lookahead) throws IllegalSymbolException {
        stack.add(addInStack(lookahead));
        input_buffer.removeFirst();
    }

    /**
     * 执行算符优先解析
     *
     * @return 解析结果
     * @throws ExpressionException 当表达式解析出错时抛出异常
     */
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

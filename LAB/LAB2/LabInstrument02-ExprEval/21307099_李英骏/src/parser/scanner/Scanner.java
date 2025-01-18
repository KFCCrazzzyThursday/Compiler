package scanner;

import exceptions.*;
import DFA.*;
import token.Token;

import java.util.ArrayList;

/**
 * 这个类表示一个扫描器，用于解析算术表达式并生成令牌列表
 */
public class Scanner {
    private final String formula_stream;
    private final ArrayList<Token> tokens;
    private static DFA dfa;

    /**
     * 主方法，用于测试扫描器
     *
     * @param args 命令行参数
     * @throws ExpressionException 当表达式解析出错时抛出异常
     */
    public static void main(String[] args) throws ExpressionException {
        // 测试表达式
        String expression = "-31 + 5 * ( 2 - 8e1 ) < sin(-2.1)? 2 : 1";
        Scanner myScanner = new Scanner(expression);
        ArrayList<Token> tokens = myScanner.scan();

        // 输出扫描结果
        for (Token token : tokens) {
            System.out.println("Token: " + token.getValue() + ", Type: " + token.getType());
        }
    }

    /**
     * 构造一个扫描器对象，初始化表达式流和DFA
     *
     * @param formula_stream 要解析的算术表达式
     */
    public Scanner(String formula_stream) {
        this.formula_stream = formula_stream;
        this.tokens = new ArrayList<>();
        dfa = new DFA();
    }

    /**
     * 判断当前字符是否为一元运算符
     *
     * @param current 当前字符
     * @return 如果是则返回true，否则返回false
     */
    private boolean isUnary(String current) {
        if (current.equals("!")) {
            return true;
        }
        if (current.equals("-")) {
            // 如果是首个token则必然是负号,
            // 如果不是则看前一个token
            int token_length = tokens.size();
            if (token_length > 0) {
                Token lasttoken = tokens.get(token_length - 1);
                return !lasttoken.getType().equals("decimal") &&
                        !lasttoken.getType().equals(")") &&
                        !lasttoken.getType().equals("boolean");
            }
            return true;
        }
        return false;
    }

    /**
     * 扫描输入表达式流并返回令牌列表
     * 该方法使用DFA识别不同的令牌
     *
     * @return 从输入表达式流中提取的令牌列表
     * @throws ExpressionException 如果存在非法令牌或表达式为空
     */
    public ArrayList<Token> scan() throws ExpressionException {
        int expressionLength = formula_stream.length(); // 输入表达式的长度
        int index = 0; // 当前字符索引

        StringBuilder currentToken = new StringBuilder(); // 当前扫描到的token
        dfa.reset(); // 重置DFA状态
        boolean isLetter = false; // 当前token是否以字母开头
        boolean isDecimal = false; // 当前token是否以数字开头

        while (index < expressionLength) {
            char now = formula_stream.charAt(index); // 当前字符
            char lookahead = (index + 1 < expressionLength) ?
                    formula_stream.charAt(index + 1) : '$'; // 下一个字符

            // 跳过空格
            if (now == ' ') {
                index++;
                continue;
            }

            // 如果DFA处于初始状态，检测当前字符是否是字母或数字
            if (dfa.getState() == 0) {
                if (Character.isLetter(now))
                    isLetter = true;
                else if (Character.isDigit(now) || now == '.')
                    isDecimal = true;
            }

            currentToken.append(now); // 将当前字符添加到当前token中
            String tokenType = dfa.step(now, lookahead); // 通过DFA的step方法获取token类型

            // 如果DFA还未识别出完整token，继续扫描下一个字符
            if (tokenType.equals("valid")) {
                index++;
                continue;
            }
            // 如果DFA识别出异常token，抛出相应的异常
            else if (tokenType.equals("exception")) {
                if (isLetter)
                    throw new IllegalIdentifierException(); // 非法标识符异常
                else if (isDecimal)
                    throw new IllegalDecimalException(); // 非法小数异常
                else
                    throw new IllegalSymbolException(); // 非法符号异常
            } else {
                // 如果DFA识别出完整token，将其添加到token列表中
                tokens.add(dfa.getToken(currentToken.toString(), isUnary(currentToken.toString())));
                dfa.reset(); // 重置DFA状态
                currentToken = new StringBuilder(); // 重置当前token
                isLetter = false; // 重置isLetter标志
                isDecimal = false; // 重置isDecimal 标志
            }
            index++;
        }

        // 如果表达式为空，抛出异常
        if (tokens.isEmpty())
            throw new EmptyExpressionException();

        return tokens; // 返回token列表
    }
}

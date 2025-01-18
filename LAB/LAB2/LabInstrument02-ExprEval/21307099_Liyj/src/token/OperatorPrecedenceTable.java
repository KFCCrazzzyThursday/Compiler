package token;

import java.util.HashMap;
import java.util.Map;
/**
 * 该类用于构建和处理算符优先关系表。它包含了各类运算符的优先级和结合律，
 * 并提供了根据优先级关系进行操作的相关方法。
 * 算符优先关系表在编译器设计中用于决定在何时进行规约（reduce）和移入（shift）操作。
 */
public class OperatorPrecedenceTable {

    private static final Map<String, Integer> precedence = new HashMap<>();
    private static final Map<String, Boolean> associativity = new HashMap<>();

    static {
        // 定义运算符的优先级
        precedence.put("boolean", 0);
        precedence.put("decimal", 1);
        precedence.put("(", 2);
        precedence.put(")", 3);
        precedence.put("function", 4);
        precedence.put("negative", 5);
        precedence.put("^", 6);
        precedence.put("*", 7);
        precedence.put("/", 8);
        precedence.put("+", 9);
        precedence.put("-", 10);
        precedence.put("relation", 11);
        precedence.put("!", 12);
        precedence.put("&", 13);
        precedence.put("|", 14);
        precedence.put("?", 15);
        precedence.put(":", 16);
        precedence.put(",", 17);
        precedence.put("$", 18);

        // 定义运算符的结合律（左结合为 true，右结合为 false）
        associativity.put("function", false);
        associativity.put("negative", false);
        associativity.put("!", false);
        associativity.put("?", false);
        associativity.put("^", false);
        associativity.put(",", false);

        associativity.put("*", true);
        associativity.put("/", true);

        associativity.put("+", true);
        associativity.put("-", true);

        associativity.put("relation", true);
        associativity.put("&", true);
        associativity.put("|", true);
        associativity.put(":", true);
        associativity.put("$", true);
    }

    /**
     * 测试方法：打印算符优先关系表和编码后的优先关系表。
     * 该方法应仅用于测试目的。
     */
    public static void main(String[] args) {
        String[] operators = {"boolean", "decimal", "(", ")", "function", "negative", "^", "*", "/", "+", "-", "relation", "!", "&", "|", "?", ":", ",", "$"};
        String[][] precedenceTable = new String[operators.length][operators.length];
        int[][] precedenceCode = getCodedPrecedenceTable();
        for (int i = 0; i < operators.length; i++) {
            for (int j = 0; j < operators.length; j++) {
                precedenceTable[i][j] = getRelation(operators[i], operators[j]);
            }
        }
        // 打印算符优先关系表
        printTable(operators, precedenceTable);
        // 打印编码后的优先关系表
        printCodeTable(operators, precedenceCode);
    }

    /**
     * 获取编码后的优先关系表。
     *
     * @return 二维数组表示的优先关系表，0 表示 shift，1 表示 reduce，2 表示 accept，负数表示各种错误。
     */
    public static int[][] getCodedPrecedenceTable() {
        String[] operators = {"boolean", "decimal", "(", ")", "function", "negative", "^", "*", "/", "+", "-", "relation", "!", "&", "|", "?", ":", ",", "$"};
        String[][] precedenceTable = new String[operators.length][operators.length];
        int[][] precedenceCode = new int[operators.length][operators.length];
        for (int i = 0; i < operators.length; i++) {
            for (int j = 0; j < operators.length; j++) {
                precedenceTable[i][j] = getRelation(operators[i], operators[j]);
                switch (precedenceTable[i][j]) {
                    case "shift":
                        precedenceCode[i][j] = 0;
                        break;
                    case "reduce":
                        precedenceCode[i][j] = 1;
                        break;
                    case "accept":
                        precedenceCode[i][j] = 2;
                        break;
                    case "E1":
                        precedenceCode[i][j] = -1;
                        break;
                    case "E2":
                        precedenceCode[i][j] = -2;
                        break;
                    case "E3":
                        precedenceCode[i][j] = -3;
                        break;
                    case "E4":
                        precedenceCode[i][j] = -4;
                        break;
                    case "E5":
                        precedenceCode[i][j] = -5;
                        break;
                    case "E6":
                        precedenceCode[i][j] = -6;
                        break;
                    default:
                        precedenceCode[i][j] = Integer.MIN_VALUE;
                        break;
                }
            }
        }
        return precedenceCode;
    }

    /**
     * 根据给定的操作符，获取其与另一个操作符的优先关系。
     *
     * @param op1 第一个操作符
     * @param op2 第二个操作符
     * @return 字符串表示的优先关系：shift, reduce, accept 或错误代码 E1-E6。
     */
    private static String getRelation(String op1, String op2) {
        String E = errorDetected(op1, op2);
        if (E != null) {
            return E;
        }
        if (op1.equals("$") && op2.equals("$")) {
            return "accept";
        }
        // 左括号应shift
        if (op1.equals("(") || op2.equals("(")) {
            return "shift";
        }
        // 右括号说明子式结束 应reduce(一般情况下)
        if (op1.equals(")") || (op2.equals(")") && !op1.equals(","))) {
            return "reduce";
        }
        // ?后面不报错就应shift
        if (op1.equals("?")) {
            return "shift";
        }
        // ,后面不报错就应shift
        if (op1.equals(",")) {
            return "shift";
        }
        int prec1 = precedence.get(op1);
        int prec2 = precedence.get(op2);
        if (prec1 == prec2) {
            Boolean assoc = associativity.get(op1);
            return Boolean.TRUE.equals(assoc) ? "reduce" : "shift";
        }
        return prec1 < prec2 ? "reduce" : "shift"; // 优先级高的遇到低的shift(一般情况下)
    }

    /**
     * 检测操作符之间的错误关系。
     *
     * @param op1 第一个操作符
     * @param op2 第二个操作符
     * @return 错误代码 E1-E6 或 null 表示没有错误。
     */
    private static String errorDetected(String op1, String op2) {
        // E1: MissingOperator
        // 连续value或者)后面会缺运算符
        if (op1.equals("decimal") || op1.equals("boolean")) {
            if (op2.equals("decimal") || op2.equals("boolean") ||
                    op2.equals("(") || op2.equals("function") || op2.equals("!")) {
                return "E1";
            }
        }
        if (op1.equals(")") && (op2.equals("boolean") || op2.equals("decimal") ||
                op2.equals("(") || op2.equals("function") || op2.equals("!"))) {
            return "E1";
        }
        // E2: MissingOperand
        if (op1.equals("?")) {
            if (op2.equals(")")) {
                return "E2";
            }
        }
        // E3: LeftParenthesis
        // 此阶段只有在$后的)能判断缺少左括号
        if (op1.equals("$") && op2.equals(")")) {
            return "E3";
        }
        // E4: RightParenthesis
        // 此阶段只有在(后的$能判断缺少左括号
        if (op1.equals("(") && op2.equals("$")) {
            return "E4";
        }
        // E5: FunctionCall
        // function后面只能出现左括号, 因此任何非左括号的跟随都是E5
        if (op1.equals("function") && !op2.equals("(")) {
            return "E5";
        }
        // E6: TrinaryOperation
        // 三目运算符?:的异常有
        // (: --如5 ? (8 : 8)
        // ?$ --如1>0?
        // $: --如2:3
        if ((op1.equals("(") && op2.equals(":")) ||
                (op1.equals("?") && op2.equals("$")) ||
                (op1.equals("$") && op2.equals(":"))) {
            return "E6";
        }
        return null;
    }

    /**
     * 打印算符优先关系表。
     *
     * @param operators 操作符数组
     * @param table     优先关系表
     */
    private static void printTable(String[] operators, String[][] table) {
        System.out.print("           ");
        for (String op : operators) {
            System.out.printf("%-10s", op);
        }
        System.out.println();
        System.out.print("           ");
        for (int i = 0; operators.length > 0 && i < operators.length; i++) {
            System.out.print("----------");
        }
        System.out.println();
        for (int i = 0; i < operators.length; i++) {
            System.out.printf("%-10s", operators[i]);
            for (int j = 0; j < operators.length; j++) {
                System.out.printf("%-10s", table[i][j] == null ? "E" : table[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 打印编码后的优先关系表。
     *
     * @param operators 操作符数组
     * @param table     编码后的优先关系表
     */
    public static void printCodeTable(String[] operators, int[][] table) {
        System.out.print("           ");
        for (String op : operators) {
            System.out.printf("%-10s", op);
        }
        System.out.println();
        System.out.print("           ");
        for (int i = 0; operators.length > 0 && i < operators.length; i++) {
            System.out.print("----------");
        }
        System.out.println();
        for (int i = 0; i < operators.length; i++) {
            System.out.printf("%-10s", operators[i]);
            for (int j = 0; j < operators.length; j++) {
                System.out.printf("%-10s", table[i][j] == Integer.MIN_VALUE ? "E" : table[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 获取给定操作符的优先级。
     *
     * @param operator 操作符
     * @return 操作符的优先级
     */
    public static int getPrecedence(String operator) {
        return precedence.getOrDefault(operator, Integer.MAX_VALUE);
    }

    /**
     * 判断给定操作符是否是左结合的。
     *
     * @param operator 操作符
     * @return 如果是左结合返回 true，否则返回 false
     */
    public static Boolean isLeftAssociative(String operator) {
        return associativity.getOrDefault(operator, true); // 默认左结合
    }
}

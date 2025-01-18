package arithmetic;

import token.*;
import exceptions.*;
import token.Value.DecimalToken;
import token.Function.*;
import java.util.*;

/**
 * 这个类表示一个算术函数
 */
public class arithmetic_function {

    private final Token token;
    private final ArrayList<Token> args;
    private final int length;

    /**
     * 带参数的构造方法，初始化函数和参数列表
     * @param _func 函数令牌
     * @param _args 参数令牌列表
     */
    public arithmetic_function(Token _func, ArrayList<Token> _args) {
        token = new Function(_func);
        args = new ArrayList<Token>(_args);
        length = args.size();
    }

    /**
     * 检查参数是否合法
     * @throws ExpressionException 当参数不合法时抛出异常
     */
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
            } else {
                if (!args.get(i).getType().equals("comma"))
                    throw new FunctionCallException();
            }
        }
        if (length % 2 == 0)
            throw new MissingOperandException();
    }

    /**
     * 计算正弦或余弦函数 使用toLowerCase确保大小写无关
     * @return 计算结果
     * @throws ExpressionException 当参数不合法时抛出异常
     */
    private Token Sin_Cos() throws ExpressionException {
        if (length == 0)
            throw new MissingOperandException();
        if (length != 1)
            throw new FunctionCallException();

        DecimalToken value = (DecimalToken) args.getFirst();

        return switch (token.getValue().toLowerCase(Locale.ROOT)) {
            case "sin" -> new DecimalToken(Math.sin(value.getDecimal()), false);
            case "cos" -> new DecimalToken(Math.cos(value.getDecimal()), false);
            default -> throw new FunctionCallException();
        };
    }

    /**
     * 计算最大值或最小值函数 使用toLowerCase确保大小写无关
     * @return 计算结果
     * @throws ExpressionException 当参数不合法时抛出异常
     */
    private Token Max_Min() throws ExpressionException {
        if (length == 0)
            throw new MissingOperandException();

        Token firstValue = args.getFirst();
        double maxValue = ((DecimalToken) firstValue).getDecimal();
        double minValue = maxValue;
        for (int i = 1; i < length; i++) {
            if (i % 2 == 0) {
                Token iValue = args.get(i);

                double nowValue = ((DecimalToken) iValue).getDecimal();
                maxValue = Math.max(nowValue, maxValue);
                minValue = Math.min(nowValue, minValue);
            } else {
                if (!args.get(i).getType().equals("comma"))
                    throw new FunctionCallException();
            }
        }

        if ((length + 1) / 2 <= 1)
            throw new MissingOperandException();

        return switch (token.getValue().toLowerCase(Locale.ROOT)) {
            case "max" -> new DecimalToken(maxValue, false);
            case "min" -> new DecimalToken(minValue, false);
            default -> throw new FunctionCallException();
        };
    }

    /**
     * 计算并返回函数的结果 使用toLowerCase确保大小写无关
     * @return 计算结果
     * @throws ExpressionException 当参数不合法时抛出异常
     */
    public Token result() throws ExpressionException {
        checkArgs();

        if (token.getValue().toLowerCase(Locale.ROOT).equals("sin") || token.getValue().toLowerCase(Locale.ROOT).equals("cos"))
            return Sin_Cos();
        else
            return Max_Min();
    }
}

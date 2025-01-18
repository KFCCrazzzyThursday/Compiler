package arithmetic;

import token.*;
import exceptions.*;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

/**
 * 这个类表示一个算术一元运算符
 */
public class arithmetic_unary_operator {
    private final Token token;

    /**
     * 构造一个算术一元运算符对象
     *
     * @param value 需要进行一元运算的令牌
     * @throws TypeMismatchedException 当令牌类型不匹配时抛出该异常
     */
    public arithmetic_unary_operator(Token value) throws TypeMismatchedException {
        if (value.getType().equals("decimal")) {
            token = new DecimalToken(-((DecimalToken) value).getDecimal(), false);
        } else if (value.getType().equals("boolean")) {
            token = new BooleanToken(((BooleanToken) value).getBoolean(), false);
        } else {
            throw new TypeMismatchedException();
        }
    }

    /**
     * 返回一元运算的结果
     *
     * @return 运算结果令牌
     */
    public Token result() {
        return token;
    }
}

package token.Value;

import token.Token;

/**
 * 这个类表示一个十进制数值令牌
 */
public class DecimalToken extends ValueToken {
    private final double decimalValue;

    /**
     * 使用字符串值构造十进制数值令牌
     *
     * @param value 字符串值
     */
    public DecimalToken(String value) {
        super(1, "decimal", value);
        this.decimalValue = Double.parseDouble(value);
    }

    /**
     * 使用数值和终结状态构造十进制数值令牌
     *
     * @param value    数值
     * @param terminal 终结状态
     */
    public DecimalToken(double value, boolean terminal) {
        super(1, "decimal", String.valueOf(value));
        this.decimalValue = value;
        this.terminal = terminal;
    }

    /**
     * 使用Token对象构造十进制数值令牌
     *
     * @param copy Token对象
     */
    public DecimalToken(Token copy) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = copy.getTerminal();
        this.decimalValue = Double.parseDouble(copy.getValue());
    }

    /**
     * 使用Token对象和终结状态构造十进制数值令牌
     *
     * @param copy     Token对象
     * @param terminal 终结状态
     */
    public DecimalToken(Token copy, boolean terminal) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = terminal;
        this.decimalValue = Double.parseDouble(copy.getValue());
    }

    /**
     * 获取十进制数值
     *
     * @return 十进制数值
     */
    public double getDecimal() {
        return decimalValue;
    }
}

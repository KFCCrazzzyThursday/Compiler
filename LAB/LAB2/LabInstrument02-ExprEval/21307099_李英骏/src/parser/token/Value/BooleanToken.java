package token.Value;

import token.Token;

/**
 * 这个类表示一个布尔值令牌
 */
public class BooleanToken extends ValueToken {
    private final boolean booleanValue;

    /**
     * 使用字符串值构造布尔值令牌
     *
     * @param value 字符串值
     */
    public BooleanToken(String value) {
        super(0, "boolean", value);
        this.booleanValue = value.equalsIgnoreCase("true");
    }

    /**
     * 使用布尔值和终结状态构造布尔值令牌
     *
     * @param value    布尔值
     * @param terminal 终结状态
     */
    public BooleanToken(boolean value, boolean terminal) {
        super(0, "boolean", value ? "true" : "false");
        this.booleanValue = value;
        this.terminal = terminal;
    }

    /**
     * 使用Token对象构造布尔值令牌
     *
     * @param copy Token对象
     */
    public BooleanToken(Token copy) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = copy.getTerminal();
        this.booleanValue = copy.getValue().equalsIgnoreCase("true");
    }

    /**
     * 使用Token对象和终结状态构造布尔值令牌
     *
     * @param copy     Token对象
     * @param terminal 终结状态
     */
    public BooleanToken(Token copy, boolean terminal) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = terminal;
        this.booleanValue = copy.getValue().equalsIgnoreCase("true");
    }

    /**
     * 获取布尔值
     *
     * @return 布尔值
     */
    public boolean getBoolean() {
        return booleanValue;
    }
}

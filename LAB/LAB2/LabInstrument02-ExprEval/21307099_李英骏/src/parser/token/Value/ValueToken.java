package token.Value;

import token.Token;

/**
 * 这个类表示一个抽象的值令牌
 */
public abstract class ValueToken implements Token {
    protected int priorityId;
    protected String type;
    protected boolean terminal;
    protected String value;

    /**
     * 使用优先级、类型和值构造值令牌
     *
     * @param priorityId 优先级
     * @param type       类型
     * @param value      值
     */
    protected ValueToken(int priorityId, String type, String value) {
        this.priorityId = priorityId;
        this.type = type;
        this.value = value;
        this.terminal = true;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getPriority() {
        return priorityId;
    }

    @Override
    public boolean getTerminal() {
        return terminal;
    }
}

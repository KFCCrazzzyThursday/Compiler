package token.Operator;

import token.Token;

/**
 * 这个类表示一个抽象的运算符令牌
 */
public abstract class OperatorToken implements Token {
    protected int priorityId;
    protected String type;
    protected String value;

    /**
     * 使用运算符值构造运算符令牌
     *
     * @param value 运算符值
     */
    protected OperatorToken(String value) {
        this.priorityId = 0;
        this.type = "operator";
        this.value = value;
    }

    /**
     * 使用Token对象构造运算符令牌
     *
     * @param token Token对象
     */
    protected OperatorToken(Token token) {
        this.priorityId = token.getPriority();
        this.type = token.getType();
        this.value = token.getValue();
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
        return true;
    }
}

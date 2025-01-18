package token.Symbol;

import token.Token;
import token.OperatorPrecedenceTable;

/**
 * 这个类表示一个抽象的符号令牌
 */
public abstract class SymbolToken implements Token {
    protected int priorityId;
    protected String type;
    protected boolean terminal;
    protected String value;

    /**
     * 使用符号值构造符号令牌
     *
     * @param value 符号值
     */
    protected SymbolToken(String value) {
        this.priorityId = OperatorPrecedenceTable.getPrecedence(value);
        this.terminal = true;
        this.value = value;
    }

    /**
     * 使用Token对象构造符号令牌
     *
     * @param token Token对象
     */
    protected SymbolToken(Token token) {
        this.priorityId = token.getPriority();
        this.type = token.getType();
        this.terminal = token.getTerminal();
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

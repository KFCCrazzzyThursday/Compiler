package token.Function;

import token.OperatorPrecedenceTable;
import token.Token;

/**
 * 这个类表示一个函数令牌
 */
public class Function implements Token {
    private final int priorityId;
    private final String type;
    private final String value;

    /**
     * 使用函数名构造函数令牌
     *
     * @param func 函数名
     */
    public Function(String func) {
        this.priorityId = OperatorPrecedenceTable.getPrecedence("function");
        this.type = "function";
        this.value = func;
    }

    /**
     * 使用Token对象构造函数令牌
     *
     * @param token Token对象
     */
    public Function(Token token) {
        this.priorityId = token.getPriority();
        this.type = token.getType();
        this.value = token.getValue();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getPriority() {
        return this.priorityId;
    }

    @Override
    public boolean getTerminal() {
        return true;
    }
}

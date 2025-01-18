package token.Symbol;

import token.Token;

/**
 * 这个类表示一个括号令牌
 */
public class ParenthesisToken extends SymbolToken {

    /**
     * 使用括号值构造括号令牌
     *
     * @param value 括号值
     */
    public ParenthesisToken(String value) {
        super(value);
        this.type = "parenthesis";
    }

    /**
     * 使用Token对象构造括号令牌
     *
     * @param token Token对象
     */
    public ParenthesisToken(Token token) {
        super(token);
    }
}

package token.Symbol;

import token.Token;

/**
 * 这个类表示一个分隔符令牌
 */
public class DelimiterToken extends SymbolToken {

    /**
     * 使用分隔符值构造分隔符令牌
     *
     * @param value 分隔符值
     */
    public DelimiterToken(String value) {
        super(value);
        switch (value) {
            case "$":
                this.type = "eoe";
                break;
            case ",":
                this.type = "comma";
                break;
        }
    }

    /**
     * 使用Token对象构造分隔符令牌
     *
     * @param token Token对象
     */
    public DelimiterToken(Token token) {
        super(token);
    }
}

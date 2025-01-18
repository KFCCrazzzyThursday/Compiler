package arithmetic;

import exceptions.MissingOperandException;
import token.Token;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

/**
 * 这个类表示一个算术标识符
 */
public class arithmetic_identity {
    private final Token token;

    /**
     * 构造一个算术标识符对象
     *
     * @param token_ 标识符的令牌
     * @throws MissingOperandException 当令牌类型不匹配时抛出该异常
     */
    public arithmetic_identity(Token token_) throws MissingOperandException {
        switch (token_.getType()) {
            case "decimal":
                this.token = new DecimalToken(token_, false);
                break;
            case "boolean":
                this.token = new BooleanToken(token_, false);
                break;
            default:
                throw new MissingOperandException();
        }
    }

    /**
     * 返回标识符的结果
     *
     * @return 标识符的令牌
     */
    public Token result() {
        return token;
    }
}

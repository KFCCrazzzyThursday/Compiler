package token.Operator;

import token.Token;
import token.OperatorPrecedenceTable;

/**
 * 这个类表示一个一元运算符令牌
 */
public class UnaryOperatorToken extends OperatorToken {

    /**
     * 使用运算符值构造一元运算符令牌
     *
     * @param value 运算符值
     */
    public UnaryOperatorToken(String value) {
        super(value);
        if (value.equals("-")) {
            this.value = "negative";
        } else if (value.equals("!")) {
            this.value = "!";
        }
        this.type = "unary";
        this.priorityId = OperatorPrecedenceTable.getPrecedence(this.value);
    }

    /**
     * 使用Token对象构造一元运算符令牌
     *
     * @param token Token对象
     */
    public UnaryOperatorToken(Token token) {
        super(token);
    }
}

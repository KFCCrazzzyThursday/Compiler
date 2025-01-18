package token.Operator;

import token.OperatorPrecedenceTable;
import token.Token;

/**
 * 这个类表示一个三元运算符令牌
 */
public class TernaryOperatorToken extends OperatorToken {

    /**
     * 使用运算符值构造三元运算符令牌
     *
     * @param value 运算符值
     */
    public TernaryOperatorToken(String value) {
        super(value);
        this.type = "ternary";
        this.priorityId = OperatorPrecedenceTable.getPrecedence(this.value);
    }

    /**
     * 使用Token对象构造三元运算符令牌
     *
     * @param token Token对象
     */
    public TernaryOperatorToken(Token token) {
        super(token);
    }

}

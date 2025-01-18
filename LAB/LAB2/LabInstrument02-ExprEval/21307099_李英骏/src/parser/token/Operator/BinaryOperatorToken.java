package token.Operator;

import token.OperatorPrecedenceTable;
import token.Token;

/**
 * 这个类表示一个二元运算符令牌
 */
public class BinaryOperatorToken extends OperatorToken {
    /**
     * 使用运算符值构造二元运算符令牌
     *
     * @param value 运算符值
     */
    public BinaryOperatorToken(String value) {
        super(value);
        this.type = switch (value) {
            case "+", "-", "*", "/", "^" -> "arithmetic";
            default -> "relation";
        };

        this.priorityId =(this.type.equals("relation"))?
            11: OperatorPrecedenceTable.getPrecedence(this.value);
    }

    /**
     * 使用Token对象构造二元运算符令牌
     *
     * @param token Token对象
     */
    public BinaryOperatorToken(Token token) {
        super(token);
    }

    /**
     * 获取运算结果类型
     *
     * @return 运算结果类型
     */
    public String getReturnType() {
        return switch (value) {
            case "+", "-", "*", "/", "^" -> "decimal";
            default -> "boolean";
        };
    }
}

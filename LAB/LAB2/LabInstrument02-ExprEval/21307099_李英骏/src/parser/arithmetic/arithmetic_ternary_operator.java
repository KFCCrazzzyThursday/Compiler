package arithmetic;

import token.*;
import exceptions.*;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

/**
 * 这个类表示一个算术三元运算符
 */
public class arithmetic_ternary_operator {
    private final BooleanToken condition;
    private final Token branch_1;
    private final Token branch_2;

    /**
     * 构造一个算术三元运算符对象
     *
     * @param condition_ 条件令牌
     * @param branch_1_  分支1的令牌
     * @param branch_2_  分支2的令牌
     * @throws ExpressionException 当令牌类型不匹配或缺失操作数时抛出该异常
     */
    public arithmetic_ternary_operator(Token condition_, Token branch_1_, Token branch_2_) throws ExpressionException {
        if (!condition_.getType().equals("boolean"))
            throw new TypeMismatchedException();
        condition = new BooleanToken(condition_);

        if (branch_1_.getType().equals("decimal"))
            branch_1 = new DecimalToken(branch_1_);
        else if (branch_1_.getType().equals("boolean"))
            branch_1 = new BooleanToken(branch_1_);
        else
            throw new MissingOperandException();

        if (branch_2_.getType().equals("decimal"))
            branch_2 = new DecimalToken(branch_2_);
        else if (branch_2_.getType().equals("boolean"))
            branch_2 = new BooleanToken(branch_2_);
        else
            throw new MissingOperandException();
    }

    /**
     * 计算并返回三元运算的结果
     *
     * @return 运算结果令牌
     * @throws ExpressionException 当运算过程中发生错误时抛出该异常
     */
    public Token result() throws ExpressionException {
        boolean choose = condition.getBoolean();

        if (choose) {
            if (branch_1.getType().equals("decimal"))
                return new DecimalToken(branch_1, false);
            else
                return new BooleanToken(branch_1, false);
        }

        if (branch_2.getType().equals("decimal"))
            return new DecimalToken(branch_2, false);
        else
            return new BooleanToken(branch_2, false);
    }
}

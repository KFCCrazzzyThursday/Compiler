package token.Operator;
import token.OperatorPrecedenceTable;
import token.Token;

public class TernaryOperatorToken extends OperatorToken {
    public TernaryOperatorToken(String value) {
        super(value);
        this.type = "ternary";
        this.priorityId = OperatorPrecedenceTable.getPrecedence(this.value);
    }

    public TernaryOperatorToken(Token token) {
        super(token);
    }

}


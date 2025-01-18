package token.Operator;
import token.Token;
import token.OperatorPrecedenceTable;
public class UnaryOperatorToken extends OperatorToken {
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
    public UnaryOperatorToken(Token token) {
        super(token);
    }
}

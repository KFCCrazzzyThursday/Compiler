package token.Symbol;
import token.Token;
public class ParenthesisToken extends SymbolToken {

    public ParenthesisToken(String value) {
        super(value);
        this.type = "parenthesis";
    }

    public ParenthesisToken(Token token) {
        super(token);
    }
}

package token.Symbol;
import token.Token;
public class DelimiterToken extends SymbolToken {

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

    public DelimiterToken(Token token) {
        super(token);
    }
}

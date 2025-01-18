package arithmetic;

import exceptions.MissingOperandException;
import token.Token;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

public class arithmetic_identity {
    private Token token;
    public arithmetic_identity(Token token_) throws MissingOperandException {
        switch (token_.getType()) {
            case "decimal":
                this.token = new DecimalToken(token_,false);
                break;
            case "boolean":
                this.token = new BooleanToken(token_,false);
                break;
            default:
                throw new MissingOperandException();
        }
    }

    public Token result() {
        return token;
    }
}

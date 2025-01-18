package token.Value;
import token.Token;
public class BooleanToken extends ValueToken {
    private final boolean booleanValue;

    public BooleanToken(String value) {
        super(0, "boolean", value);
        this.booleanValue = value.equalsIgnoreCase("true");
    }

    public BooleanToken(boolean value, boolean terminal) {
        super(0, "boolean", value ? "true" : "false");
        this.booleanValue = value;
        this.terminal = terminal;
    }

    public BooleanToken(Token copy) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = copy.getTerminal();
        this.booleanValue = copy.getValue().equalsIgnoreCase("true");
    }

    public BooleanToken(Token copy, boolean terminal) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = terminal;
        this.booleanValue = copy.getValue().equalsIgnoreCase("true");
    }

    public boolean getBoolean() {
        return booleanValue;
    }

}

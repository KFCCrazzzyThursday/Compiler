package token.Value;
import token.Token;

public class DecimalToken extends ValueToken {
    private final double decimalValue;

    public DecimalToken(String value) {
        super(1, "decimal", value);
        this.decimalValue = Double.parseDouble(value);
    }

    public DecimalToken(double value, boolean terminal) {
        super(1, "decimal", String.valueOf(value));
        this.decimalValue = value;
        this.terminal = terminal;
    }

    public DecimalToken(Token copy) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = copy.getTerminal();
        this.decimalValue = Double.parseDouble(copy.getValue());
    }

    public DecimalToken(Token copy, boolean terminal) {
        super(copy.getPriority(), copy.getType(), copy.getValue());
        this.terminal = terminal;
        this.decimalValue = Double.parseDouble(copy.getValue());
    }

    public double getDecimal() {
        return decimalValue;
    }
}

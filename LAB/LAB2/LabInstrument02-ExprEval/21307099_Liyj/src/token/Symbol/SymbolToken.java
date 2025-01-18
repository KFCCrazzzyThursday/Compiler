package token.Symbol;
import token.Token;
import token.OperatorPrecedenceTable;
public abstract class SymbolToken implements Token {
    protected int priorityId;
    protected String type;
    protected boolean terminal;
    protected String value;

    protected SymbolToken(String value) {
        this.priorityId = OperatorPrecedenceTable.getPrecedence(value);
        this.terminal = true;
        this.value = value;
    }

    protected SymbolToken(Token token) {
        this.priorityId = token.getPriority();
        this.type = token.getType();
        this.terminal = token.getTerminal();
        this.value = token.getValue();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getPriority() {
        return priorityId;
    }

    @Override
    public boolean getTerminal() {
        return true;
    }

}

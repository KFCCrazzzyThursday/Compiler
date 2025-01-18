package token.Function;
import token.OperatorPrecedenceTable;
import token.Token;

public class Function implements Token {
    private final int priorityId;
    private final String type;
    private final String value;

    public Function(String func){
        this.priorityId = OperatorPrecedenceTable.getPrecedence("function");
        this.type = "function";
        this.value = func;
    }
    public Function(Token token){
        this.priorityId = token.getPriority();
        this.type = token.getType();
        this.value = token.getValue();
    }
    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getPriority() {
        return this.priorityId;
    }

    @Override
    public boolean getTerminal() {
        return true;
    }
}

package token.Operator;

import token.Token;

public abstract class OperatorToken implements Token {
    protected int priorityId;
    protected String type;
    protected String value;

    protected OperatorToken(String value) {
        this.priorityId = 0;
        this.type = "operator";
        this.value = value;
    }

    protected OperatorToken(Token token) {
        this.priorityId = token.getPriority();
        this.type = token.getType();
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

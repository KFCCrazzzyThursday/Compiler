package token.Value;

import token.Token;

public abstract class ValueToken implements Token {
    protected int priorityId;
    protected String type;
    protected boolean terminal;
    protected String value;

    protected ValueToken(int priorityId, String type, String value) {
        this.priorityId = priorityId;
        this.type = type;
        this.value = value;
        this.terminal = true;
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
        return terminal;
    }


}
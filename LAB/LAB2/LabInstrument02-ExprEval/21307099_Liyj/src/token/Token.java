package token;

public interface Token {
    String getValue();
    String getType();
    int getPriority();
    boolean getTerminal();
}
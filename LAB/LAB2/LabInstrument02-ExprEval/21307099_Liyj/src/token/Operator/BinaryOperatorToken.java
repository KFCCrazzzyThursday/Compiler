package token.Operator;
import token.OperatorPrecedenceTable;
import token.Token;
public class BinaryOperatorToken extends OperatorToken {
    public BinaryOperatorToken(String value) {
        super(value);
        this.type = switch (value) {
            case "+", "-", "*", "/", "^" -> "arithmetic";
            default -> "relation";
        };

        this.priorityId = switch(this.type){
            case "relation"->11;
            default -> OperatorPrecedenceTable.getPrecedence(this.value);
        };
    }

    public BinaryOperatorToken(Token token) {
        super(token);
    }

    public String getReturnType() {
        return switch (value) {
            case "<", "<=", ">", ">=", "=", "<>" -> //System.out.println("Relation: " + value);
                    "decimal";
            default -> "boolean";
        };
    }

}
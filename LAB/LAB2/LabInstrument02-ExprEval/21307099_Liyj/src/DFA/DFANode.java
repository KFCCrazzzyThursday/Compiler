package DFA;

import token.Function.Function;
import token.Operator.BinaryOperatorToken;
import token.Operator.TernaryOperatorToken;
import token.Operator.UnaryOperatorToken;
import token.Symbol.DelimiterToken;
import token.Symbol.ParenthesisToken;
import token.Token;
import token.Value.BooleanToken;
import token.Value.DecimalToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFANode {
    private final int state;
    private final boolean isAccept;
    private final Map<Character, List<Integer>> edges;
    private final String type;
    public DFANode(int state, boolean isAccept, String type) {
        this.state = state;
        this.isAccept = isAccept;
        this.edges = new HashMap<>();
        this.type = type;
    }

    public void addEdge(char symbol, int toState) {
        edges.computeIfAbsent(symbol, k -> new ArrayList<>()).add(toState);
        // 检查边的添加
        //System.out.println("Adding edge from state " + state + " to state " + toState + " with symbol " + symbol);
    }

    public int getState() {
        return state;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public Map<Character, List<Integer>> getEdges() {
        return edges;
    }

    public String getType() {
        return type;
    }

    public Token getToken(String name){
        return switch (type) {
            case "decimal" -> new DecimalToken(name);
            case "boolean" -> new BooleanToken(name);
            case "ternary" -> new TernaryOperatorToken(name);
            case "arithmetic", "relation" -> new BinaryOperatorToken(name);
            case "unary" -> new UnaryOperatorToken(name);
            case "comma" -> new DelimiterToken(name);
            case "parenthesis" -> new ParenthesisToken(name);
            case "function" -> new Function(name);
            default -> null;
        };
    }
}
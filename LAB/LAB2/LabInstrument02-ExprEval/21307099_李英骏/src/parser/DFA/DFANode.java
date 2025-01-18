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

/**
 * 这个类表示DFA中的一个节点
 */
public class DFANode {
    private final int state;
    private final boolean isAccept;
    private final Map<Character, List<Integer>> edges;
    private final String type;

    /**
     * 构造一个DFA节点对象
     *
     * @param state    节点状态
     * @param isAccept 是否为接受状态
     * @param type     节点类型
     */
    public DFANode(int state, boolean isAccept, String type) {
        this.state = state;
        this.isAccept = isAccept;
        this.edges = new HashMap<>();
        this.type = type;
    }

    /**
     * 为节点添加一条边
     *
     * @param symbol   边上的符号
     * @param toState  边的目标状态
     */
    public void addEdge(char symbol, int toState) {
        edges.computeIfAbsent(symbol, k -> new ArrayList<>()).add(toState);
        // 检查边的添加
        // System.out.println("Adding edge from state " + state + " to state " + toState + " with symbol " + symbol);
    }

    /**
     * 获取节点的状态
     *
     * @return 节点状态
     */
    public int getState() {
        return state;
    }

    /**
     * 判断节点是否为接受状态
     *
     * @return 如果是接受状态，则返回true；否则返回false
     */
    public boolean isAccept() {
        return isAccept;
    }

    /**
     * 获取节点的边集合
     *
     * @return 边集合
     */
    public Map<Character, List<Integer>> getEdges() {
        return edges;
    }

    /**
     * 获取节点的类型
     *
     * @return 节点类型
     */
    public String getType() {
        return type;
    }

    /**
     * 根据节点类型获取相应的令牌
     *
     * @param name 令牌名称
     * @return 对应的令牌
     */
    public Token getToken(String name) {
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

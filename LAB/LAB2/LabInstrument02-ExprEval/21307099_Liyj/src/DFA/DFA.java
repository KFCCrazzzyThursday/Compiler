package DFA;

import token.Operator.UnaryOperatorToken;
import token.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFA {
    private int state;
    private final Map<Integer, DFANode> nodes;

    public DFA() {
        this.state = 0;
        this.nodes = new HashMap<>();
        init();
    }

    public static void main(String[] args) {
        DFA dfa = new DFA();
        // 打印 Graphviz 代码
        System.out.println(dfa.toGraphviz());
    }
    public void reset(){
        this.state = 0;
    }
    public int getState(){
        return this.state;
    }
    private void init() {
        // 添加所有节点
        int[] acceptStates = {1, 3, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 21, 22, 23, 24, 25, 28, 32, 33, 34, 35, 39, 44, 45};
        for (int i = 0; i <= 45; i++) {
            String type = getType(i);
            addNode(i, contains(acceptStates, i), type);
        }
        // 添加所有边
        addEdge(0, 1, '0');
        addEdge(0, 1, '1');
        addEdge(0, 1, '2');
        addEdge(0, 1, '3');
        addEdge(0, 1, '4');
        addEdge(0, 1, '5');
        addEdge(0, 1, '6');
        addEdge(0, 1, '7');
        addEdge(0, 1, '8');
        addEdge(0, 1, '9');
        addEdge(0, 7, '?');
        addEdge(0, 8, '^');
        addEdge(0, 9, '|');
        addEdge(0, 10, ':');
        addEdge(0, 11, '=');
        addEdge(0, 12, '!');
        addEdge(0, 13, '+');
        addEdge(0, 14, '&');
        addEdge(0, 15, '(');
        addEdge(0, 16, '-');
        addEdge(0, 17, ')');
        addEdge(0, 18, '*');
        addEdge(0, 19, 's');
        addEdge(0, 22, '/');
        addEdge(0, 23, '<');
        addEdge(0, 26, 'c');
        addEdge(0, 29, 'm');
        addEdge(0, 34, '>');
        addEdge(0, 36, 't');
        addEdge(0, 36, 'T');
        addEdge(0, 40, 'f');
        addEdge(0, 40, 'F');
        addEdge(0, 45, ',');

        addEdge(1, 2, '.');
        addEdge(2, 3, '0');
        addEdge(2, 3, '1');
        addEdge(2, 3, '2');
        addEdge(2, 3, '3');
        addEdge(2, 3, '4');
        addEdge(2, 3, '5');
        addEdge(2, 3, '6');
        addEdge(2, 3, '7');
        addEdge(2, 3, '8');
        addEdge(2, 3, '9');
        addEdge(3, 4, 'E');
        addEdge(3, 4, 'e');
        addEdge(4, 5, '+');
        addEdge(4, 5, '-');
        addEdge(5, 6, '0');
        addEdge(5, 6, '1');
        addEdge(5, 6, '2');
        addEdge(5, 6, '3');
        addEdge(5, 6, '4');
        addEdge(5, 6, '5');
        addEdge(5, 6, '6');
        addEdge(5, 6, '7');
        addEdge(5, 6, '8');
        addEdge(5, 6, '9');
        addEdge(1, 1, '0');
        addEdge(1, 1, '1');
        addEdge(1, 1, '2');
        addEdge(1, 1, '3');
        addEdge(1, 1, '4');
        addEdge(1, 1, '5');
        addEdge(1, 1, '6');
        addEdge(1, 1, '7');
        addEdge(1, 1, '8');
        addEdge(1, 1, '9');
        addEdge(3, 3, '0');
        addEdge(3, 3, '1');
        addEdge(3, 3, '2');
        addEdge(3, 3, '3');
        addEdge(3, 3, '4');
        addEdge(3, 3, '5');
        addEdge(3, 3, '6');
        addEdge(3, 3, '7');
        addEdge(3, 3, '8');
        addEdge(3, 3, '9');
        addEdge(6, 6, '0');
        addEdge(6, 6, '1');
        addEdge(6, 6, '2');
        addEdge(6, 6, '3');
        addEdge(6, 6, '4');
        addEdge(6, 6, '5');
        addEdge(6, 6, '6');
        addEdge(6, 6, '7');
        addEdge(6, 6, '8');
        addEdge(6, 6, '9');
        addEdge(1, 4, 'E');
        addEdge(1, 4, 'e');
        addEdge(4, 6, '0');
        addEdge(4, 6, '1');
        addEdge(4, 6, '2');
        addEdge(4, 6, '3');
        addEdge(4, 6, '4');
        addEdge(4, 6, '5');
        addEdge(4, 6, '6');
        addEdge(4, 6, '7');
        addEdge(4, 6, '8');
        addEdge(4, 6, '9');

        addEdge(29, 30, 'i');
        addEdge(29, 31, 'a');
        addEdge(30, 32, 'n');
        addEdge(31, 33, 'x');
        addEdge(23, 24, '=');
        addEdge(23, 25, '>');
        addEdge(34, 35, '=');
        addEdge(36, 37, 'r');
        addEdge(37, 38, 'u');
        addEdge(38, 39, 'e');
        addEdge(40, 41, 'a');
        addEdge(41, 42, 'l');
        addEdge(42, 43, 's');
        addEdge(43, 44, 'e');
        addEdge(19, 20, 'i');
        addEdge(20, 21, 'n');
        addEdge(26, 27, 'o');
        addEdge(27, 28, 's');
    }

    private static String getType(int i) {
        String type = "InnerPoint";
        if(i == 1 || i == 3 || i == 6){
            type = "decimal";
        }else if(i == 39 || i == 44){
            type = "boolean";
        }else if(i == 7 || i == 10){
            type = "ternary";
        } else if (i == 8 || i == 13 || i ==16 || i ==18 || i == 22) {
        type = "arithmetic";
        }else if(i == 9 || i ==11 || i == 14 ||  i == 23 || i == 24 || i == 25 || i ==34 || i ==35){
            type = "relation";
        }else if(i == 12){
            type = "unary";
        }else if(i == 45){
            type = "comma";
        }else if (i==15 || i == 17){
            type = "parenthesis";
        }
        else{
            type = "function";
        }
        return type;
    }

    public void addNode(int state, boolean isAccept, String type) {
        nodes.put(state, new DFANode(state, isAccept, type));
    }

    public void addEdge(int fromState, int toState, char symbol) {
        nodes.get(fromState).addEdge(symbol, toState);
    }

    public String toGraphviz() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph finite_state_machine {\n");
        sb.append("\tsize=\"20,7\";\n");
        sb.append("\tnode [shape = doublecircle]; ");
        for (DFANode node : nodes.values()) {
            if (node.isAccept()) {
                sb.append(node.getState()).append(" ");
            }
        }
        sb.append(";\n");
        sb.append("\tnode [shape = circle];\n");
        for (DFANode node : nodes.values()) {
            // 打印当前节点的边信息 调试用
            // System.out.println("Node " + node.getState() + " edges: " + node.getEdges());
            for (Map.Entry<Character, List<Integer>> edge : node.getEdges().entrySet()) {
                for (int targetState : edge.getValue()) {
                    sb.append("\t").append(node.getState()).append(" -> ").append(targetState)
                            .append(" [label=\"").append(edge.getKey()).append("\"];\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    private static boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    public String step(char current_element, char lookahead) {
        if (!nodes.get(state).getEdges().containsKey(current_element)) {
            return "exception";
        }
        state = nodes.get(state).getEdges().get(current_element).get(0);
        DFANode tempState = nodes.get(state);
        if (!tempState.getEdges().containsKey(lookahead) && tempState.isAccept() || lookahead == '$') {
            if (!tempState.isAccept()) {
                return "exception";
            }
            return tempState.getType();
        }
        return "valid";
    }
    public Token getToken(String name, boolean isUnary){
        if(isUnary){
            return new UnaryOperatorToken(name);
        }
        return nodes.get(state).getToken(name);
    }

}

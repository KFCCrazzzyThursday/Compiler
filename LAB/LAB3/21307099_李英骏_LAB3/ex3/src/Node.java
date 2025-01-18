/**
 * 表示一个带有名称和类型的节点。
 */
/* 5 - 46 行为GPT生成 */
public class Node {

    /**
     * 节点的名称。
     */
    public String name;

    /**
     * 节点的类型。
     */
    public String type;

    /**
     * 使用指定的名称和类型初始化节点。
     *
     * @param name 节点的名称。
     * @param type 节点的类型。
     */
    public Node(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * 使用默认值初始化节点。
     * 名称和类型都将被设置为空字符串。
     */
    public Node() {
        this.name = "";
        this.type = "";
    }

    /**
     * 复制构造函数，使用另一个节点的值初始化新节点。
     *
     * @param n 要复制的节点。
     */
    public Node(Node n) {
        this.name = n.name;
        this.type = n.type;
    }
}
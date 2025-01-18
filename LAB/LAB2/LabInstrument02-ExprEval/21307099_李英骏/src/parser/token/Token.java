package token;

/**
 * 这个接口表示一个token，用于解析和处理表达式中的各种元素
 */
public interface Token {

    /**
     * 获取token的值
     *
     * @return token的值
     */
    String getValue();

    /**
     * 获取token的类型
     *
     * @return token的类型
     */
    String getType();

    /**
     * 获取token的优先级
     *
     * @return token的优先级
     */
    int getPriority();

    /**
     * 判断token是否为终结符
     *
     * @return 如果是终结符则返回true，否则返回false
     */
    boolean getTerminal();
}

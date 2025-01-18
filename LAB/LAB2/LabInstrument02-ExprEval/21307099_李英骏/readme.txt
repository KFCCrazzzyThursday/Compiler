李英骏 21307099 liyj323@mail2.sysu.edu.cn

其他说明:
我本周感冒发烧,晚上很早睡觉. 因此迟交了几天,非常抱歉!

本人新增的文件结构说明:

|   build.bat(有改动)
|   design.pdf(报告)
|   mytest.bat
|   readme.txt
|
|
+---src
|   |   .DS_Store
|   |
|   \---parser
|       |   Calculator.java
|       |   Parser.java
|       |   Reducer.java
|       |
|       +---arithmetic
|       |       arithmetic_binary_operator.java
|       |       arithmetic_function.java
|       |       arithmetic_identity.java
|       |       arithmetic_ternary_operator.java
|       |       arithmetic_unary_operator.java
|       |
|       +---DFA
|       |       DFA.java
|       |       DFANode.java
|       |
|       +---scanner
|       |       Scanner.java
|       |
|       +---test
|       |       ParserTest.java
|       |
|       \---token
|           |   OperatorPrecedenceTable.java
|           |   Token.java
|           |
|           +---Function
|           |       Function.java
|           |
|           +---Operator
|           |       BinaryOperatorToken.java
|           |       OperatorToken.java
|           |       TernaryOperatorToken.java
|           |       UnaryOperatorToken.java
|           |
|           +---Symbol
|           |       DelimiterToken.java
|           |       ParenthesisToken.java
|           |       SymbolToken.java
|           |
|           \---Value
|                   BooleanToken.java
|                   DecimalToken.java
|                   ValueToken.java
|
\---testcases
        mytest.xml
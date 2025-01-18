import java.io.*;
import exceptions.*;

/**
 * 主类 Main 处理输入文件并使用 OberonScanner 和 Parser 进行词法和语法分析。
 */
public class Main {

    /**
     * 程序的入口点。
     *
     * @param args 命令行参数，包含要处理的文件名列表。
     */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            /* 16 - 28 行为GPT生成 */
            OberonScanner oberonscanner = null;
            try {
                oberonscanner = new OberonScanner(new java.io.FileReader(args[i]));
            } catch (FileNotFoundException ex) {
                // 词法分析器错误
                ex.printStackTrace();
            }
            Parser p = new Parser(oberonscanner);
            System.out.print(args[i] + ":");
            try {
                p.parse();
            } catch (Exception ex) {
                // 语法分析错误
                int line = oberonscanner.getLine() + 1;
                int column = oberonscanner.getColumn() + 1;
                System.out.println("# Error occurs at LINE " + line + " COL " + column + " " + ex + "\n");
            }
        }
    }
}

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 主类 Main 处理输入文件并使用 OberonScanner 进行词法分析。
 */
/* 8 - 41 行为GPT生成 */
public class Main {
    /**
     * 程序的入口点。
     * @param args 命令行参数，包含要处理的文件名列表。
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("The file coded by Oberon-0 is needed.");
            return;
        }

        for (String fileName : args) {
            processFile(fileName);
        }
    }

    /**
     * 处理给定的文件，使用 OberonScanner 进行词法分析。
     * @param fileName 要处理的文件名。
     */
    private static void processFile(String fileName) {
        try (FileInputStream stream = new FileInputStream(fileName);
             Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            OberonScanner scanner = new OberonScanner(reader);
            int token;
            do {
                token = scanner.yylex();
            } while (token != Symbol.EOF);
        } catch (FileNotFoundException e) {
            System.out.println("The file is not found: " + fileName);
        } catch (IOException e) {
            System.out.println("IO error scanning file: " + fileName);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected exception:");
            e.printStackTrace();
        }
    }
}

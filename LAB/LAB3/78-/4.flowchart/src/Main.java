import java.io.*;

public class Main {
	public static void main(String[] argv) throws Exception{
		for (int i = 0; i < argv.length; i++) {
			Lexer m_lexer = new Lexer(new java.io.FileReader(argv[i]));
			Parser parser = new Parser(m_lexer);
			System.out.println(argv[i] + ":");
			try {
				parser.parser();
			} catch (Exception ex) {
				int line = m_lexer.get_line()+1;
				int column = m_lexer.get_column() +1;
				System.out.println("Error position : Line "+line+"  Column "+ column + " " + ex.getMessage() +"\n");
			}
		}		
	}

}

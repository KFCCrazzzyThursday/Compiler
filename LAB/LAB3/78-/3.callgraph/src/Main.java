import java.io.*;
import exceptions.*;

/**
 * The main class
 *
 */
public class Main {
	/**
	 */
	public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            Lexer lexer = null;
            try {
                lexer = new Lexer(new java.io.FileReader(args[i]));
            } catch (FileNotFoundException ex) {
                //lexer error
                ex.printStackTrace();
            }
			Parser p = new Parser(lexer);
            System.out.print(args[i]+":");
            try {	
                p.parse();
            }
            catch(Exception ex){
                //parse error
				int line = lexer.getLine()+1;
				int column = lexer.getColumn() +1;
                System.out.println("Error position : Line " + line + "  Column "+ column + " " + ex +"\n");
                
            }
        }

	}

}

import exceptions.*;
import java.util.*;
import flowchart.*;
import java.io.*;

public class Parser 
{
	/**
	 * The private Stack of statement.
	 */
	public Stack<StatementSequence> m_sign;
	
	public Vector<Type>	m_caller;
	public Vector<Type>	m_callee;
	public Vector<Type> m_typeV;
	public Lexer m_lexer;
	public Symbol m_sym;
	int LeftParen;
	int RightParen;
	
	/**
	 * The instance of a parser
	 */
	flowchart.Module m_testModule;
	
	/**
	 * The instance of a procedure
	 */
	Procedure m_proc;

	/**
	 * The instance of a WHILE statement
	 */
	WhileStatement m_whileStmt;

	/**
	 * The instance of a IF statement
	 */
	IfStatement m_ifStmt;  
	
	public Parser(Lexer lexer) throws Exception {
		m_sign = new Stack<StatementSequence>(); 
		
		LeftParen = 0;
		RightParen = 0;
		m_callee = new Vector<Type>();
		m_typeV = new Vector<Type>();
		m_caller = new Vector<Type>();
		
		m_testModule = null;
		m_proc = null;	
		m_whileStmt = null;
		m_ifStmt = null;
		
		this.m_lexer = lexer;

	}
	

	public boolean parser()throws Exception {
		String name1, name2;
		
		m_sym = nextNode();
		if(m_sym.m_sym != Node.MODULE)
			throw new SyntacticException();

		m_sym = nextNode();
		name1 = m_sym.m_name;
		m_testModule = new flowchart.Module(name1);
		System.out.println("Parsing " + name1 + " ...");

		m_sym = nextNode();
		if(m_sym.m_sym != Node.SEMI)
			throw new SyntacticException();

		m_sym = nextNode();
		declarations();

		if (m_sym.m_sym == Node.BEGIN){
			module();
		}

		if(m_sym.m_sym != Node.END)
			throw new SyntacticException();
		
		m_sym = nextNode();
		name2 = m_sym.m_name;
		if (name1.equals(name2) == false)
			throw new ModuleNameMismatchedException();
		
		m_sym = nextNode();
		if(m_sym.m_sym != Node.PERIOD)
			throw new SyntacticException();
		
		if (LeftParen < RightParen)
			throw new MissingLeftParenthesisException();
		else if (LeftParen > RightParen)
			throw new MissingRightParenthesisException();

		int name_match = 0;
		for (int i=0; i<m_callee.size(); i++) {
			for (int j=0; j<m_caller.size(); j++) {
				if (m_callee.elementAt(i).m_name.equals(m_caller.elementAt(j).m_name)) {
					if (m_callee.elementAt(i).m_id != m_caller.elementAt(j).m_id)
						throw new MissingOperandException();

					for (int k=0; k<m_callee.elementAt(i).m_id; k++)
						if (m_callee.elementAt(i).m_recordElement.elementAt(k).m_id !=
							m_caller.elementAt(j).m_recordElement.elementAt(k).m_id)
							throw new TypeMismatchedException();
					name_match = 1;
				}
			}
			if (name_match == 0)
				throw new SemanticException(" " + m_callee.elementAt(i).m_name + " is not declared");
			name_match = 0;
		}
		System.out.println("Parse success.");
		m_testModule.show();
		return true;
	}

	

	public void module() throws Exception {

		if(m_sym.m_sym != Node.BEGIN)
			throw new SyntacticException();
		
		m_proc = m_testModule.add("Main");
		
		statement();	
	}


	public void declarations() throws Exception {

		if(m_sym.m_sym != Node.CONST && m_sym.m_sym != Node.TYPE && m_sym.m_sym != Node.VAR &&
		   m_sym.m_sym != Node.PROCEDURE && m_sym.m_sym != Node.BEGIN && m_sym.m_sym != Node.END)
		   throw new SyntacticException();

		if (m_sym.m_sym == Node.CONST)
			const_declare();
		else if (m_sym.m_sym == Node.TYPE)
			type_declare();
		else if (m_sym.m_sym == Node.VAR)
			var_declare();
		else if (m_sym.m_sym == Node.PROCEDURE)
			procedure_declaration();
		if (m_sym.m_sym == Node.BEGIN || m_sym.m_sym == Node.END)
			return;
		else declarations();
	}
	

	public void procedure_declaration() throws Exception {

		String name1, name2;
		name1 = procedure_heading();
		
		if(m_sym.m_sym != Node.SEMI)
			throw new SyntacticException();
		
		m_sym = nextNode();
		name2 = procedure_body();
		
		if (name1.equals(name2) == false){
			throw new ProcedureNameMismatchedException();}
		
		m_sym = nextNode();
		if(m_sym.m_sym != Node.SEMI)
			throw new SyntacticException();
		
		m_sym = nextNode();
	}
	

	public String procedure_body() throws Exception {
		
		declarations();
		if (m_sym.m_sym == Node.BEGIN)
			proc_begin();
		
		if(m_sym.m_sym != Node.END)
			throw new SyntacticException();
		
		m_sym = nextNode();
		return m_sym.m_name;
	}
	
	public void proc_begin() throws Exception {
		if(m_sym.m_sym != Node.BEGIN)
			throw new SyntacticException();
		
		statement();
	}
	
	public String procedure_heading() throws Exception {
		Type func_p;
		String name;
		if(m_sym.m_sym != Node.PROCEDURE)
			throw new SyntacticException();
		
		m_sym = nextNode();
		name = new String (m_sym.m_name);
		
		m_sym = nextNode();
		if (m_sym.m_sym == Node.LPAREN) {
			func_p = formal_parameters(name);
			m_sym = nextNode();
		}
		else func_p = new Type();
		func_p.m_name = name;
		m_caller.addElement(func_p);
		
		m_proc = m_testModule.add(name);
		
		return name;
	}
	
	public Type formal_parameters(String name) throws Exception {
		if(m_sym.m_sym != Node.LPAREN)
			throw new SyntacticException();
		
		Type func_p = new Type(0, name, new Vector<Type>());
		
		m_sym = nextNode();
		if (m_sym.m_sym == Node.RPAREN)
			return func_p;
		
		fp_section(func_p);

		if(m_sym.m_sym != Node.RPAREN)  
			throw new SyntacticException();
	
		return func_p;
	}
	
	
	public void fp_section(Type func_p) throws Exception{
        while(true) {
		    Type fp_t;
		    Vector<String> fp_name = new Vector<String>();
		
		    if (m_sym.m_sym == Node.VAR)
			    m_sym = nextNode();
		
		    identifier_list(fp_name, m_sym);
		
		    if(m_sym.m_sym != Node.COLON)
			    throw new SyntacticException();
		
		    fp_t =  type_id();
		
		    for (int i=0; i<fp_name.size(); i++)
		    {
			    //the para just get the type, not the para name
			    func_p.m_recordElement.addElement(new Type(fp_t));     
			
			    fp_t.m_name = fp_name.elementAt(i);
			    m_typeV.addElement(new Type(fp_t));
		    }
		    func_p.m_id += fp_name.size();
		
		    m_sym = nextNode();
		    if (m_sym.m_sym == Node.SEMI)
		    {
			    m_sym = nextNode();
			    continue;
		    }
            else
                break;
        }
	}
	
	public void const_declare()throws Exception {
        while(true) {
		    Type tmp = new Type();
		    Symbol t;
		
		    m_sym = nextNode();
		    if (m_sym.m_sym != Node.IDENTIFIER)
			    return ;
		
		    tmp.m_name = m_sym.m_name;
		
		    m_sym = nextNode();
		    if(m_sym.m_sym != Node.EQ)
			    throw new SyntacticException();
		
		    t = expression();
		    tmp.m_id = t.m_sym;		
		    m_typeV.addElement(tmp);
		
		    if(m_sym.m_sym != Node.SEMI)
			    throw new SyntacticException();
		}
	}
	
	public void var_declare() throws Exception {
        while(true) {
		    Type tmp;
		    Vector<String> id_v = new Vector<String>();
		    m_sym = nextNode();
		    if (m_sym.m_sym != Node.IDENTIFIER)
			    return;
		
		    identifier_list(id_v, m_sym);  //
		
		    if(m_sym.m_sym != Node.COLON)
			    throw new SyntacticException();
		
		    tmp = type_id();  //
		    for (int i = 0; i < id_v.size(); i++) {
			    tmp.m_name = id_v.elementAt(i);
			    m_typeV.addElement(new Type(tmp));
		    }
		
		    m_sym = nextNode();
		    if(m_sym.m_sym != Node.SEMI)
			    throw new SyntacticException();
		}
	}
	
	public void type_declare()throws Exception {
        while(true) {
		    Type tmp;
		
		    m_sym = nextNode();
		    if (m_sym.m_sym != Node.IDENTIFIER)
			    return ;
		    String name = new String(m_sym.m_name);
		
		    m_sym = nextNode();
		    if(m_sym.m_sym != Node.EQ)
			    throw new SyntacticException();
		
		    tmp = type_id();
		    tmp.m_name = name;
		    m_typeV.addElement(tmp);
		
		    m_sym = nextNode();
		    if(m_sym.m_sym != Node.SEMI)
			    throw new SyntacticException();
		}
	}
	
	public Type type_id() throws Exception {
		m_sym = nextNode();
		if (m_sym.m_sym == Node.INTEGER)
			return new Type(1);		//int
		
		else if (m_sym.m_sym == Node.BOOLEAN)
			return new Type(2);		//bool
		
		else if (m_sym.m_sym == Node.IDENTIFIER) {
			for (int i=0; i<m_typeV.size(); i++) {
				if (m_sym.m_name.equals(m_typeV.elementAt(i).m_name))
					return m_typeV.elementAt(i);
			}
			throw new SemanticException("Error: varible ( " + m_sym.m_name + " ) is not declared!");
		}
		else if (m_sym.m_sym == Node.ARRAY)
			return array_type();
		
		else if (m_sym.m_sym == Node.RECORD)
			return record_type();
		
		else
			throw new SyntacticException();
	}

	public Type record_type() throws Exception {
		if(m_sym.m_sym != Node.RECORD)
			throw new SyntacticException();
		
		Type tmp = new Type(4);
		tmp.m_recordElement = new Vector<Type>();
		
		m_sym = nextNode();
		if (m_sym.m_sym == Node.END) 
			return tmp;
		else 							
			return field_list(tmp);
	}

	public Type field_list(Type field) throws Exception {
        while(true) {
		    Vector<String> tmp = new Vector<String>();
		    identifier_list(tmp, m_sym);	
		
		    if (m_sym.m_sym == Node.END)
			    break;
		
		    else if (m_sym.m_sym == Node.SEMI) {
			    m_sym = nextNode();
			    continue;
		    }  
		
		    if (tmp.isEmpty() == false) { 
			    if(m_sym.m_sym != Node.COLON)  
				    throw new SyntacticException();
			
			    Type t = type_id();
			    for (int i=0; i<tmp.size(); i++) {
				    t.m_name = tmp.elementAt(i);
				    field.m_recordElement.addElement(new Type(t));
			    }
		    }
		    m_sym = nextNode();
        }
		return field;
	}

	public Type array_type() throws Exception {
		if(m_sym.m_sym != Node.ARRAY)
			throw new SyntacticException();
		
		Type arr = new Type(3);  
		
		Symbol tmp = expression();
		if (tmp.m_sym != 1)
			throw new TypeMismatchedException();
		
		if(m_sym.m_sym != Node.OF)
			throw new SyntacticException();
		
		arr.m_arrayElement = new Type(type_id());
		return arr;
	}

	public void identifier_list(Vector<String> id_v, Symbol id) throws Exception {
        Symbol lid = id;
        while(true) {
		    if (lid.m_sym != Node.IDENTIFIER)
			    break;
		
		    id_v.addElement(lid.m_name);
		
		    m_sym = nextNode();
		    if (m_sym.m_sym != Node.COMMA)
			    break;
		    else {
			    m_sym = nextNode();
                lid = m_sym;
		    }
        }
	}
	
	public void statement() throws Exception {
        while(true) {
		    Type type_of_ap;
		    String name;
		    String ap = new String();
				
		    if (m_sym.m_sym == Node.END)
			    return;
			
		    m_sym = nextNode();
		    if (m_sym.m_sym == Node.WHILE)
			    while_statement();
		
		    else if (m_sym.m_sym == Node.IF)
			    if_statement();

            else if(m_sym.m_sym == Node.READ) {
		        String t;
		        Symbol expr = new Symbol(0);
			    m_sym = nextNode();
			    if (m_sym.m_sym != Node.LPAREN)
				    throw new MissingLeftParenthesisException();
			
			    expr = expression(); 
			
			    if (expr.m_sym == 0) 
				    throw new MissingOperatorException();
			
			    if (m_sym.m_sym != Node.RPAREN) {
					throw new MissingRightParenthesisException();
				}
			
			    t = new String("Read( " + expr.m_name + " )");
			
			    m_sym = nextNode();	
		        add_stmt(new PrimitiveStatement(t));   
            }
            else if(m_sym.m_sym == Node.WRITE) {
		        String t;
		        Symbol expr = new Symbol(0);
			    m_sym = nextNode();
			    if (m_sym.m_sym != Node.LPAREN)
				    throw new MissingLeftParenthesisException();
			
			    expr = expression();
			    if (expr.m_sym == 0)
				    throw new MissingOperatorException();
			
			    if (m_sym.m_sym != Node.RPAREN)
				    throw new MissingRightParenthesisException();
				
			    t = new String("Write( "+expr.m_name+" )");
			    m_sym = nextNode();
		        add_stmt(new PrimitiveStatement(t));   
            }
            else if(m_sym.m_sym == Node.WRITELN) {
		        String t;
		        Symbol expr = new Symbol(0);
			    m_sym = nextNode();		
			    if (m_sym.m_sym == Node.LPAREN) { 
				
				    expr = expression();	
				
				    if (m_sym.m_sym != Node.RPAREN)
					    throw new MissingRightParenthesisException();
				
				    m_sym = nextNode();	
				    t = new String("Write("+expr.m_name+")");
			    }
			    else
				    t = new String("Writeln");
		        add_stmt(new PrimitiveStatement(t));   
            }
			
		    else if (m_sym.m_sym == Node.IDENTIFIER) {
			    name = new String (m_sym.m_name);
			
			    m_sym = nextNode();
			    if (m_sym.m_sym == Node.LPAREN || m_sym.m_sym == Node.SEMI) {
                    // procedure call
				    type_of_ap = new Type(0, name, new Vector<Type>());
				
				    if (m_sym.m_sym == Node.LPAREN) {  
					    ap = actual_parameters(type_of_ap);
					    m_sym = nextNode();
				    }
				    m_callee.addElement(type_of_ap);
				
				    String t = new String(name+"( "+ap+" )");
				    add_stmt(new PrimitiveStatement(t));
			    }
			    else  {
                    // assignment
                    assign(name);
                }
		    }
		
		    if (m_sym.m_sym == Node.ELSE || m_sym.m_sym == Node.ELSIF)
			    break;
		
		    //statement sequence
		    if(m_sym.m_sym == Node.SEMI)
			    continue;
            else
                break;
        }
	}

	
	public void assign(String name) throws Exception {
		Symbol l, r;
		Type id_t;
		
		l = new Symbol(0, new String());
		for (int i=0; i<m_typeV.size(); i++) {
			if (m_typeV.elementAt(i).m_name.equals(name)) {
				id_t = m_typeV.elementAt(i);
				selector(id_t, l, 0);
				break;
			}
		}
		if (l.m_sym == 0)
			throw new SemanticException("varible ( "+ name +" ) is not declared!");
		else {
			if (m_sym.m_sym != Node.ASSIGN)
				throw new SyntacticException();
			
			r = expression();
			
			if (l.m_sym != r.m_sym)
				throw new TypeMismatchedException();
				
			add_stmt(new PrimitiveStatement(name + " := " + r.m_name));
		}	
	}

	public String actual_parameters(Type ap_type) throws Exception{
		Symbol expr;
		try {
			//actual_parameters is empty
			expr = expression();
		}
		catch (Exception ex) {
			return "";
		}
		ap_type.m_recordElement.addElement(new Type(expr.m_sym));
		ap_type.m_id++;
		
		if (m_sym.m_sym == Node.RPAREN)
			return expr.m_name;
		
		else if (m_sym.m_sym == Node.COMMA) {
			expr.m_name += ", ";
			expr.m_name += actual_parameters(ap_type);
		}
		else throw new SyntacticException();
		
		return expr.m_name;
	}

	public void while_statement() throws Exception {
		Symbol expr;
		
		if (m_sym.m_sym != Node.WHILE)
				throw new SyntacticException();
		
		expr = expression();
		
		if (m_sym.m_sym != Node.DO)
				throw new SyntacticException();
		
		m_whileStmt = new WhileStatement(expr.m_name);
		add_stmt(m_whileStmt);
		
		m_sign.push(m_whileStmt.getLoopBody());	
		statement();	
		m_sign.pop();	
		
		if (m_sym.m_sym != Node.END)
				throw new SyntacticException();
		
		m_sym = nextNode();
	}

	
	public void if_statement() throws Exception {
		Symbol expr;
		
		if (m_sym.m_sym != Node.IF)
			throw new MissingLeftParenthesisException();
		
		expr = expression();  
		
		if (m_sym.m_sym != Node.THEN)
			throw new MissingLeftParenthesisException();
		
		m_ifStmt = new IfStatement(expr.m_name);
		add_stmt(m_ifStmt);

		m_sign.push(m_ifStmt.getFalseBody());
		m_sign.push(m_ifStmt.getTrueBody());
		
		statement(); 
		m_sign.pop();	
		
		if (m_sym.m_sym == Node.ELSIF)
			elsif_statement();
		else if (m_sym.m_sym == Node.ELSE)
			else_statement();
		
		if (m_sym.m_sym == Node.END)	
			m_sym = nextNode();

		m_sign.pop();		
	}

	public void elsif_statement() throws Exception {
		
		if (m_sym.m_sym != Node.ELSIF)
			throw new MissingLeftParenthesisException();
		
		Symbol expr;
		expr = expression();
		
		IfStatement elsif = new IfStatement(expr.m_name);
		add_stmt(elsif);	
		m_sign.pop();		
		
		m_sign.push(elsif.getFalseBody());
		m_sign.push(elsif.getTrueBody());	
		
		if (m_sym.m_sym != Node.THEN)
				throw new MissingLeftParenthesisException();
		
		statement();  
		m_sign.pop();		
		if (m_sym.m_sym == Node.ELSIF)
			elsif_statement();
		else return;
	}
	
	public void else_statement() throws Exception {	
		if (m_sym.m_sym != Node.ELSE)
			throw new MissingLeftParenthesisException();
		
		statement();
		
		if (m_sym.m_sym != Node.END)
			throw new MissingLeftParenthesisException();
	}

	public Symbol expression() throws Exception {
	
		Symbol expr = new Symbol(2);
		expr = simple_expression();
		
		int t = m_sym.m_sym;
		if (t==Node.EQ || t==Node.NEQ || t==Node.LT ||
				t==Node.LE || t==Node.GT || t==Node.GE ) {
			if (t == Node.EQ)	expr.m_name += " = ";
			if (t == Node.NEQ) expr.m_name += " # ";
			if (t == Node.LT)	expr.m_name += " &lt ";
			if (t == Node.LE)	expr.m_name += " &lt = ";
			if (t == Node.GT)	expr.m_name += " &gt ";
			if (t == Node.GE)	expr.m_name += " &gt = ";
			expr.m_name = expr.m_name.toString() + simple_expression().m_name.toString();
			expr.m_sym = 2;
		}
		return expr;
	}

	public Symbol simple_expression() throws Exception {
		
		Symbol expr = new Symbol(2);     
		expr = term();
		int t = m_sym.m_sym;
		if (t==Node.PLUS || t==Node.MINUS || t==Node.OR) {
			if (t == Node.PLUS)	expr.m_name += "+";
			if (t == Node.MINUS)	expr.m_name += "-";
			if (t == Node.OR) {
				expr.m_sym = 2;
				expr.m_name += "OR";
			}
			expr.m_name += simple_expression().m_name;
		}
		return expr;
	}

	public Symbol term() throws Exception {
		
		Symbol expr = new Symbol(2);
		expr = factor();
		
		int t = m_sym.m_sym;
		if (t==Node.TIMES || t==Node.DIVIDE || t==Node.MOD || t==Node.AND) {
			if (expr.m_sym != 2 && t==Node.AND)  
				throw new TypeMismatchedException();
				
			if (expr.m_sym != 1 && (t==Node.TIMES || t==Node.DIVIDE || t==Node.MOD)) 
				throw new TypeMismatchedException();
				
			if (t == Node.TIMES)	expr.m_name += " * ";
			if (t == Node.DIVIDE)	expr.m_name += " DIV ";
			if (t == Node.MOD)		expr.m_name += " MOD ";
			
			if (t == Node.AND) {
				expr.m_name += " & ";
				expr.m_sym = 2;
			}
			expr.m_name += term().m_name;
		}
		else if(t==Node.IDENTIFIER || t==Node.NUMBER || t==Node.BOOLEAN) 
			throw new MissingOperatorException();
		
		return expr;
	}

	public Symbol factor() throws Exception {    
		
		Symbol sy = new Symbol(0, new String());  
		
		m_sym = nextNode();
		int sym = m_sym.m_sym;
		int neg = 0;
		
		if (sym==Node.PLUS || sym==Node.MINUS){  //term_head
			if(sym == Node.MINUS) {
				neg=1;
				sy = new Symbol(0, "-");
			}
			m_sym = nextNode();
			sym = m_sym.m_sym;
		}
		
		if (sym == Node.NUMBER) {
			if(neg == 0)
				sy = new Symbol(1, m_sym.m_name);
			else sy = new Symbol(1, "-" + m_sym.m_name);
			m_sym = nextNode();
		}
		
		else if (sym == Node.NOT) {
			if(neg == 1) throw new TypeMismatchedException(); 
			sy = factor();
			m_sym = nextNode();
		}
		
		else if (sym == Node.IDENTIFIER) {
			int i;
			for (i=0; i<m_typeV.size(); i++) {
				if (m_sym.m_name.equals(m_typeV.elementAt(i).m_name)) {
					selector(m_typeV.elementAt(i), sy, 0);
					break;
				}
			}
			if (i >= m_typeV.size())
				throw new SemanticException("varible ( "+ m_sym.m_name +" ) hasn't been declared!");
			if (sy.m_sym == 0) {
				sy = new Symbol(m_typeV.elementAt(i).m_id, m_typeV.elementAt(i).m_name);
				m_sym = nextNode();
			}
			
		}
		
		else if (sym == Node.LPAREN) {
			sy = expression();	
			
			if (m_sym.m_sym != Node.RPAREN)
				throw new MissingRightParenthesisException();
			
			if(neg == 0)
				sy.m_name = "( " + sy.m_name + " )";
		    else sy.m_name = "-( " + sy.m_name + " )";
			m_sym = nextNode();	
		}
		
		else if (sym==Node.PLUS || sym==Node.MINUS || sym==Node.TIMES || sym==Node.DIVIDE ||
				sym==Node.AND || sym==Node.OR || sym==Node.NOT || sym==Node.LT || 
				sym==Node.LE ||sym==Node.GT || sym==Node.GE || sym==Node.NEQ || sym==Node.EQ)
			throw new MissingOperandException();
		
		if (sy.m_sym == 0) throw new MissingOperandException();
		
		return sy;
	}

	public void selector(Type t, Symbol sy, int level) throws Exception {
		
		Symbol expr;
		if (t.m_name != null && level == 0)
			sy.m_name = sy.m_name + t.m_name;
		
		if (m_sym.m_sym == Node.IDENTIFIER) 
			m_sym = nextNode();
		
		if (m_sym.m_sym == Node.PERIOD) {
			int match = 0;
			
			m_sym = nextNode();
			for (int i = 0; i < t.m_recordElement.size(); i++) { 
				Type t1 = t.m_recordElement.elementAt(i);
				if (t1.m_name.equals(m_sym.m_name)) {
					match = 1;
					sy.m_name = sy.m_name + ("." + m_sym.m_name);
					selector(t1, sy, level + 1);
				}
			}
			if (match == 0)
				throw new SemanticException("varible ( "+ m_sym.m_name + " ) in RECORD " + t.m_name + " without declarations!");
		}
		else if (m_sym.m_sym == Node.LBRACKET) {
			if (t.m_id != 3)  //
				throw new SemanticException("The variable is not an array");
			
			expr = expression();
			
			if (expr.m_sym != 1) 
				throw new TypeMismatchedException();
			
			sy.m_name = sy.m_name + ("[" + expr.m_name + "]");
			
			t = t.m_arrayElement;
			
			m_sym = nextNode();
			if (m_sym.m_sym == Node.RBRACKET)
				m_sym = nextNode();

			selector(t, sy, level + 1);	 
			return ;	
		}
		else {
			sy.m_sym = t.m_id;	
		}
		
		if (m_sym.m_sym == Node.RBRACKET)
			m_sym = nextNode();
	}
	

	public void add_stmt(AbstractStatement t) {
		StatementSequence stmt;
		
		if (m_sign.isEmpty())
			m_proc.add(t);
		else {
			stmt = m_sign.peek();
			stmt.add(t);
		}	
	}

	public Symbol nextNode() throws Exception {
		Symbol tem;
		try {
			tem = m_lexer.nextNode();
			if (tem.m_sym == Node.LPAREN)
				LeftParen++;
			if (tem.m_sym == Node.RPAREN)
				RightParen++;
		}
		catch(Exception ex) {
			throw ex;
		}
		return tem;
	}
}

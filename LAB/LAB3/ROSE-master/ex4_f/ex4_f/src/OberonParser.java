import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

import exceptions.*;
import flowchart.IfStatement;
import flowchart.Module;
import flowchart.PrimitiveStatement;
import flowchart.Procedure;
import flowchart.StatementSequence;
import flowchart.WhileStatement;

public class OberonParser {

	public OberonScanner scanner;
	public Token lookahead;
	public Stack<HashMap<String, Variable>> varStack;
	public Stack<StatementSequence> stateStack;
	private Module mainModule;
	private Procedure curProcedure;
	private int curIndex;

	public OberonParser(OberonScanner oberonScanner) {
		this.scanner = oberonScanner;
		varStack = new Stack<>();
		stateStack = new Stack<>();
	}

	public void parse() throws OberonException, IOException {
		System.out.println("Begin parsering...");
		lookahead = scanner.next_token();
		HashMap<String, Variable> para = new HashMap<>();
		para.put("something_1", new Variable(IdType.INTEGER));
		Variable rw = new Variable(IdType.PROCEDURE, para);

		HashMap<String, Variable> para_2 = new HashMap<>();
		Variable wln = new Variable(IdType.PROCEDURE, para_2);

		HashMap<String, Variable> topMap = new HashMap<>();
		topMap.put("write", rw);
		topMap.put("read", rw);
		topMap.put("writeln", wln);
		varStack.push(topMap);

		module();
		System.out.println("Success...now show the flow chart...");
		mainModule.show();
	}

	private void module() throws OberonException, IOException {
		match(new Token(Symbol.MODULE, scanner.getLine(), scanner.getColumn(), "MODULE"));
		lookahead = scanner.next_token();
		match(new Token(Symbol.IDENTIFIER, scanner.getLine(), scanner.getColumn(), "IDENTIFIER"));
		String moduleName = (String) lookahead.getValue();
		mainModule = new Module(moduleName);
		System.out.println("Module name is " + moduleName);

		lookahead = scanner.next_token();
		match(new Token(Symbol.SEMI, scanner.getLine(), scanner.getColumn(), "MODULE"));

		lookahead = scanner.next_token();
		declaration();

		if (lookahead.getSymbol() == Symbol.BEGIN) {
			curProcedure = mainModule.add("Main");
			lookahead = scanner.next_token();
			statement();
		}

		if (lookahead.getSymbol() == Symbol.END) {
			lookahead = scanner.next_token();
			match(new Token(Symbol.IDENTIFIER, scanner.getLine(), scanner.getColumn(), "IDENTIFIER"));
			String endName = (String) lookahead.getValue();
			if (!endName.equals(moduleName)) {
				throw new MismatchedBlockIdException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			lookahead = scanner.next_token();
			match(new Token(Symbol.DOT, scanner.getLine(), scanner.getColumn(), "."));

			varStack.pop();
		} else {
			System.out.println("Missing end of module");
			throw new SyntacticException(lookahead.getYyline(), lookahead.getYycolumn());
		}
	}

	private void declaration() throws OberonException, IOException {
		HashMap<String, Variable> curMap = new HashMap<String, Variable>();
		HashMap<String, Variable> topMap = new HashMap<String, Variable>();
		if (!varStack.empty()) {
			topMap = varStack.peek();
			Iterator<Entry<String, Variable>> iter = topMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Variable> entry = iter.next();
				if (entry.getValue().getType() == IdType.PROCEDURE) {
					curMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		varStack.push(curMap);

		if (lookahead.getSymbol() == Symbol.CONST) {
			lookahead = scanner.next_token();
			const_declarations();
		}
		if (lookahead.getSymbol() == Symbol.TYPE) {
			lookahead = scanner.next_token();
			type_declarations();
		}
		if (lookahead.getSymbol() == Symbol.VAR) {
			lookahead = scanner.next_token();
			var_declarations();
		}
		if (lookahead.getSymbol() == Symbol.PROCEDURE) {
			lookahead = scanner.next_token();
			procedure_declaration();
		}

		HashMap<String, Variable> curMap_2 = varStack.pop();
		HashMap<String, Variable> topMap_2 = new HashMap<String, Variable>();
		if (!varStack.empty()) {
			topMap_2 = varStack.peek();
			Iterator<Entry<String, Variable>> iter = topMap_2.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Variable> entry = iter.next();
				if (curMap_2.get(entry.getKey()) == null) {
					curMap_2.put(entry.getKey(), entry.getValue());
				}
			}
		}
		varStack.push(curMap_2);
	}

	private void const_declarations() throws OberonException, IOException {
		if (lookahead.getSymbol() == Symbol.IDENTIFIER) {

			String idName = (String) lookahead.getValue();
			lookahead = scanner.next_token();
			match(new Token(Symbol.EQUAL, scanner.getLine(), scanner.getColumn(), "="));
			lookahead = scanner.next_token();
			Variable val = expression();

			if (val.getType() == IdType.INTEGER) {
				HashMap<String, Variable> curMap = varStack.peek();
				if (curMap.get(idName) != null) {
					throw new IdentifierConflictException(lookahead.getYyline(), lookahead.getYycolumn());
				} else {
					curMap.put(idName, val);
				}
				// System.out.println("push: " + idName);
			} else {
				System.out.println("Const variable must be integer");
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			match(new Token(Symbol.SEMI, scanner.getLine(), scanner.getColumn(), ";"));

			lookahead = scanner.next_token();
			const_declarations();
		}
	}

	private void type_declarations() throws OberonException, IOException {
		if (lookahead.getSymbol() == Symbol.IDENTIFIER) {

			String idName = (String) lookahead.getValue();
			lookahead = scanner.next_token();
			match(new Token(Symbol.EQUAL, scanner.getLine(), scanner.getColumn(), "="));
			lookahead = scanner.next_token();
			Variable val = type();

			HashMap<String, Variable> curMap = varStack.peek();
			if (curMap.get(idName) != null) {
				throw new IdentifierConflictException(lookahead.getYyline(), lookahead.getYycolumn());
			} else {
				curMap.put(idName, val);
				// System.out.println("push: " + idName);
			}
			match(new Token(Symbol.SEMI, scanner.getLine(), scanner.getColumn(), ";"));

			lookahead = scanner.next_token();
			type_declarations();
		}
	}

	private void var_declarations() throws OberonException, IOException {
		ArrayList<String> id_list = identifier_list();
		if (!id_list.isEmpty()) {
			match(new Token(Symbol.COLON, scanner.getLine(), scanner.getColumn(), ":"));
			lookahead = scanner.next_token();
			Variable val = type();
			for (int i = 0; i < id_list.size(); i++) {
				HashMap<String, Variable> curMap = varStack.peek();
				if (curMap.get(id_list.get(i)) != null) {
					throw new IdentifierConflictException(lookahead.getYyline(), lookahead.getYycolumn());
				} else {
					curMap.put(id_list.get(i), val);

				}
				// System.out.println("push: " + id_list.get(i));
			}
			match(new Token(Symbol.SEMI, scanner.getLine(), scanner.getColumn(), ";"));
			lookahead = scanner.next_token();
			var_declarations();
		}
	}

	private void procedure_declaration() throws OberonException, IOException {
		HashMap<String, Variable> topMap = varStack.peek();
		HashMap<String, Variable> curMap = new HashMap<String, Variable>();
		HashMap<String, Variable> head = procedure_heading();
		Iterator<Entry<String, Variable>> iter = head.entrySet().iterator();
		String id = new String();
		while (iter.hasNext()) {
			Entry<String, Variable> entry = iter.next();
			if (entry.getValue().getType() == IdType.PROCEDURE) {
				id = entry.getKey();
				curMap.put(entry.getKey(), entry.getValue());
				break;
			}
		}
		if (topMap.get(id) != null) {
			throw new IdentifierConflictException(lookahead.getYyline(), lookahead.getYycolumn());
		} else {
			topMap.putAll(curMap);
		}
		String end_name = procedure_body(head, id);
		if (!id.equals(end_name)) {
			throw new MismatchedBlockIdException(lookahead.getYyline(), lookahead.getYycolumn());
		}

		if (lookahead.getSymbol() == Symbol.PROCEDURE) {
			lookahead = scanner.next_token();
			procedure_declaration();
		}
	}

	private String procedure_body(HashMap<String, Variable> head, String id) throws OberonException, IOException {

		declaration();
		HashMap<String, Variable> curMap = varStack.peek();
		HashMap<String, Variable> var = (HashMap<String, Variable>) head.get(id).getValue();
		Iterator<Entry<String, Variable>> iter = var.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Variable> entry = iter.next();
			String para_id = (entry.getKey().split("_"))[0];

			curMap.put(para_id, entry.getValue());

		}

		if (lookahead.getSymbol() == Symbol.BEGIN) {
			lookahead = scanner.next_token();
			curProcedure = mainModule.add(id);

			statement();
		}

		match(new Token(Symbol.END, scanner.getLine(), scanner.getColumn(), "END"));
		lookahead = scanner.next_token();
		match(new Token(Symbol.IDENTIFIER, scanner.getLine(), scanner.getColumn(), "IDENTIFIER"));
		String end_name = (String) lookahead.getValue();
		lookahead = scanner.next_token();
		match(new Token(Symbol.SEMI, scanner.getLine(), scanner.getColumn(), ";"));
		lookahead = scanner.next_token();
		varStack.pop();
		return end_name;
	}

	private HashMap<String, Variable> procedure_heading() throws OberonException, IOException {
		match(new Token(Symbol.IDENTIFIER, scanner.getLine(), scanner.getColumn(), "IDENTIFIER"));
		String proc_id = (String) lookahead.getValue();
		lookahead = scanner.next_token();
		HashMap<String, Variable> paraList = new HashMap<>();
		if (lookahead.getSymbol() == Symbol.LPARENTHESE) {
			paraList = formal_parameters();
		}
		HashMap<String, Variable> proc_head = new HashMap<>();
		proc_head.put(proc_id, new Variable(IdType.PROCEDURE, paraList));

		match(new Token(Symbol.SEMI, scanner.getLine(), scanner.getColumn(), ";"));
		lookahead = scanner.next_token();
		return proc_head;
	}

	private HashMap<String, Variable> formal_parameters() throws OberonException, IOException {
		HashMap<String, Variable> curMap = new HashMap<>();
		match(new Token(Symbol.LPARENTHESE, scanner.getLine(), scanner.getColumn(), "("));
		this.curIndex = 0;
		lookahead = scanner.next_token();
		HashMap<String, Variable> formal_para = fp_section();
		HashMap<String, Variable> fp_section = fp_section_key();
		curMap.putAll(formal_para);
		curMap.putAll(fp_section);
		match(new Token(Symbol.RPARENTHESE, scanner.getLine(), scanner.getColumn(), ")"));
		this.curIndex = 0;
		lookahead = scanner.next_token();
		return curMap;
	}

	private HashMap<String, Variable> fp_section() throws OberonException, IOException {
		HashMap<String, Variable> curMap = new HashMap<>();
		if (lookahead.getSymbol() == Symbol.VAR) {
			lookahead = scanner.next_token();
		}
		ArrayList<String> id_list = identifier_list();
		if (!id_list.isEmpty()) {
			match(new Token(Symbol.COLON, scanner.getLine(), scanner.getColumn(), ":"));
			lookahead = scanner.next_token();
			Variable val = type();
			for (int i = 0; i < id_list.size(); i++) {
				if (curMap.get(id_list.get(i) + "_" + curIndex + 1) != null) {
					throw new IdentifierConflictException(lookahead.getYyline(), lookahead.getYycolumn());
				} else {
					curMap.put(id_list.get(i) + "_" + (++curIndex), val);
				}
				// System.out.println("push: " + id_list.get(i));
			}
		}
		return curMap;
	}

	private HashMap<String, Variable> fp_section_key() throws OberonException, IOException {
		HashMap<String, Variable> curMap = new HashMap<>();
		if (lookahead.getSymbol() == Symbol.SEMI) {
			lookahead = scanner.next_token();
			HashMap<String, Variable> fp_sec = fp_section();
			if (!fp_sec.isEmpty()) {
				curMap.putAll(fp_sec);
				curMap.putAll(fp_section_key());
			}
		}
		return curMap;
	}

	private Variable type() throws OberonException, IOException {
		Variable type = null;
		if (lookahead.getSymbol() == Symbol.INTEGER) {
			lookahead = scanner.next_token();
			type = new Variable(IdType.INTEGER);
		} else if (lookahead.getSymbol() == Symbol.BOOLEAN) {
			lookahead = scanner.next_token();
			type = new Variable(IdType.BOOLEAN);
		} else if (lookahead.getSymbol() == Symbol.RECORD) {
			lookahead = scanner.next_token();
			type = record_type();
		} else if (lookahead.getSymbol() == Symbol.ARRAY) {
			lookahead = scanner.next_token();
			type = array_type();
		} else if (lookahead.getSymbol() == Symbol.IDENTIFIER) {
			String id = (String) lookahead.getValue();
			lookahead = scanner.next_token();
			HashMap<String, Variable> curMap = varStack.peek();
			Variable id_type = curMap.get(id);
			if (id_type == null) {
				throw new IllegalIdentifierException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			type = id_type;
		}
		return type;
	}

	private Variable array_type() throws OberonException, IOException {

		Variable var = expression();
		if (var.getType() != IdType.INTEGER) {
			throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
		}

		match(new Token(Symbol.OF, scanner.getLine(), scanner.getColumn(), "OF"));
		lookahead = scanner.next_token();

		return type();
	}

	private Variable record_type() throws OberonException, IOException {
		HashMap<String, Variable> record_tp = new HashMap<>();
		HashMap<String, Variable> field_lst = field_list();
		HashMap<String, Variable> field_lst_key = field_list_key();
		record_tp.putAll(field_lst);
		record_tp.putAll(field_lst_key);

		int rec_tp_size = record_tp.entrySet().size();
		int field_lst_size = field_lst.entrySet().size();
		int field_key_size = field_lst_key.entrySet().size();
		if (rec_tp_size != (field_key_size + field_lst_size)) {
			throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
		}
		match(new Token(Symbol.END, scanner.getLine(), scanner.getColumn(), "END"));
		lookahead = scanner.next_token();
		// System.out.println("recordType:"+record_tp);
		return new Variable(IdType.RECORD, record_tp);
	}

	private HashMap<String, Variable> field_list() throws OberonException, IOException {
		HashMap<String, Variable> field_lst = new HashMap<>();
		ArrayList<String> id_list = identifier_list();
		if (!id_list.isEmpty()) {
			match(new Token(Symbol.COLON, scanner.getLine(), scanner.getColumn(), ":"));
			lookahead = scanner.next_token();
			Variable val = type();
			for (int i = 0; i < id_list.size(); i++) {
				if (field_lst.get(id_list.get(i)) != null) {
					throw new IdentifierConflictException(lookahead.getYyline(), lookahead.getYycolumn());
				} else {
					field_lst.put(id_list.get(i), val);
				}

			}
		}
		return field_lst;
	}

	private HashMap<String, Variable> field_list_key() throws OberonException, IOException {
		HashMap<String, Variable> curMap = new HashMap<>();
		if (lookahead.getSymbol() == Symbol.SEMI) {
			lookahead = scanner.next_token();
			HashMap<String, Variable> field_lst = field_list();
			if (!field_lst.isEmpty()) {
				curMap.putAll(field_lst);
				curMap.putAll(field_list_key());
			}
		}
		return curMap;
	}

	private void statement() throws OberonException, IOException {
		if (lookahead.getSymbol() == Symbol.IF) {
			lookahead = scanner.next_token();
			if_statement();
		} else if (lookahead.getSymbol() == Symbol.WHILE) {
			lookahead = scanner.next_token();
			while_statement();
		} else if (lookahead.getSymbol() == Symbol.IDENTIFIER) {
			Variable sec = selector();

			if (lookahead.getSymbol() == Symbol.ASSIGN) {
				lookahead = scanner.next_token();
				Variable var = expression();
				if (sec.getType() != var.getType()) {
					throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
				}
				PrimitiveStatement statement = new PrimitiveStatement((String) sec.getValue() + ":="
						+ (String) var.getValue());
				if (stateStack.isEmpty()) {
					curProcedure.add(statement);
				} else {
					stateStack.peek().add(statement);
				}

			} else if (lookahead.getSymbol() == Symbol.LPARENTHESE) {
				PrimitiveStatement statement = new PrimitiveStatement((String) sec.getValue()
						+ procedure_call((String) sec.getValue()));
				if (stateStack.isEmpty()) {
					curProcedure.add(statement);
				} else {
					stateStack.peek().add(statement);
				}
			} else if (lookahead.getSymbol() == Symbol.ELSIF || lookahead.getSymbol() == Symbol.ELSE) {
				return;
			}
		}

		statement_key();
	}

	private void statement_key() throws OberonException, IOException {
		if (lookahead.getSymbol() == Symbol.SEMI) {
			lookahead = scanner.next_token();
			statement();
		}
	}

	private String procedure_call(String id) throws OberonException, IOException {
		String res = new String();
		match(new Token(Symbol.LPARENTHESE, scanner.getLine(), scanner.getColumn(), "("));
		lookahead = scanner.next_token();
		res += "(";

		ArrayList<Variable> act_para = actual_parameters();
		ArrayList<Variable> act_para_key = actual_parameters_key();
		ArrayList<Variable> val = new ArrayList<>();
		val.addAll(act_para);
		val.addAll(act_para_key);
		if (!val.isEmpty()) {
			res += (String) val.get(0).getValue();
			for (int i = 1; i < val.size(); i++) {
				res += ", " + (String) val.get(i).getValue();
			}
		}
		HashMap<String, Variable> topMap = varStack.peek();
		HashMap<String, Variable> parameter = new HashMap<>();
		Variable procedure = topMap.get(id);
		if (procedure == null) {
			throw new IllegalIdentifierException(lookahead.getYyline(), lookahead.getYycolumn());
		} else {
			try {
				parameter = (HashMap<String, Variable>) procedure.getValue();
			} catch (ClassCastException e) {
				System.out.println("This identifier is not a procedure!");
				throw new SyntacticException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			if (parameter.entrySet().size() != val.size()) {
				throw new ParameterMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			Iterator<Entry<String, Variable>> iter = parameter.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Variable> entry = iter.next();
				Integer index = Integer.parseInt((entry.getKey().split("_"))[1]);
				if (val.get(index - 1).getType() != entry.getValue().getType()) {
					throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
				}
			}
		}
		match(new Token(Symbol.RPARENTHESE, scanner.getLine(), scanner.getColumn(), ")"));
		lookahead = scanner.next_token();
		res += ")";
		return res;
	}

	private ArrayList<Variable> actual_parameters() throws OberonException, IOException {
		ArrayList<Variable> res = new ArrayList<>();
		if (lookahead.getSymbol() == Symbol.RPARENTHESE) {
			return res;
		}
		res.add(expression());
		return res;
	}

	private ArrayList<Variable> actual_parameters_key() throws OberonException, IOException {
		ArrayList<Variable> res = new ArrayList<>();
		if (lookahead.getSymbol() == Symbol.COMMA) {
			lookahead = scanner.next_token();
			ArrayList<Variable> paras = actual_parameters();
			if (!paras.isEmpty()) {
				res.addAll(paras);
				res.addAll(actual_parameters_key());
			}
		}
		return res;
	}

	private void if_statement() throws OberonException, IOException {
		Variable var = expression();

		if (var.getType() != IdType.BOOLEAN) {
			throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
		}

		match(new Token(Symbol.THEN, scanner.getLine(), scanner.getColumn(), "THEN"));
		lookahead = scanner.next_token();
		IfStatement statement = new IfStatement((String) var.getValue());
		if (stateStack.isEmpty()) {
			curProcedure.add(statement);
		} else {
			stateStack.peek().add(statement);
		}

		stateStack.push(statement.getTrueBody());
		statement();

		stateStack.pop();
		stateStack.push(statement.getFalseBody());

		if (lookahead.getSymbol() == Symbol.ELSIF) {
			lookahead = scanner.next_token();
			elseif_statement();
		}
		if (lookahead.getSymbol() == Symbol.ELSE) {
			lookahead = scanner.next_token();
			else_statement();
		}
		if (lookahead.getSymbol() == Symbol.END) {
			lookahead = scanner.next_token();
		}
		stateStack.pop();
	}

	private void elseif_statement() throws OberonException, IOException {
		Variable var = expression();

		if (var.getType() != IdType.BOOLEAN) {
			throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
		}
		match(new Token(Symbol.THEN, scanner.getLine(), scanner.getColumn(), "THEN"));
		lookahead = scanner.next_token();
		IfStatement statement = new IfStatement((String) var.getValue());
		if (stateStack.isEmpty()) {
			System.out.println("Missing if statement");
			throw new SyntacticException(lookahead.getYyline(), lookahead.getYycolumn());
		} else {
			stateStack.peek().add(statement);
		}

		stateStack.push(statement.getTrueBody());
		statement();

		stateStack.pop();
		stateStack.push(statement.getFalseBody());

		if (lookahead.getSymbol() == Symbol.ELSIF) {
			lookahead = scanner.next_token();
			elseif_statement();
		} else if (lookahead.getSymbol() == Symbol.ELSE) {
			lookahead = scanner.next_token();
			else_statement();
		}
		stateStack.pop();
	}

	private void else_statement() throws OberonException, IOException {
		statement();
	}

	private void while_statement() throws OberonException, IOException {
		Variable var = expression();

		if (var.getType() != IdType.BOOLEAN) {
			throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
		}
		match(new Token(Symbol.DO, scanner.getLine(), scanner.getColumn(), "DO"));
		lookahead = scanner.next_token();
		WhileStatement statement = new WhileStatement((String) var.getValue());
		if (stateStack.isEmpty()) {
			curProcedure.add(statement);
		} else {
			stateStack.peek().add(statement);
		}

		stateStack.push(statement.getLoopBody());
		statement();

		match(new Token(Symbol.END, scanner.getLine(), scanner.getColumn(), "END"));
		lookahead = scanner.next_token();
		stateStack.pop();
	}

	private ArrayList<String> identifier_list() throws OberonException, IOException {
		ArrayList<String> id_list = new ArrayList<>();
		if (lookahead.getSymbol() == Symbol.IDENTIFIER) {
			id_list.add((String) lookahead.getValue());

			lookahead = scanner.next_token();
			id_list.addAll(identifier_list_key());
		}
		return id_list;
	}

	private ArrayList<String> identifier_list_key() throws OberonException, IOException {
		ArrayList<String> id_list = new ArrayList<>();
		if (lookahead.getSymbol() == Symbol.COMMA) {
			lookahead = scanner.next_token();
			match(new Token(Symbol.IDENTIFIER, scanner.getLine(), scanner.getColumn(), "IDENTIFIER"));
			id_list.add((String) lookahead.getValue());

			lookahead = scanner.next_token();
			id_list.addAll(identifier_list_key());
		}
		return id_list;
	}

	private Variable expression() throws OberonException, IOException {
		Variable var = null;
		Variable sim_var = simple_expression();

		if (lookahead.getSymbol() == Symbol.EQUAL || lookahead.getSymbol() == Symbol.NOTEQ
				|| lookahead.getSymbol() == Symbol.BIG || lookahead.getSymbol() == Symbol.BIGEQ
				|| lookahead.getSymbol() == Symbol.LESS || lookahead.getSymbol() == Symbol.LESSEQ) {
			if (sim_var.getType() != IdType.INTEGER) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var = new Variable(IdType.BOOLEAN);
			var.setValue((String) sim_var.getValue());
			if (lookahead.getSymbol() == Symbol.LESS) {
				var.setValue((String) var.getValue() + "&lt;");
			} else if (lookahead.getSymbol() == Symbol.LESSEQ) {
				var.setValue((String) var.getValue() + "&lt;=");
			} else {
				var.setValue((String) var.getValue() + (String) lookahead.getValue());
			}
			lookahead = scanner.next_token();
			Variable simple_val = simple_expression();
			if (simple_val.getType() != IdType.INTEGER) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) simple_val.getValue());
		} else {
			var = sim_var;
		}
		return var;
	}

	private Variable simple_expression() throws OberonException, IOException {
		Variable var = new Variable(IdType.INTEGER, "");
		if (lookahead.getSymbol() == Symbol.ADD || lookahead.getSymbol() == Symbol.MINUS) {

			var.setValue((String) var.getValue() + (String) lookahead.getValue());
			lookahead = scanner.next_token();

			Variable term_var = term();
			if (term_var.getType() != IdType.INTEGER) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) term_var.getValue());
		} else {
			Variable term_var = term();
			var.setType(term_var.getType());
			var.setValue((String) var.getValue() + (String) term_var.getValue());
		}

		Variable simple_val = simple_expression_key();
		if (simple_val != null) {
			if (simple_val.getType() != var.getType()) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) simple_val.getValue());
		}

		return var;
	}

	private Variable simple_expression_key() throws OberonException, IOException {
		Variable var = null;
		if (lookahead.getSymbol() == Symbol.ADD || lookahead.getSymbol() == Symbol.MINUS) {
			var = new Variable(IdType.INTEGER, "");
			var.setValue((String) var.getValue() + (String) lookahead.getValue());
			lookahead = scanner.next_token();

			Variable term_var = term();
			if (term_var.getType() != IdType.INTEGER) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) term_var.getValue());

			Variable expr_key = simple_expression_key();
			if (expr_key != null) {
				if (expr_key.getType() != IdType.INTEGER) {
					throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
				}
				var.setValue((String) var.getValue() + (String) expr_key.getValue());
			}
		} else if (lookahead.getSymbol() == Symbol.OR) {
			var = new Variable(IdType.BOOLEAN, "");
			var.setValue((String) var.getValue() + (String) lookahead.getValue());
			lookahead = scanner.next_token();

			Variable term_var = term();
			if (term_var.getType() != IdType.BOOLEAN) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) term_var.getValue());

			Variable expr_key = simple_expression_key();
			if (expr_key != null) {
				if (expr_key.getType() != IdType.BOOLEAN) {
					throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
				}
				var.setValue((String) var.getValue() + (String) expr_key.getValue());
			}
		}
		return var;
	}

	private Variable term() throws OberonException, IOException {
		Variable var = new Variable(IdType.INTEGER, "");
		var = factor();
		Variable term_key = term_key();
		if (term_key != null) {
			if (var.getType() != term_key.getType()) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) term_key.getValue());
		}
		return var;
	}

	private Variable term_key() throws OberonException, IOException {
		Variable var = null;
		if (lookahead.getSymbol() == Symbol.MUL || lookahead.getSymbol() == Symbol.DIV
				|| lookahead.getSymbol() == Symbol.MOD) {
			var = new Variable(IdType.INTEGER, "");
			var.setValue((String) var.getValue() + (String) lookahead.getValue());
			lookahead = scanner.next_token();

			Variable term_var = factor();
			if (term_var.getType() != IdType.INTEGER) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) term_var.getValue());

			Variable key_var = term_key();
			if (key_var != null) {
				if (key_var.getType() != IdType.INTEGER) {
					throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
				}
				var.setValue((String) var.getValue() + (String) key_var.getValue());
			}
		} else if (lookahead.getSymbol() == Symbol.AND) {
			var = new Variable(IdType.BOOLEAN, "");
			var.setValue((String) var.getValue() + (String) lookahead.getValue());
			lookahead = scanner.next_token();

			Variable term_var = factor();
			if (term_var.getType() != IdType.BOOLEAN) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) term_var.getValue());

			Variable key_var = term_key();
			if (key_var != null) {
				if (key_var.getType() != IdType.BOOLEAN) {
					throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
				}
				var.setValue((String) var.getValue() + (String) key_var.getValue());
			}
		}
		return var;
	}

	private Variable factor() throws OberonException, IOException {
		Variable var = null;
		if (lookahead.getSymbol() == Symbol.NUMBER) {
			var = new Variable(IdType.INTEGER, "");
			var.setValue(lookahead.getValue().toString());
			lookahead = scanner.next_token();
		} else if (lookahead.getSymbol() == Symbol.LPARENTHESE) {
			var = new Variable(IdType.INTEGER, "");
			var.setValue((String) lookahead.getValue());
			lookahead = scanner.next_token();
			Variable expr = expression();
			var.setType(expr.getType());
			// System.out.println("factor:"+var);
			// System.out.println("factor_expr:"+expr);
			var.setValue((String) var.getValue() + (String) expr.getValue());
			match(new Token(Symbol.RPARENTHESE, scanner.getLine(), scanner.getColumn(), ")"));
			var.setValue((String) var.getValue() + (String) lookahead.getValue());
			lookahead = scanner.next_token();
		} else if (lookahead.getSymbol() == Symbol.NOT) {
			var = new Variable(IdType.BOOLEAN, "");
			var.setValue((String) lookahead.getValue());
			lookahead = scanner.next_token();
			Variable fac = factor();
			if (fac.getType() != IdType.BOOLEAN) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			var.setValue((String) var.getValue() + (String) fac.getValue());
		} else if (lookahead.getSymbol() == Symbol.IDENTIFIER) {
			var = selector();
		} else {
			System.out.println("Unexpected \"" + (String) lookahead.getValue() + "\"");
			throw new SyntacticException(lookahead.getYyline(), lookahead.getYycolumn());
		}

		return var;
	}

	private Variable selector() throws OberonException, IOException {
		Variable var = null;
		String id = (String) lookahead.getValue();
		HashMap<String, Variable> curMap = varStack.peek();
		Variable var_id = curMap.get(id);
		if (var_id == null) {
			throw new IllegalIdentifierException(lookahead.getYyline(), lookahead.getYycolumn());
		}
		lookahead = scanner.next_token();

		if (lookahead.getSymbol() == Symbol.LSQBRACKET || lookahead.getSymbol() == Symbol.DOT) {
			var = selector_key(id, var_id);
		} else {
			var = new Variable(var_id.getType(), id);
		}
		return var;
	}

	private Variable selector_key(String id, Variable type_id) throws OberonException, IOException {
		Variable var = type_id;
		// System.out.println(lookahead.getValue());
		if (lookahead.getSymbol() == Symbol.DOT) {
			HashMap<String, Variable> recMap = (HashMap<String, Variable>) type_id.getValue();
			match(new Token(Symbol.DOT, scanner.getLine(), scanner.getColumn(), "."));
			id += ".";
			lookahead = scanner.next_token();
			match(new Token(Symbol.IDENTIFIER, scanner.getLine(), scanner.getColumn(), "IDENTIFIER"));
			String rec_id = (String) lookahead.getValue();
			id += rec_id;
			lookahead = scanner.next_token();

			Variable rec_var = recMap.get(rec_id);
			if (rec_var == null) {
				throw new IllegalIdentifierException(lookahead.getYyline(), lookahead.getYycolumn());
			} else {
				var = rec_var;
			}
			if (lookahead.getSymbol() == Symbol.LSQBRACKET || lookahead.getSymbol() == Symbol.DOT) {
				return selector_key(id, var);
			} else {
				return new Variable(var.getType(), id);
			}
		} else if (lookahead.getSymbol() == Symbol.LSQBRACKET) {

			match(new Token(Symbol.LSQBRACKET, scanner.getLine(), scanner.getColumn(), "["));
			id += "[";
			lookahead = scanner.next_token();

			Variable expr = expression();
			if (expr.getType() != IdType.INTEGER) {
				throw new TypeMismatchedException(lookahead.getYyline(), lookahead.getYycolumn());
			}
			id += (String) expr.getValue();

			match(new Token(Symbol.RSQBRACKET, scanner.getLine(), scanner.getColumn(), "]"));
			id += "]";
			lookahead = scanner.next_token();
			if (lookahead.getSymbol() == Symbol.LSQBRACKET || lookahead.getSymbol() == Symbol.DOT) {
				return selector_key(id, var);
			} else {
				return new Variable(var.getType(), id);
			}
		}
		return var;
	}

	private void match(Token sym) throws OberonException, IOException {
		if (lookahead.getSymbol() != sym.getSymbol()) {
			System.out.println("Unexpected \"" + lookahead.getValue() + "\", it wants a \"" + (String) sym.getValue()
					+ "\".");
			if (sym.getSymbol() == Symbol.LPARENTHESE) {
				throw new MissingLeftParenthesisException(lookahead.getYyline(), lookahead.getYycolumn());
			} else if (sym.getSymbol() == Symbol.RPARENTHESE) {
				throw new MissingRightParenthesisException(lookahead.getYyline(), lookahead.getYycolumn());
			} else if (sym.getSymbol() == Symbol.SEMI) {
				throw new MissingSemicolonException(lookahead.getYyline(), lookahead.getYycolumn());
			} else {
				throw new SyntacticException(lookahead.getYyline(), lookahead.getYycolumn());
			}
		}
	}

}
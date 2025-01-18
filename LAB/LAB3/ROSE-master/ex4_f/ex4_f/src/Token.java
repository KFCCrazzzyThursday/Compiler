public class Token {

	private int symbol;
	private Object value;
	private int yyline;
	private int yycolumn;

	public Token(int sym, int yyline, int yycolumn, Object value) {
		this.symbol = sym;
		this.yyline = yyline;
		this.yycolumn = yycolumn;
		this.value = value;
	}

	public int getSymbol() {
		return symbol;
	}

	public void setSymbol(int symbol) {
		this.symbol = symbol;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getYyline() {
		return yyline + 1;
	}

	public int getYycolumn() {
		return yycolumn;
	}
}

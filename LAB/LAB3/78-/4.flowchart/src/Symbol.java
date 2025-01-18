public class Symbol {	
	/**
	 */
	public int m_sym;
	
	/**
	 */
	public String m_name;
	
	public Symbol(int sym, String name) {
		this.m_sym = sym;
		this.m_name = name;
	}
	
	public Symbol(int sym) {
		this.m_sym = sym;
		this.m_name = null;
	}
	
	public Symbol() {
		this.m_sym = 0;
		this.m_name = null;
	}
	
}

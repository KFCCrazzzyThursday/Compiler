public class Variable {
	public static String UNDEFINE = "This Variable is undefine!";
	private int type;
	private Object value;

	public Variable(int type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Variable(int type) {
		this.type = type;
		if (type == IdType.BOOLEAN) {
			value = "false";
		} else if (type == IdType.INTEGER) {
			value = "0";
		} else {
			value = UNDEFINE;
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String temp = new String();
		if (type == IdType.BOOLEAN) {
			temp = "Boolean";
		} else if (type == IdType.INTEGER) {
			temp = "Integer";
		} else if (type == IdType.RECORD) {
			temp = "Record";
		} else if (type == IdType.PROCEDURE) {
			temp = "Procedure";
		} else {
			temp = "Undefine";
		}
		return "Type: " + temp + " Value: " + value;
	}
}

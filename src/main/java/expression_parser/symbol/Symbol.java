package expression_parser.symbol;

public abstract class Symbol {
	// 表达式中string
	protected String identifier;

	public String getIdentifier() {
		return this.identifier;
	}

	public abstract String getType();

	public String toString() {
		return "(" + getType() + ")" + this.identifier;
	}
}

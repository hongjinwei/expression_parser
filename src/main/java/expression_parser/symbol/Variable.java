package expression_parser.symbol;

public class Variable extends Symbol {
	DataType type;

	public Variable(DataType type, String id) {
		this.type = type;
		super.identifier = id;
	}

	public String getType() {
		return type.toString();
	}
}

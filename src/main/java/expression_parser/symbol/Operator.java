package expression_parser.symbol;

public class Operator extends Symbol {

	public Operator(String op) {
		this.identifier = op;
	}

	public String getType() {
		return "OP";
	}

	private Variable calcPlus(Variable a, Variable b) throws Exception {
		if (a.type == DataType.STRING || b.type == DataType.STRING) {
			return new Variable(DataType.STRING, a.identifier + b.identifier);
		}
		double aa, bb;
		aa = Double.parseDouble(a.identifier);
		bb = Double.parseDouble(b.identifier);
		double ans = aa + bb;
		if (a.type == DataType.FLOAT || b.type == DataType.FLOAT) {
			return VarFactory.createFLOAT(ans);
		}

		if (a.type == DataType.BOOL || b.type == DataType.BOOL) {
			System.out.println("error option \"+\" for BOOL");
		}
		return VarFactory.createINT(ans);
	}

	private Variable calcMinus(Variable a, Variable b) throws Exception {
		double aa = Double.parseDouble(a.identifier);
		double bb = Double.parseDouble(b.identifier);
		double ans = aa - bb;
		if (a.type == DataType.FLOAT || b.type == DataType.FLOAT) {
			return VarFactory.createFLOAT(ans);
		}
		return VarFactory.createINT(ans);
	}

	private Variable calcMultiply(Variable a, Variable b) throws Exception {
		double aa = Double.parseDouble(a.identifier);
		double bb = Double.parseDouble(b.identifier);
		double ans = aa * bb;
		if (a.type == DataType.FLOAT || b.type == DataType.FLOAT) {
			return VarFactory.createFLOAT(ans);
		}
		return VarFactory.createINT(ans);
	}

	private Variable calcDivide(Variable a, Variable b) {
		double aa = Double.parseDouble(a.identifier);
		double bb = Double.parseDouble(b.identifier);
		double ans = aa / bb;

		if (a.type == DataType.FLOAT || b.type == DataType.FLOAT) {
			return VarFactory.createFLOAT(ans);
		}
		return VarFactory.createINT(ans);
	}

	private Variable biggerThan(Variable a, Variable b) throws Exception {
		boolean ans = true;
		boolean right = true;
		if ((a.type == DataType.FLOAT || a.type == DataType.INT) && (b.type == DataType.FLOAT || b.type == DataType.INT)) {
			ans = Double.parseDouble(a.identifier) > Double.parseDouble(b.identifier);
		} else if (a.type == DataType.STRING && b.type == DataType.INT) {
			ans = a.identifier.length() > Double.parseDouble(b.identifier);
		} else if (a.type == DataType.STRING && b.type == DataType.INT) {
			ans = a.identifier.length() > Integer.parseInt(b.identifier);
		} else {
			right = false;
		}
		if (right) {
			return VarFactory.createBOOL(ans);
		}
		throw new Exception("error type for option" + a.getType() + " > " + b.getType());
	}

	private Variable biggerThanOrEqual(Variable a, Variable b) throws Exception {
		boolean ans = true;
		boolean right = true;
		if ((a.type == DataType.FLOAT || a.type == DataType.INT) && (b.type == DataType.FLOAT || b.type == DataType.INT)) {
			ans = Double.parseDouble(a.identifier) >= Double.parseDouble(b.identifier);
		} else if (a.type == DataType.STRING && b.type == DataType.INT) {
			ans = a.identifier.length() >= Double.parseDouble(b.identifier);
		} else if (a.type == DataType.STRING && b.type == DataType.INT) {
			ans = a.identifier.length() >= Integer.parseInt(b.identifier);
		} else {
			right = false;
		}
		if (right) {
			return VarFactory.createBOOL(ans);
		}
		throw new Exception("error type for option" + a.getType() + " >= " + b.getType());
	}

	private Variable smallerThan(Variable a, Variable b) throws Exception {
		boolean ans = true;
		boolean right = true;
		if ((a.type == DataType.FLOAT || a.type == DataType.INT) && (b.type == DataType.FLOAT || b.type == DataType.INT)) {
			ans = Double.parseDouble(a.identifier) < Double.parseDouble(b.identifier);
		} else if (a.type == DataType.STRING && b.type == DataType.STRING) {
			ans = a.identifier.length() < b.identifier.length();
		} else if (a.type == DataType.STRING && b.type == DataType.INT) {
			ans = a.identifier.length() < Integer.parseInt(b.identifier);
		} else {
			right = false;
		}
		if (right) {
			return VarFactory.createBOOL(ans);
		}
		throw new Exception("error type for option" + a.getType() + " < " + b.getType());
	}

	private Variable smallerThanOrEqual(Variable a, Variable b) throws Exception {
		boolean ans = true;
		boolean right = true;
		if ((a.type == DataType.FLOAT || a.type == DataType.INT) && (b.type == DataType.FLOAT || b.type == DataType.INT)) {
			ans = Double.parseDouble(a.identifier) <= Double.parseDouble(b.identifier);
		} else if (a.type == DataType.STRING && b.type == DataType.STRING) {
			ans = a.identifier.length() <= b.identifier.length();
		} else if (a.type == DataType.STRING && b.type == DataType.INT) {
			ans = a.identifier.length() <= Integer.parseInt(b.identifier);
		} else {
			right = false;
		}
		if (right) {
			return VarFactory.createBOOL(ans);
		}
		throw new Exception("error type for option" + a.getType() + " <=" + b.getType());
	}

	private Variable equal(Variable a, Variable b) throws Exception {
		boolean ans;
		if (a.type == DataType.STRING && b.type == DataType.INT) {
			ans = a.getIdentifier().length() == Integer.parseInt(b.identifier);
		} else {
			ans = a.identifier.equals(b.identifier);
		}
		return VarFactory.createBOOL(ans);
	}

	private Variable and(Variable a, Variable b) throws Exception {
		boolean ans;
		if (a.type == DataType.BOOL && b.type == DataType.BOOL) {
			ans = a.getIdentifier().toLowerCase().equals("true") && b.getIdentifier().toLowerCase().equals("true");
			return VarFactory.createBOOL(ans);
		} else {
			throw new Exception("error type for &&");
		}
	}

	private Variable or(Variable a, Variable b) throws Exception {
		boolean ans;
		if (a.type == DataType.BOOL && b.type == DataType.BOOL) {
			ans = a.getIdentifier().toLowerCase().equals("true") || b.getIdentifier().toLowerCase().equals("true");
			return VarFactory.createBOOL(ans);
		} else {
			throw new Exception("error type for ||");
		}
	}

	public Variable calc(Variable a, Variable b) throws Exception {
		if (this.identifier.equals("+")) {
			return calcPlus(a, b);
		} else if (this.identifier.equals("-")) {
			return calcMinus(a, b);
		} else if (this.identifier.equals("*")) {
			return calcMultiply(a, b);
		} else if (this.identifier.equals("/")) {
			return calcDivide(a, b);
		} else if (this.identifier.equals(">")) {
			return biggerThan(a, b);
		} else if (this.identifier.equals(">=")) {
			return biggerThanOrEqual(a, b);
		} else if (this.identifier.equals("<")) {
			return smallerThan(a, b);
		} else if (this.identifier.equals("<=")) {
			return smallerThanOrEqual(a, b);
		} else if (this.identifier.equals("==")) {
			return equal(a, b);
		} else if (this.identifier.equals("&&")) {
			return and(a, b);
		} else if (this.identifier.equals("||")) {
			return or(a, b);
		} else {
			throw new Exception("error operator :" + this.identifier);
		}
	}

	public static void main(String[] args) throws Exception {
		String s = "3+2";
		Operator o = new Operator("+");
		Variable a = new Variable(DataType.INT, "4");
		Variable b = new Variable(DataType.INT, "4");
		System.out.println(o.calc(a, b).identifier);
	}

}

package expression_parser.symbol;

public class VarFactory {

	public static Variable createBOOL(boolean b) {
		return new Variable(DataType.BOOL, b + "");
	}

	public static Variable createINT(int a) {
		return new Variable(DataType.INT, a + "");
	}

	public static Variable createINT(double a) {
		int ans = (int) a;
		return new Variable(DataType.INT, ans + "");
	}

	public static Variable createINT(String a) {
		try {
			Integer.parseInt(a);
			return new Variable(DataType.INT, a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Variable createFLOAT(int a) {
		return new Variable(DataType.FLOAT, a + "");
	}

	public static Variable createFLOAT(double a) {
		return new Variable(DataType.FLOAT, a + "");
	}

	public static Variable createFLOAT(String a) {
		try {
			Double.parseDouble(a);
			return new Variable(DataType.FLOAT, a + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Variable createSTRING(int a) {
		return new Variable(DataType.STRING, a + "");
	}

	public static Variable createSTRING(String a) {
		return new Variable(DataType.STRING, a);
	}

}

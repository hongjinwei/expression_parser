package expression_parser;

import java.util.ArrayList;
import java.util.Stack;

import expression_parser.symbol.DataType;
import expression_parser.symbol.Operator;
import expression_parser.symbol.Symbol;
import expression_parser.symbol.Variable;
import expression_parser.symbol.func.Function;
import expression_parser.symbol.func.FunctionUtils;

public class ConditionParser {

	private static final char GET_RES_OP = '$';

	public static String preprocess(String res, String exp) {
		String ans = exp.replaceAll("\\$\\{result\\}", res.replaceAll("\\\"", "\\\\\\\\\""));
		return ans;
	}

	private static void handleOp(Symbol op, Stack<Symbol> ostack, ArrayList<Symbol> out) {
		if (op.getIdentifier().equals("(")) {
			ostack.push(op);
			return;
		} else if (op.getIdentifier().equals(")")) {
			Symbol o = ostack.pop();
			while (!o.getIdentifier().equals("(")) {
				out.add(o);
				o = ostack.pop();
			}
			if (ostack.peek() instanceof Function) {
				out.add(ostack.pop());
			}
		} else if (ostack.isEmpty()) {
			ostack.push(op);
		} else if (ostack.peek().getIdentifier().equals("(")) {
			ostack.push(op);
		} else {
			int priority1 = Utils.getPriority(ostack.peek());
			int priority2 = Utils.getPriority(op);
			if (priority2 > priority1) {
				ostack.push(op);
			} else {
				out.add(ostack.pop());
			}
		}
	}

	public static ArrayList<Symbol> parse(String expression) throws Exception {
		Stack<Symbol> ostack = new Stack<Symbol>();
		ArrayList<Symbol> out = new ArrayList<Symbol>();
		int p = 0;
		if (expression.length() == 0) {
			System.out.println("null");
			return null;
		}
		while (p < expression.length()) {
			char a = expression.charAt(p);
			if (a == ' ') {
				p++;
				continue;
			} else if (a == GET_RES_OP) {

			} else if (a >= '0' && a <= '9') {
				String tmp = "";
				boolean isFloat = false;
				while (p < expression.length() && expression.charAt(p) >= '0' && expression.charAt(p) <= '9') {
					if (expression.charAt(p) == '.') {
						isFloat = true;
					}
					tmp += expression.charAt(p);
					p++;
				}
				DataType t = (isFloat) ? DataType.FLOAT : DataType.INT;
				Variable ans = new Variable(t, tmp);
				out.add(ans);
			} else if (a == '"') {
				p++;
				String tmp = "";
				while (p < expression.length() && expression.charAt(p) != '"') {
					if (expression.charAt(p) == '\\') {
						p++;
						tmp += expression.charAt(p);
						p++;
					}
					tmp += expression.charAt(p);
					p++;
				}
				Variable ans = new Variable(DataType.STRING, tmp);
				out.add(ans);
				p++;
			} else if (p + 2 < expression.length() && Utils.isOperator(expression.substring(p, p + 2))) {
				Operator op = new Operator(expression.substring(p, p + 2));
				handleOp(op, ostack, out);
				p += 2;
			} else if (Utils.isOperator(expression.substring(p, p + 1))) {
				Operator op = new Operator(expression.substring(p, p + 1));
				handleOp(op, ostack, out);
				p++;
			} else if (a == ',') {
				Symbol o = ostack.peek();
				while (!(o instanceof Operator && o.getIdentifier().equals("("))) {
					o = ostack.pop();
					out.add(o);
					o = ostack.peek();
				}
				p++;
			} else {
				String tmp = "";
				while (p < expression.length() && expression.charAt(p) != '(') {
					tmp += expression.charAt(p);
					p++;
				}
				Function func = FunctionUtils.getFunc(tmp);
				ostack.push(func);
				Operator op = new Operator(expression.substring(p, p + 1));// "("
				if (!op.getIdentifier().equals("(")) {
					throw new Exception("error syntax" + op.getIdentifier());
				}
			}
		}
		while (!ostack.isEmpty()) {
			out.add(ostack.pop());
		}
		return out;
	}

	public static Variable calc(ArrayList<Symbol> parsedSuffix) throws Exception {
		Stack<Variable> calcStack = new Stack<Variable>();
		for (int i = 0; i < parsedSuffix.size(); i++) {
			Symbol symbol = parsedSuffix.get(i);
			if (symbol instanceof Variable) {
				calcStack.push((Variable) symbol);
			} else if (symbol instanceof Operator) {
				Variable b = calcStack.pop();
				Variable a = calcStack.pop();
				calcStack.push(((Operator) symbol).calc(a, b));
			} else if (symbol instanceof Function) {
				((Function) symbol).calc(calcStack);
			}
		}
		if (calcStack.size() != 1) {
			throw new Exception("calc error");
		}
		return calcStack.pop();
	}

	public static Variable execute(String exp) throws Exception {
		return calc(parse(exp));
	}

	public static void main(String[] args) throws Exception {
		String exp = "md5(123)";
		String exp1 = "md5(12 + \"3\")";
		String exp2 = "contains(\"abbcc\" + \"dd\", \"a*c\")";
		String exp3 = "\"a  \\\"aa\" == 3";
		System.out.println(exp3);
		System.out.println(execute(exp3));

		// System.out.println(preprocess("aa\"aa", "${result} > 3"));
	}
}

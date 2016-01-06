package expression_parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import expression_parser.symbol.DataType;
import expression_parser.symbol.Operator;
import expression_parser.symbol.Symbol;
import expression_parser.symbol.VarFactory;
import expression_parser.symbol.Variable;
import expression_parser.symbol.func.Function;
import expression_parser.symbol.func.FunctionUtils;

public class ConditionParser {

	private static final char GET_RES_OP = '$';

	private HashMap<String, Variable> valueMap = new HashMap<String, Variable>();

	public ConditionParser() {

	}

	/**
	 * 将result存入默认的变量result中
	 * 
	 * @param result
	 */
	public ConditionParser(String result) {
		this.valueMap.put("result", VarFactory.createSTRING(result));
	}

	/**
	 * 将result存入默认的变量result中
	 * 
	 * @param result
	 */
	public ConditionParser(int result) {
		this.valueMap.put("result", VarFactory.createINT(result));
	}

	/**
	 * 将result存入默认的变量result中
	 * 
	 * @param result
	 */
	public ConditionParser(boolean result) {
		this.valueMap.put("result", VarFactory.createBOOL(result));
	}

	/**
	 * 将result存入默认的变量result中
	 * 
	 * @param result
	 */
	public ConditionParser(double result) {
		this.valueMap.put("result", VarFactory.createFLOAT(result));
	}

	/**
	 * 将变量表传入parser
	 * 
	 * @param result
	 */
	public ConditionParser(HashMap<String, Variable> valueMap) {
		this.valueMap = valueMap;
	}

	public void assign(String key, String value) {
		this.valueMap.put(key, VarFactory.createSTRING(value));
	}

	public void assign(String key, int value) {
		this.valueMap.put(key, VarFactory.createINT(value));
	}

	public void assign(String key, double value) {
		this.valueMap.put(key, VarFactory.createFLOAT(value));
	}

	public void assign(String key, boolean value) {
		this.valueMap.put(key, VarFactory.createBOOL(value));
	}

	public void assign(String key, Variable value) {
		this.valueMap.put(key, value);
	}

	public Variable getValue(String key) {
		return this.valueMap.get(key);
	}

	public String preprocess(String res, String exp) {
		String ans = exp.replaceAll("\\$\\{result\\}", res.replaceAll("\\\"", "\\\\\\\\\""));
		return ans;
	}

	private void handleOp(Symbol op, Stack<Symbol> ostack, ArrayList<Symbol> out) {
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
			if (priority2 >= priority1) {
				ostack.push(op);
			} else {
				while (priority2 < priority1) {
					out.add(ostack.pop());
					priority1 = Utils.getPriority(ostack.peek());
					priority2 = Utils.getPriority(op);
				}
				ostack.push(op);
			}
		}
	}

	public ArrayList<Symbol> parse(String expression) throws Exception {
		Stack<Symbol> ostack = new Stack<Symbol>();
		ArrayList<Symbol> out = new ArrayList<Symbol>();
		int p = 0;
		if (expression.length() == 0) {
			System.out.println("null expression");
			return null;
		}
		while (p < expression.length()) {
			char a = expression.charAt(p);
			if (a == ' ') {
				p++;
				continue;
			} else if (a == GET_RES_OP) {
				p++;
				if (expression.charAt(p) != '{') {
					throw new Exception("error syntax :" + GET_RES_OP + expression.charAt(p));
				}
				p++;
				StringBuilder sb = new StringBuilder();
				char o = expression.charAt(p);
				while (o != '}') {
					sb.append(o);
					p++;
					o = expression.charAt(p);
				}
				p++;
				String key = sb.toString();
				out.add(getValue(key));
			} else if (a >= '0' && a <= '9') {
				StringBuilder sb = new StringBuilder();
				boolean isFloat = false;
				while (p < expression.length() && expression.charAt(p) >= '0' && expression.charAt(p) <= '9') {
					if (expression.charAt(p) == '.') {
						isFloat = true;
					}
					sb.append(expression.charAt(p));
					p++;
				}
				DataType t = (isFloat) ? DataType.FLOAT : DataType.INT;
				Variable ans = new Variable(t, sb.toString());
				out.add(ans);
			} else if (a == '"') {
				p++;
				StringBuilder sb = new StringBuilder();
				while (p < expression.length() && expression.charAt(p) != '"') {
					if (expression.charAt(p) == '\\') {
						sb.append(expression.charAt(p + 1));
						p += 2;
						continue;
					}
					sb.append(expression.charAt(p));
					p++;
				}
				Variable ans = new Variable(DataType.STRING, sb.toString());
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
				StringBuilder sb = new StringBuilder();
				while (p < expression.length() && expression.charAt(p) != '(') {
					sb.append(expression.charAt(p));
					p++;
				}
				Function func = FunctionUtils.getFunc(sb.toString());
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
		System.out.println(out);
		return out;
	}

	public Variable calc(ArrayList<Symbol> parsedSuffix) throws Exception {
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

	public Variable execute(String exp) throws Exception {
		return calc(parse(exp));
	}

	public ConditionParser excuteAndAssign(String key, String exp) throws Exception {
		this.assign(key, execute(exp));
		return this;
	}

	public static void main(String[] args) throws Exception {
		String exp = "1+2*3-4/5";
		String exp1 = "md5(12 + \"3\")";
		String exp2 = "contains(\"abbcc\" + \"dd\", \"a*c\")";
		String exp3 = "${result}";
		String exp4 = "${hello} + \"ssss\"";
		ConditionParser parser = new ConditionParser("hh\"a");
		parser.assign("hello", "hello world");

		System.out.println(exp);
		System.out.println(parser.execute(exp));

		System.out.println(exp4);
		System.out.println(parser.execute(exp4));

		// System.out.println(preprocess("aa\"aa", "${result} > 3"));
	}
}

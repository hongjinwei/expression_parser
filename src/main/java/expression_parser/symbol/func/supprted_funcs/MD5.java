package expression_parser.symbol.func.supprted_funcs;

import java.util.ArrayList;
import java.util.Stack;

import expression_parser.Utils;
import expression_parser.symbol.DataType;
import expression_parser.symbol.VarFactory;
import expression_parser.symbol.Variable;
import expression_parser.symbol.func.Function;

public class MD5 extends Function {

	static int dimen = 1;

	public MD5() {
		super(dimen, "MD5");
	}

	public void calc(Stack<Variable> calcStack) {
		try {
			Variable a = this.getVariable(calcStack).get(0);
			String md5 = Utils.MD5(a.getIdentifier());
			Variable ans = VarFactory.createSTRING(md5);
			calcStack.push(ans);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Utils.MD5("123"));
	}
}

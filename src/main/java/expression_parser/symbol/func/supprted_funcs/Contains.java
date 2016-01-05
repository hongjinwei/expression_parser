package expression_parser.symbol.func.supprted_funcs;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import expression_parser.symbol.DataType;
import expression_parser.symbol.VarFactory;
import expression_parser.symbol.Variable;
import expression_parser.symbol.func.Function;

public class Contains extends Function {

	static int dimen = 2;

	public Contains() {
		super(dimen, "Contains");
	}

	@Override
	public void calc(Stack<Variable> stack) {
		List<Variable> vars = this.getVariable(stack);
		String regex = vars.get(0).getIdentifier();
		String context = vars.get(1).getIdentifier();

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(context);
		boolean b = m.find();
		Variable ans = VarFactory.createBOOL(b);
		stack.push(ans);
	}
}

package expression_parser.symbol.func;

import java.util.ArrayList;
import java.util.Stack;

import expression_parser.symbol.Symbol;
import expression_parser.symbol.Variable;

public abstract class Function extends Symbol {
	// 参数个数
	protected int dimension;

	public Function(int dimen, String identifier) {
		super.identifier = identifier;
		this.dimension = dimen;
	}

	public String getType() {
		return "FUNC";
	}

	public Function() {

	}

	/**
	 * 获得函数的运行的参数
	 * 注意获得的参数的反的，即第0个参数实际是函数的最后一个参数
	 * @param calcStack
	 * @return
	 */

	public ArrayList<Variable> getVariable(Stack<Variable> calcStack) {
		ArrayList<Variable> ans = new ArrayList<Variable>();
		try {
			for (int i = 0; i < dimension; ++i) {
				ans.add(calcStack.pop());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	public abstract void calc(Stack<Variable> stack);
}

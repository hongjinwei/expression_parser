package expression_parser.symbol.func;

import java.io.File;
import java.util.ArrayList;

import expression_parser.symbol.func.supprted_funcs.Contains;
import expression_parser.symbol.func.supprted_funcs.MD5;

public class FunctionUtils {

	private static final String funcPackagePrefix = "expression_parser.func.";

	private static final String[] registered_func_names = { "MD5", "Contains" };

	public static ArrayList<String> funcNames = new ArrayList<String>();

	public static Function getFunc(FuncType type) {
		if (type == FuncType.MD5) {
			return new MD5();
		}
		return null;
	}

	public static Function getFunc(String exp) {
		if (exp.toLowerCase().equals("md5")) {
			return new MD5();
		}
		if (exp.toLowerCase().equals("contains")) {
			return new Contains();
		}
		System.out.println("error");
		return null;
	}

	public static boolean isFunc(String c) {
		for (String name : registered_func_names) {
			if (c.toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		// System.out.println(System.getProperty("java.class.path"));
		System.out.println(FunctionUtils.class.getClassLoader().getResource("").getPath() + "func/");
		File dir = new File(FunctionUtils.class.getClassLoader().getResource("").getPath() + "func");
		System.out.println(dir.isDirectory());
		File[] files = dir.listFiles();
		for (File f : files) {
			System.out.println(f.getName());
		}
	}
}

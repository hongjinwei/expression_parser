package expression_parser;

import java.security.MessageDigest;

import expression_parser.symbol.Symbol;
import expression_parser.symbol.func.Function;

public class Utils {

	public static boolean isOperator(String c) {
		return c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("(") || c.equals(")")|| c.equals("<") || c.equals(">") || c.equals("<=") || c.equals(">=") || c.equals("==");
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static int getPriority(Symbol op) {
		String id = op.getIdentifier();
		if (id.equals("(") || id.equals(")")) {
			return 10;
		} else if (op instanceof Function) {
			return 9;
		} else if (id.equals("*") || id.equals("/")) {
			return 8;
		} else if (id.equals("+") || id.equals("-")) {
			return 7;
		} else if (id.equals("<") || id.equals("<=") || id.equals(">") || id.equals(">=")) {
			return 6;
		} else if (id.equals("==") || id.equals("!=")) {
			return 5;
		} else if (id.equals("&&")) {
			return 4;
		} else if (id.equals("||")) {
			return 3;
		} else {
			System.out.println("unkown op :" + id);
			return 0;
		}
	}

	public static void main(String[] args) {
		System.out.println(MD5("12345"));
	}
}

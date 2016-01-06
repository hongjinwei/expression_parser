package expression_parser.parser;

import java.util.ArrayList;

import expression_parser.symbol.Symbol;

public class ParseResult {
	private ArrayList<Symbol> parsedSuffix;
	private boolean ifStore;
	private String storeKey;

	public ParseResult(ArrayList<Symbol> parsedSuffix, boolean ifStore, String storeKey) {
		this.parsedSuffix = parsedSuffix;
		this.ifStore = ifStore;
		this.storeKey = storeKey;
	}

	public ArrayList<Symbol> getParsedSuffix() {
		return parsedSuffix;
	}

	public void setParsedSuffix(ArrayList<Symbol> parsedSuffix) {
		this.parsedSuffix = parsedSuffix;
	}

	public boolean isIfStore() {
		return ifStore;
	}

	public void setIfStore(boolean ifStore) {
		this.ifStore = ifStore;
	}

	public String getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}

}

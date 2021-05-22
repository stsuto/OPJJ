package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class <code>ElementOperator</code> inherits <code>Element</code>, 
 * and has a single read-only String property: symbol.
 * 
 * @author stipe
 */
public class ElementOperator extends Element {

	/**
	 * symbol of this <code>ElementOperator</code>
	 */
	private String symbol;
	
	/**
	 * Constructor.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Returns this <code>ElementOperator's</code> symbol.
	 * @return String symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Creates a String representation of this object and returns it. 
	 * ElementOperator implements it to return its symbol.
	 * @return String of this object's symbol
	 */
	@Override
	public String asText() {
		return symbol;
	}
}

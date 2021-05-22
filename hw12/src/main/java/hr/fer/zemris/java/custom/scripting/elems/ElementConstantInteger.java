package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class <code>ElementConstantInteger</code> inherits <code>Element</code>, 
 * and has a single read-only int property: value.
 * 
 * @author stipe
 */
public class ElementConstantInteger extends Element {

	/**
	 * value of this <code>ElementConstantInteger</code>
	 */
	private int value;
	
	/**
	 * Constructor.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * Returns this instance of <code>ElementConstantInteger's</code> value.
	 * @return int value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Creates a String representation of this object and returns it. 
	 * ElementConstantInteger implements it to return its value.
	 * @return String of this object's value
	 */
	@Override
	public String asText() {
		return String.valueOf(value);
	}

}

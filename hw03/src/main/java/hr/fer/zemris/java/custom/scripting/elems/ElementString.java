package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class <code>ElementString</code> inherits <code>Element</code>, 
 * and has a single read-only String property: value.
 * 
 * @author stipe
 */
public class ElementString extends Element {

	/**
	 * value of this <code>ElementString</code>
	 */
	private String value;
	
	/**
	 * Constructor.
	 */
	public ElementString(String value) {
		this.value = value;
	}

	/**
	 * Returns this <code>ElementString's</code> value.
	 * @return String value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Creates a String representation of this object and returns it. 
	 * ElementString implements it to return its value.
	 * @return String value of this object
	 */
	@Override
	public String asText() {
		return value;
	}
	
}

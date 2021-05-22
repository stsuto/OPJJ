package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class <code>ElementConstantDouble</code> inherits <code>Element</code>, 
 * and has a single read-only double property: value.
 * 
 * @author stipe
 */
public class ElementConstantDouble extends Element {

	/**
	 * value of this <code>ElementConstantDouble</code>
	 */
	private double value;
	
	/**
	 * Constructor.
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * Returns this instance of <code>ElementConstantDouble's</code> value.
	 * @return double value
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * Creates a String representation of this object and returns it. 
	 * ElementConstantDouble implements it to return its value.
	 * @return String of this object's value
	 */
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
}

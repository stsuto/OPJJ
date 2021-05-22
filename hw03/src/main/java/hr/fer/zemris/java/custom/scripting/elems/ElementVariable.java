package hr.fer.zemris.java.custom.scripting.elems;


/**
 * Class <code>ElementVariable</code> inherits <code>Element</code>, 
 * and has a single read-only String property: name.
 * 
 * @author stipe
 */
public class ElementVariable extends Element {
	
	/**
	 * name of this <code>ElementVariable</code>
	 */
	private String name;
	
	/**
	 * Constructor.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	/**
	 * Returns this <code>ElementVariable's</code> name.
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Creates a String representation of this object and returns it. 
	 * ElementVariable implements it to return its name.
	 * @return String of this object's name
	 */
	@Override
	public String asText() {
		return name;
	}
}

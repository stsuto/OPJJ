package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class <code>ElementFunction</code> inherits <code>Element</code>, 
 * and has a single read-only String property: name.
 * 
 * @author stipe
 */
public class ElementFunction extends Element {

	/**
	 * name of this <code>ElementFunction</code>
	 */
	private String name;
	
	/**
	 * Constructor.
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	/**
	 * Returns this <code>ElementFunction's</code> name.
	 * @return String name
	 */
	public String getname() {
		return name;
	}
	
	/**
	 * Creates a String representation of this object and returns it. 
	 * ElementFunction implements it to return its name.
	 * @return String of this object's name
	 */
	@Override
	public String asText() {
		return name;
	}
	
}

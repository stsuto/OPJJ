package hr.fer.zemris.java.hw06.shell.commands.massrename;

/**
 * Class {@code NameBuilderImpls} contains static methods used for 
 * creating {@link NameBuilder} objects with certain functions.
 * 
 * @author stipe
 *
 */
public class NameBuilderImpls {

	/**
	 * Adds the simple text to the builder.
	 * 
	 * @param t text
	 * @return an updated NameBuilder
	 */
	public static NameBuilder text(String t) {
		return (result, sb) -> sb.append(t);
	}
	
	/**
	 * Inserts the group content into the builder.
	 * 
	 * @param index group's index
	 * @return an updated NameBuilder
	 */
	public static NameBuilder group(int index) {
		return (result, sb) -> sb.append(result.group(index));
	}

	/**
	 * Inserts the group content into the builder, following
	 * the given extra options about padding.
	 * 
	 * @param index group's index
	 * @param padding character to pad with
	 * @param minWidth the minimum width of the group value
	 * @return an updated NameBuilder
	 */
	public static NameBuilder group(int index, char padding, int minWidth) {
		return (result, sb) -> 
			sb.append(String.format("%" + minWidth + "s", 
						result.group(index)).replace(' ', padding));
	}
	
	/**
	 * Private constructor that prevents the default constructor from being
	 * auto-created as instances of this class shouldn't be created.
	 */
	private NameBuilderImpls() {}
}

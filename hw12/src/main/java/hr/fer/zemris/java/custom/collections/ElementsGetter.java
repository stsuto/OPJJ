package hr.fer.zemris.java.custom.collections;

/**
 * Interface <code>ElementsGetter</code> enables fetching elements from a collection that implements it.
 * It serves as a communication object between the collection and the user.
 * 
 * @author stipe
 */
public interface ElementsGetter {

	/**
	 * Checks if there are any unfetched elements in the collection.
	 * @return <code>true</code> if there is at least one more element, <code>false</code> otherwise
	 */
	boolean hasNextElement();
	
	/**
	 * Fetches the next element of the collection.
	 * @return Object value of the next element
	 */
	Object getNextElement();
	
	/**
	 * Processes all previously unfetched elements of the collection with regard to the given Processor.
	 * @param p the Processor whose method process is to be called
	 */
	default void processRemaining(Processor p) {
		while (true) {
			if (hasNextElement()) {
				p.process(getNextElement());
			}
		}
	}
}

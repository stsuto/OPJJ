package hr.fer.zemris.java.custom.collections;

/**
 * Class <code>Collection</code> represents some general collection of objects 
 * that can be manipulated by using the defined methods.
 * 
 * @author stipe
 *
 */
public class Collection {

	/**
	 * Protected default constructor.
	 */
	protected Collection() {
		super();
	}
	
	/**
	 * Method that checks if the collection is empty.
	 * @return <code>true</code> if collection contains no objects and <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Method that determines the number of currently stored objects in this collections.
	 * Subclasses must implement it to properly return the exact size as this implementation always returns 0.
	 * @return int value of the number of currently stored objects
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the given object into this collection.
	 * Subclasses must implement it as it is here implemented to do nothing.
	 * @param value the object to be added
	 */
	public void add(Object value) {
	}
	
	/**
	 * Determines if the collection contains the object in question.
	 * Subclasses must implement it by using <code>equals</code> method. It is implemented here to do nothing.
	 * @param value the object to be tested if it is present in the collection
	 * @return <code>true</code> if the collection contains given value, otherwise returns <code>true</code>
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Removes one occurrence of the given value.
	 * Here it always returns false, so it must be implemented in subclasses.
	 * @param value value the object of which one occurrence is to be removed
	 * @return <code>true</code> if the collection contained given value and removed one occurrence of it, otherwise returns <code>true</code>
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates new array with size equal to the size of this collections, 
	 * fills it with collection content and returns the array. 
	 * It must be overriden as it is here implemented to throw UnsupportedOperationException.
	 * @throws UnsupportedOperationException if not overriden
	 * @return array of objects from this collection
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Ensures that all elements of this collection will be processed.
	 * Here it is implemented to do nothing,
	 * subclasses must implement it so the method calls processor.process(.).
	 * @param processor the processor whose method process will be called
	 */
	public void forEach(Processor processor) {
	}
	
	/**
	 * Adds all elements from the given collection into the current collection, leaving the given collection unchanged.
	 * Creates a local processor class whose method process will call the method add,
	 * and then calls forEach on the other collection with that same processor as the argument.
	 * @param other the collection whose elements are to be copied into the current collection
	 */
	public void addAll(Collection other) {
		class LocalProcessor extends Processor {
			
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		
		other.forEach(new LocalProcessor());
	}
	
	/**
	 * Removes all elements from this collection.
	 * It is here implemented to do nothing, so subclasses must implement the proper solution.
	 */
	public void clear() {
	}
	
}

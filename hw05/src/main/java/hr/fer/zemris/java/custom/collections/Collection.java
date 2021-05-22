package hr.fer.zemris.java.custom.collections;

/**
 * Interface <code>Collection</code> represents some general collection of objects 
 * that can be manipulated by using the defined methods.
 * 
 * @author stipe
 *
 */
public interface Collection<E> {
	
	/**
	 * Method that checks if the collection is empty.
	 * @return <code>true</code> if collection contains no objects and <code>false</code> otherwise
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Method that determines the number of currently stored elements in this collections.
	 * @return int value of the number of currently stored element
	 */
	int size();
	
	/**
	 * Adds the given element into this collection.
	 * @param value the object to be added
	 */
	void add(E value);
	
	/**
	 * Determines if the collection contains the element in question.
	 * @param value the element to be tested if it is present in the collection
	 * @return <code>true</code> if the collection contains given value, otherwise returns <code>false</code>
	 */
	boolean contains(Object value);
	
	/**
	 * Removes one occurrence of the given value.
	 * @param value value the object of which one occurrence is to be removed
	 * @return <code>true</code> if the collection contained given value and removed one occurrence of it, otherwise returns <code>true</code>
	 */
	boolean remove(Object value);
	
	/**
	 * Allocates new array with size equal to the size of this collections, 
	 * fills it with collection content and returns the array. 
	 * @throws UnsupportedOperationException if not overriden
	 * @return array of objects from this collection
	 */
	Object[] toArray();
	
	/**
	 * Ensures that all elements of this collection will be processed.
	 * @param processor the processor whose method process will be called
	 */
	default void forEach(Processor<? super E> processor) {
		ElementsGetter<E> getter = createElementsGetter();
		while (getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}
	
	/**
	 * Adds all elements from the given collection into the current collection, leaving the given collection unchanged.
	 * Creates a local processor class whose method process will call the method add,
	 * and then calls forEach on the other collection with that same processor as the argument.
	 * @param other the collection whose elements are to be copied into the current collection
	 */
	default void addAll(Collection<? extends E> other) {
		class LocalProcessor implements Processor<E> {
			
			@Override
			public void process(E value) {
				add(value);
			}

		}
		
		other.forEach(new LocalProcessor());
	}
	
	/**
	 * Removes all elements from this collection.
	 */
	public void clear();
	
	/**
	 * Creates and returns an object that implements the ElementsGetter interface for iterating purposes.
	 * @return new instance of ElementsGetter class
	 */
	ElementsGetter<E> createElementsGetter();
	
	/**
	 * Adds all satisfying elements from the given collection to this collection, as determined by the given Tester.
	 * @param col the collection whose elements are to be copied
	 * @param tester instance of Tester that decides which elements are added
	 */
	default void addAllSatisfying(Collection<? extends E> col, Tester<? super E> tester) {
		ElementsGetter<? extends E> getter = col.createElementsGetter();
		while (getter.hasNextElement()) {
			E value = getter.getNextElement();
			if (tester.test(value)) {
				add(value);
			}
		}
	}

}

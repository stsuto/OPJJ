package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class is an implementation of a resizable array-backed collection of objects.
 * Duplicate elements are allowed, but storage of null references is not.
 * An instance of this class can be created using a wanted capacity, other collection, both of those, but also neither of those,
 * that is, using any of the four constructors provided.
 * 
 * @author stipe
 */
public class ArrayIndexedCollection<E> implements List<E> {

	/**
	 * default capacity at which the capacity is set unless written otherwise
	 */
	private static final int DEFAULT_CAPACITY = 16;
	/**
	 * number of elements in this collection
	 */
	private int size;
	/**
	 * array in which all elements of this collection are stored
	 */
	private E[] elements;
	
	/**
	 * number of modifications done to this collection
	 */
	private long modificationCount;
	
	/**
	 * Default constructor that delegates the construction process to a more complex one using default values.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructor that requests the capacity of the collection to be set to the given value.
	 * @param initialCapacity int value of the capacity this collection should be set to
	 * @throws IllegalArgumentException if initialCapacity is less than 1
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("The capacity must be greater than 0!");
		}
		elements = (E[]) new Object[initialCapacity]; // Declaration of the collections array
		modificationCount = 0;
	}
	
	/**
	 * Constructor that delegates the construction process to the most complex constructor,
	 * but requests that all elements of the given collection be copied to this collection.
	 * @param other collection whose elements are to be copied to this collection
	 * @throws NullPointerException if the other collection is null
	 */
	public ArrayIndexedCollection(Collection<E> other) {
		this(other, DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructor that uses a simpler constructor for checking if the capacity argument 
	 * is valid and initializing the elements array and copies the elements of the given collection.
	 * @param other collection whose elements are to be copied to this collection
	 * @param initialCapacity int value of the capacity requested
	 * @throws NullPointerException if the other collection is null
	 * @throws IllegalArgumentException if initialCapacity is less than 1
	 */
	public ArrayIndexedCollection(Collection<E> other, int initialCapacity) {
		this(initialCapacity);
		
		if (size > initialCapacity) {	// If the collection overstepped the requested capacity, the capacity must be increased to the collection's size.
			elements = Arrays.copyOf(elements, size);
		}
		
		addAll(Objects.requireNonNull(other)); // Copying the other collections elements to this collection
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementsGetter<E> createElementsGetter() {
		return new ArrayIterator<>(this);
	}
	
	/**
	 * Private static class <code>ArrayIterator</code> represents an implementation 
	 * of the ElementsGetter interface for iterating of ArrayIndexedCollection.
	 * 
	 * @author stipe
	 * @param <E>
	 */
	private static class ArrayIterator<E> implements ElementsGetter<E> {
		/**
		 * index of next element to be fetched
		 */
		private int index;
		/**
		 * array backed collection of all the elements
		 */
		private ArrayIndexedCollection<E> collection;
		/**
		 * number of elements in the collection
		 */
		private int size;
		/**
		 * modification count of the collection at the time of this iterator's creation
		 */
		private long savedModificationCount;
		
		/**
		 * The constructor that creates a new instance of <code>ArrayIterator</code> of the given collection.
		 * @param collection the collection to be iterated
		 */
		private ArrayIterator(ArrayIndexedCollection<E> collection) {
			this.collection = collection;
			this.size = collection.size;
			this.index = 0;
			this.savedModificationCount = collection.modificationCount;
		}

		/**
		 * {@inheritDoc}
		 * @throws ConcurrentModificationException if the collection has been modified since iterator was created
		 */
		@Override
		public boolean hasNextElement() {
			checkModifications();
			return index < size;
		}
		
		/**
		 * {@inheritDoc}
		 * @throws ConcurrentModificationException if the collection has been modified since iterator was created
		 * @throws NoSuchElementException if there are no unfetched elements left in the collection
		 */
		@Override
		public E getNextElement() {
			checkModifications();
			if (!hasNextElement()) {
				throw new NoSuchElementException();
			}
			return collection.elements[index++];
		}	
		
		/**
		 * Compares if the collection has been modified since the iterator's creation.
		 * @throws ConcurrentModificationException if the collection has been modified since iterator was created
		 * @see #getNextElement()
		 * @see #hasNextElement()
		 */
		private void checkModifications() {
			if (collection.modificationCount != savedModificationCount) {
				throw new ConcurrentModificationException();
			}
		}
	}
	
	/**
	 * Adds the given non-null object into this collection by adding the reference into first empty place in the elements array;
	 * if the elements array is full, it is reallocated by doubling its size, which is done in the private checkIfNullOrNoSpace method.
	 * Average complexity of this method is O(1), but its more when the array must be reallocated.
	 * @param value the object to be added into the collection
	 * @throws NullPointerException if null value is being added
	 */
	@Override
	public void add(E value) {
		checkIfNoSpace();
		elements[size] = Objects.requireNonNull(value);
		size++;
		modificationCount++;
	}
	
	/**
	 * Checks if there is enough space in the elements array for a new element.
	 * If not, the array is reallocated and its size is doubled.
	 */
	private void checkIfNoSpace() {
		if (size == elements.length) {
			elements = Arrays.copyOf(elements, 2 * elements.length);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * This method has the average complexity of O(1).
	 * @throws IndexOutOfBoundsException if index is not in [0, size-1]
	 */
	@Override
	public E get(int index) {
		checkIndexValidity(index, 0, size - 1);
		return elements[index];
	}
	
	/**
	 * Checks if the given index is a valid position in the collection.
	 * @param index int value of the position of the wanted object
	 * @throws IndexOutOfBoundsException if index is not in [minIndex, maxIndex]
	 * @see #remove(int)
	 * @see #get(int)
	 * @see #insert(Object, int)
	 */
	private void checkIndexValidity(int index, int minIndex, int maxIndex) {
		if (index < minIndex || index > maxIndex) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Removes all elements from the collection without changing the capacity of the array.
	 */
	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * The method works in conformance with the method add.
	 * The average complexity of this method is O(n/2) = O(n).
	 * @throws IndexOutOfBoundsException if argument position is not in [0, size]
	 * @throws NullPointerException if the given value is null
	 */
	@Override
	public void insert(E value, int position) {
		checkIndexValidity(position, 0, size);
		Objects.requireNonNull(value);
		checkIfNoSpace();
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
		size++;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * The average complexity of this method is O(n/2) = O(n).
	 * @see #contains(Object)
	 */
	public int indexOf(Object value) {
		int index = -1;
		for(int i = 0; i < size; i++) {
			if (elements[i].equals(value)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IndexOutOfBoundsException if index not in [0, size-1]
	 * @see #remove(Object)
	 */
	public void remove(int index) {
		checkIndexValidity(index, 0, size - 1);
		for (int i = index; i < size; i++) {
			elements[i] = elements[i + 1];
		}
		size--;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index > - 1) {
			remove(index); // remove(int index) does size-- and modCounter++ for this method
			return true;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf((Object[]) elements, size);	
	}

}

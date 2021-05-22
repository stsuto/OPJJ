package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class is an implementation of a resizable array-backed collection of objects.
 * Duplicate elements are allowed, but storage of null references is not.
 * An instance of this class can be created using a wanted capacity, other collection, both of those, but also neither of those,
 * that is, using any of the four constructors provided.
 * 
 * @author stipe
 */
public class ArrayIndexedCollection extends Collection{

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
	private Object[] elements;
	
	/**
	 * Default constructor that delegates the construction process to the private 
	 * constructor using default values as arguments.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY, new Collection());
	}
	
	/**
	 * Constructor that delegates the construction process to the private constructor,
	 * but requests the capacity of the collection to be set to the given value.
	 * @param initialCapacity int value of the capacity this collection should be set to
	 * @throws IllegalArgumentException if initialCapacity is less than 1
	 * 
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		this(initialCapacity, new Collection());
	}
	
	/**
	 * Constructor that delegates the construction process to the private constructor,
	 * but requests that all elements of the given collection be copied to this collection.
	 * @param other collection whose elements are to be copied to this collection
	 * @throws NullPointerException if the other collection is null
	 */
	public ArrayIndexedCollection(Collection other) {
		this(DEFAULT_CAPACITY, other);
	}
	
	/**
	 * Constructor that delegates the construction process to the private constructor,
	 * using the wanted capacity and the collection to be copied.
	 * @param other collection whose elements are to be copied to this collection
	 * @param initialCapacity int value of the capacity requested
	 * @throws NullPointerException if the other collection is null
	 * @throws IllegalArgumentException if initialCapacity is less than 1
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		this(initialCapacity, other);
	}
	
	/**
	 * The constructor that does the construction process in line with the given arguments.
	 * @param capacity int value of the capacity requested or given by default
	 * @param other collection whose elements are to be copied to this collection
	 * @throws NullPointerException if the other collection is null
	 * @throws IllegalArgumentException if initialCapacity is less than 1
	 */
	private ArrayIndexedCollection(int capacity, Collection other) {
		if (capacity < 1) {
			throw new IllegalArgumentException("The capacity must be greater than 0!");
		}
		elements = new Object[capacity]; // Declaration of the collections array
		addAll(Objects.requireNonNull(other)); // Copying the other collections elements to this colelction
		
		if (size > capacity) {	// If the collection overstepped the requested capacity, the capacity must be increased to the collection's size.
			elements = Arrays.copyOf(elements, size);
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
	public void add(Object value) {
		checkIfNullOrNoSpace(value);
		elements[size] = value;
		size++;
	}
	
	/**
	 * Checks if the value to be added is null and if there is enough space in the elements array for a new element.
	 * If not, the array is reallocated and returned.
	 * @param value non-null 
	 * @throws NullPointerException if null value is being added
	 * @return the elements array that was reallocated if necessary
	 */
	private Object[] checkIfNullOrNoSpace(Object value) {
		Objects.requireNonNull(value);
		if (size == elements.length) {
			elements = Arrays.copyOf(elements, 2 * elements.length);
		}
		return elements;
	}
	
	/**
	 * Returns the object that is stored in backing array at position index.
	 * This method has the average complexity of O(1).
	 * @param index int value of the position of the wanted object
	 * @throws IndexOutOfBoundsException if index is not in [0, size-1]
	 * @return object from the array at index index
	 */
	public Object get(int index) {
		checkIndexValidity(index, 0, size - 1);
		return elements[index];
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
	}
	
	/**
	 * Inserts without overwriting the given value at the given position in array.
	 * The method works in conformance with the method add.
	 * The average complexity of this method is O(n/2) = O(n).
	 * @param value object to be added 
	 * @param position the index in the array that the value should be added to
	 * @throws IndexOutOfBoundsException if argument position is not in [0, size]
	 */
	public void insert(Object value, int position) {
		checkIndexValidity(position, 0, size);
		checkIfNullOrNoSpace(value);
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
		size++;
	}
	
	/**
	 * Checks if the given position is valid, that is, if it's in the defined interval [minIndex, maxIndex].
	 * @param position the index in question
	 * @param minIndex the lowest value of index allowed
	 * @param maxIndex the highest value of index allowed
	 * @throws IndexOutOfBoundsException if the position is not in the defined interval
	 * @see #get(int)
	 * @see #insert(Object, int)
	 * @see #remove(int)
	 */
	private void checkIndexValidity(int position, int minIndex, int maxIndex) {
		if (position < minIndex || position > maxIndex) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Searches the collection and returns the index of the first occurrence of the given value.
	 * If the value is equal to an element in the collection is determined by using <code>equals</code> method.
	 * The average complexity of this method is O(n/2) = O(n).
	 * @param value the value whose index we are searching for
	 * @return int value of the index of the given value, or <code>-1</code> if the value isn't present
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
	 * Removes element at specified index from collection. Element that was previously at location index+1
	 * after this operation is on location index, etc.
	 * @param index int value of the position at which the value to be deleted is
	 * @throws IndexOutOfBoundsException if index not in [0, size-1]
	 */
	public void remove(int index) {
		checkIndexValidity(index, 0, size - 1);
		for (int i = index; i < size; i++) {
			elements[i] = elements[i + 1];
		}
		size--;
	}
	
	/**
	 * Method that determines the number of currently stored objects in this collections.
	 * @return int value of the number of currently stored objects
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Determines if the collection contains the object in question.
	 * @param value the object to be tested if it is present in the collection
	 * @return <code>true</code> if the collection contains given value, otherwise returns <code>true</code>
	 */
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Removes one occurrence of the given value.
	 * @param value value the object of which one occurrence is to be removed
	 * @return <code>true</code> if the collection contained given value and removed one occurrence of it, otherwise returns <code>true</code>
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index > - 1) {
			remove(index);
			return true;
		}
		return false;
	}
	
	/**
	 * Allocates new array with size equal to the size of this collections, 
	 * fills it with collection content and returns the array. 
	 * @return array of objects from this collection
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);	
	}
	
	/**
	 * Ensures that all elements of this collection will be processed.
	 * @param processor the processor whose method process will be called
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}
}

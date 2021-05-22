package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * This class is an implementation of a linked list-backed collection of objects.
 * Duplicate elements are allowed, but storage of null references is not.
 * An instance of this class can be created using no arguments or with another collection as an argument.
 * 
 * @author stipe
 */
public class LinkedListIndexedCollection extends Collection {
	
	/**
	 * keeps the number of elements in this collection. 
	 */
	private int size;
	/**
	 * reference to the first node of the collection.
	 */
	private ListNode first;
	/**
	 * reference to the last node of the collection.
	 */
	private ListNode last;
	
	/**
	 * Class <code>ListNode</code> represents one node in a linked list of elements of a collection.
	 * 
	 * @author stipe
	 */
	private static class ListNode {
		/**
		 * reference to the ListNode that comes before this node in the collection
		 */
		ListNode previous;
		/**
		 * reference to the ListNode that comes before this node in the collection
		 */
		ListNode next;
		/**
		 * value of this ListNode
		 */
		Object value;
	}
	
	/**
	 * Default constructor that just creates an empty collection.
	 */
	public LinkedListIndexedCollection() {
		this.first = this.last = null;
	}
	
	/**
	 * Constructor that creates a collection with elements of the other collection.
	 * @param other collection whose elements are to be copied to the current collection
	 */
	public LinkedListIndexedCollection(Collection other) {
		this.first = this.last = null;
		addAll(other);
	}

	/**
	 * Adds the given non-null object into this collection at the end of collection; 
	 * newly added element becomes the element at the biggest index. 
	 * The complexity of this method is O(1).
	 * @param value the object to be added
	 * @throws NullPointerException if null is to be added
	 */
	@Override
	public void add(Object value) {
		ListNode newNode = new ListNode();
		newNode.value = Objects.requireNonNull(value);
		if (first == null) {
			first = newNode;
		} else {
			last.next = newNode;
			newNode.previous = last;
		}
		last = newNode;
		size++;
	}
	
	/**
	 * Returns the object that is stored in linked list at position index.
	 * This method never has the complexity greater than n/2+1
	 * @param index the object's position in the collection
	 * @throws IndexOutOfBoundsException if <code>index</code> is not in [0, <code>size</code>-1]
	 * @return value of the ListNode at position <code>index</code>
	 */
	public Object get(int index) {
		return getNode(index).value;
	}
	
	/**
	 * Private method that searches for the ListNode at position <code>index</code>.
	 * @param index the object's position in the collection
	 * @return ListNode at position <code>index</code>
	 */
	private ListNode getNode(int index) {
		checkIndexValidity(index, 0, size - 1);
		
		ListNode currentNode; // Declared on a larger scope so only one return statement is needed.
		if (index >= (size / 2)) {
			currentNode = last;
			int currentIndex = size - 1;
			
			while (currentIndex != index) {
				currentNode = currentNode.previous;
				currentIndex--;
			}

		} else {
			currentNode = first;
			int currentIndex = 0;
			
			while (currentIndex != index) {
				currentNode = currentNode.next;
				currentIndex++;
			}
		}
		
		return currentNode;
	}
	
	/**
	 * Checks if the given position is valid, that is, if it's in the defined interval [minIndex, maxIndex].
	 * @param position the index in question
	 * @param minIndex the lowest value of index allowed
	 * @param maxIndex the highest value of index allowed
	 * @throws IndexOutOfBoundsException if the position is not in the defined interval
	 * @see #getNode(int)
	 * @see #insert(Object, int)
	 */
	private void checkIndexValidity(int position, int minIndex, int maxIndex) {
		if (position < minIndex || position > maxIndex) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public void clear() {
		first = last = null;
		size = 0;
	}
	
	/**
 	 * Inserts without overwriting the given value at the given position in linked list.
	 * The method works in conformance with the method add.
	 * The average complexity of this method is O(n/2) = O(n).
	 * @param value object to be added 
	 * @param position the position in linked list that the value should be added at
	 * @throws IndexOutOfBoundsException if argument position is not in [0, size]
	 */
	public void insert(Object value, int position) {
		checkIndexValidity(position, 0, size);
		if (position == size) {	// If we wish to insert to index equal to size, we can just use add() as there is nothing there yet.
			add(value);
		} else {
			ListNode newNode = new ListNode();
			newNode.value = Objects.requireNonNull(value);
			
			ListNode current = getNode(position); // We get the node at index = position.
			
			if (position != 0) {
				newNode.previous = current.previous; // Restructuring list to insert the new node at the wanted position.
				newNode.previous.next = newNode;
			} else {
				first = newNode;
			}
			current.previous = newNode;
			newNode.next = current;
			
			size++;
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
		ListNode current = first;
		for (int i = 0; i < size; i++) {
			if (current.value.equals(value)) {
				index = i;
				break;
			} else {
				current = current.next;
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
		ListNode current = getNode(index); // Node to be removed.
		
		dereferenceNode(current); // We change the references so nothing has a reference to the node that should be removed.

		size--;
	}
	
	/**
	 * Private method that changes the references of the node's to be deleted previous and next nodes.
	 * @param current node to be deleted from the collection
	 */
	private void dereferenceNode(ListNode current) {
		if (!current.equals(first)) {
			current.previous.next = current.next; // The previous node now points to the next node.
		}
		if (!current.equals(last)) {
			current.next.previous = current.previous; // The next node points back to the previous node.
		}
	}
	
	/**
	 * Removes one occurrence of the given value.
	 * @param value value the object of which one occurrence is to be removed
	 * @return <code>true</code> if the collection contained given value and removed one occurrence of it, otherwise returns <code>true</code>
	 */
	@Override
	public boolean remove(Object value) {

		ListNode current = first;
		
		for (int i = 0; i < size; i++) {
			if (current.value.equals(value)) {
				size--;
				dereferenceNode(current); // We change the references so nothing has a reference to the node that should be removed.
				return true;
				
			} else {
				current = current.next;
			}
		}

		return false;
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
		return (indexOf(value) != -1); // If the method indexOf returns -1, there is no node with that value.
	}
	
	/**
	 * Allocates new array with size equal to the size of this collections, 
	 * fills it with collection content and returns the array. 
	 * @return array of objects from this collection
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		ListNode current = first;
		
		for (int i = 0; i < size; i++) {
			array[i] = current.value;
			current = current.next;
		}
		
		return array;
	}
	
	/**
	 * Ensures that all elements of this collection will be processed.
	 * @param processor the processor whose method process will be called
	 */
	@Override
	public void forEach(Processor processor) {
		Object[] array = toArray();
		for (int i = 0; i < size; i++) {
			processor.process(array[i]);
		}
	}
}

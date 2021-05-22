package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class is an implementation of a linked list-backed collection of objects.
 * Duplicate elements are allowed, but storage of null references is not.
 * An instance of this class can be created using no arguments or with another collection as an argument.
 * 
 * @author stipe
 */
public class LinkedListIndexedCollection<E> implements List<E> {
	
	/**
	 * keeps the number of elements in this collection. 
	 */
	private int size;
	/**
	 * reference to the first node of the collection.
	 */
	private ListNode<E> first;
	/**
	 * reference to the last node of the collection.
	 */
	private ListNode<E> last;
	
	/**
	 * number of modifications done to this collection
	 */
	private long modificationCount;
	
	/**
	 * Class <code>ListNode</code> represents one node in a linked list of elements of a collection.
	 * 
	 * @author stipe
	 */
	private static class ListNode<E> {
		/**
		 * reference to the ListNode that comes before this node in the collection
		 */
		ListNode<E> previous;
		/**
		 * reference to the ListNode that comes before this node in the collection
		 */
		ListNode<E> next;
		/**
		 * value of this ListNode
		 */
		E value;
	}
	
	/**
	 * Default constructor that just creates an empty collection.
	 */
	public LinkedListIndexedCollection() {
		this.first = this.last = null;
		modificationCount = 0;
	}
	
	/**
	 * Constructor that creates a collection with elements of the other collection.
	 * @param other collection whose elements are to be copied to the current collection
	 */
	public LinkedListIndexedCollection(Collection<E> other) {
		this.first = this.last = null;
		addAll(other);
		modificationCount = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementsGetter<E> createElementsGetter() {
		return new LinkedListIterator<>(this);
	}
	
	/**
	 * Private static class <code>LinkedListIterator</code> represents an implementation 
	 * of the ElementsGetter interface for iterating of LinkedListCollection.
	 * 
	 * @author stipe
	 */
	private static class LinkedListIterator<E> implements ElementsGetter<E> {
		/**
		 * node whose value is to be returned next
		 */
		private ListNode<E> current;
		/**
		 * modification count of the collection at the time of this iterator's creation
		 */
		private long savedModificationCount;
		/**
		 * linked list backed collection of all the elements
		 */
		private LinkedListIndexedCollection<E> collection;
			
		/**
		 * The constructor that creates a new instance of <code>LinkedListIterator</code> of the given collection.
		 * @param collection the collection to be iterated
		 */
		private LinkedListIterator(LinkedListIndexedCollection<E> collection) {
			this.collection = collection;
			this.current = collection.first;
			this.savedModificationCount = collection.modificationCount;
		}

		/**
		 * {@inheritDoc}
		 * @throws ConcurrentModificationException if the collection has been modified since iterator was created
		 */
		@Override
		public boolean hasNextElement() {
			checkModifications();
			return current != null;
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
			E value = current.value;
			current = current.next;
			return value;
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
	 * Adds the given non-null object into this collection at the end of collection; 
	 * newly added element becomes the element at the biggest index. 
	 * The complexity of this method is O(1).
	 * @param value the object to be added
	 * @throws NullPointerException if null is to be added
	 */
	@Override
	public void add(E value) {
		ListNode<E> newNode = new ListNode<>();
		newNode.value = Objects.requireNonNull(value);
		if (first == null) {
			first = newNode;
		} else {
			last.next = newNode;
			newNode.previous = last;
		}
		last = newNode;
		size++;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * This method never has the complexity greater than n/2+1
	 * @throws IndexOutOfBoundsException if <code>index</code> is not in [0, <code>size</code>-1]
	 * @return value of the ListNode at position <code>index</code>
	 */
	@Override
	public E get(int index) {
		return getNode(index).value;
	}
	
	/**
	 * Private method that searches for the ListNode at position <code>index</code>.
	 * @param index the object's position in the collection
	 * @throws IndexOutOfBoundsException if index is not in [0, size - 1]
	 * @return ListNode at position <code>index</code>
	 */
	private ListNode<E> getNode(int index) {
		checkIndexValidity(index, 0, size - 1);
		
		ListNode<E> currentNode; // Declared on a larger scope so only one return statement is needed.
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
	 * Checks if the given index is a valid position in the collection.
	 * @param index int value of the position of the wanted object
	 * @throws IndexOutOfBoundsException if index is not in [minIndex, maxIndex]
	 * @see #remove(int)
	 * @see #get(int)
	 */
	private void checkIndexValidity(int index, int minIndex, int maxIndex) {
		if (index < minIndex || index > maxIndex) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		first = last = null;
		size = 0;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * The method works in conformance with the method add.
	 * The average complexity of this method is O(n/2) = O(n).
	 * @throws IndexOutOfBoundsException if argument position is not in [0, size]
	 */
	@Override
	public void insert(E value, int position) {
		checkIndexValidity(position, 0, size);
		
		if (position == size) {	// If we wish to insert to index equal to size, we can just use add() as there is nothing there yet.
			add(value);
		} else {
			ListNode<E> newNode = new ListNode<>();
			newNode.value = Objects.requireNonNull(value);
			
			ListNode<E> current = getNode(position); // We get the node at index = position.
			
			if (position != 0) {
				newNode.previous = current.previous; // Restructuring list to insert the new node at the wanted position.
				newNode.previous.next = newNode;
			} else {
				first = newNode;
			}
			current.previous = newNode;
			newNode.next = current;
			
			size++;
			modificationCount++;
		}	
	}
	
	/**
	 * {@inheritDoc}
	 * The average complexity of this method is O(n/2) = O(n).
	 * @see #contains(Object)
	 */
	@Override
	public int indexOf(Object value) {
		int index = -1;
		ListNode<E> current = first;
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
	 * {@inheritDoc}
	 * @throws IndexOutOfBoundsException if index not in [0, size-1]
	 */
	public void remove(int index) {
		ListNode<E> current = getNode(index); // Node to be removed.
		
		dereferenceNode(current); // We change the references so nothing has a reference to the node that should be removed.

		size--;
		modificationCount++;
	}
	
	/**
	 * Private method that changes the references of the node's to be deleted previous and next nodes.
	 * @param current node to be deleted from the collection
	 * @see #remove(int) 
	 * @see #remove(Object)
	 */
	private void dereferenceNode(ListNode<E> current) {
		if (!current.equals(first)) {
			current.previous.next = current.next; // The previous node now points to the next node.
		}
		if (!current.equals(last)) {
			current.next.previous = current.previous; // The next node points back to the previous node.
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object value) {

		ListNode<E> current = first;
		
		for (int i = 0; i < size; i++) {
			if (current.value.equals(value)) {
				size--;
				modificationCount++;
				dereferenceNode(current); // We change the references so nothing has a reference to the node that should be removed.
				return true;
				
			} else {
				current = current.next;
			}
		}
		
		return false;
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
		return (indexOf(value) != -1); // If the method indexOf returns -1, there is no node with that value.
	}
	
	/**
	 * {@inheritDoc}
	 */

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		ListNode<E> current = first;
		
		for (int i = 0; i < size; i++) {
			array[i] = current.value;
			current = current.next;
		}
		
		return array;
	}
	
}

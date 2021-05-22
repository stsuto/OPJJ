package hr.fer.zemris.java.custom.collections;

/**
 * Interface <code>List</code> offers methods for creating a list based collection.
 * 
 * @author stipe
 */
public interface List<E> extends Collection<E> {

	/**
	 * Returns the element the specified position in this list.
	 * @param index int value of the position of the element to return
	 * @return object from the collection at index index
	 */
	E get(int index);
	
	/**
	 * Inserts without overwriting the given value at the given position in collection.
	 * @param value element to be added 
	 * @param position the index in the collection that the value should be added to
	 */
	void insert(E value, int position);
	
	/**
	 * Searches the collection and returns the index of the first occurrence of the given value.
	 * @param value the value whose index we are searching for
	 * @return int value of the index of the given value, or <code>-1</code> if the value isn't present
	 */
	int indexOf(Object value);
	
	/**
	 * Removes element at specified index from this list. Element that was previously at location index+1
	 * after this operation is on location index, etc.
	 * @param index int value of the position at which the value to be deleted is
	 */
	void remove(int index);
	
}

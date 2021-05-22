package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * The <code>Dictionary</code> class is a collection used for mapping keys to values.
 * Every key and every value is an object. In any one Dictionary object, every key is associated with at most one value.
 * Given a Dictionary and a key, the associated element can be looked up.
 * Any non-null object can be used as a key, but value can be null.
 * 
 * @author stipe
 *
 * @param <K> type of key in <code>Dictionary</code>
 * @param <V> type of value in <code>Dictionary</code>
 */
public class Dictionary<K, V> {
	
	/**
	 * list of every <code>Record</code> in dictionary
	 */
	private ArrayIndexedCollection<Record<K, V>> collection;
	
	/**
	 * Constructor.
	 */
	public Dictionary() {
		collection = new ArrayIndexedCollection<>();
	}
	
	/**
	 * Method that checks if the dictionary is empty.
	 * 
	 * @return <code>true</code> if dictionary contains no objects and <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	
	/**
	 * Method that determines the number of currently stored elements in this dictionary.
	 * 
	 * @return int value of the number of currently stored element
	 */
	public int size() {
		return collection.size();
	}
	
	/**
	 * Removes all elements from this dictionary.
	 */
	public void clear() {
		collection.clear();
	}
	
	/**
	 * Adds a new <code>Record</code> with the given <code>key</code> and <code>value</code> into the dictionary.
	 * If the given key already has a value, it is overwritten with the new one.
	 * 
	 * @param key new <code>Record</code>'s key
	 * @param value new <code>Record</code>'s value
	 * @throws NullPointerException if the given key is null
	 */
	public void put(K key, V value) {
		ElementsGetter<Record<K, V>> getter = collection.createElementsGetter();
		while (getter.hasNextElement()) {
			Record<K, V> record = getter.getNextElement();
			if (record.key.equals(key)) {
				record.value = value;
				return;
			}
		}
		collection.add(new Record<K, V>(key, value));
	}
	
	/**
	 * Returns the value mapped to the given key.
	 * The value returned is null in cases when the key isn't in the dictionary or if it is present but its value is null.
	 *
	 * @param key key to which the value is mapped
	 * @return the value which is mapped to the given key, or null if the key isnt present in the dictionary
	 */
	public V get(Object key) {
		ElementsGetter<Record<K, V>> getter = collection.createElementsGetter();
		while (getter.hasNextElement()) {
			Record<K, V> record = getter.getNextElement();
			if (record.key.equals(key)) {
				return record.value;
			}
		}
		return null;
	}
	
	/**
	 * Class <code>Record</code> represents one record or entry in <code>Dictionary</code>.
	 * 
	 * @author stipe
	 *
	 * @param <K> type of key in <code>Record</code>
	 * @param <V> type of value in <code>Record</code>
	 */
	private static class Record<K, V> {
		/**
		 * the record's key
		 */
		private K key;
		/**
		 * the record's value
		 */
		private V value;
		
		/**
		 * Constructor which creates a new instance of <code>Record</code> with the given key and value.
		 * 
		 * @param key key of newly created <code>Record</code>
		 * @param value value of newly created <code>Record</code>
		 */
		private Record(K key, V value) {
			this.key = Objects.requireNonNull(key);
			this.value = value;
		}		
	}

}

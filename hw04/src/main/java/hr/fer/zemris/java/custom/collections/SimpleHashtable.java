package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Class <code>SimpleHashTable</code> represents a hash table which enables storage of mapping pairs (key -> value).
 * Instances of this class can be created using the wanted table size or a table with predefined capacity if not specified otherwise.
 * Table size will be the size of the smallest power of 2, equal or greater to the given capacity.
 * 
 * @author stipe
 *
 * @param <K> type of key in <code>SimpleHashTable</code>
 * @param <V> type of value in <code>SimpleHashTable</code>
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {

	/**
	 * number of entries in this table
	 */
	private int size;
	/**
	 * table which stores all entries
	 */
	private TableEntry<K,V>[] table;
	/**
	 * counter of modifications made to this hash table
	 */
	private int modificationCount;
	
	/**
	 * The default capacity of the table, used when the wanted capacity is not specified.
	 */
	private static final int DEFAULT_CAPACITY = 16;
	/**
	 * The percentage threshold of capacity filled at which rehashing happens.
	 */
	private static final double REHASH_TRESHOLD = 0.75;
	/**
	 * Multiplier which determines how much will the table increase during rehashing.
	 */
	private static final int REHASH_MULTIPLIER = 2;
	
	/**
	 * Default constructor that creates an instance of this class using the default size of the table.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructor that creates an instance of this class with table with size of the smallest power of 2, 
	 * equal or greater to the given capacity.
	 * 
	 * @param capacity used for the size of the table
	 * @throws IllegalArgumentException if given capacity is less than 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("Table capacity must be bigger!");
		}
		int tableCapacity = (int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2))); // 2 raised to the smallest whole number bigger than base-2 logarithm of the given capacity 
		table = (TableEntry<K, V>[]) new TableEntry<?, ?>[tableCapacity];
	}

	/**
	 * If no entry with the given key is present, a new entry is created and added into the table.
	 * If the key is present, the old value mapped to that key is overwritten with the new, given value.
	 * After the number of entries has surpassed the {@link #REHASH_TRESHOLD}, {@link #rehash()} happens. 
	 * 
	 * @param key new entry's key
	 * @param value new entry's value
	 * @throws NullPointerException if the given key is null
	 */
	public void put(K key, V value) {
		int index = Math.abs(Objects.requireNonNull(key).hashCode()) % table.length;
		TableEntry<K, V> entry = table[index];
		if (table[index] != null) {
			while (entry.next != null) { // try to find the key to be added
				if (entry.key.equals(key)) {
					entry.value = value;
					return; 			 // if the key already existed, update it's value and return from the method
				}
				entry = entry.next;
			}
			entry.next = new TableEntry<>(key, value); // if the key didn't previously exist, add it with the given value to the next spot
		
		} else {
			table[index] = new TableEntry<>(key, value); // if there are no elements here, just add the new pair
		}
		modificationCount++;
		if (++size >= REHASH_TRESHOLD * table.length) { // increase the value and check if the table should be increased
			rehash();
		}
	}
	
	/**
	 * Increases the capacity of the hash table by {@link #REHASH_MULTIPLIER} 
	 * so the hashing is more efficient and the average complexity stays O(1).
	 * Rehashes all existing entries so their locations in the table don't stay the same.
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		TableEntry<K, V>[] oldTable = Arrays.copyOf(table, table.length);
		table = (TableEntry<K, V>[]) new TableEntry<?, ?>[REHASH_MULTIPLIER * oldTable.length];
		size = 0; // size is set to 0 so when all entries are put back in, they increase the size back to the real number
		
		for (int i = 0; i < oldTable.length; i++) {
			if (oldTable[i] == null) { // skip all indices in the table that dont have anything
				continue;
			}
			TableEntry<K, V> entry = oldTable[i];
			while (entry != null) { // add all elements from the old table to the new one
				put(entry.key, entry.value);
				entry = entry.next;
			}
		}
		modificationCount++;
	}

	/**
	 * Returns the value mapped to the given key.
	 * The value returned is null in cases when the key isn't in the dictionary or if it is present but its value is null.
	 *
	 * @param key key to which the value is mapped
	 * @return the value which is mapped to the given key, or null if the key isnt present in the dictionary
	 */
	public V get(Object key) {
		if (key != null) {			
			int index = Math.abs(key.hashCode()) % table.length;
			TableEntry<K, V> entry = table[index];
			while (entry != null) {
				if (entry.key.equals(key)) {
					return entry.value;
				}
				entry = entry.next;
			}
		}
		return null;
	}
	
	/**
	 * Returns the number of entries in hash table.
	 * 
	 * @return size of table
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks if this hash table contains the given key.
	 * 
	 * @param key the key whose presence is checked
	 * @return <code>true</code> if the hash table contains given key, otherwise returns <code>false</code>
	 */
	public boolean containsKey(Object key) {
		if (key != null) {	
			int index = Math.abs(key.hashCode()) % table.length;
			TableEntry<K, V> entry = table[index];
			while (entry != null) {
				if (entry.key.equals(key)) {
					return true;
				}
				entry = entry.next;
			}
		}
		return false;
	}
	
	/**
	 * Checks if this hash table contains the given value.
	 * 
	 * @param value the value whose presence is checked
	 * @return <code>true</code> if the hash table contains given value, otherwise returns <code>false</code>
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> entry : table) {
			while (entry != null) {
				if (entry.value.equals(value)) {
					return true;
				}
				entry = entry.next;
			}
		}
		return false;
	}
	
	/**
	 * Removes the entry whose key is equal to the given key.
	 * 
	 * @param key key of the entry to be removed
	 */
	public void remove(Object key) {
		if (key != null) {
			int index = Math.abs(key.hashCode()) % table.length;
			TableEntry<K, V> entry = table[index];
			TableEntry<K, V> prevEntry = null;
			while (entry != null) {
				if (entry.key.equals(key)) {
					if (prevEntry == null) { // if the entry to be removed is the first entry in the slot
						table[index] = entry.next;
					} else {
						prevEntry.next = entry.next;
					}
					size--;
				}
				prevEntry = entry;
				entry = entry.next;
			}
		}
		modificationCount++;
	}	
	
	/**
	 * Checks if this hash table is empty.
	 * 
	 * @return <code>true</code> if the hash table is empty, otherwise returns <code>false</code>
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Creates a String representation of this hash table used for printing its content.
	 * 
	 * @return String of this hash table's content
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) {
				continue;
			}
			TableEntry<K, V> entry = table[i];
			while (entry != null) {
				sb.append(entry.key + "=" + entry.value + ", ");
				entry = entry.next;
			}
		}
		if (sb.length() > 1) { // if anything was added after "["
			sb.setLength(sb.length() - 2); // deletes the last ", "
		}
		return sb.append("]").toString();	
	}
	
	/**
	 * Removes all entries from the hash table.
	 */
	public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        modificationCount++;
        size = 0;
	}
	
	/**
	 * Creates and returns an iterator of entries from this hash table.
	 * 
	 * 
	 * @return Iterator of <code>TableEntries</code> from this hash table.
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Class <code>IteratorImpl</code> is the iterator used for iterating content of instances of <code>SimpleHashTable</code>.
	 * The iterator works with <code>TableEntry</code> and implements methods {@link #hasNext()}, {@link #next()}, and {@link #remove()}.
	 * 
	 * @author stipe
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {
		/**
		 * The index of the slot in the table at which the current element is.
		 */
		private int index;
		/**
		 * The number of iterated entries that haven't been deleted from within the iterator.
		 */
		private int iterated;
		/**
		 * The number of modifications made to this hash table from the moment this iterator was created, 
		 * plus the modifications from within this iterator.
		 */
		private int modCount;
		/**
		 * The entry which represents the next entry to be returned.
		 */
		private TableEntry<K, V> entry;
		/**
		 * The last returned entry.
		 */
		private TableEntry<K, V> lastReturned;
		
		/**
		 * Default constructor used for saving the number of modifications at the time of this iterator's creation.
		 */
		public IteratorImpl() {
			modCount = modificationCount;
		}
		
		/**
		 * Checks if there are more entries in this collection.
		 * 
		 * @return <code>true</code> if there are more entries, otherwise returns <code>false</code>
		 */
		public boolean hasNext() {
			checkModifications();
			return iterated < size;
		}

		/**
		 * Gets the next entry and returns it.
		 * 
		 * @return the next {@link #entry}
		 */
		public SimpleHashtable.TableEntry<K, V> next() {
			checkModifications();
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			while (entry == null) { // if first call or if last returned was the last on that index, go to next index with something in it
				entry = table[index++];
			}
			lastReturned = entry; // entry to be returned
			entry = entry.next; // entry that should be returned unless it's the last in this slot
			iterated++; // counter of how many entries have been sent
			return lastReturned;
		}
		
		/**
		 * Removes the entry whose key is equal to the given key, but does it so this iterator can still keep iterating.
		 * 
		 * @param key key of the entry to be removed
		 * @throws IllegalStateException if this method is called twice without {@link #next()} being called, 
		 * or if it is called before first {@link #next()} was called
		 */
		public void remove() {
			if (lastReturned == null) {
				throw new IllegalStateException("That element has already been returned!");
			}
			SimpleHashtable.this.remove(lastReturned.key);
			modCount++; // hash table's modification counter was increased so this one should too
			iterated--; // as the size of the hash table has decreased, this counter must as well so all entries can be iterated through
			lastReturned = null; // sets the ariable to null so if this method is called immediately again exception is thrown
		}

		/**
		 * Checks if there have been any modifications to this hash table from out of this iterator.
		 */
		private void checkModifications() {
			if (modCount != modificationCount) {
				throw new ConcurrentModificationException();
			}
		}
		
	}

	/**
	 * Class <code>TableEntry</code> represents a single entry consisting of key-value pair.
	 * 
	 * @author stipe
	 *
	 * @param <K> type of key in <code>TableEntry</code>
	 * @param <V> type of value in <code>TableEntry</code>
	 */
	public static class TableEntry<K, V> {
		/**
		 * the entry's key
		 */
		private K key;
		/**
		 * the entry's value
		 */
		private V value;
		/**
		 * next entry
		 */
		private TableEntry<K, V> next;
		
		/**
		 * Constructor which creates a new <code>TableEntry</code> object with the pair of given parameters.
		 * 
		 * @param key new entry's key
		 * @param value new entry's value
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return V {@link #value} of this entry
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets this entry's {@link #value} to the given parameter.
		 * @param value
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * @return K {@link #key} of this entry.
		 */
		public K getKey() {
			return key;
		}
	}

	
}

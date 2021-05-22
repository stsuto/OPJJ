package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code ObjectMultistack} represents a map of stack-like abstractions.
 * The keys used for this map are {@code String} objects, and the values are 
 * stack-like structures of {@link ValueWrapper} objects.
 * <p>
 * The {@code ObjectMultistack} can't contain null keys or values, but it can contain a
 * {@code ValueWrapper} containing a null value.
 * 
 * @author stipe
 *
 */
public class ObjectMultistack {

	/**
	 * Map which maps stacks of {@code ValueWrapper} objects to {@code String} keys.
	 */
	private Map<String, MultistackEntry> map;
	
	/**
	 * Constructor which initializes the map.
	 */
	public ObjectMultistack() {
		 map = new HashMap<>();
	}

	/**
	 * Adds a new {@code ValueWrapper} object to the stack mapped to the given
	 * {@code String} key.
	 * 
	 * @param keyName key of {@link #map}
	 * @param value value of {@link #map}
	 * @throws NullPointerException if given key or value are null
	 */
	public void push(String keyName, ValueWrapper value) {
		MultistackEntry entry = new MultistackEntry(
				Util.requireNonNull(value, "Value"),
				map.get(Util.requireNonNull(keyName, "Key")));
		
		map.put(keyName, entry);
	}

	/**
	 * Removes the top-most object from the stack mapped to the given key.
	 * 
	 * @param keyName the key to which the stack is mapped to
	 * @return the removed {@code ValueWrapper}
	 * @throws NullPointerException if the given key is null
	 * @throws EmptyStackException if the stack mapped to that string is empty
	 */
	public ValueWrapper pop(String keyName) {
		MultistackEntry entry = peekEntry(Util.requireNonNull(keyName, "Key"));
		
		if (entry.next == null) {
			map.remove(keyName);
		} else {
			map.put(keyName, entry.next);			
		}
		
		return entry.value;
	}

	/**
	 * Returns the top-most object from the stack mapped to the 
	 * given key without removing the object from the stack.
	 * 
	 * @param keyName the key to which the stack is mapped to
	 * @return the top-most {@code ValueWrapper}
	 * @throws EmptyStackException if the stack mapped to that string is empty 
	 */
	public ValueWrapper peek(String keyName) {
		return peekEntry(keyName).value;
	}

	/**
	 * Returns the stack entry containing the top-most value
	 * mapped to the stack with the given key, without removing
	 * the object from the stack.
	 * 
	 * @param keyName the key to which the stack is mapped to
	 * @return the {@code MultistackEntry} containing the top-most {@code ValueWrapper}
	 * @throws EmptyStackException if the stack mapped to that string is empty 
	 */
	private MultistackEntry peekEntry(String keyName) {
		if (!map.containsKey(keyName)) {
			throw new EmptyStackException();
		}
		
		return map.get(keyName);
	}

	/**
	 * Checks if the stack mapped to the given key is empty.
	 * 
	 * @param keyName the key to which the stack is mapped to
	 * @return {@code true} if the stack is empty, {@code false} if not
	 */
	public boolean isEmpty(String keyName) {
		return map.get(keyName) == null;
	}

	/**
	 * Structure used for simulation of a stack. It stores a reference to 
	 * a {@link ValueWrapper} object and a reference to the next 
	 * {@code MultistackEntry} object.
	 * 
	 * @author stipe
	 *
	 */
	private static class MultistackEntry {
		/**
		 * Wrapped value on stack.
		 */
		ValueWrapper value;
		/**
		 * The next stack entry.
		 */
		MultistackEntry next;

		/**
		 * Constructors that initializes the properties.
		 * 
		 * @param value {@link #value}
		 * @param next {@link #next}
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
	}

}

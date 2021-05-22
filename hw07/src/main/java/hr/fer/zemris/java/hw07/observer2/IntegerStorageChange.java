package hr.fer.zemris.java.hw07.observer2;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code IntegerStorageChange} represents one change of {@link IntegerStorage} state.
 * It encapsulates a reference to the {@code IntegerStorage}, the value of stored integer 
 * before the change has occurred, and the new value of currently stored integer.
 * 
 * @author stipe
 *
 */
public class IntegerStorageChange {

	/**
	 * Storage of current integer.
	 */
	private IntegerStorage storage;
	/**
	 * Value stored before the change.
	 */
	private int oldValue;
	/**
	 * Value stored after the change.
	 */
	private int newValue;
	
	/**
	 * Constructor accepting the given properties.
	 * 
	 * @param storage {@link #storage}
	 * @param oldValue {@link #oldValue}
	 * @param newValue {@link #newValue}
	 */
	public IntegerStorageChange(IntegerStorage storage, int oldValue, int newValue) {
		this.storage = Util.requireNonNull(storage, "Storage");
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Getter for the storage.
	 * 
	 * @return {@link #storage}
	 */
	public IntegerStorage getStorage() {
		return storage;
	}

	/**
	 * Getter for the old value.
	 * 
	 * @return {@link #oldValue}
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Getter for the new value.
	 * 
	 * @return {@link #newValue}
	 */
	public int getNewValue() {
		return newValue;
	}
	
}

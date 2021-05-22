package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code IntegerStorage} is the Subject of the observer pattern. 
 * It represents a storage of one integer value which can't be then used with
 * some observer objects.
 * 
 * @author stipe
 *
 */
public class IntegerStorage {
	
	/**
	 * Stored value.
	 */
	private int value;
	/**
	 * Observers currently observing this storage.
	 */
	private List<IntegerStorageObserver> observers;
	/**
	 * Observers that were observing this storage during the last modification.
	 * Used for concurrent modification of observer list.
	 */
	private List<IntegerStorageObserver> copiedObservers;
	
	/**
	 * Constructor which sets this storage's value to the given value.
	 * 
	 * @param initialValue value to which {@link #value} is set
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds the given observer to the list of this storage's observers.
	 * 
	 * @param observer observer to be added to {@link #observers}
	 * @throws NullPointerException if the given parameter is null
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observers == null) {
			observers = new ArrayList<>();
		}
		if (!observers.contains(Util.requireNonNull(observer, "Observer"))) {
			observers.add(observer);
		}
	}

	/**
	 * Removes the given observer from the list of this storage's observers.
	 * 
	 * @param observer observer to be removed from {@link #observers}
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Removes all observers from the list of this storage's observers.
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * Returns the value stored in this storage.
	 * 
	 * @return {@link #value}
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets this storage's value to the given parameter.
	 * 
	 * @param value value to which {@link #value} is set to 
	 */
	public void setValue(int value) {
		// Only if the value actually changed.
		if (this.value != value) {
			IntegerStorageChange storageChange = new IntegerStorageChange(this, this.value, value);
			this.value = value;
			if (observers != null) {
				// If observers changed in the last modification, copiedObservers must be changed too.
				// This ensures writing operations happen only when really necessary.
				if (!observers.equals(copiedObservers)) {
					copiedObservers = new ArrayList<>(observers);
				}
				for (IntegerStorageObserver observer : copiedObservers) {
					observer.valueChanged(storageChange);
				}
			}
		}
	}
}
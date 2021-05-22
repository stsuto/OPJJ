package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Class representing the list model used for prime number listing.
 * 
 * @author stipe
 *
 */
public class PrimListModel implements ListModel<Integer> {

	/**
	 * Last generated prime number.
	 */
	private int lastPrim;
	/**
	 * List of prime numbers.
	 */
	private List<Integer> prims;
	/**
	 * Listeners connected to this model.
	 */
	private List<ListDataListener> listeners;
		
	/**
	 * Constructor.
	 */
	public PrimListModel() {
		this.lastPrim = 1;
		this.prims = new ArrayList<>();
		prims.add(lastPrim);
		this.listeners = new ArrayList<>();
	}

	@Override
	public int getSize() {
		return prims.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return prims.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	/**
	 * Adds the next prime number to the list.
	 */
	public void next() {
		prims.add(getNthPrime(getSize()));
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getSize(), getSize());
		listeners.forEach(l -> l.intervalAdded(event));
	}
	
	/**
	 * Finds and returns the n-th prime number.
	 * 
	 * @param n the index of the wanted prime number
	 * @return the n-th prime number
	 */
	private Integer getNthPrime(int n) {
		int primeCounter = 0, current = lastPrim + 1;
		while (true) {
			if (isPrime(current)) {
				if (++primeCounter == n) {
					return current;
				}
			}
			// If current number is 2, then increment by one to continue. If it is more than 2,
			// increment by 2 since even numbers after 2 can't be prime.
			current += current == 2 ? 1 : 2;
		}
	}
	
	/**
	 * Checks if the current number is prime.
	 * 
	 * @param current the number which is checked if prime
	 * @return {@code true} if the given numebr is prime, {@code false} otherwise
	 */
	private boolean isPrime(int current) {
	    for(int i = 2; i < Math.sqrt(current); ++i) {
	        if (current % i == 0) {
	            return false;
	        }
	    }
	    return true;
	}
	
}

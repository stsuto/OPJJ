package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class {@code PrimesCollection} represents a collection of prime numbers 
 * which the user can use to get the n-th prime number starting from 2.
 * 
 * @author stipe
 *
 */
public class PrimesCollection implements Iterable<Integer>{

	/**
	 * The index of the wanted prime number.
	 */
	private int number;
	
	/**
	 * Constructor that initializes this classes property using the given parameter.
	 * 
	 * @param number {@link #number}
	 */
	public PrimesCollection(int number) {
		this.number = number;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new PrimesCollectionIterator();
	}

	/**
	 * Class {@code PrimesCollectionIterator} is an implementation of an iterator
	 * of prime numbers in this {@code PrimesCollection}.
	 * 
	 * @author stipe
	 *
	 */
	private class PrimesCollectionIterator implements Iterator<Integer>{
		/**
		 * The number of already found prime numbers.
		 */
		int foundPrimes = 0;

		@Override
		public boolean hasNext() {
			return foundPrimes < number;
		}

		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException("Iterated through all elements!");
			}
			return getNthPrime(++foundPrimes);
		}

		/**
		 * Finds and returns the n-th prime number.
		 * 
		 * @param n the index of the wanted prime number
		 * @return the n-th prime number
		 */
		private Integer getNthPrime(int n) {
			int primeCounter = 0, current = 2;
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
		    for(int i = 2; i < current; ++i) {
		        if (current % i == 0) {
		            return false;
		        }
		    }
		    return true;
		}
	}

}

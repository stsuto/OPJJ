package searching.slagalica;

import java.util.Arrays;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code KonfiguracijaSlagalice} represents one state of the puzzle, that is
 * one configuration of puzzle blocks.
 * 
 * @author stipe
 *
 */
public class KonfiguracijaSlagalice {

	/**
	 * Array of numbers keeping the positions of numbers within the puzzle.
	 */
	private int[] config;

	/**
	 * Constructor which initializes the configuration via the given paramter.
	 * 
	 * @param config {@link #config}
	 */
	public KonfiguracijaSlagalice(int[] config) {
		if (Util.requireNonNull(config, "Configuration").length != 9) {
			throw new IllegalArgumentException(
					"The configuration must be an array of length 9!");
		}
		this.config = config;
	}
	
	/**
	 * Returns the copy of this configuration.
	 * 
	 * @return configuration
	 */
	public int[] getPolje() {
		return Arrays.copyOf(config, config.length);
	}
	
	/**
	 * Finds and returns the index of 0/space in the puzzle.
	 * 
	 * @return index of 0, or space
	 * @throws RuntimeException if configuration doesnt contain space
	 */
	public int indexOfSpace() {
		for (int i = 0; i < config.length; i++) {
			if (config[i] == 0) {
				return i;
			}
		}
		throw new RuntimeException("Configuration doesn't contain space!");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < config.length; i++) {
			if (i % 3 != 2) {
				sb.append((config[i] == 0 ? "*" : config[i]) + " ");				
			} else {
				sb.append((config[i] == 0 ? "*" : config[i]) + "\n");
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(config);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KonfiguracijaSlagalice))
			return false;
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(config, other.config);
	}
	
	
}

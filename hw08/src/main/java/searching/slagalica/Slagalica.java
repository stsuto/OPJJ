package searching.slagalica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

/**
 * Class {@code Slagalica} represents the solving process of the puzzle.
 * It contains methods which contain actions which are performed to solve
 * the puzzle.
 * 
 * @author stipe
 *
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>, 
								  Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, 
								  Predicate<KonfiguracijaSlagalice> {

	/**
	 * Puzzle's configuration.
	 */
	private KonfiguracijaSlagalice config;
	
	/**
	 * Constructor initializing the puzzle's configuration with the given
	 * parameter
	 * 
	 * @param config {@link #config}
	 */
	public Slagalica(KonfiguracijaSlagalice config) {
		this.config = config;
	}

	/**
	 * {@inheritDoc} <p>
	 * In this implementation, checks if the goal has been reached.
	 */
	@Override
	public boolean test(KonfiguracijaSlagalice state) {
		int[] field = state.getPolje();
		int fieldLength = field.length;
		for (int i = 0; i < fieldLength; i++) {
			if (field[i] != (i + 1) % fieldLength) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc} <p>
	 * In this implementation, finds transitions of neighboring solutions.
	 */
	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice state) {
		List<Transition<KonfiguracijaSlagalice>> succ = new ArrayList<>();
		int index = state.indexOfSpace();
		int[] field = state.getPolje();
		
		if (index % 3 != 0) {
			int[] changedField = swap(field, index, index - 1);
			succ.add(new Transition<>(new KonfiguracijaSlagalice(changedField), 1));
		}
		if (index % 3 != 2) {
			int[] changedField = swap(field, index, index + 1);
			succ.add(new Transition<>(new KonfiguracijaSlagalice(changedField), 1));
		}
		if (index / 3 != 0) {
			int[] changedField = swap(field, index, index - 3);
			succ.add(new Transition<>(new KonfiguracijaSlagalice(changedField), 1));
		}
		if (index / 3 != 2) {
			int[] changedField = swap(field, index, index + 3);
			succ.add(new Transition<>(new KonfiguracijaSlagalice(changedField), 1));
		}
		
		return succ;
	}

	/**
	 * Swaps two elements within the array.
	 * 
	 * @param field the array
	 * @param first first element
	 * @param second second element
	 * @return new array with swapped elements
	 */
	private int[] swap(int[] field, int first, int second) {
		int[] changedField = Arrays.copyOf(field, field.length);
		
		int temp = changedField[first];
		changedField[first] = changedField[second];
		changedField[second] = temp;
		
		return changedField;
	}

	/**
	 * {@inheritDoc} <p>
	 * In this implementation, returns the starting configuration.
	 */
	@Override
	public KonfiguracijaSlagalice get() {
		return config;
	}
	
	
	
	
	
}

package searching.algorithms;

/**
 * Class {@code Transition} represents a decorated pair of a state and
 * the cost to get to that state. 
 * 
 * @author stipe
 *
 * @param <S> type of value of state
 */
public class Transition<S> {

	/**
	 * This transition's state.
	 */
	private S state;
	/**
	 * The cost of this transition's state.
	 */
	private double cost;
	
	/**
	 * Constructor which initializes the properties with the given parameters.
	 * 
	 * @param state {@link #state}
	 * @param cost {@link #cost}
	 */
	public Transition(S state, double cost) {
		this.state = state;
		this.cost = cost;
	}

	/**
	 * Getter for this transitions' state.
	 * 
	 * @return {@link #state}
	 */
	public S getState() {
		return state;
	}
	
	/**
	 * Getter for this transitions' cost.
	 * 
	 * @return {@link #cost}
	 */
	public double getCost() {
		return cost;
	}

}

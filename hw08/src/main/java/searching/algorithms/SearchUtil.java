package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code SearchUtil} offers bfs and bfsv algorithms used
 * for space exploration.
 * 
 * @author stipe
 *
 */
public class SearchUtil {

	/**
	 * Breadth-First Search explores neighboring solutions before 
	 * further spreading.
	 * 
	 * @param s0 starting state
	 * @param succ function that finds neighboring states
	 * @param goal predicate which tests whether or not the goal has been reached
	 * @return node containing the final state, and references to its parent node,
	 * enabling recreation of steps
	 * @throws NullPointerException if any of the parameters are null
	 */
	public static <S> Node<S> bfs(Supplier<S> s0,Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		LinkedList<Node<S>> toBeExplored = new LinkedList<>(); 
		toBeExplored.add(new Node<S>(null, Util.requireNonNull(s0, "Starting state").get(), 0));
		
		while (!toBeExplored.isEmpty()) {
			Node<S> ni = toBeExplored.removeFirst();
			if (Util.requireNonNull(goal, "Goal").test(ni.getState())) {
				return ni;
			}
			List<Transition<S>> transitions = Util.requireNonNull(succ, "Function").apply(ni.getState());
			transitions.forEach(s -> toBeExplored.add(new Node<S>(ni, s.getState(), 
									ni.getCost() + s.getCost())));
		}
		
		return null;
	}
	
	/**
	 * BFSV is an improved version of BFS as it memorizes the already explored
	 * states and ignores them in further exploration.
	 * 
	 * @param s0 starting state
	 * @param succ function that finds neighboring states
	 * @param goal predicate which tests whether or not the goal has been reached
	 * @return node containing the final state, and references to its parent node,
	 * enabling recreation of steps
	 * @throws NullPointerException if any of the parameters are null
	 */
	public static <S> Node<S> bfsv(Supplier<S> s0,Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		LinkedList<Node<S>> toBeExplored = new LinkedList<>(); 
		Set<S> explored = new HashSet<>();
		explored.add(Util.requireNonNull(s0, "Starting state").get());
		toBeExplored.add(new Node<S>(null, s0.get(), 0));
		
		while (!toBeExplored.isEmpty()) {
			Node<S> ni = toBeExplored.removeFirst();
			if (Util.requireNonNull(goal, "Goal").test(ni.getState())) {
				return ni;
			}
			List<Transition<S>> children = Util.requireNonNull(succ, "Function").apply(ni.getState());
			children.removeIf(s -> explored.contains(s.getState()));
			children.forEach(s -> {
				toBeExplored.add(new Node<S>(ni, s.getState(), ni.getCost() + s.getCost()));
				explored.add(s.getState());				
			});
		}
		
		return null;
	}

	/**
	 * Disables the creation of instances of this class.
	 */
	public SearchUtil() {}
	
}

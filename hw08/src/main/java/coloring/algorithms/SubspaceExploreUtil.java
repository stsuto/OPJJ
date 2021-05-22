package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code SubspaceExploreUtil} offers multiple algorithms used
 * for space exploration. Those algorithms are bfs, dfs and bfsv.
 * 
 * @author stipe
 *
 */
public class SubspaceExploreUtil {

	/**
	 * Breadth-First Search explores neighboring solutions before 
	 * further spreading.
	 * 
	 * @param s0 starting state
	 * @param process action to be done with the given argument
	 * @param succ function to be performed upon given argument
	 * @param acceptable test to be done
	 * @throws NullPointerException if any of the parameters are null
	 */
	public static <S> void bfs(Supplier<S> s0, Consumer<S> process,
				Function<S,List<S>> succ, Predicate<S> acceptable) {
		
		LinkedList<S> toBeExplored = new LinkedList<>();
		toBeExplored.add(Util.requireNonNull(s0, "Starting state").get());
		
		while (!toBeExplored.isEmpty()) {
			S si = toBeExplored.removeFirst();
			if (!Util.requireNonNull(acceptable, "Predicate").test(si)) {
				continue;
			}
			Util.requireNonNull(process, "Action").accept(si);
			toBeExplored.addAll(
					Util.requireNonNull(succ, "Function").apply(si));
		}
	}

	/**
	 * Depth-First Search explores solutions from one way, or one depth,
	 * before continuing to neighboring solutions.
	 * 
	 * @param s0 starting state
	 * @param process action to be done with the given argument
	 * @param succ function to be performed upon given argument
	 * @param acceptable test to be done
	 * @throws NullPointerException if any of the parameters are null
	 */
	public static <S> void dfs(Supplier<S> s0, Consumer<S> process,
			Function<S,List<S>> succ, Predicate<S> acceptable) {
	
		LinkedList<S> toBeExplored = new LinkedList<>();
		toBeExplored.add(s0.get());
		
		while (!toBeExplored.isEmpty()) {
			S si = toBeExplored.removeFirst();
			if (!acceptable.test(si)) {
				continue;
			}
			process.accept(si);
			toBeExplored.addAll(0, succ.apply(si));
		}
	}
	
	/**
	 * BFSV is an improved version of BFS that memorizes already explored
	 * states and ignores them when looking for new states.
	 * 
	 * @param s0 starting state
	 * @param process action to be done with the given argument
	 * @param succ function to be performed upon given argument
	 * @param acceptable test to be done
	 * @throws NullPointerException if any of the parameters are null
	 */
	public static <S> void bfsv(Supplier<S> s0, Consumer<S> process,
			Function<S,List<S>> succ, Predicate<S> acceptable) {
	
		LinkedList<S> toBeExplored = new LinkedList<>();
		toBeExplored.add(s0.get());
		Set<S> explored = new HashSet<>();
		explored.add(s0.get());
		
		while (!toBeExplored.isEmpty()) {
			S si = toBeExplored.removeFirst();
			if (!acceptable.test(si)) {
				continue;
			}
			process.accept(si);
			List<S> children = succ.apply(si);
			children.removeAll(explored);
			toBeExplored.addAll(children);
			explored.addAll(children);
		}
	}
	
	/**
	 * Disables creating instances of this class.
	 */
	private SubspaceExploreUtil() {}
}

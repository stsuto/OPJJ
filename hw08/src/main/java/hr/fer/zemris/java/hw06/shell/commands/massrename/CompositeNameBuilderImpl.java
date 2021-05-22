package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code CompositeNameBuilderImpl} is an implementation of 
 * the {@link NameBuilder} interface that has the capability of 
 * storing multiple {@code NameBuilder} objects, creating a composite.
 * 
 * @author stipe
 *
 */
public class CompositeNameBuilderImpl implements NameBuilder {

	/**
	 * List of builders forming a composite.
	 */
	private List<NameBuilder> composition = new ArrayList<>();
	
	@Override
	public void execute(FilterResult result, StringBuilder sb) {
		for (NameBuilder builder : composition) {
			builder.execute(result, sb);
		}
	}

	/**
	 * Adds the given builder to the composite.
	 * 
	 * @param nb NameBuilder to be added
	 */
	public void addBuilder(NameBuilder nb) {
		composition.add(nb);
	}

	/**
	 * Removes the given builder from the composite.
	 * 
	 * @param nb NameBuilder to be removed
	 */
	public void removeBuilder(NameBuilder nb) {
		composition.remove(nb);
	}
	
}

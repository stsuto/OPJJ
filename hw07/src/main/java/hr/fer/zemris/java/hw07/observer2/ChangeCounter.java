package hr.fer.zemris.java.hw07.observer2;

/**
 * Class {@link ChangeCounter} is an observer which counts the number of times 
 * the value stored has been changed since this observer's registration and
 * writes that number to standard output.
 * 
 * @author stipe
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * The counter of changes to the storage state.
	 */
	private int counter = 0;
	
	@Override
	public void valueChanged(IntegerStorageChange iStorageChange) {
		System.out.println("Number of value changes since tracking: " + ++counter);
	}

}

package hr.fer.zemris.java.hw07.observer2;

/**
 * Class {@link SquareValue} is an observer which writes the square of the 
 * integer stored in {@link IntegerStorage} to the standard output.
 * 
 * @author stipe
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange iStorageChange) {
		int value = iStorageChange.getNewValue();
		System.out.printf("Provided new value: %d, square is %d%n", value, value * value);
	}

}

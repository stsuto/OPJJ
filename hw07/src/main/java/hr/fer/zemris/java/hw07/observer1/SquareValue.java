package hr.fer.zemris.java.hw07.observer1;

/**
 * Class {@link SquareValue} is an observer which writes the square of the 
 * integer stored in {@link IntegerStorage} to the standard output.
 * 
 * @author stipe
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorage istorage) {
		int value = istorage.getValue();
		System.out.printf("Provided new value: %d, square is %d%n", value, value * value);
	}

}

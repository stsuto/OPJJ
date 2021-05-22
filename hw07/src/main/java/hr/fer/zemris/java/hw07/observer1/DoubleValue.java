package hr.fer.zemris.java.hw07.observer1;

/**
 * Class {@link DoubleValue} is an observer which writes to the standard 
 * output two times the value of the current value which is stored in subject,
 * but does so only first n times since its registration with the subject.
 * The n is given in constructor and after writing the double value for the 
 * n-th time, the observer automatically unregisters itself from the subject.
 * 
 * @author stipe
 *
 */
public class DoubleValue implements IntegerStorageObserver {

	/**
	 * Counter of how many times the value must be printed.
	 */
	private int counter;
	
	/**
	 * Constructor accepting and setting the counter.
	 * 
	 * @param counter {@link #counter}
	 */
	public DoubleValue(int counter) {
		this.counter = counter;
	}

	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Double value: " + 2 * istorage.getValue());
		if (--counter == 0) {
			istorage.removeObserver(this);
		}
	}

}

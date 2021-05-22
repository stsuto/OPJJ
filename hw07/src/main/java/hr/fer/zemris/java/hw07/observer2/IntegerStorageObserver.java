package hr.fer.zemris.java.hw07.observer2;

/**
 * Interface {@code IntegerStorageObserver} is an abstraction used for
 * modeling observer objects observing a {@link IntegerStorage} subject.
 * 
 * @author stipe
 *
 */
public interface IntegerStorageObserver {
	
	/**
	 * Action defined by this method is executed whenever the state of
	 * the subject is modified.
	 * 
	 * @param iStorageChange the object representing the change of state
	 */
	void valueChanged(IntegerStorageChange iStorageChange);
	
}

package hr.fer.zemris.java.hw17.jvdraw.drawing;

/**
 * This interface is an abstraction of a listener of a drawing model.
 * 
 * @author stipe
 *
 */
public interface DrawingModelListener {

	/**
	 * Happens when an object is added to the model.
	 * 
	 * @param source model
	 * @param index0
	 * @param index1
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	/**
	 * Happens when an object is removed from the model.
	 * 
	 * @param source model
	 * @param index0
	 * @param index1
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	/**
	 * Happends when an object from the model is changed.
	 * 
	 * @param source model
	 * @param index0
	 * @param index1
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
	
}

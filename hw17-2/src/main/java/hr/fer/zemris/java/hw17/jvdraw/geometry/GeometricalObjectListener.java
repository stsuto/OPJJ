package hr.fer.zemris.java.hw17.jvdraw.geometry;

/**
 * This interface is an abstract representaton of a geometrical object listener,
 * an object which is notified when the object it is listening to has changed.
 * 
 * @author stipe
 *
 */
public interface GeometricalObjectListener {

	/**
	 * Method which is performed when the object this listener is listening to
	 * has been changed.
	 * 
	 * @param o object
	 */
	public void geometricalObjectChanged(GeometricalObject o);

}

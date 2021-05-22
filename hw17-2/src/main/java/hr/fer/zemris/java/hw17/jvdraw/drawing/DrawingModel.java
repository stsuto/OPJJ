package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;

/**
 * This interface represents the drawing model which contains
 * all defined objects and offers methods to use, change, add, or
 * remove them.
 * 
 * @author stipe
 *
 */
public interface DrawingModel {

	/**
	 * @return size
	 */
	public int getSize();
	/** 
	 * @param index index
	 * @return object on index
	 */
	public GeometricalObject getObject(int index);
	/**
	 * Adds the object to the model
	 * 
	 * @param object object
	 */
	public void add(GeometricalObject object);
	/**
	 * Removes the object from the model.
	 * 
	 * @param object object
	 */
	public void remove(GeometricalObject object);
	/**
	 * Changes the order of objects.
	 * 
	 * @param object object
	 * @param offset offset
	 */
	public void changeOrder(GeometricalObject object, int offset);
	/**
	 * @param object
	 * @return index of object
	 */
	public int indexOf(GeometricalObject object);
	/**
	 * Clears all objects from the model.
	 */
	public void clear();
	/**
	 * Clears the modified flag.
	 */
	public void clearModifiedFlag();
	/**
	 * Flag representing the modified state of the model.
	 * 
	 * @return true if modified, false otherwise
	 */
	public boolean isModified();
	/**
	 * Registers the listener to this model.
	 * @param l listener
	 */
	public void addDrawingModelListener(DrawingModelListener l);
	/**
	 * Unregisters the listener from this model.
	 * @param l listener
	 */
	public void removeDrawingModelListener(DrawingModelListener l);

}
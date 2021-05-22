package hr.fer.zemris.java.hw17.jvdraw.geometry;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.GeometricalObjectEditor;

/**
 * This class represents a geometrical object.
 * 
 * @author stipe
 *
 */
public abstract class GeometricalObject {

	/**
	 * List of listeners.
	 */
	protected List<GeometricalObjectListener> listeners = new ArrayList<>();
	
	/**
	 * Notifies all listeners that this object has changed.
	 */
	protected void notifyListeners() {
		listeners.forEach(l -> l.geometricalObjectChanged(this));
	}
	
	/**
	 * Accepts the given visitor.
	 * 
	 * @param v visitor
	 */
	public abstract void accept(GeometricalObjectVisitor v);
		
	/**
	 * Creates a properly subtyped GeometricalObjectEditor
	 * 
	 * @return editor
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();
	
	/**
	 * Registers a listener.
	 * 
	 * @param l listener.
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	/**
	 * Unregisters a listener.
	 * 
	 * @param l listener
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
}

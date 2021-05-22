package hr.fer.zemris.java.hw17.jvdraw.drawing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectListener;

/**
 * This class is an implementation of a drawng model which is registered on
 * all of its objects as their listener.
 * 
 * @author stipe
 *
 */
public class GeometricalDrawingModel implements DrawingModel, GeometricalObjectListener {

	/**
	 * Object list.
	 */
	private List<GeometricalObject> objects;
	/**
	 * Listener list.
	 */
	private List<DrawingModelListener> listeners;
	/*
	 * Flag representing the modified state of this model.
	 */
	private boolean modifiedFlag;
	
	/**
	 * Constructor.
	 */
	public GeometricalDrawingModel() {
		this.objects = new ArrayList<>();
		this.listeners = new ArrayList<>();
		this.modifiedFlag = false;
	}
	
	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		if (objects.contains(object)) {
			return;
		}
		objects.add(object);
		object.addGeometricalObjectListener(this);
		int index = objects.indexOf(object);
		listeners.forEach(l -> l.objectsAdded(this, index, index));
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		objects.remove(index);
		object.removeGeometricalObjectListener(this);
		listeners.forEach(l -> l.objectsRemoved(this, index, index));
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int index = objects.indexOf(object);
		int changedIndex = getChangedIndex(index, offset);
		if (changedIndex < 0) return;
		
		Collections.swap(objects, index, changedIndex);
		listeners.forEach(l -> l.objectsChanged(this, index, changedIndex));
	}

	/**
	 * Gets the index of the changed object.
	 * 
	 * @param index old index
	 * @param offset offset
	 * @return
	 */
	private int getChangedIndex(int index, int offset) {
		int changedIndex = index + offset;
		if (changedIndex >= objects.size() || changedIndex < 0) {
			return -1;
		}
		return index + offset;
	}

	@Override
	public int indexOf(GeometricalObject object) {
		return objects.indexOf(object);
	}

	@Override
	public void clear() {
		objects.clear();
	}

	@Override
	public void clearModifiedFlag() {
		modifiedFlag = false;
	}

	@Override
	public boolean isModified() {
		return modifiedFlag;
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int index = objects.indexOf(o);
		listeners.forEach(l -> l.objectsChanged(this, index, index));
	}

}

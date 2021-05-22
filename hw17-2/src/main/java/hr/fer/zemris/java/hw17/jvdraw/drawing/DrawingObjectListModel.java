package hr.fer.zemris.java.hw17.jvdraw.drawing;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;

/**
 * This class is an implementation of a drawing model listener which serves as a list model.
 * 
 * @author stipe
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Drawing model
	 */
	private DrawingModel drawingModel;
	
	/**
	 * @param drawingModel
	 */
	public DrawingObjectListModel(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		
		drawingModel.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return drawingModel.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return drawingModel.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(source, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(source, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(source, index0, index1);
	}

}

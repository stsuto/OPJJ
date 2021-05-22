package hr.fer.zemris.java.hw17.jvdraw.component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.GeometricalObjectEditor;

/**
 * This class is a JList of GeometricalObjects which is created from
 * a drawing model.
 * 
 * @author stipe
 *
 */
public class JObjectList extends JList<GeometricalObject> {

	private static final long serialVersionUID = 1L;

	/**
	 * Drawing model.
	 */
	private DrawingModel drawingModel;
	
	/**
	 * Constructor.
	 * 
	 * @param drawingModel {@link #drawingModel}
	 */
	public JObjectList(DrawingModel drawingModel) {
		super(new DrawingObjectListModel(drawingModel));
		this.drawingModel = drawingModel;
		
		addEditListener();
		addShortcutListener();
	}
	
	/**
	 * Adds a shortcut listener.
	 */
	private void addShortcutListener() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int index = getSelectedIndex();
				if (index < 0) return;
				
				GeometricalObject selectedObject = drawingModel.getObject(index);

				switch (e.getKeyCode()) {
				case KeyEvent.VK_PLUS:
				case KeyEvent.VK_ADD:
					if (index + 1 < drawingModel.getSize()) {
						drawingModel.changeOrder(selectedObject, 1); // Moving up the hierarchy.
						setSelectedIndex(index + 1);
					}
					return;

				case KeyEvent.VK_MINUS:
				case KeyEvent.VK_SUBTRACT:
					if (index - 1 >= 0) {
						drawingModel.changeOrder(selectedObject, -1); // // Moving down the hierarchy.
						setSelectedIndex(index - 1);
					}
					return;
					
				case KeyEvent.VK_DELETE:
					if (index + 1 < drawingModel.getSize()) {
						setSelectedIndex(index + 1);
					} else if (index - 1 >= 0) {
						setSelectedIndex(index - 1);
					}
					drawingModel.remove(selectedObject);
				}	
				
			}
		});
	}

	/**
	 * Adds an edit listener.
	 */
	private void addEditListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) return; // Not a double click.
				
				int index = getSelectedIndex();
				if (index < 0) return; // Blank double click.
				
				GeometricalObject selectedObject = drawingModel.getObject(index);
				GeometricalObjectEditor editor = selectedObject.createGeometricalObjectEditor();
				
				showDialog(editor);
			}
		});
	}

	/**
	 * Shows dialog.
	 * 
	 * @param editor editor
	 */
	private void showDialog(GeometricalObjectEditor editor) {
		if (createEditorDialog(editor) == JOptionPane.YES_OPTION) {
			try {
				editor.checkEditing();
				editor.acceptEditing();
			} catch (RuntimeException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		}
	}

	/**
	 * Creates an editor dialog.
	 * 
	 * @param editor editor
	 * @return dialog
	 */
	private int createEditorDialog(GeometricalObjectEditor editor) {
		return JOptionPane.showConfirmDialog(
				this, 
				editor, 
				"Edit values", 
				JOptionPane.YES_NO_OPTION
			);
	}

}

package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.color.JColorAreaLabel;
import hr.fer.zemris.java.hw17.jvdraw.component.JObjectList;
import hr.fer.zemris.java.hw17.jvdraw.component.ToolButton;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.GeometricalDrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.drawing.tool.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.drawing.tool.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.drawing.tool.LineTool;
import hr.fer.zemris.java.hw17.jvdraw.drawing.tool.Tool;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.visitor.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.geometry.visitor.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.geometry.visitor.GeometricalObjectWriter;

/**
 * This class represents a console class of a JVDraw application.
 * 
 * @author stipe
 *
 */
public class JVDraw extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Front color.
	 */
	private IColorProvider fgProvider;
	/**
	 * Background color.
	 */
	private IColorProvider bgProvider;
	/**
	 * Canvas.
	 */
	private JDrawingCanvas canvas;
	/**
	 * Drawing model.
	 */
	private DrawingModel drawingModel;
	/**
	 * Current tool used.
	 */
	private Tool tool;
	/**
	 * Line tool.
	 */
	private Tool lineTool;
	/**
	 * Circle tool.
	 */
	private Tool circleTool;
	/**
	 * Filled circle tool.
	 */
	private Tool filledCircleTool;
	/**
	 * Exit action performed upon trying to exit the application.
	 */
	private Action exit;
	/**
	 * Open action.
	 */
	private Action open;
	/**
	 * Save action.
	 */
	private Action save;
	/**
	 * Save as action.
	 */
	private Action saveAs;
	/**
	 * Export action
	 */
	private Action export;
	/**
	 * File path of the currently opened file.
	 */
	private Path filePath;
	
	/**
	 * Constructor.
	 */
	public JVDraw() {
		setCloseOperation();
		
		setLocation(20, 20);
		setSize(1200, 600);
		setTitle("JVDraw");
		
		initGUI();
	}

	/**
	 * Sets the closing operation wiht regard to the exit action.
	 */
	private void setCloseOperation() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit.actionPerformed(null);
			}
		});
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		setLayout(new BorderLayout());
		
		createActions();

		setJMenuBar(createJMenuBar());
		add(createToolbar(), BorderLayout.PAGE_START);
		add(createColorAreaLabel(), BorderLayout.PAGE_END);
		
		initFields();
		
		add(canvas, BorderLayout.CENTER);
		add(new JScrollPane(new JObjectList(drawingModel)), BorderLayout.LINE_END);
	}

	/**
	 * Initializes fields.
	 */
	private void initFields() {
		drawingModel = new GeometricalDrawingModel();
		canvas = new JDrawingCanvas(drawingModel, this::getTool);
		lineTool = new LineTool(drawingModel, fgProvider, canvas);
		circleTool = new CircleTool(drawingModel, fgProvider, canvas);
		filledCircleTool = new FilledCircleTool(drawingModel, fgProvider, bgProvider, canvas);
		tool = lineTool;	
	}

	/**
	 * Creates a color area label which shws the current color selections.
	 * 
	 * @return color area label
	 */
	private JColorAreaLabel createColorAreaLabel() {
		JColorAreaLabel colorLabel = new JColorAreaLabel(fgProvider, bgProvider);
		colorLabel.newColorSelected(
				fgProvider, 
				fgProvider.getCurrentColor(), 
				fgProvider.getCurrentColor()
			);
		return colorLabel;
	}

	/**
	 * Creates a JMenuBar.
	 * 
	 * @return jmenubar
	 */
	private JMenuBar createJMenuBar() {
		JMenuBar mb = new JMenuBar();
		
		JMenu file = new JMenu("File");
		
		file.add(createJMenuItem(open, "Open"));
		file.add(createJMenuItem(save, "Save"));
		file.add(createJMenuItem(saveAs, "Save as"));
		file.add(createJMenuItem(export, "Export"));
		file.add(createJMenuItem(exit, "Exit"));
		
		mb.add(file);

		return mb;
	}

	/**
	 * Creates a JMenuItem with an action and text.
	 * 
	 * @param action action
	 * @param text text
	 * @return JMenuItem object
	 */
	private JMenuItem createJMenuItem(Action action, String text) {
		JMenuItem item = new JMenuItem(action);
		item.setText(text);
		return item;
	}

	/**
	 * Creates a JToolBar.
	 * 
	 * @return JToolbar object
	 */
	private JToolBar createToolbar() {
		JToolBar tb = new JToolBar(JToolBar.HORIZONTAL);
		
		JColorArea fgColorArea = new JColorArea(Color.RED);
		fgColorArea.setSize(fgColorArea.getPreferredSize());
		fgProvider = fgColorArea;
		tb.add(fgColorArea);
		tb.addSeparator();
		JColorArea bgColorArea = new JColorArea(Color.BLUE);
		bgProvider = bgColorArea;
		tb.add(bgColorArea);
		
		tb.addSeparator();
		
		ButtonGroup buttons = new ButtonGroup();
		JToggleButton lineButton = createLineButton();
		tb.add(lineButton);
		buttons.add(lineButton);
		tb.addSeparator();
		JToggleButton circleButton = createCircleButton();
		tb.add(circleButton);
		buttons.add(circleButton);
		tb.addSeparator();
		JToggleButton filledCircleButton = createFilledCircleButton();
		tb.add(filledCircleButton);
		buttons.add(filledCircleButton);
		
		return tb;
	}

	/**
	 * Creates a ToolButton which sets the current tool to a FilledCircleTool.
	 * 
	 * @return button
	 */
	private JToggleButton createFilledCircleButton() {
		return new ToolButton("Filled circle", e -> tool = filledCircleTool, 100);
	}

	/**
	 * Creates a ToolButton which sets the current tool to a CircleTool.
	 * 
	 * @return button
	 */
	private JToggleButton createCircleButton() {
		return new ToolButton("Circle", e -> tool = circleTool, 60);
	}

	/**
	 * Creates a ToolButton which sets the current tool to a LineTool.
	 * 
	 * @return button
	 */
	private JToggleButton createLineButton() {
		return new ToolButton("Line", e -> tool = lineTool, 55);
	}
	
	/**
	 * Returns the current tool.
	 * 
	 * @return tool
	 */
	private Tool getTool() {
		return tool;
	}
	
	/**
	 * Creates actions.
	 */
	private void createActions() {
		exit = createExitAction();
		open = createOpenAction();
		save = createSaveAction();
		saveAs = createSaveAsAction();
		export = createExportAction();
		
		addShortcuts();
	}
	
	private void addShortcuts() {
		open.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		open.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_O);

		save.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		save.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_S);

		saveAs.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt S"));
		saveAs.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_D);

		exit.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control Q"));
		exit.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_Q);

		export.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control E"));
		export.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_E);

	}

	/**
	 * Creates exit action.
	 * 
	 * @return {@link #exit}
	 */
	@SuppressWarnings("serial")
	private Action createExitAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (drawingModel.isModified()) {
					if (isSaveCanceled()) {
						return;
					}
				} else if (!isExitAccepted()) {
					return;
				}
				dispose();
			}
		};
	}
	
	/**
	 * Checks if the exit option was accepted.
	 * 
	 * @return true if yes, false if no
	 */
	private boolean isExitAccepted() {
		return createExitDialog() == JOptionPane.YES_OPTION;
	}

	/**
	 * Checks if the saving process was canceled.
	 * 
	 * @return true if yes, no otherwise
	 */
	private boolean isSaveCanceled() {
		switch (createUnsavedDialog()) {
		case JOptionPane.CANCEL_OPTION:
			return true;
			
		case JOptionPane.YES_OPTION:
			save.actionPerformed(null);
		}
		
		return false;
	}

	/**
	 * Creates an exit dialog.
	 * 
	 * @return exit dialog
	 */
	private int createExitDialog() {
		return JOptionPane.showConfirmDialog(
				this,
				"Do you really wish to exit?",
				"Exit",
				JOptionPane.YES_NO_OPTION
			);
	}
	
	/**
	 * Creates a dialog that checks if the unsaved changes should be saved.
	 * 
	 * @return unsaved dialog
	 */
	private int createUnsavedDialog() {
		return JOptionPane.showConfirmDialog(
				this, 
				"Save unsaved changes before exiting document?", 
				"Exit document",
				JOptionPane.YES_NO_CANCEL_OPTION
			);
	}

	/**
	 * Creates open action.
	 * 
	 * @return {@link #open}
	 */
	@SuppressWarnings("serial")
	private Action createOpenAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (drawingModel.isModified()) {
					if (isSaveCanceled()) {
						return;
					}
				}
				
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JVDraw", "jvd");
				jfc.setFileFilter(filter);
				jfc.setDialogTitle("Open file");
				
				if(jfc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				drawingModel.clear();
				
				Path selectedPath = jfc.getSelectedFile().toPath();			
				try {
					openFile(selectedPath);					
				} catch (Exception ex) {
					ex.printStackTrace();
					showErrorDialog("Couldn't read file.");
				}
			}
		};
	}

	/**
	 * Creates error dialog.
	 * 
	 * @param message error message
	 */
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Opens the file in the application.
	 * 
	 * @param path path
	 * @throws IOException exception
	 */
	private void openFile(Path path) throws IOException {
		GeometricalObjectParser parser = new GeometricalObjectParser(path);
		List<GeometricalObject> parsedObjects = parser.getParsedObjects();
		
		parsedObjects.forEach(drawingModel::add);
		
		filePath = path;
		drawingModel.clearModifiedFlag();
	}

	/**
	 * Creates save action.
	 * 
	 * @return {@link #save}
	 */
	@SuppressWarnings("serial")
	private Action createSaveAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (filePath == null) {
					saveAs.actionPerformed(e);
					return;
				}
				
				GeometricalObjectWriter writer = new GeometricalObjectWriter();
				acceptVisitor(writer);
				
				try {
					Files.writeString(filePath, writer.getDefinitionText());
				} catch (IOException ex) {
					showErrorDialog("Error while saving file.");
					return;
				}
				
				drawingModel.clearModifiedFlag();
				showInfoDialog("File saved.");
			}
		};
	}

	/**
	 * Shows info dialog.
	 * 
	 * @param message informational message
	 */
	private void showInfoDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Creates save as action.
	 * 
	 * @return {@link #saveAs}
	 */
	@SuppressWarnings("serial")
	private Action createSaveAsAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JVDraw", "jvd");
				jfc.setFileFilter(filter);
				jfc.setDialogTitle("Save file");
				
				if(jfc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				Path selectedPath = jfc.getSelectedFile().toPath();		
				
				if (!isNameValid(selectedPath, "jvd")) {
					showErrorDialog("File must be of type 'jvd'.");
					return;
				} else if (isNameTaken(selectedPath) && !isOverWriteApproved()) {
					return;
				}
				
				filePath = selectedPath;
				save.actionPerformed(e);
			}
		};
	}

	/**
	 * Checks if overwrite was approved.
	 * 
	 * @return true if yes, false otherwise
	 */
	private boolean isOverWriteApproved() {
		return JOptionPane.showConfirmDialog(
				this, 
				"Overwrite existing file?", 
				"Overwrite",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
		) == JOptionPane.YES_OPTION;
	}

	/**
	 * Checks if the name of the file is taken.
	 * 
	 * @param path file path
	 * @return true if the name is taken, false otherwise
	 */
	private boolean isNameTaken(Path path) {
		return Files.exists(path);
	}

	/**
	 * Checks if the name is valid.
	 * 
	 * @param path file path
	 * @param extensions valid extensions
	 * @return true if it is valid, false otherwise
	 */
	private boolean isNameValid(Path path, String... extensions) {
		String name = path.getFileName().toString();
		int index = name.indexOf('.');
		if (index == -1 || index == name.length()) {
			return false;
		}
		String extension = name.substring(index + 1);
		for (String ext : extensions) {
			if (extension.equals(ext)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates export action.
	 * 
	 * @return {@link #export}
	 */
	@SuppressWarnings("serial")
	private Action createExportAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Rectangle box = getBoundingBox();
				BufferedImage image = new BufferedImage(
					box.width, box.height, BufferedImage.TYPE_3BYTE_BGR
				);
				Graphics2D g = image.createGraphics();
				g.translate(-box.x, -box.y);
				drawObjects(g, box);
				g.dispose();
				
				File file = getSelectedFile();
				if (file == null) {
					return;
				}
				Path selectedPath = file.toPath();
				
				if (!isNameValid(selectedPath, "png", "gif", "jpg")) {
					showErrorDialog("File must be of type 'png', 'gif', or 'jpg'.");
					return;
				} else if (isNameTaken(selectedPath) && !isOverWriteApproved()) {
					return;
				}
				
				try {
					ImageIO.write(image, "png", file);
				} catch (IOException ex) {
					showErrorDialog("Couldn't export image.");
				}
				
				showInfoDialog("Image exported.");
			}
		};
	}
	
	/**
	 * Gets the selected file from JFileChooser.
	 * 
	 * @return selected file
	 */
	private File getSelectedFile() {
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		jfc.setDialogTitle("Save file");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "png", "gif", "jpg");
		jfc.setFileFilter(filter);
		
		if(jfc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		return jfc.getSelectedFile();
	}

	/**
	 * Draws the objects
	 * 
	 * @param g graphics
	 * @param box border box
	 */
	private void drawObjects(Graphics2D g, Rectangle box) {
		Color old = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(box.x, box.y, box.width, box.height);
		g.setColor(old);
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
		acceptVisitor(painter);
	}

	/**
	 * Returns the bounding box.
	 * 
	 * @return bounding box
	 */
	private Rectangle getBoundingBox() {
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		acceptVisitor(bbcalc);
		return bbcalc.getBoundingBox();
	}

	/**
	 * Iterates through all objects in drawing model and calls
	 * accept with the given visitor upon all of them.
	 * 
	 * @param visitor visitor
	 */
	private void acceptVisitor(GeometricalObjectVisitor visitor) {
		int size = drawingModel.getSize();
		for (int i = 0; i < size; i++) {
			drawingModel.getObject(i).accept(visitor);
		}
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(
			() -> new JVDraw().setVisible(true)
		);
	}
		
}

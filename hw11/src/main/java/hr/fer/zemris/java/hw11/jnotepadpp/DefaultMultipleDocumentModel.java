package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Class representing a model containing multiple documents in form of tabs.
 * 
 * @author stipe
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	private static final long serialVersionUID = 1L;

	/**
	 * Icon of a document which hasn't been modified.
	 */
	private static final String NOT_MODIFIED_ICON = "icons/greenDisk.png";
	/**
	 * Icon of a document which has been modified.
	 */
	private static final String MODIFIED_ICON = "icons/redDisk.png";
	/**
	 * Icon size.
	 */
	private static final int ICON_SIZE = 13;
	
	/**
	 * Documents of the model.
	 */
	private List<SingleDocumentModel> documents;
	/**
	 * Current document.
	 */
	private SingleDocumentModel currentDoc;
	/**
	 * Model's listeners.
	 */
	private List<MultipleDocumentListener> listeners;
	
	/**
	 * Constructor that initializes fields and adds proper listeners.
	 */
	public DefaultMultipleDocumentModel() {
		this.documents = new ArrayList<>();
		// Even though null is already the default value, here it is initialized for clarity.
		this.currentDoc = null;
		this.listeners = new ArrayList<>();
		
		addCurrentDocListener();
	}
	
	/**
	 * Adds a listener for current document changes.
	 */
	private void addCurrentDocListener() {
		addChangeListener(e -> 
			changeDocAndNotify(documents.get(getSelectedIndex())));
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		return createModel(null);
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDoc;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path, "Document path mustn't be null!");
		int index = getPathIndex(path);
		if (index == -1) {
			createModel(path);
			index = documents.size() - 1;
		}
		setSelectedIndex(index);
		return documents.get(index);
	}

	/**
	 * Returns the title of the document.
	 * 
	 * @param path document's path
	 * @return document title
	 */
	private String getTitle(Path path) {
		return path == null ? "(unnamed)" : path.getFileName().toString();
	}

	/**
	 * Returns the tooltip of the document.
	 * 
	 * @param path document's path
	 * @return document tooltip
	 */
	private String getTooltip(Path path) {
		return path == null ? "(unnamed)" : path.toString();
	}

	/**
	 * Returns the proper icon for the document at current state.
	 * 
	 * @param iconName name of icon
	 * @return document's icon
	 */
	private Icon getIcon(String iconName) {
		try (InputStream is = this.getClass().getResourceAsStream(
				Objects.requireNonNull(iconName, "Icon name mustn't be null!"))) {
			if (is == null) {
				throw new RuntimeException("Invalid icon name: " + iconName + "!");
			}
			
			Image scaled = new ImageIcon(is.readAllBytes())
					.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
			
			return new ImageIcon(scaled);
		
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load icon " + iconName + "!");
		}
	}

	/**
	 * Creates a new document. If the given path is null, a blank document is created, 
	 * otherwise a document containing the text from the file on the given path is open.
	 * 
	 * @param path path of file
	 * @return new document
	 */
	private SingleDocumentModel createModel(Path path) {
		String text = path == null ? "" : getTextFromFile(path);
		SingleDocumentModel docModel = new DefaultSingleDocumentModel(path, text);

		addDocAndNotify(docModel);
		changeDocAndNotify(docModel);
		addDocModelListener(docModel);
		
		addTab(
			getTitle(path),
			getIcon(NOT_MODIFIED_ICON),
			createModelComponent(docModel),
			getTooltip(path)
		);
		
		setSelectedIndex(documents.indexOf(docModel));
		return docModel;
	}

	/**
	 * Adds a listener which updates the document's icon and title when changes 
	 * to the document happen.
	 * 
	 * @param docModel
	 */
	private void addDocModelListener(SingleDocumentModel docModel) {
		docModel.addSingleDocumentListener(new SingleDocumentListener() {
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel docModel) {
				setIconAt(documents.indexOf(docModel), 
						docModel.isModified() 
							? getIcon(MODIFIED_ICON) 
							: getIcon(NOT_MODIFIED_ICON));
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel docModel) {
				Path path = docModel.getFilePath();
				setTitleAt(documents.indexOf(docModel), getTitle(path));
				setToolTipTextAt(documents.indexOf(docModel), getTooltip(path));
				changeDocAndNotify(currentDoc); // Signals that the document name has changed so JFrame title should be updated.
			}
		});
	}

	/**
	 * Changes the current document and notifies the listeners of the change.
	 * 
	 * @param docModel document to be set as current
	 */
	private void changeDocAndNotify(SingleDocumentModel docModel) {
		SingleDocumentModel old = currentDoc; 
		currentDoc = docModel;
		listeners.forEach(l -> l.currentDocumentChanged(old, currentDoc));
	}

	/**
	 * Adds the given document and notifies the listeners.
	 * 
	 * @param docModel document to added
	 */
	private void addDocAndNotify(SingleDocumentModel docModel) {
		documents.add(docModel);
		listeners.forEach(l -> l.documentAdded(docModel));
	}

	/**
	 * Reads the text from the file on the given path.<p>
	 * UTF-8 charset is used.
	 * 
	 * @param path path of file
	 * @return text from file
	 */
	private String getTextFromFile(Path path) {
		try {
			return Files.readString(path);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read file " + path + "!");
		}
	}

	/**
	 * Creates a panel component containing the models text component.
	 * 
	 * @param docModel model whose panel is added
	 * @return component to be added as a new tab
	 */
	private Component createModelComponent(SingleDocumentModel docModel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(docModel.getTextComponent()), BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * Returns the index within the model of the document on the given path.
	 * 
	 * @param path document's path
	 * @return index of document
	 */
	private int getPathIndex(Path path) {
		int size = documents.size();
		for (int i = 0; i < size; i++) {
			if (path.equals(documents.get(i).getFilePath())) {
				return i;
			}	
		}
		return -1;
	}
	
	@Override
	public void saveDocument(SingleDocumentModel docModel, Path newPath) {
		if (newPath != null) {
			if (pathAlreadyExists(docModel, newPath)) {
				throw new RuntimeException("Path " + newPath 
						+ " is already in use for a different opened document!");
			}
			docModel.setFilePath(newPath);
		}
		String text = docModel.getTextComponent().getText();
		writeToFile(docModel.getFilePath(), text.getBytes(StandardCharsets.UTF_8));
		docModel.setModified(false);
	}

	/**
	 * Writes the given bytes to the file on the given path.
	 * 
	 * @param filePath path of file
	 * @param bytes bytes to be written
	 */
	private void writeToFile(Path filePath, byte[] bytes) {
		try {
			Files.write(filePath, bytes);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't write text to file " + filePath + "!");
		}
	}

	/**
	 * Checks if the given path is already used for a different document.
	 * 
	 * @param docModel document to be saved to the given path
	 * @param newPath path to which the document will be saved
	 * @return {@code true} if the path is used for a different document,
	 * 		   {@code false} otherwise
	 */
	private boolean pathAlreadyExists(SingleDocumentModel docModel, Path newPath) {
		return documents.stream()
				.filter(doc -> newPath.equals(doc.getFilePath()))
				.anyMatch(doc -> !doc.equals(docModel));
	}

	@Override
	public void closeDocument(SingleDocumentModel docModel) {
		// If the document to be closed is the last open document, open a new blank document after closing the current one.
		if (documents.size() == 1) {
			createNewDocument();
		}
		removeDocAndNotify(docModel);
	}

	/**
	 * Removes the document from the model.
	 * 
	 * @param docModel document to be removed
	 */
	private void removeDocAndNotify(SingleDocumentModel docModel) {
		int currentIndex = documents.indexOf(docModel);
		documents.remove(docModel);
		removeTabAt(currentIndex);
		listeners.forEach(l -> l.documentRemoved(docModel));
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}

}
package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Class which represents JNotepad++, a customized document editor offering 
 * additional functionalities such as letter case toggling, duplicate line 
 * removing, line sorting, statistical information and more.<p>
 * Application supports three languages: English (default), Croatian, and German. 
 * 
 * @author stipe
 *
 */
public class JNotepadPP extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Localization provider.
	 */
	private ILocalizationProvider lp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	/**
     * Model for multiple document usage.
     */
    private MultipleDocumentModel model;
    /**
     * Label showing the length of the document.
     */
	private LJLabel lengthLabel;
	/**
	 * Label showing current caret info such as line, 
	 * column and selected text length.
	 */
	private JLabel caretLabel;
	/**
	 * Label which shows the time.
	 */
	private ClockLabel timeLabel;
	
	/**
	 * Constructor.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addExitListener();
		
		setLocation(20, 20);
		setSize(500, 500);
	
		initGUI();
	}
	
	/**
	 * Adds a listener to the exit application button, 
	 * adding a programmed functionality.
	 */
	private void addExitListener() {
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
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		DefaultMultipleDocumentModel tabbedPane = new DefaultMultipleDocumentModel();
		cp.add(tabbedPane, BorderLayout.CENTER);
	
		model = tabbedPane;
		lengthLabel = new LJLabel("length", lp);
		caretLabel = new JLabel();
		
		// Localization listener used so components incompatible with customized 
		// components (such as LJLabel) can be updated properly.
		ILocalizationListener localListener = () -> {
			changeCaretInfo();
			setTitle(getChangedTitle());
		};
		lp.addLocalizationListener(localListener);
		
		addModelListener(e -> performChanges());
		
		model.createNewDocument();
				
		createActions();
		setJMenuBar(createMenuBar());
		cp.add(createToolbar(), BorderLayout.PAGE_START);
		cp.add(createStatusBar(), BorderLayout.PAGE_END);
	}
	
	/**
	 * Perform changes when caret change has happened.
	 */
	private void performChanges() {
		changeCaretInfo();
		enableOrDisable();
	}

	/**
	 * Updates the caret status and its info.
	 */
	private void changeCaretInfo() {
		lengthLabel.setText(updateLength());
		caretLabel.setText(updateCaret());		
	}
	
	/**
	 * Enables or disable some actions depending on if any part of the text is selected.
	 */
	private void enableOrDisable() {
		JTextComponent area = model.getCurrentDocument().getTextComponent();
		boolean selectionExists = area.getCaret().getDot() != area.getCaret().getMark();
		
		cut.setEnabled(selectionExists);
		copy.setEnabled(selectionExists);
		upperCase.setEnabled(selectionExists);
		lowerCase.setEnabled(selectionExists);
		invertCase.setEnabled(selectionExists);
		sortAsc.setEnabled(selectionExists);
		sortDesc.setEnabled(selectionExists);
		unique.setEnabled(selectionExists);	
	}

	/**
	 * Adds a listener to the multiple document model's text areas.
	 * 
	 * @param caretListener listener to be added to the caret
	 */
	private void addModelListener(ChangeListener caretListener) {
		model.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel model) {}
			
			@Override
			public void documentAdded(SingleDocumentModel model) {}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				setTitle(getChangedTitle());
				
				if (previousModel != null) {
					previousModel.getTextComponent().getCaret().removeChangeListener(caretListener);
				}
				// In this implementation this should never be null.
				if (currentModel != null) {
					currentModel.getTextComponent().getCaret().addChangeListener(caretListener);
				}
				
				caretListener.stateChanged(null); // Alternatively: performChanges();
			}
		
		});
	}
	
	/**
	 * Gets the title of the main frame.
	 * The title is the full path of the currently opened document.
	 * 
	 * @return frame title
	 */
	private String getChangedTitle() {
		Path path = model.getCurrentDocument().getFilePath();
		return String.format("%s - JNotepad++", 
					path == null 
						? "(" + lp.getString("unnamed") + ")" 
						: path.toString()
				);
	}

	/**
	 * Creates action properties.
	 */
	private void createActions() {
		createNewDocument.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		createNewDocument.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_N);

		openDocument.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openDocument.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_O);

		saveDocument.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveDocument.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_S);

		saveDocumentAs.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt S"));
		saveDocumentAs.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_D); // sto treba s mnemonicima

		closeDocument.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control W"));
		closeDocument.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_W);

		cut.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		cut.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_X);

		copy.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));
		copy.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_C);

		paste.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));
		paste.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_V);

		statInfo.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control I"));
		statInfo.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_I);

		exit.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control Q"));
		exit.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_Q);
		
		setEnglish.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt E"));
		setEnglish.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_E);
		
		setCroatian.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt C"));
		setCroatian.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_R);
		
		setGerman.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt G"));
		setGerman.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_T);
		
		upperCase.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control U"));
		upperCase.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_U);
		
		lowerCase.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control L"));
		lowerCase.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_L);
		
		invertCase.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control M"));
		invertCase.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_I);
		
		sortAsc.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt A"));
		sortAsc.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_A);
		
		sortDesc.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt D"));
		sortDesc.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_D);
		
		unique.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt U"));
		unique.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_H);
	}

	/**
	 * Creates a menu bar containing various submenus and items offering all 
	 * of the application's features.
	 * 
	 * @return application's JMenuBar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar mb = new JMenuBar();
		
		JMenu file = new LJMenu("file", lp);
		mb.add(file);
		file.add(new JMenuItem(createNewDocument));
		file.add(new JMenuItem(openDocument));
		file.add(new JMenuItem(saveDocument));
		file.add(new JMenuItem(saveDocumentAs));
		file.addSeparator();
		file.add(new JMenuItem(closeDocument));
		file.add(new JMenuItem(exit));
		
		JMenu edit = new LJMenu("edit", lp);
		mb.add(edit);
		edit.add(new JMenuItem(cut));
		edit.add(new JMenuItem(copy));
		edit.add(new JMenuItem(paste));
		
		JMenu info = new LJMenu("info", lp);
		mb.add(info);
		info.add(new JMenuItem(statInfo));
		
		JMenu lang = new LJMenu("lang", lp);
		mb.add(lang);
		lang.add(new JMenuItem(setEnglish));
		lang.add(new JMenuItem(setCroatian));
		lang.add(new JMenuItem(setGerman));
		
		JMenu tools = new LJMenu("tools", lp);
		mb.add(tools);
		JMenu changeCase = new LJMenu("changeCase", lp);
		tools.add(changeCase);
		changeCase.add(new JMenuItem(upperCase));
		changeCase.add(new JMenuItem(lowerCase));
		changeCase.add(new JMenuItem(invertCase));
		JMenu sort = new LJMenu("sort", lp);
		tools.add(sort);
		sort.add(new JMenuItem(sortAsc));
		sort.add(new JMenuItem(sortDesc));
		tools.add(new JMenuItem(unique));
		
		return mb;
	}
	
	/**
	 * Creates a dockable toolbar with some of the application's features.
	 * 
	 * @return new JToolBar to be added
	 */
	private JToolBar createToolbar() {
		JToolBar tb = new JToolBar();
		tb.setFloatable(true);
		
		tb.add(new JButton(createNewDocument));
		tb.add(new JButton(openDocument));
		tb.add(new JButton(saveDocument));
		tb.add(new JButton(saveDocumentAs));
		tb.addSeparator();
		tb.add(new JButton(closeDocument));
		tb.add(new JButton(exit));
		
		return tb;
	}
	
	/**
	 * Creates a status bar with informations about document length,
	 * caret position, and current time.
	 * 
	 * @return new JToolBar to be added
	 */
	private JToolBar createStatusBar() {
		JToolBar tb = new JToolBar(); 
		tb.setFloatable(true);
		
		JPanel panel = new JPanel(new GridLayout(1, 3));
		panel.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.GRAY));
		tb.add(panel);
		
		lengthLabel.setText(updateLength());
		caretLabel.setText(updateCaret());
		timeLabel = new ClockLabel();
		
		panel.add(lengthLabel);
		panel.add(caretLabel);
		panel.add(timeLabel);
		

		return tb;
	}
	
	/**
	 * Static class representing a label with a clock showing the current time.
	 * At the closing of the application, the clock is stopped using the {@link ClockLabel#stop()} method.
	 * 
	 * @author stipe
	 *
	 */
	static class ClockLabel extends JLabel {
		private static final long serialVersionUID = 1L;
		
		/**
		 * Date and time formatter.
		 */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		/**
		 * Stopping flag.
		 */
		private boolean stop;
		
		/**
		 * Constructor.
		 */
		public ClockLabel() {
			setHorizontalAlignment(RIGHT);
			updateTime();
			
			Thread t = new Thread(()->{
				while(true) {
					try {
						if (stop) {
							break;
						}
						Thread.sleep(500);
					} catch(Exception ex) {}
					SwingUtilities.invokeLater(()->{
						updateTime();
					});
				}
			});
			t.setDaemon(true);
			t.start();
		}
		
		/**
		 * Updates the time shown in the label.
		 */
		private void updateTime() {
			setText(formatter.format(LocalDateTime.now()));
		}
		
		/**
		 * Stops the clock.
		 */
		private void stop() {
			stop = true;
		}
	}

	/**
	 * Action which creates a new, blank document.
	 */
	private final Action createNewDocument = new LocalizableAction("new", lp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
		}
	};

	/**
	 * Action which opens an already existing document.
	 */
	private final Action openDocument = new LocalizableAction("open", lp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
			jfc.setDialogTitle("Open file");
			if(jfc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			Path filePath = jfc.getSelectedFile().toPath();			
			try {
				model.loadDocument(filePath);				
			} catch (RuntimeException ex) {
				getErrorMessage(ex);
			}
		}
	};

	/**
	 * Action which saves the current document.
	 */
	private final Action saveDocument = new LocalizableAction("save", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel currentDoc = model.getCurrentDocument();
			if (currentDoc.getFilePath() == null) {
				saveDocumentAs.actionPerformed(e);
				return;
			}
			
			try {
				model.saveDocument(currentDoc, null);
			} catch (RuntimeException ex) {
				getErrorMessage(ex);
				return;
			}
			
			getInfoMessage(lp.getString("saveSuc"));
		}
	};

	/**
	 * Action which saves the current document under a different name.
	 */
	private final Action saveDocumentAs = new LocalizableAction("saveAs", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
			jfc.setDialogTitle("Save file");
			if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				getInfoMessage(lp.getString("notSave"));
				return;
			}
			
			Path newPath = jfc.getSelectedFile().toPath();
			if (Files.exists(newPath)) {
				if (getOverwriteConfirmation() == JOptionPane.NO_OPTION) {
					return;
				}
			}
			
			try {
				model.saveDocument(model.getCurrentDocument(), newPath);
			} catch (RuntimeException ex) {
				getErrorMessage(ex);
				return;
			}
			getInfoMessage(lp.getString("saveSuc"));
		}
	};

	/**
	 * Action which closes the current document.
	 * If the document hasn't been saved, user is asked to save it.
	 */
	private final Action closeDocument = new LocalizableAction("close", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel currentDoc = model.getCurrentDocument();
			if (currentDoc.isModified()) {
				int answer = getSaveConfirmation(currentDoc);
				if (answer == JOptionPane.YES_OPTION) {
					saveDocument.actionPerformed(e);
				} else if (answer == JOptionPane.CANCEL_OPTION) {
					return;
				} else {
					getInfoMessage(lp.getString("closeWOS"));
				}
			}
			model.closeDocument(currentDoc);
		}
	};
	
	/**
	 * Action which cuts the selected text to the clipboard.
	 */
	private final Action cut = new LocalizableAction("cut", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().cut();
		}
	};

	/**
	 * Action which copies the selected text to the clipboard.
	 */
	private final Action copy = new LocalizableAction("copy", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().copy();	
		}
	};
	
	/**
	 * Action which pasts text from the clipboard.
	 */
	private final Action paste = new LocalizableAction("paste", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().paste();
		}
	};
	
	/**
	 * Action which shows a pop up with statistical information
	 * about the document.
	 */
	private final Action statInfo = new LocalizableAction("statInfo", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			String info = String.format("Your document has %d characters, "
					+ "%d non-blank characters, %d lines.", 
					getLen(), getNonBlankLen(), getNumberOfLines());
			
			getInfoMessage(info);
		}
	};
	
	/**
	 * Action which exits the application, offering to save all modified
	 * documents beforehand.
	 */
	private final Action exit = new LocalizableAction("exit", lp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (getExitMsg() == JOptionPane.NO_OPTION) {
				return;
			}
			int i = model.getNumberOfDocuments() - 1;
			while (i >= 0) {
				SingleDocumentModel currentDoc = model.getDocument(i--);
				if (currentDoc.isModified()) {
					int answer = getSaveConfirmation(currentDoc);
					if (answer == JOptionPane.YES_OPTION) {
						saveDocument.actionPerformed(e);
					} else if (answer == JOptionPane.CANCEL_OPTION) {
						return;
					} else {
						getInfoMessage(lp.getString("closeWOS"));
					}
				}
				model.closeDocument(currentDoc);	
			}
			
			timeLabel.stop();
			dispose();
		}
	};
	
	/**
	 * Action which sets the current language to English.
	 */
	private final Action setEnglish = new LocalizableAction("en", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};
	
	/**
	 * Action which sets the current language to Croatian.
	 */
	private final Action setCroatian = new LocalizableAction("hr", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};
	
	/**
	 * Action which sets the current language to German.
	 */
	private final Action setGerman = new LocalizableAction("de", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};
	
	/**
	 * Action which sets selected text to upper case.
	 */
	private final Action upperCase = new LocalizableAction("upCase", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedLetters(String::toUpperCase);
		}
	};
	
	/**
	 * Action which sets selected text to lower case.
	 */
	private final Action lowerCase = new LocalizableAction("loCase", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedLetters(String::toLowerCase);
		}
	};
	
	/**
	 * Action which inverts the case of the letters in the selected text.
	 */
	private final Action invertCase = new LocalizableAction("invCase", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedLetters(JNotepadPP.this::invertCase);
		}
	};
	
	/**
	 * Action which sorts the selected lines ascending.
	 */
	private final Action sortAsc = new LocalizableAction("sortAsc", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedLines(JNotepadPP.this::doAscSort);
		}
	};
	
	/**
	 * Action which sorts the selected lines descending.
	 */
	private final Action sortDesc = new LocalizableAction("sortDesc", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedLines(JNotepadPP.this::doDescSort);
		}
	};
	
	/**
	 * Action which removes duplicate lines from selected lines.
	 */
	private final Action unique = new LocalizableAction("unique", lp) {
		private static final long serialVersionUID = 1L;
	
		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedLines(JNotepadPP.this::removeCopies);
		}
	};
	
	/**
	 * Eliminates duplicate lines from text.
	 * 
	 * @param text text
	 * @return text without duplicate lines
	 */
	private String removeCopies(String text) {
		return text.lines().distinct().collect(Collectors.joining("\n"));
	}

	/**
	 * Sorts lines from text ascending.
	 * 
	 * @param text text
	 * @return text with sorted lines
	 */
	private String doAscSort(String text) {
		return doSort(text, true);
	}
	
	/**
	 * Sorts lines from text descending.
	 * 
	 * @param text text
	 * @return text with sorted lines
	 */
	private String doDescSort(String text) {
		return doSort(text, false);
	}
	
	/**
	 * Sorts the lines from text, order depending on given boolean argument.
	 * 
	 * @param text text
	 * @param isAsc flag representing if the sort is to be ascending
	 * @return text with sorted lines
	 */
	private String doSort(String text, boolean isAsc) {
		Comparator<String> comp = isAsc ? Comparator.naturalOrder() : Comparator.reverseOrder();
		return text.lines().sorted(comp).collect(Collectors.joining("\n"));
	}

	/**
	 * Return the caret values to be shown in status bar.
	 * 
	 * @return text containing caret info
	 */
	private String updateCaret() {
		JTextArea area = model.getCurrentDocument().getTextComponent();
		try {
			int row = area.getLineOfOffset(area.getCaretPosition());
			int column = area.getCaretPosition() - area.getLineStartOffset(row);
			int selected = updateSelectedLength();
			return String.format("%s:%d   %s:%d   %s:%d", lp.getString("line"), row + 1, 
					lp.getString("column"), column + 1, lp.getString("selected"), selected);
			
		} catch (BadLocationException ignorable) {
			return null;			
		}
	}

	/**
	 * Returns the length of the selected text.
	 * 
	 * @return selected text length
	 */
	private int updateSelectedLength() {
		Caret caret = model.getCurrentDocument().getTextComponent().getCaret();
		return Math.abs(caret.getDot() - caret.getMark());
	}
	
	/**
	 * Returns the values to be shown in length part of the status bar.
	 * 
	 * @return String containing values about document length
	 */
	private String updateLength() {
		return lp.getString("length") + ":" + Integer.toString(getLen());
	}
	
	/**
	 * Returns the number of lines in the document.
	 * 
	 * @return numebr of lines
	 */
	private int getNumberOfLines() {		
		return model.getCurrentDocument().getTextComponent().getLineCount();
	}
	
	/**
	 * Changes the selected part of the text in a way defined by the arguments.
	 * 
	 * @param textChooser way of choosing the selected text
	 * @param transform way of transforming the text
	 */
	private void changeSelected(Function<JTextComponent, int[]> textChooser, 
				Function<String, String> transform) {
		JTextComponent area = model.getCurrentDocument().getTextComponent();
		Document doc = area.getDocument();

		int[] results = textChooser.apply(area);
		int offset = results[0];
		int len = results[1];
		
		if(len == 0) return;
			
		try {
			String text = doc.getText(offset, len);
			text = transform.apply(text);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch(BadLocationException ignorable) {
			ignorable.printStackTrace();
		}
	}
	
	/**
	 * Changes the letters of the selected text in the way defined by the argument.
	 * 
	 * @param transform way of transforming text
	 */
	private void changeSelectedLetters(Function<String, String> transform) {
		Function<JTextComponent, int[]> textChooser = area -> {
			Caret caret = area.getCaret();
			
			int offset = Math.min(caret.getDot(), caret.getMark());
			int len = Math.abs(caret.getDot() - caret.getMark());
			
			return new int[] {offset, len};
		};
		
		changeSelected(textChooser, transform);
	}
	
	/**
	 * Changes the lines of the selected text in the way defined by the argument.
	 * 
	 * @param transform way of transforming text
	 */
	private void changeSelectedLines(Function<String, String> transform) {
		Function<JTextComponent, int[]> textChooser = area -> {
			Document doc = area.getDocument();
			Caret caret = area.getCaret();
			
			int start = Math.min(caret.getDot(), caret.getMark());
			int end = Math.max(caret.getDot(), caret.getMark());
			
			Element root = doc.getDefaultRootElement();
			int offset = root.getElement(root.getElementIndex(start)).getStartOffset();
			int len = root.getElement(root.getElementIndex(end)).getEndOffset() - offset - 1;
			return new int[] {offset, len};
		};
		
		changeSelected(textChooser, transform);
	}
	
	/**
	 * Inverts the case of all letters in text.
	 * 
	 * @param text text
	 * @return text with inverted cases
	 */
	private String invertCase(String text) {
		char[] chars = text.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if(Character.isUpperCase(c)) {
				chars[i] = Character.toLowerCase(c);
			} else if(Character.isLowerCase(c)) {
				chars[i] = Character.toUpperCase(c);
			}
		}
		
		return new String(chars);
	}
	
	/**
	 * Returns the number of non-blank characters in document.
	 * 
	 * @return number of non-blank characters
	 */
	private int getNonBlankLen() {
		return model.getCurrentDocument().getTextComponent().getText()
				.replaceAll("\\s++", "").length();
	}
	
	/**
	 * Returns the number of characters in document.
	 * 
	 * @return number of characters
	 */
	private int getLen() {
		return model.getCurrentDocument().getTextComponent().getText().length();
	}

	/**
	 * Get confirmation about saving the current document.
	 * 
	 * @param currentDoc document to be saved
	 * @return user's answer
	 */
	private int getSaveConfirmation(SingleDocumentModel currentDoc) {
		Path path = currentDoc.getFilePath();
		String fileName = path == null ? "(unnamed)" : path.getFileName().toString();
		Object[] options = new Object[] {
				lp.getString("yesMsg"),
				lp.getString("noMsg"),
				lp.getString("cancelMsg")
		};
		return JOptionPane.showOptionDialog(
				this, 
				lp.getString("saveFileMsg") + "\"" + fileName + "\"?",
				lp.getString("saveMsg"), 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.WARNING_MESSAGE, 
				null, 
				options, 
				JOptionPane.YES_OPTION);
	}
	
	/**
	 * Get confirmation about overwriting a document.
	 * 
	 * @return user's answer
	 */
	private int getOverwriteConfirmation() {
		Object[] options = new Object[] {
				lp.getString("yesMsg"),
				lp.getString("noMsg"),
		};
		return JOptionPane.showOptionDialog(
				this, 
				lp.getString("overwriteMsg"),
				lp.getString("overwriteTitle"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.WARNING_MESSAGE, 
				null, 
				options, 
				JOptionPane.YES_OPTION);
	}

	/**
	 * Get information window.
	 * 
	 * @param msg message
	 */
	private void getInfoMessage(String msg) {
		JOptionPane.showOptionDialog(
				this, 
				msg,
				lp.getString("infoMsg"), 
				JOptionPane.OK_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, 
				new Object[] {lp.getString("okMsg")}, 
				JOptionPane.OK_OPTION);
	}

	/**
	 * Gets error message window.
	 * 
	 * @param ex exception that caused the error
	 */
	private void getErrorMessage(RuntimeException ex) {
		JOptionPane.showOptionDialog(
				this, 
				ex.getMessage(),
				lp.getString("errorMsg"), 
				JOptionPane.OK_OPTION, 
				JOptionPane.ERROR_MESSAGE, 
				null, 
				new Object[] {lp.getString("okMsg")}, 
				JOptionPane.OK_OPTION);
	}
	
	/**
	 * Gets exit message. Asks user if he is sure he wants 
	 * to exit the application.
	 * 
	 * @return user's answer
	 */
	private int getExitMsg() {
		Object[] options = new Object[] {
				lp.getString("yesMsg"), 
				lp.getString("noMsg")
		};
		return JOptionPane.showOptionDialog(
				this, 
				lp.getString("closeApp"),
				lp.getString("closeAppTitle"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.WARNING_MESSAGE, 
				null, 
				options, 
				JOptionPane.YES_OPTION);
	}
	
	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->
			new JNotepadPP().setVisible(true)
		);
	}
	
}

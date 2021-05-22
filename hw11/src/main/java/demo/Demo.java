package demo;

import java.awt.BorderLayout;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;

public class Demo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Demo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(10, 10);
		setSize(600, 600);
	
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				System.out.println("Tab: " + tabbedPane.getSelectedIndex());
			}
		});
		
		add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addTab("Lol", new JTextArea());
		tabbedPane.addTab("lmao", new JButton());
		JFileChooser jfc = new JFileChooser();
		jfc.showSaveDialog(this);

			JOptionPane.showConfirmDialog(
					this, 
					"File already exists on that path.\n Overwrite?",
					"Overwrite?",
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE);
		
	}

	public static void main(String[] args) {
		
//		SwingUtilities.invokeLater(() ->
//			new Demo().setVisible(true)
//		);
		
		String s = "a	\ng";
		System.out.println(s.length());
		System.out.println(s.replaceAll("\\s+", "").length());
				
	
	}
	
}

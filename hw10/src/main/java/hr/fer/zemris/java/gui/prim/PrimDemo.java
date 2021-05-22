package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class PrimDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(20, 20);
		setSize(300, 500);
		initGUI();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JButton nextButton = new JButton("sljedeći");
		cp.add(nextButton, BorderLayout.PAGE_END);
		
		JPanel centerPanel = new JPanel(new GridLayout(1, 2));
		cp.add(centerPanel, BorderLayout.CENTER);
		
		PrimListModel model = new PrimListModel();
		JList<Integer> leftListView = new JList<>(model);
		JList<Integer> rightListView = new JList<>(model);
		
		centerPanel.add(new JScrollPane(leftListView));
		centerPanel.add(new JScrollPane(rightListView));
		
		nextButton.addActionListener(e -> model.next());
	}
	
	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new PrimDemo().setVisible(true);
		});
	}
	
}

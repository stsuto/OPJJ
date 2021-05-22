package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.gui.calc.buttons.BasicMathButton;
import hr.fer.zemris.java.gui.calc.buttons.BasicOperationButton;
import hr.fer.zemris.java.gui.calc.buttons.DigitButton;
import hr.fer.zemris.java.gui.calc.buttons.InversiveButton;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Main class representing a calculator.
 * 
 * @author stipe
 *
 */
public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Calculator model.
	 */
	private CalcModelImpl model;
	/**
	 * Calculator screen.
	 */
	private JLabel screen;
	/**
	 * Stack of values.
	 */
	private Stack<Double> stack;
	/**
	 * List of Inversive buttons.
	 */
	private List<InversiveButton> inversiveButtons;
	
	/**
	 * Constructor.
	 */
	public Calculator() {
		model = new CalcModelImpl();
		model.addCalcValueListener(model -> screen.setText(model.toString()));
		stack = new Stack<>();
		inversiveButtons = new ArrayList<>();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 400);
		initGUI();
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		setLayout(new CalcLayout(4));
		
		addSpecialComponents();
		addDigitButtons();
		addBasicOperationButtons();
		addBasicMathButtons();
		addInversionButtons();
	}

	/**
	 * Adds special components.
	 */
	private void addSpecialComponents() {
		screen = createScreen();
		add(screen, "1,1");
		
		JCheckBox checkBox = new JCheckBox("Inv");
		checkBox.addActionListener(e -> 
			inversiveButtons.forEach(ib -> ib.invertButton()));
		add(checkBox, "5,7");
	}
	
	/**
	 * Creates the calculator screen.
	 * 
	 * @return JLabel representing the screen
	 */
	private JLabel createScreen() {
		JLabel screen = new JLabel("0", JLabel.RIGHT);
		
		screen.setFont(new Font("Comic Sans", Font.BOLD, 30));
		screen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		screen.setBackground(Color.YELLOW);
		screen.setOpaque(true);
		
		return screen;
	}

	/**
	 * Adds the digit buttons.
	 */
	private void addDigitButtons() {
		for (int i = 0; i < 10; i++) {
			add(new DigitButton(i, model), getPosition(i));
		}
	}

	/**
	 * Gets the {@link RCPosition} for the given index
	 * 
	 * @param i index
	 * @return {@link RCPosition}
	 */
	private RCPosition getPosition(int i) {
		int row = 2 + (9 - i) / 3;
		int column = 3 + (i > 0 ? i - 1 : 0) % 3;
		return new RCPosition(row, column);
	}

	/**
	 * Adds basic operation buttons.
	 */
	private void addBasicOperationButtons() {
		add(new BasicOperationButton("=", e -> {
				model.setValue(
					model.getPendingBinaryOperation().applyAsDouble(
						model.getActiveOperand(),
						model.getValue())
				);
				model.clearActiveOperand();
				model.setPendingBinaryOperation(null);
		}), "1,6");
		add(new BasicOperationButton("clr", e -> model.clear()), "1,7");
		add(new BasicOperationButton("reset", e -> model.clearAll()), "2,7");
		add(new BasicOperationButton("push", e -> stack.push(model.getValue())), "3,7");
		add(new BasicOperationButton("pop", e -> model.setValue(stack.pop())), "4,7");
		add(new BasicOperationButton(".", e -> model.insertDecimalPoint()), "5,5");
		add(new BasicOperationButton("+/-", e -> model.swapSign()), "5,4");	
	}
	
	/**
	 * Adds basic math buttons.
	 */
	private void addBasicMathButtons() {
		add(new BasicMathButton("/", (x, y) -> x / y, model), "2,6");
		add(new BasicMathButton("*", (x, y) -> x * y, model), "3,6");
		add(new BasicMathButton("-", (x, y) -> x - y, model), "4,6");
		add(new BasicMathButton("+", (x, y) -> x + y, model), "5,6");		
		add(new BasicMathButton("1/x", x -> 1 / x, model), "2,1");
	}
	
	/**
	 * Adds inversive buttons.
	 */
	private void addInversionButtons() {
		addInvButton(new InversiveButton(
				"sin", Math::sin, 
				"arcsin", Math::asin, 
				model), "2,2"
		);
		addInvButton(new InversiveButton(
				"cos", Math::cos, 
				"arccos", Math::acos, 
				model), "3,2"
		);
		addInvButton(new InversiveButton(
				"tan", Math::tan, 
				"arctan", Math::atan, 
				model), "4,2"
		);
		addInvButton(new InversiveButton(
				"ctg", x -> 1 / Math.tan(x), 
				"arcctg", x -> 1 / Math.atan(x), 
				model), "5,2"
		);
		addInvButton(new InversiveButton(
				"x^n", Math::pow, 
				"x^(1/n)", (x, n) -> Math.pow(x, 1 / n), 
				model), "5,1"
		);
		addInvButton(new InversiveButton(
				"ln", Math::log, 
				"e^x", x -> Math.pow(Math.E, x), 
				model), "4,1"
		);
		addInvButton(new InversiveButton(
				"log", Math::log10, 
				"10^x", x -> Math.pow(10, x), 
				model), "3,1"
		);
	}

	/**
	 * Adds the given inversive button to the list of buttons and onto the GUI.
	 * 
	 * @param inversiveButton button
	 * @param position position
	 */
	private void addInvButton(InversiveButton inversiveButton, String position) {
		inversiveButtons.add(inversiveButton);
		add(inversiveButton, position);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> 
			new Calculator().setVisible(true)
		);
	}
	
}

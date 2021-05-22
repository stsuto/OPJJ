package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import hr.fer.zemris.java.gui.calc.Calculator;

/**
 * Class representing a layout used for {@link Calculator}.
 * 
 * @author stipe
 *
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Number of rows.
	 */
	private static final int ROWS = 5;
	/**
	 * Number of columns.
	 */
	private static final int COLUMNS = 7;
	/**
	 * Number of columns that the big first component takes up.
	 */
	private static final int BIG_COLUMNS = 5;	
	
	/**
	 * Free space between components.
	 */
	private int freeSpace;
	/**
	 * Components and their positions.
	 */
	private Map<Component, RCPosition> components;
	
	/**
	 * Default constructor which sets the {@link #freeSpace} to {@code 0}.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param freeSpace {@link #freeSpace}
	 */
	public CalcLayout(int freeSpace) {
		if (freeSpace < 0) {
			throw new IllegalArgumentException("Space between components can't be negative!");
		}
		this.freeSpace = freeSpace;
		this.components = new HashMap<>();
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return getDimensions(parent, comp -> comp.getPreferredSize());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return getDimensions(parent, comp -> comp.getMinimumSize());
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets ins = parent.getInsets();
		int[] widths = getWidths(parent);
		int[] heights = getHeights(parent);
		int[] startX = getStart(parent, widths, COLUMNS, ins.left);
		int[] startY = getStart(parent, heights, ROWS, ins.top);
		
		for (Map.Entry<Component, RCPosition> entry : components.entrySet()) {
			Component comp = entry.getKey();
			RCPosition pos = entry.getValue();
			int row = pos.getRow();
			int column = pos.getColumn();
			
			if (row == 1 && column == 1) {
				comp.setBounds(startX[0], startY[0], startX[BIG_COLUMNS] - freeSpace, heights[0]);
			} else {
				comp.setBounds(startX[column - 1], startY[row - 1], widths[column - 1], heights[row - 1]);
			}
		}
	}
	
	/**
	 * Gets the widths of the components.
	 * 
	 * @param parent container
	 * @return int array of widths
	 */
	private int[] getWidths(Container parent) {
		Insets ins = parent.getInsets();
		int componentSpace = parent.getWidth() - ins.left - ins.right - (COLUMNS - 1) * freeSpace;
		int compWidth = componentSpace / COLUMNS;
		int leftover = componentSpace -  COLUMNS * compWidth;
		
		int widths[] = spreadLeftovers(leftover, COLUMNS);
//		Arrays.stream(widths).map(e -> e + compWidth).toArray();

		for (int i = 0; i < widths.length; i++) {
			widths[i] += compWidth;
		}

		return widths;
	}

	/**
	 * Spreads the leftover of the component space equally.
	 * 
	 * @param leftover number of leftover pixcels
	 * @param number number of components
	 * @return array of component sizes
	 */
	private int[] spreadLeftovers(int leftover, int number) {
		int[] spreads = new int[number];
		boolean allow = true;

		if (leftover == 0) {
			return spreads;
		}
		
		while (true) {
			for (int i = 0; i < number; i++) {
				if (spreads[i] == 0) {
					if (allow) {
						spreads[i] = 1;
						if (--leftover == 0) {
							return spreads;
						}
						allow = false;
					
					} else {
						allow = true;
					}
				}
			}
		}
	}

	/**
	 * Gets the heights of the components.
	 * 
	 * @param parent container
	 * @return int array of heights
	 */
	private int[] getHeights(Container parent) {
		Insets ins = parent.getInsets();
		int compHeight = (parent.getHeight() - ins.top - ins.bottom - (ROWS - 1) * freeSpace) / ROWS;
		int leftover = parent.getHeight() - ins.top - ins.bottom - (ROWS - 1) * freeSpace - ROWS * compHeight;
		
		int heights[] = spreadLeftovers(leftover, ROWS);

		for (int i = 0; i < heights.length; i++) {
			heights[i] += compHeight;
		}
		
		return heights;
	}

	/**
	 * Gets the starting points of the components.
	 * 
	 * @param parent container
	 * @param sizes sizes
	 * @param number number of components
	 * @param inset relevant inset
	 * @return int array of starting points
	 */
	private int[] getStart(Container parent, int[] sizes, int number, int inset) {
		int[] start = new int[number];
		
		start[0] = inset;
		for(int i = 1; i < number; i++) {
			start[i] = start[i - 1] + sizes[i - 1] + freeSpace;
		}
		
		return start;
	}
	
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition position = checkConstraints(constraints);
		
		if (components.containsValue(position)) {
			throw new CalcLayoutException("Another component is already placed on that position!");
		}
		
		components.put(comp, position);
	}

	/**
	 * Checks the constraints.
	 * 
	 * @param constraints
	 * @return
	 */
	private RCPosition checkConstraints(Object constraints) {
		RCPosition position;
		
		if (constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
		
		} else if (constraints instanceof String) {
			position = parseConstraints((String) constraints);
			
		} else {
			throw new UnsupportedOperationException("Invalid type of constraint object!");
		}
		
		int x = position.getRow();
		int y = position.getColumn();
		if (x < 1 || x > ROWS || y < 1 || y > COLUMNS
				|| (x == 1 && y != 1 && y <= BIG_COLUMNS)) {
			throw new CalcLayoutException("Layout constraints were not satisfied!");
		}
		
		return position;
	}
	
	/**
	 * Parses the string constraints into {@code RCPosition}.
	 * 
	 * @param constraints constraint
	 * @return {@link RCPosition}
	 */
	private RCPosition parseConstraints(String constraints) {
		String[] parts = ((String) constraints).split(",");
		if (parts.length != 2) {
			throw new CalcLayoutException("Wrong parsed constriction format!");
		}
		try {
			return new RCPosition(
				Integer.parseInt(parts[0]), 
				Integer.parseInt(parts[1])
			);
			
		} catch (NumberFormatException e) {
			throw new CalcLayoutException("Invalid number format in parsed constriction!");
		}
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return getDimensions(target, comp -> comp.getMaximumSize());
	}

	/**
	 * Creates a new dimension.
	 * 
	 * @param target container
	 * @param fun function
	 * @return new dimension
	 */
	private Dimension getDimensions(Container target, Function<Component, Dimension> fun) {
		Dimension minDim = new Dimension(0, 0);
		for (Map.Entry<Component, RCPosition> entry : components.entrySet()) {
			Component comp = entry.getKey();
			RCPosition pos = entry.getValue();
			Dimension dim = fun.apply(comp);
			
			if (dim == null) continue;
			
			if (pos.getRow() == 1 && pos.getColumn() == 1) {
				int bigWidth = dim.width - (BIG_COLUMNS - 1) * freeSpace;
				minDim.width = Math.max(minDim.width, bigWidth / BIG_COLUMNS);		
			} else {
				minDim.width = Math.max(minDim.width, dim.width);	
			}
			
			minDim.height = Math.max(minDim.height, dim.height);
		}
		
		Insets ins = target.getInsets();
		
		return new Dimension(
				COLUMNS * minDim.width + (COLUMNS - 1) * freeSpace + ins.left + ins.right,
				ROWS * minDim.height + (ROWS - 1) * freeSpace + ins.bottom + ins.top
			);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {}

	
}

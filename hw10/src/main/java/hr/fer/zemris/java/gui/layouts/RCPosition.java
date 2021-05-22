package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Class representing a position within the {@link CalcLayout}.
 * 
 * @author stipe
 *
 */
public class RCPosition {

	/**
	 * Row of position.
	 */
	private int row;
	/**
	 * Column of position.
	 */
	private int column;
	
	/**
	 * Constructor.
	 * 
	 * @param row {@link #row}
	 * @param column {@link #column}
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}
	
	
	
}

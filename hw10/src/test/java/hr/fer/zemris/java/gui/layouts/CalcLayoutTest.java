package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;

import javax.swing.JLabel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalcLayoutTest {

	@Test
	void testAddLayoutComponentComponentObject() {
		CalcLayout layout = new CalcLayout();
		
		Object[] constraints = new Object[] { 
				new RCPosition(0, 5),
				new RCPosition(3, -3),
				new RCPosition(1, 3),
				"0,5",
				"3,-3",
				"1.3,3",
				"1,3.7",
				"1,3,4,1"
		};
	
		Object[] constraints2 = new Object[] { 
				Integer.valueOf(3),
				Double.valueOf(3.14),
				new Object(),
				new JLabel()
		};
		
		Component comp = new JLabel();
		
		for (Object constraint : constraints) {
			assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(comp, constraint));
		}
		
		for (Object constraint : constraints2) {
			assertThrows(UnsupportedOperationException.class, () -> layout.addLayoutComponent(comp, constraint));
		}
	}

}

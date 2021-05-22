package hr.fer.zemris.lsystems.impl.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;

class LSystemBuilderImplTest {

	LSystemBuilderImpl builder;
	
	@Test
	void testLSystemBuilderImpl() {
		assertNotNull(new LSystemBuilderImpl());
	}
	
	@BeforeEach
	void initBuilder() {
		builder = new LSystemBuilderImpl();
	}
	
	@Test
	void testBuild() {
		assertNotNull(builder.build());
	}

	@Test
	void testGenerate() {
		builder.setAxiom("F").registerProduction('F', "F+F--F+F");
		LSystem built = builder.build();
		
		assertEquals("F", built.generate(0));
		assertEquals("F+F--F+F", built.generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", built.generate(2));
	}

	@Test
	void testConfigureFromText() {
		String[] data1 = new String[] {
				"origin 0.05 0.4",
				"angle 0",
				"unitLength 0.9",
				"unitLengthDegreeScaler 1.0 / 3.0",
				"",
				"command F draw 1",
				"command + rotate 60",
				"command - rotate -60",
				"",
				"axiom F",
				"",
				"production F F+F--F+F"
		};
		assertDoesNotThrow(() -> builder.configureFromText(data1));
		String[] data2 = new String[] {"unitLengthDegreeScaler 1.0 / 3.0"};
		assertDoesNotThrow(() -> builder.configureFromText(data2));
		String[] data3 = new String[] {"unitLengthDegreeScaler 1.0 /3.0"};
		assertDoesNotThrow(() -> builder.configureFromText(data3));
		String[] data4 = new String[] {"unitLengthDegreeScaler 1.0/ 3.0"};
		assertDoesNotThrow(() -> builder.configureFromText(data4));
		String[] data5 = new String[] {"unitLengthDegreeScaler 1.0/3.0"};
		assertDoesNotThrow(() -> builder.configureFromText(data5));

		String[] data6 = new String[] {"unitLengthDegreeScaler 1.0 3.0"};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data6));
		String[] data7 = new String[] {"command AA pop"};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data7));
		String[] data8 = new String[] {"unitLength "};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data8));
		String[] data9 = new String[] {"command F draw"};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data9));
		String[] data10 = new String[] {"command F draw 1 2"};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data10));
		String[] data11 = new String[] {"command F color 00AA"};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data11));
		String[] data12 = new String[] {"command skip 1.2 1"};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data12));
		String[] data13 = new String[] {"command rotate -10 20"};
		assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(data13));
		
	}

	@Test
	void testRegisterCommand() {
		assertDoesNotThrow(() -> builder.registerCommand('F', "draw 1"));
		assertDoesNotThrow(() -> builder.registerCommand('+', "rotate 60"));
		assertDoesNotThrow(() -> builder.registerCommand('-', "rotate -60"));
		assertDoesNotThrow(() -> builder.registerCommand('+', "scale 0.5"));
		assertDoesNotThrow(() -> builder.registerCommand('-', "skip 1"));
		assertDoesNotThrow(() -> builder.registerCommand('+', "color ff0000"));

		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "draw 1 2"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "draw"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "rotate 60 30"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "rotate"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "scale 60 30"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "scale"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "color"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "color 00AA"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "skip 1 2"));
		assertThrows(IllegalArgumentException.class, () -> builder.registerCommand('F', "skip"));
	}
	
}

package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexTest {

	@Test
	void testToString() {
		Complex c = new Complex(3.14, 0);
		assertEquals("(3.1+i0.0)", c.toString());
		
		c = new Complex(3.14, 0);
		assertEquals("(3.1+i0.0)", c.toString());
		
		c = new Complex(0, 1.535);
		assertEquals("(0.0+i1.5)", c.toString());
	
		c = new Complex(0, -1.535);
		assertEquals("(0.0-i1.5)", c.toString());
		
		c = new Complex(3.14, 0.000000000001);
		assertEquals("(3.1+i0.0)", c.toString());
		
		c = new Complex(3.14, 1.4342);
		assertEquals("(3.1+i1.4)", c.toString());
		
		c = new Complex(3.14, -1.4342);
		assertEquals("(3.1-i1.4)", c.toString());
	}

	@Test
	void testParseValid() {
		String[] input = new String[] {
				"1",
				"-1 + i0",
				"i",
				"0 - i1",
				"0 - i0",
				"i0",
				"-i",
				"i",
			};
		
		Complex[] output = new Complex[] {
				Complex.ONE,
				Complex.ONE_NEG,
				Complex.IM,
				Complex.IM_NEG,
				Complex.ZERO,
				Complex.ZERO,
				Complex.IM_NEG,
				Complex.IM	
			};
		
		int len = input.length;
		for (int i = 0; i < len; i++) {
			assertEquals(output[i], Complex.parse(input[i]));
		}
	}
	
	@Test
	void testParseInvalid() {
		String[] input = new String[] {
				"+1",
				"-1 +- i0",
				"",
				"2i - i1",
				"1.1 i3",
				"3 4 i",
				"2 - 3i"
			};
		
		for (String s : input) {
			assertThrows(NumberFormatException.class, () -> Complex.parse(s));
		}
		
	}
	
}

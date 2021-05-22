package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FactorialTest {
	
	@Test
	public void testWholeNumberInBounds() {
		assertEquals(6, Factorial.calculateFactorial(3));
	}
	
	@Test
	public void testSmallWholeNumber() {
		assertEquals(2, Factorial.calculateFactorial(2));
	}

	@Test
	public void testNegativeNumber() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.calculateFactorial(-2));
	}
	
	@Test
	public void testBigWholeNumber() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.calculateFactorial(21));
	}
}

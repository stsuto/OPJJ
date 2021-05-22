package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexNumberTest {

	@Test
	void testComplexNumber() {
		ComplexNumber number = assertDoesNotThrow(() -> new ComplexNumber(3, 1.1));
		assertNotNull(number);
	}
	
	@Test
	void testGetReal() {
		ComplexNumber number = new ComplexNumber(2.5, 1.1);
		assertEquals(2.5, number.getReal());
	}
	
	@Test
	void testGetImaginary() {
		ComplexNumber number = new ComplexNumber(2.5, 1.1);
		assertEquals(1.1, number.getImaginary());
	}

	@Test
	void testFromReal() {
		ComplexNumber number = ComplexNumber.fromReal(7.1);
		assertNotNull(number);
		assertEquals(7.1, number.getReal());
	}

	@Test
	void testFromImaginary() {
		ComplexNumber number = ComplexNumber.fromImaginary(2);
		assertNotNull(number);
		assertEquals(0, number.getReal());
		assertEquals(2, number.getImaginary());
	}

	@Test
	void testFromMagnitudeAndAngle() {
		ComplexNumber number = ComplexNumber.fromMagnitudeAndAngle(1, 0);
		assertNotNull(number);
		assertEquals(1, number.getReal());
		assertEquals(0, number.getImaginary());
	}

	@Test
	void testParse() {
		ComplexNumber number = ComplexNumber.parse("+2.71+3.15i"); //+2.71+3.15i
		assertNotNull(number);
		assertEquals(2.71, number.getReal());
		assertEquals(3.15, number.getImaginary());
	}

	@Test
	void testGetMagnitude() {
		ComplexNumber number = ComplexNumber.fromMagnitudeAndAngle(1.1, Math.PI);
		assertEquals(1.1, number.getMagnitude());
	}

	@Test
	void testGetAngle() {
		ComplexNumber number = ComplexNumber.fromMagnitudeAndAngle(2.1, Math.PI);
		double expected = Math.PI;
		assertTrue(Math.abs(expected - number.getAngle()) < 1e-4);
		
		ComplexNumber number1 = new ComplexNumber(2, 1);
		ComplexNumber number2 = new ComplexNumber(-2, 1);
		ComplexNumber number3 = new ComplexNumber(-2, -1);
		ComplexNumber number4 = new ComplexNumber(2, -1);
		assertTrue(number1.getAngle() > 0 && number1.getAngle() < 2 * Math.PI);
		assertTrue(number2.getAngle() > 0 && number2.getAngle() < 2 * Math.PI);
		assertTrue(number3.getAngle() > 0 && number3.getAngle() < 2 * Math.PI);
		assertTrue(number4.getAngle() > 0 && number4.getAngle() < 2 * Math.PI);
	}

	@Test
	void testEquals() {
		ComplexNumber number1 = new ComplexNumber(1.1, 2.2);
		ComplexNumber number2 = new ComplexNumber(1.1, 2.2);
		assertTrue(number1.equals(number2));
		
		number2 = new ComplexNumber(-1.1, 2.2);
		assertFalse(number1.equals(number2));
	}
	
	@Test
	void testAdd() {
		ComplexNumber number1 = new ComplexNumber(1.1, 2.2);
		ComplexNumber number2 = new ComplexNumber(3.3, 4.4);
		
		ComplexNumber result = number1.add(number2);
		ComplexNumber number3 = new ComplexNumber(1.1 + 3.3, 2.2 + 4.4);
		
		assertTrue(result.equals(number3));
	}

	@Test
	void testSub() {
		ComplexNumber number1 = new ComplexNumber(1.1, 2.2);
		ComplexNumber number2 = new ComplexNumber(3.3, 4.4);
		
		ComplexNumber result = number1.sub(number2);
		ComplexNumber number3 = new ComplexNumber(1.1 - 3.3, 2.2 - 4.4);
		
		assertTrue(result.equals(number3));
	}

	@Test
	void testMul() {
		ComplexNumber number1 = new ComplexNumber(1, 2);
		ComplexNumber number2 = new ComplexNumber(3, 4);
		
		ComplexNumber result = number1.mul(number2);
		ComplexNumber number3 = new ComplexNumber(-5, 10);
		
		assertTrue(result.equals(number3));
	}

	@Test
	void testDiv() {
		ComplexNumber number1 = new ComplexNumber(1, 2);
		ComplexNumber number2 = new ComplexNumber(2, 4);
		
		ComplexNumber result = number1.div(number2);
		ComplexNumber number3 = new ComplexNumber(0.5, 0);
		
		assertTrue(result.equals(number3));
	}

	@Test
	void testPower() {
		ComplexNumber number = new ComplexNumber(1, 1);
		int exponent = 2;
		
		ComplexNumber result = number.power(exponent);
		ComplexNumber number3 = new ComplexNumber(0, 2);
		
		assertTrue(result.equals(number3));
	}

	@Test
	void testRoot() {
		ComplexNumber number = new ComplexNumber(3, 4);
		int root = 2;
		
		ComplexNumber[] result = number.root(root);
		ComplexNumber[] array = new ComplexNumber[2];
		array[0] = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(5), (Math.atan(4d / 3) / root));
		array[1] = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(5), (Math.atan(4d / 3) + 2 * Math.PI) / root);
		
		for (int i = 0; i < result.length; i++) {
			assertTrue(result[i].equals(array[i]));
		}
	}

	@Test
	void testToString() {
		ComplexNumber number = new ComplexNumber(0, 9.21);
		assertEquals("9.21i", number.toString());
	}

}

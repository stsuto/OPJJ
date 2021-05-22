package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexPolynomialTest {

//	@Test
//	void testComplexPolynomial() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testOrder() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testMultiply() {
//		fail("Not yet implemented");
//	}

	
	@Test
	void testDerive() {
		// (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
		ComplexPolynomial pol = new ComplexPolynomial(new Complex[] {new Complex(1, 0), new Complex(5,0), new Complex(2, 0), new Complex(7,2)});
		ComplexPolynomial res = new ComplexPolynomial(new Complex[] {new Complex(5, 0), new Complex(4,0), new Complex(21, 6)});
		assertEquals(res, pol.derive());
	}

	@Test
	void testApply() {
		Complex[] factors = new Complex[] {new Complex(1.1,2.2), new Complex(4.4,-2.5)};
		ComplexPolynomial pol = new ComplexPolynomial(factors);
		Complex result = pol.apply(Complex.ONE);
		assertEquals(new Complex(5.5,-0.3), result);
	}

	@Test
	void testToString() {
		Complex[] factors = new Complex[] {new Complex(1.1,2.2), new Complex(4.4,-2.52)};
		ComplexPolynomial pol = new ComplexPolynomial(factors);
		assertEquals("(4.4-i2.5)z^1+(1.1+i2.2)", pol.toString());
	}

}

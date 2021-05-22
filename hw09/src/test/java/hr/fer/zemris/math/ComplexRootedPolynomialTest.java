package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ComplexRootedPolynomialTest {

//	@Test
//	void testComplexRootedPolynomial() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testApply() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testToComplexPolynom() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testToString() {
		Complex[] roots = new Complex[] {new Complex(1.1,2.2), new Complex(4.4,-2.52)};
		Complex constant = new Complex(1.1,1.1);
		ComplexRootedPolynomial pol = new ComplexRootedPolynomial(constant, roots);
		assertEquals("(1.1+i1.1)*(z-(1.1+i2.2))*(z-(4.4-i2.5))", pol.toString());
	}
//
//	@Test
//	void testIndexOfClosestRootFor() {
//		fail("Not yet implemented");
//	}

}

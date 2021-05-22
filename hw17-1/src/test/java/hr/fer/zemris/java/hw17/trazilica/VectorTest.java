package hr.fer.zemris.java.hw17.trazilica;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

	double DELTA = 1e-6;
	
	@Test
	void testCosAngle() {
		List<Double> list1 = Arrays.asList(new Double[] {1d, 0d});
		Vector first = new Vector(list1);
		
		List<Double> list2 = Arrays.asList(new Double[] {0d, 1d});
		Vector second = new Vector(list2);
	
		List<Double> list3 = Arrays.asList(new Double[] {3d, 2d});
		Vector third = new Vector(list3);
		
		assertEquals(0, first.cosAngle(second), DELTA);
		assertEquals(0, second.cosAngle(first), DELTA);
		assertEquals(0.832050294, first.cosAngle(third), DELTA);
		assertEquals(0.55470019622, second.cosAngle(third), DELTA);
		assertEquals(0.55470019622, third.cosAngle(second), DELTA);
	}

}

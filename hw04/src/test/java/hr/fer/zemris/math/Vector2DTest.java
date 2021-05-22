package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Vector2DTest {
	
	Vector2D vector1, vector2;
	
	private static final double DIFF = 1e-6;
	
	@Test
	void testVector2D() {
		assertNotNull(new Vector2D(2.1, -1.8));
	}

	@BeforeEach
	void initVector() {
		vector1 = new Vector2D(2, 1);
		vector2 = new Vector2D(-1, 3);
	}
	
	@Test
	void testGetX() {
		assertEquals(2, vector1.getX());
		assertEquals(-1, vector2.getX());
	}

	@Test
	void testGetY() {
		assertEquals(1, vector1.getY());
		assertEquals(3, vector2.getY());
	}

	@Test
	void testTranslate() {
		vector1.translate(vector2);
		assertEquals(1, vector1.getX());
		assertEquals(4, vector1.getY());
	}

	@Test
	void testTranslated() {
		Vector2D result = vector1.translated(vector2);
		assertEquals(1, result.getX());
		assertEquals(4, result.getY());
		assertEquals(2, vector1.getX());
		assertEquals(1, vector1.getY());
	}

	@Test
	void testRotate() {
		vector1.rotate(Math.PI);
		assertTrue(Math.abs(-2 - vector1.getX()) < DIFF);
		assertTrue(Math.abs(-1 - vector1.getY()) < DIFF);
	}

	@Test
	void testRotated() {
		Vector2D result = vector1.rotated(Math.PI);
		assertTrue(Math.abs(-2 - result.getX()) < DIFF);
		assertTrue(Math.abs(-1 - result.getY()) < DIFF);
		assertTrue(Math.abs(2 - vector1.getX()) < DIFF);
		assertTrue(Math.abs(1 - vector1.getY()) < DIFF);
	}

	@Test
	void testScale() {
		vector1.scale(3);
		assertEquals(6, vector1.getX());
		assertEquals(3, vector1.getY());
	}

	@Test
	void testScaled() {
		Vector2D result = vector1.scaled(3);
		assertEquals(6, result.getX());
		assertEquals(3, result.getY());
		assertEquals(2, vector1.getX());
		assertEquals(1, vector1.getY());
	}

	@Test
	void testCopy() {
		Vector2D result = vector1.copy();
		assertTrue(result.getX() == vector1.getX());
		assertTrue(result.getY() == vector1.getY());
	}

}

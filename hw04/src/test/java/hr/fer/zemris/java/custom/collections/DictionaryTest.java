package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DictionaryTest {

	Dictionary<String, Integer> grades;
	
	@BeforeEach
	void initDictionary() {
		grades = new Dictionary<>();
	}

	@Test
	void testDictionary() {
		assertNotNull(new Dictionary<>());
	}
	
	@Test
	void testPutAndGet() {
		grades.put("Marie", 2);
		assertEquals(2, grades.get("Marie")); // can't check if it was really added without using get, even though it is untested as well
		grades.put("Marie", 1);
		assertEquals(1, grades.get("Marie"));
		grades.put("Antoinette", 3);
		assertEquals(3, grades.get("Antoinette"));
		grades.put("Ariel", null);
		assertEquals(null, grades.get("Ariel"));
		assertEquals(null, grades.get("NotPresent"));
		assertThrows(NullPointerException.class, () -> grades.put(null, 5));
	}

	@Test
	void testIsEmpty() {
		assertTrue(grades.isEmpty());
		grades.put("Belle", 5);
		assertFalse(grades.isEmpty());
	}

	@Test
	void testSize() {
		assertEquals(0, grades.size());
		grades.put("Ariana", 1);
		assertEquals(1, grades.size());
		grades.put("Arijadna", 3);
		assertEquals(2, grades.size());
		grades.put("Arihanna", null);
		assertEquals(3, grades.size());
		grades.put("Ariana", 5);
		assertEquals(3, grades.size());
	}

	@Test
	void testClear() {
		assertEquals(0, grades.size());
		grades.put("White", 4);
		assertEquals(1, grades.size());
		grades.put("Orange", 2);
		assertEquals(2, grades.size());
		grades.clear();
		assertEquals(0, grades.size());
		assertEquals(null, grades.get("White"));
		assertEquals(null, grades.get("Orange"));
	}


}

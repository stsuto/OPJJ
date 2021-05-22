package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueWrapperTest {

	static final double OFFSET = 1e-6;
	
	@Test
	void testValueWrapper() {
		assertDoesNotThrow(() -> new ValueWrapper(null));
		assertDoesNotThrow(() -> new ValueWrapper(Integer.valueOf(9)));
		assertDoesNotThrow(() -> new ValueWrapper(Double.valueOf(3.14)));
		assertDoesNotThrow(() -> new ValueWrapper(String.valueOf("4.1")));
		assertDoesNotThrow(() -> new ValueWrapper(String.valueOf("Constructor tests!")));
		assertDoesNotThrow(() -> new ValueWrapper(Boolean.valueOf(7 > 3/2)));
	}
	
	@Test
	void testGetValue() {
		ValueWrapper rapper1 = new ValueWrapper(7);
		assertEquals(7, rapper1.getValue());
		
		ValueWrapper rapper2 = new ValueWrapper(Integer.valueOf(5));
		assertEquals(5, rapper2.getValue());
		
	}

	@Test
	void testSetValue() {
		ValueWrapper rapper = new ValueWrapper(String.valueOf("Samoyed"));
		assertEquals("Samoyed", rapper.getValue());
		
		rapper.setValue("Saint Bernard");
		assertEquals("Saint Bernard", rapper.getValue());
	}
	
	@Test
	void testAddIntegers() {
		ValueWrapper wrap1 = new ValueWrapper(3);
		ValueWrapper wrap2 = new ValueWrapper(9);
		assertEquals(3, wrap1.getValue());
		assertEquals(9, wrap2.getValue());
		
		wrap1.add(10);
		assertEquals(13, wrap1.getValue());
		
		wrap1.add(wrap2.getValue());
		assertEquals(22, wrap1.getValue());
		assertEquals(9, wrap2.getValue());
		
		wrap2.add(wrap1.getValue());
		assertEquals(31, wrap2.getValue());	
	}
	
	@Test
	void testAddIntegerAndDouble() {
		ValueWrapper wrap1 = new ValueWrapper(3);
		ValueWrapper wrap2 = new ValueWrapper(31.4e-1);
		assertTrue(wrap1.getValue() instanceof Integer);
		assertEquals(3, wrap1.getValue());
		assertTrue(wrap2.getValue() instanceof Double);
		double value = (double) wrap2.getValue();
		assertEquals(3.14, value, OFFSET);
		
		wrap2.add(wrap1.getValue());
		assertTrue(wrap2.getValue() instanceof Double);
		value = (double) wrap2.getValue();
		assertEquals(6.14, value, OFFSET);
		assertTrue(wrap1.getValue() instanceof Integer);
		assertEquals(3, wrap1.getValue());

		
		wrap1.add(wrap2.getValue());
		assertTrue(wrap1.getValue() instanceof Double);
		assertEquals(9.14, (double) wrap1.getValue(), OFFSET);
	}
	
	@Test
	void testAddTwoNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
		assertEquals(0, v1.getValue());
		assertEquals(null, v2.getValue());
		assertFalse(v2.getValue() instanceof Integer);
		v2.add(null);
		assertTrue(v2.getValue() instanceof Integer);
		assertEquals(0, v2.getValue());
	}
	
	@Test
	void testAddTwoStrings() {
		ValueWrapper v1 = new ValueWrapper("3.14");
        ValueWrapper v2 = new ValueWrapper("5");
        assertTrue(v1.getValue() instanceof String);
        v1.add(v2.getValue());
        assertTrue(v1.getValue() instanceof Double);
        assertEquals(8.14, (double) v1.getValue(), OFFSET);
        
        assertTrue(v2.getValue() instanceof String);
        v2.add("0");
        assertTrue(v2.getValue() instanceof Integer); // Value is transformed only when needed for arithmetic expressions.
        assertEquals(5, v2.getValue());		
	}
	
	@Test
	void testAddStringWithOther() {
		ValueWrapper v1 = new ValueWrapper("8");
        ValueWrapper v2 = new ValueWrapper(17);
        v1.add(v2.getValue());
        assertEquals(25,v1.getValue());
        assertTrue(v1.getValue() instanceof Integer);
        assertEquals(17,v2.getValue());
	}
	
	@Test
	void testAddInvalid() {
		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		assertThrows(RuntimeException.class, () -> v7.add(v8.getValue()));
		
		ValueWrapper vv1 = new ValueWrapper(Boolean.valueOf(true));
		assertThrows(RuntimeException.class, () -> vv1.add(Integer.valueOf(5)));
		ValueWrapper vv2 = new ValueWrapper(Integer.valueOf(5));
		assertThrows(RuntimeException.class, () -> vv2.add(Boolean.valueOf(true)));
	}
	
	@Test
	void testSubtract() {
		ValueWrapper v1 = new ValueWrapper("8");
        ValueWrapper v2 = new ValueWrapper(17);
        v1.subtract(v2.getValue());
        assertEquals(-9, v1.getValue());
        assertTrue(v1.getValue() instanceof Integer);
        assertEquals(17, v2.getValue());
	}
	
	@Test
	void testMultiply() {
		ValueWrapper v1 = new ValueWrapper("3.5");
        ValueWrapper v2 = new ValueWrapper("4");
        v1.multiply(v2.getValue());
        assertTrue(v1.getValue() instanceof Double);
        assertEquals(14.0, (double) v1.getValue(), OFFSET);
        assertEquals("4", v2.getValue());
	}
	
	@Test
	void testDivideDouble() {
		ValueWrapper v1 = new ValueWrapper(11);
        ValueWrapper v2 = new ValueWrapper(3.3);
        v1.divide(v2.getValue());
        assertTrue(v1.getValue() instanceof Double);
        assertEquals(11/3.3, (double) v1.getValue(), OFFSET);
        assertEquals(3.3, (double) v2.getValue(), OFFSET);
	}
	
	@Test
	void testDivideInt() {
		ValueWrapper v1 = new ValueWrapper(11);
        ValueWrapper v2 = new ValueWrapper(3);
        v1.divide(v2.getValue());
        assertTrue(v1.getValue() instanceof Integer);
        assertEquals(3, v1.getValue());
        assertEquals(3, v2.getValue());
	}
	
	@Test
	void testNumCompare() {
		ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper("3.5");
        ValueWrapper v3 = new ValueWrapper(3.5);
        ValueWrapper v4 = new ValueWrapper(1);
    
        assertEquals(0, v1.numCompare(null));
        assertEquals(0, v1.numCompare(0));
        assertEquals(1, v1.numCompare(-3));
        assertEquals(-1, v2.numCompare(6));
        assertEquals(0, v2.numCompare(v3.getValue()));
        assertEquals(1, v3.numCompare(v4.getValue()));
	}
}


package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EmptyStackException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectMultistackTest {

	ObjectMultistack stack;
	
	@Test
	void testObjectMultistack() {
		assertDoesNotThrow(() -> new ObjectMultistack());
		assertNotNull(new ObjectMultistack());
	}

	@BeforeEach
	void initValues() {
		stack = new ObjectMultistack();
	}

	@Test
	void testIsEmpty() {
		assertTrue(stack.isEmpty("Anything"));
	}
	
	@Test
	void testPush() {
		assertTrue(stack.isEmpty("keyName"));
		stack.push("keyName", new ValueWrapper(4));
		assertFalse(stack.isEmpty("keyName"));
		assertTrue(stack.isEmpty("otherKey"));
		
		stack.push("otherKey", new ValueWrapper("value"));
		stack.push("otherKey", new ValueWrapper("newValue"));
		assertFalse(stack.isEmpty("otherKey"));
		
		assertThrows(NullPointerException.class, () -> stack.push(null, new ValueWrapper(3)));
		assertThrows(NullPointerException.class, () -> stack.push("keyName", null));
	}
	
	@Test
	void testPeek() {
		assertTrue(stack.isEmpty("keyName"));
		ValueWrapper v1 = new ValueWrapper(4);
		
		stack.push("keyName", v1);
		assertEquals(v1, stack.peek("keyName"));
		assertEquals(v1, stack.peek("keyName")); // Isn't removed.
		
		ValueWrapper v2 = new ValueWrapper("value");
		ValueWrapper v3 = new ValueWrapper("newValue");
		stack.push("otherKey", v2);
		assertEquals(v2, stack.peek("otherKey"));
		stack.push("otherKey", v3);
		assertEquals(v3, stack.peek("otherKey"));		
	}

	@Test
	void testPop() {
		assertTrue(stack.isEmpty("keyName"));
		ValueWrapper v1 = new ValueWrapper(4);
		
		stack.push("keyName", v1);
		assertEquals(v1, stack.pop("keyName"));
		assertThrows(EmptyStackException.class, () -> stack.peek("keyName")); // Is removed.
		
		ValueWrapper v2 = new ValueWrapper("value");
		ValueWrapper v3 = new ValueWrapper("newValue");
		stack.push("otherKey", v2);
		stack.push("otherKey", v3);
		assertEquals(v3, stack.pop("otherKey"));
		assertEquals(v2, stack.pop("otherKey"));
	}


}

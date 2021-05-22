package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Test class for hr.fer.zemris.java.custom.collections.ArrayIndexedCollection
 * 
 * @author stipe
 */
class ArrayIndexedCollectionTest {

	ArrayIndexedCollection collection;
	
	@BeforeEach
	void initParametars() {
		collection = new ArrayIndexedCollection();
	}
	
	@Test
	void testArrayIndexedCollection() {
		ArrayIndexedCollection collection = assertDoesNotThrow(() -> new ArrayIndexedCollection());
		assertNotNull(collection);
	}

	@Test
	void testArrayIndexedCollectionInt() {
		final int initialCapacity = 10;
		ArrayIndexedCollection collection = assertDoesNotThrow(
				() -> new ArrayIndexedCollection(initialCapacity));
		assertNotNull(collection);
		
		final int negativeCapacity = -1;
		assertThrows(IllegalArgumentException.class, 
				() -> new ArrayIndexedCollection(negativeCapacity));
	}

	@Test
	void testArrayIndexedCollectionCollection() {
		collection.add(2);
		ArrayIndexedCollection newCollection = assertDoesNotThrow(
				() -> new ArrayIndexedCollection(collection));
		assertNotNull(newCollection);
	}

	@Test
	void testArrayIndexedCollectionCollectionInt() {
		final int initialCapacity = 10;
		ArrayIndexedCollection newCollection = assertDoesNotThrow(
				() -> new ArrayIndexedCollection(collection, initialCapacity));
		assertNotNull(newCollection);
		
		final int negativeCapacity = -1;
		assertThrows(IllegalArgumentException.class, 
				() -> new ArrayIndexedCollection(collection, negativeCapacity));
		
		ArrayIndexedCollection nullCollection = null;
		assertThrows(NullPointerException.class, 
				() -> new ArrayIndexedCollection(nullCollection, initialCapacity));
		
		assertThrows(IllegalArgumentException.class, 
				() -> new ArrayIndexedCollection(nullCollection, negativeCapacity)); // Capacity validity is checked first.
	}
	
	@Test
	void testAdd() {
		Object element = "Golden retriever";
		Object element2 = 5;
		
		assertFalse(collection.contains(element));
		
		collection.add(element);
		collection.add(element2);
		assertTrue(collection.contains(element));
		
		ArrayIndexedCollection col = new ArrayIndexedCollection(collection);
		assertTrue(col.contains(element2));
	}

	@Test
	void testContains() {
		Object element = "Alaskan malamute";
		
		assertFalse(collection.contains(element));
		
		collection.add(element);
		assertTrue(collection.contains(element));
	}
	
	@Test
	void testSize() {
		assertEquals(0, collection.size());
		
		collection.add("Banana");
		collection.add(420);
		collection.add(false);
		
		assertEquals(3, collection.size());
	}

	@Test
	void testIndexOf() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Samoyed");
		collection.add("Husky");
		
		assertEquals(1, collection.indexOf("Samoyed"));
		assertEquals(-1, collection.indexOf("Akita inu"));
	}

	@Test
	void testToArray() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		Object[] array = collection.toArray();
		assertEquals(4, array.length);
		assertEquals("Shiba inu", array[2]);
	
	}

	@Test
	void testForEach() {
		collection.add(2);
		collection.add(5);
		collection.add(2);
		collection.add(3);
		
		class LocalProcessor extends Processor {
			int sum;
			
			@Override
			public void process(Object value) {
				sum += (int) value;
			}
		}
		
		LocalProcessor processor = new LocalProcessor();
		collection.forEach(processor);
		assertEquals(12, processor.sum);
	}

	@Test
	void testClear() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		assertEquals(4, collection.size());
		collection.clear();
		assertEquals(0, collection.size());
	}

	@Test
	void testGet() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		Object[] array = collection.toArray();
		
		for (int i = 0; i < array.length; i++) {
			assertTrue(collection.get(i).equals(array[i]));
		}
	}

	@Test
	void testInsert() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		Object[] original = collection.toArray();
		
		int index = 2;
		String addedValue = "Alaskan malamute";
		
		collection.insert(addedValue, index);
		assertTrue(collection.contains(addedValue));
		
		Object[] changed = collection.toArray();
		
		for (int i = 0; i < original.length; i++) {
			if (i < index) {
				assertTrue(original[i].equals(changed[i]));
			} else {
				assertTrue(original[i].equals(changed[i + 1])); // All of elements at an index >= index of insertion are shifted to the right.
			}
		}
	}

	@Test
	void testRemoveInt() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		Object[] original = collection.toArray();

		int index = 1;
		Object currentValue = collection.get(index);
		
		collection.remove(index);
		assertFalse(collection.contains(currentValue));
		
		Object[] changed = collection.toArray();
		
		for (int i = 0; i < changed.length; i++) {
			if (i < index) {
				assertTrue(original[i].equals(changed[i]));
			} else {
				assertTrue(original[i + 1].equals(changed[i])); // After the removed value's index, all elements are shifted to the left.
			}
		}
	}
	
	@Test
	void testRemoveObject() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		Object[] original = collection.toArray();
		
		Object valueToDelete = "Shiba inu";
		int index = collection.indexOf(valueToDelete);
		
		collection.remove(valueToDelete);
		assertFalse(collection.contains(valueToDelete));

		Object[] changed = collection.toArray();
		
		for (int i = 0; i < changed.length; i++) {
			if (i < index) {
				assertTrue(original[i].equals(changed[i]));
			} else {
				assertTrue(original[i + 1].equals(changed[i])); // After the removed value's index, all elements are shifted to the left.
			}
		}
	}
	
	@Test
	void testIsEmpty() {
		assertTrue(collection.isEmpty());
		
		collection.add("Husky");
		collection.add("Samoyed");
		assertFalse(collection.isEmpty());
	}
	
	@Test
	void testAddAll() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		ArrayIndexedCollection newCollection = new ArrayIndexedCollection();
		newCollection.addAll(collection);
		assertTrue(collection.size() == newCollection.size());
	}
}

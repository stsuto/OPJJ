package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {

	LinkedListIndexedCollection collection;
	
	@BeforeEach
	void initParametars() {
		collection = new LinkedListIndexedCollection();
	}
	
	@Test
	void testLinkedListIndexedCollection() {
		LinkedListIndexedCollection collection = assertDoesNotThrow(() -> new LinkedListIndexedCollection());
		assertNotNull(collection);
	}
	
	@Test
	void testAdd() {		
		Object element = "Golden retriever";
		Object element2 = 5;
		
		assertFalse(collection.contains(element));
		
		collection.add(element);
		collection.add(element2);
		assertTrue(collection.contains(element));
		assertTrue(collection.contains(element2));
	}

	@Test
	void testContains() {
		Object element = 3.14;
		
		assertFalse(collection.contains(element));

		collection.add(element);
		assertTrue(collection.contains(element));
		
		collection.remove(element);
		assertFalse(collection.contains(element));
	}
	
	@Test
	void testLinkedListIndexedCollectionCollection() {
		collection.add(2);
		LinkedListIndexedCollection newCollection = assertDoesNotThrow(
				() -> new LinkedListIndexedCollection(collection));
		assertNotNull(newCollection);
	}
	
	@Test
	void testSize() {
		assertEquals(0, collection.size());
		
		collection.add("Sir Arthur Conan Doyle");
		collection.add("The Hound of the Baskervilles");
		assertEquals(2, collection.size());
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
	void testInsert() {
		collection.add("Husky");
		collection.add("Samoyed");
		collection.add("Shiba inu");
		collection.add("Akita inu");
		
		Object[] original = collection.toArray();
		
		int index = 1;
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
		
		Object valueToDelete = "Samoyed";
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

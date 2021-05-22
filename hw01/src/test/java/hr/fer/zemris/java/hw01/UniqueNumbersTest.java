package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

public class UniqueNumbersTest {
	
	@Test
	public void testAdd() {
		TreeNode root = null;
		
		root = UniqueNumbers.addNode(root, 4);
		root = UniqueNumbers.addNode(root, 2);
		
		assertEquals(2, root.left.value);
		
	}
	@Test
	public void testRootAfterAdd() {
		TreeNode root = null;
		
		UniqueNumbers.addNode(root, 3);
		assertNotNull(root);
	}

	@Test
	public void testTreeSizeBeforeAdd() {
		TreeNode glava = null;
		int size = UniqueNumbers.treeSize(glava);
		assertEquals(0, size);
	}
	
	@Test
	public void testTreeSizeAfterAdd() {
		TreeNode glava = null;
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		
		int size = UniqueNumbers.treeSize(glava);
		assertEquals(4, size);
	}
	
	@Test
	public void testContainsValueNegative() {
		TreeNode glava = null;
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		
		assertFalse(UniqueNumbers.containsValue(glava, 55));
	}
	
	@Test
	public void testContainsValuePositive() {
		TreeNode glava = null;
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		
		assertTrue(UniqueNumbers.containsValue(glava, 42));
	}
	
}

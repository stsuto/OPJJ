package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComparisonOperatorsTest {

	@Test
	void testLike() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		assertFalse(oper.satisfied("Zagreb", "Aba*"));
		assertFalse(oper.satisfied("AAA", "AA*AA"));
		assertTrue(oper.satisfied("AAAA", "AA*AA"));
		
		assertTrue(oper.satisfied("Samoyed", "Sam*ed"));
		assertFalse(oper.satisfied("Samoyed", "Sam"));
		assertTrue(oper.satisfied("Samoyed", "Sam*"));
		assertFalse(oper.satisfied("Samoyed", "ye*"));
		assertFalse(oper.satisfied("Samoyed", "*oye"));
		assertTrue(oper.satisfied("Samoyed", "*Samoyed"));
		assertTrue(oper.satisfied("Samoyed", "Samoyed*"));
		assertTrue(oper.satisfied("Samoyed", "*"));
		assertThrows(IllegalArgumentException.class, 
				() -> oper.satisfied("Samoyed", "Sam*y*d")
		);
	}
	
	@Test
	void testLess() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertFalse(oper.satisfied("Ana", "Ana"));
		assertTrue(oper.satisfied("21", "44"));
	}
	
	@Test
	void testLessOrEquals() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
		assertTrue(oper.satisfied("21", "44"));
	}
	
	@Test
	void testGreater() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertFalse(oper.satisfied("Ana", "Ana"));
		assertFalse(oper.satisfied("21", "44"));
	}
	
	@Test
	void testGreaterOrEquals() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
		assertFalse(oper.satisfied("21", "44"));
	}
	
	@Test
	void testEquals() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
		assertFalse(oper.satisfied("21", "44"));
	}
	
	@Test
	void testNotEquals() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertFalse(oper.satisfied("Ana", "Ana"));
		assertTrue(oper.satisfied("21", "44"));
	}

}

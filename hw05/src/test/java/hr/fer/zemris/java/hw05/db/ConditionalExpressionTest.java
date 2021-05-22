package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConditionalExpressionTest {

	ConditionalExpression expr1;
	ConditionalExpression expr2;
	ConditionalExpression expr3;
	
	@Test
	void testConditionalExpression() {
		assertNotNull(new ConditionalExpression(
				FieldValueGetters.LAST_NAME, 
				"Bos*", 
				ComparisonOperators.LIKE
		));
	}

	@BeforeEach
	void initExpressions() {
		expr1 = new ConditionalExpression(
				FieldValueGetters.LAST_NAME, 
				"Bos*", 
				ComparisonOperators.LIKE
		);
		
		expr2 = new ConditionalExpression(
				FieldValueGetters.FIRST_NAME, 
				"*", 
				ComparisonOperators.LIKE
		);
		
		expr3 = new ConditionalExpression(
				FieldValueGetters.JMBAG, 
				"0000000012", 
				ComparisonOperators.GREATER
		);
	}
	
	@Test
	void testGetFieldGetter() {
		assertEquals(FieldValueGetters.LAST_NAME, expr1.getFieldGetter());
		assertEquals(FieldValueGetters.FIRST_NAME, expr2.getFieldGetter());
		assertEquals(FieldValueGetters.JMBAG, expr3.getFieldGetter());
	}

	@Test
	void testGetStringLiteral() {
		assertEquals("Bos*", expr1.getStringLiteral());
		assertEquals("*", expr2.getStringLiteral());
		assertEquals("0000000012", expr3.getStringLiteral());
	}

	@Test
	void testGetComparisonOperator() {
		assertEquals(ComparisonOperators.LIKE, expr1.getComparisonOperator());
		assertEquals(ComparisonOperators.LIKE, expr2.getComparisonOperator());
		assertEquals(ComparisonOperators.GREATER, expr3.getComparisonOperator());
	}
	
	@Test
	void testFilterWithConditionalExpression() throws IOException {
		StudentDatabase db = new StudentDatabase(
				Files.readAllLines(Paths.get("src/test/resources/database.txt"),
				StandardCharsets.UTF_8)
		);

		List<StudentRecord> list1 = db.filter(getCondExprFilter(expr1)); // 0000000003	Bosnić	Andrea	4
		assertEquals(1, list1.size());
		assertTrue(list1.get(0).equals(new StudentRecord("0000000003", "Bosnić", "Andrea", 4)));
		assertFalse(list1.contains(new StudentRecord("0000000026", "Katunarić", "Zoran", 3)));
		
		List<StudentRecord> list2 = db.filter(getCondExprFilter(expr2));
		assertEquals(63, list2.size());
		assertTrue(list2.contains(new StudentRecord("0000000026", "Katunarić", "Zoran", 3)));
		assertTrue(list2.contains(new StudentRecord("0000000003", "Bosnić", "Andrea", 4)));
		
		List<StudentRecord> list3 = db.filter(getCondExprFilter(expr3));
		assertEquals(63 - 12, list3.size());
		assertTrue(list3.contains(new StudentRecord("0000000026", "Katunarić", "Zoran", 3)));
		assertFalse(list3.contains(new StudentRecord("0000000003", "Bosnić", "Andrea", 4)));
		

	}

	private IFilter getCondExprFilter(ConditionalExpression expr) {
		return (rec) -> expr.getComparisonOperator().satisfied(
					expr.getFieldGetter().get(rec), // returns lastName from given record
					expr.getStringLiteral() // returns "Bos*"
		);
		
	}
	
}

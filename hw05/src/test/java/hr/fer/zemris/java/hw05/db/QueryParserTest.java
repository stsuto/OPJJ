package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryParserTest {

	QueryParser qp1, qp2;
	
	@Test
	void testQueryParser() {
		assertNotNull(new QueryParser(" jmbag =\"0123456789\" "));
	}
	
	@BeforeEach
	void initQueryParsers() {
		qp1 = new QueryParser(" jmbag =\"0123456789\" ");
		qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
	}

	@Test
	void testIsDirectQuery() {
		assertTrue(qp1.isDirectQuery());
		assertFalse(qp2.isDirectQuery());
	}

	@Test
	void testGetQueriedJMBAG() {
		assertEquals("0123456789", qp1.getQueriedJMBAG());
		assertThrows(IllegalStateException.class, () -> qp2.getQueriedJMBAG());
	}

	@Test
	void testGetQuery() {
		assertEquals(1, qp1.getQuery().size());
		assertEquals(
				new ConditionalExpression(FieldValueGetters.JMBAG, "0123456789", ComparisonOperators.EQUALS), 
				qp1.getQuery().get(0)
		);
		
		assertEquals(2, qp2.getQuery().size());
	}

}

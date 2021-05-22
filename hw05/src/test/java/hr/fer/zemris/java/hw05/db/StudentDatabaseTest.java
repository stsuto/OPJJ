package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentDatabaseTest {

	StudentDatabase db;
	
	@Test
	void testStudentDatabase() throws IOException {
		assertNotNull(new StudentDatabase(
				Files.readAllLines(Paths.get("src/test/resources/database.txt"))
		));
		
		List<String> list = Arrays.asList(
				"0000000001	Akšamović	Marin	2",
				"0000000002	Bakamović	Petra	3",
				"0000000001	Akšamović	Marin	2" // duplicate data
		);
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
		
		List<String> list2 = Arrays.asList(
				"0000000001	Akšamović	Marin	2",
				"0000000002	Bakamović	Petra	7" // illegal final grade
		);
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list2));	
	}

	@BeforeEach
	void initDatabase() throws IOException {
		db = new StudentDatabase(
				Files.readAllLines(Paths.get("src/test/resources/database.txt"),
				StandardCharsets.UTF_8)
		);
	}
	
	@Test
	void testForJMBAG() { 
		//0000000001	Akšamović	Marin	2
		assertEquals(new StudentRecord("0000000001", "Akšamović", "Marin", 2), db.forJMBAG("0000000001"));
		assertEquals(null, db.forJMBAG("0101010101"));
	}

	@Test
	void testFilter() {
		List<StudentRecord> listTrue = db.filter((record) -> true);
		List<StudentRecord> listFalse = db.filter((record) -> false);
		assertEquals(63, listTrue.size());
		assertEquals(0, listFalse.size());
		assertEquals(Collections.emptyList(), listFalse);
		
	}
	
	

}

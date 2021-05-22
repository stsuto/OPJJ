package hr.fer.zemris.java.hw05.db;

import static hr.fer.zemris.java.hw05.db.FieldValueGetters.FIRST_NAME;
import static hr.fer.zemris.java.hw05.db.FieldValueGetters.JMBAG;
import static hr.fer.zemris.java.hw05.db.FieldValueGetters.LAST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class FieldValueGettersTest {

	@Test
	void testFieldValueGetters() throws IOException {
		List<StudentRecord> records = Arrays.asList(
				new StudentRecord("0000000001", "Akšamović", "Marin", 2),
				new StudentRecord("0000000002", "Bakamović", "Petra", 3),
				new StudentRecord("0000000015", "Glavinić Pecotić", "Kristijan", 4)
		);
		
		for (StudentRecord record : records) {
			assertEquals(record.getFirstName(), FIRST_NAME.get(record));
			assertEquals(record.getLastName(), LAST_NAME.get(record));
			assertEquals(record.getJmbag(), JMBAG.get(record));
//			System.out.println("First name: " + FieldValueGetters.FIRST_NAME.get(record));
//			System.out.println("Last name: " + FieldValueGetters.LAST_NAME.get(record));
//			System.out.println("JMBAG: " + FieldValueGetters.JMBAG.get(record));
		}
		
	}

}

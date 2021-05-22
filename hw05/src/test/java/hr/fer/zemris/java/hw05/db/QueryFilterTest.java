package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class QueryFilterTest {

	@Test
	void testQueryFilter() {
		QueryParser parser = new QueryParser("jmbag >= \"0000000027\" aNd lastName>\"K\"");
		assertNotNull(new QueryFilter(parser.getQuery()));
	}

	@Test
	void testAccepts() throws IOException {
		StudentDatabase db =new StudentDatabase(
				Files.readAllLines(Paths.get("src/test/resources/database.txt"),
				StandardCharsets.UTF_8)
		);
		QueryParser parser = new QueryParser("jmbag >= \"0000000038\" aNd firstName>=\"Z\"");
		List<StudentRecord> filteredList = new ArrayList<>();
		
		if(parser.isDirectQuery()) {
			StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
			System.out.println(r);
		} else {
			for(StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
				// 0000000040	Mišura	Zrinka	5
				// 0000000063	Žabčić	Željko	4
				filteredList.add(r);
			}
		}

		assertEquals(2, filteredList.size());
		assertEquals(new StudentRecord("0000000040", "Mišura", "Zrinka", 5), filteredList.get(0));
		assertEquals(new StudentRecord("0000000063", "Žabčić", "Željko", 4), filteredList.get(1));
	}

}

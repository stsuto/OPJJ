package hr.fer.zemris.java.hw17.trazilica;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
class SearchDictionaryTest {

	@Test
	void patternMatchingTest() {
		String documentText = "My name is Luka\n" + 
				"I_live32on*the second floor\n";
		Pattern pattern = Pattern.compile("\\p{IsAlphabetic}+");
		Matcher matcher = pattern.matcher(documentText);
		
		List<String> results = new ArrayList<>();
		
		while (matcher.find()) {
			results.add(matcher.group().strip().toLowerCase());
		}
		
		assertEquals(results.get(0), "my");
		assertEquals(results.get(1), "name");
		assertEquals(results.get(2), "is");
		assertEquals(results.get(3), "luka");
		assertEquals(results.get(4), "i");
		assertEquals(results.get(5), "live");
		assertEquals(results.get(6), "on");
		assertEquals(results.get(7), "the");
		assertEquals(results.get(8), "second");
		assertEquals(results.get(9), "floor");
	}

	@Test
	void wordCountingTest() {
		String documentText = "My name is Luka\n" + 
				"MY_name32jeff*luka second luka\n";
		Pattern pattern = Pattern.compile("\\p{IsAlphabetic}+");
		Matcher matcher = pattern.matcher(documentText);
		
		Map<String, Integer> map = new LinkedHashMap<>();
		
		while (matcher.find()) {
			String word = matcher.group().strip().toLowerCase();
			map.merge(word, 1, (o, n) -> o + n);
		}
		
		assertEquals(2, map.get("my"));
		assertEquals(2, map.get("name"));
		assertEquals(1, map.get("is"));
		assertEquals(3, map.get("luka"));
		assertEquals(1, map.get("jeff"));
		assertEquals(1, map.get("second"));	
	}
	
}

package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

public class Primjer {

	public static void main(String[] args) {
		SimpleHashtable<String, Integer> examMarks = HashTableLoader.loadExamMarks();
		
		for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}
		System.out.println();
		for(SimpleHashtable.TableEntry<String,Integer> pair1 : examMarks) {
			for(SimpleHashtable.TableEntry<String,Integer> pair2 : examMarks) {
				System.out.printf(
					"(%s => %d) - (%s => %d)%n",
					pair1.getKey(), pair1.getValue(),
					pair2.getKey(), pair2.getValue()
				);
			}
		}
	}
	
}

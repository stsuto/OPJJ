package hr.fer.zemris.java.custom.collections.demo;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

public class Demo15 {

	public static void main(String[] args) {
		SimpleHashtable<String, Integer> examMarks = HashTableLoader.loadExamMarks();
		System.out.printf("Veličina: %d%n", examMarks.size());
		System.out.println(examMarks);
		System.out.println();
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter.remove();
		}
		System.out.println();
		System.out.printf("Veličina: %d%n", examMarks.size());
		System.out.println(examMarks);
	}
	
}

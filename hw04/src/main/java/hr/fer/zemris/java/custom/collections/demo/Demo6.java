package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

public class Demo6 {

	public static void main(String[] args) {
		
//		Collection<String> col = new ArrayIndexedCollection<>();
		Collection<String> col = new LinkedListIndexedCollection<>();
		
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		
		ElementsGetter<String> getter = col.createElementsGetter();
		
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		
		col.clear();
		
		System.out.println("Jedan element: " + getter.getNextElement());
	}
}

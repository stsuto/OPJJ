package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

public class Demo4 {

	public static void main(String[] args) {
		
		Collection<String> col = new ArrayIndexedCollection<>();
		
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		
		ElementsGetter<String> getter1 = col.createElementsGetter();
		ElementsGetter<String> getter2 = col.createElementsGetter();
		
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
	}
}

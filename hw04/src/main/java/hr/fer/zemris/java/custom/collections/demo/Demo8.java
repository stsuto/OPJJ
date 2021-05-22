package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

public class Demo8 {
	
	public static void main(String[] args) {
		
		class EvenIntegerTester implements Tester<Object> {
			public boolean test(Object obj) {
				if(!(obj instanceof Integer)) return false;
				Integer i = (Integer)obj;
				return i % 2 == 0;
			}
		}
		
		Tester<Object> t = new EvenIntegerTester();
		System.out.println(t.test("Ivo"));
		System.out.println(t.test(22));
		System.out.println(t.test(3));
	
	}
	
}

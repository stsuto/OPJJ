package hr.fer.zemris.java.hw01;

import java.util.Scanner;
/**
 * Program koji računa faktorijelu brojeva u intervalu [3, 20] u Javi.
 * Ako korisnik unese vrijednost za koju se ne može izračunati faktorijela ili vrijednost izvan dopuštenog intervala,
 * šalje se prikladna poruka.
 * 
 * @author stipe
 */
public class Factorial {

	private static final int UPPER_BOUND = 20;
	private static final int LOWER_BOUND = 3;
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * U njoj se poziva metoda za računanje faktorijela i ispisuje rezultate sve dok ne primi niz koji označava kraj.
	 *
	 * @param args brojevi čije faktorijele zelimo izračunati, zadaju se preko standardnog ulaza, tj. tipkovnice
	 */
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("Unesite broj > ");
			if (!sc.hasNextInt()) {
				String token = sc.next();
				if (token.equalsIgnoreCase("kraj")) {	//prema tekstu može i samo .equals("kraj")
					break;
				} else {
					System.out.printf("'%s' nije cijeli broj.%n", token);
				}
				
			} else {
				int token = sc.nextInt();
				if (token < LOWER_BOUND || token > UPPER_BOUND) {
					System.out.printf("'%d' nije broj u dozvoljenom rasponu.%n", token);
				} else {
					System.out.printf("%d! = %d%n", token, calculateFactorial(token));
				}
			}
		}
		sc.close();
		System.out.println("Doviđenja.");
	}

	/**
	 * Metoda koja računa faktorijel željenog argumenta, tj. broja.
	 * 
	 * @param number int vrijednost broja čiji faktorijel računamo.
	 * @throws IllegalArgumentException ako je unesen negativan broj jer se ne moze izračunati faktorijel
	 * @return result rezultat računanja faktorijela tipa long
	 */
	public static long calculateFactorial(int number) {
		if (number < 0) {
			throw new IllegalArgumentException("Broj je negativan!");
		} else if (number > UPPER_BOUND) {
			throw new IllegalArgumentException("Broj je prevelik za računanje faktorijela!");
		}
		long result = 1;
		for (int i = number; i > 1; i--) {
			result *= i;
		}
		return result;
	}
	
}

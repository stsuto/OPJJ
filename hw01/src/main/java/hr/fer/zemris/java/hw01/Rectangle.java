package hr.fer.zemris.java.hw01;

import java.util.Scanner;
/**
 * Java program koji računa površinu i opseg pravokutnika na temelju duljina 
 * stranica koje korisnik zadaje u naredbenom retku ili s tipkovnice.
 * Argumenti se mogu unijeti preko naredbenog retka kada se unesu dva argumenta,
 * ili preko tipkovnice kada u naredbenom retku ne bude nijednog argumenta.
 * 
 * @author stipe
 */
public class Rectangle {

	/**
	 * Metoda koja se izvodi pokretanjem programa.
	 * 
	 * @param args stranice pravokutnika
	 */
	public static void main(String[] args) {
		
		double width = 0;
		double height = 0;
		
		if (args.length != 0) {
			if (args.length != 2) {
				System.out.println("Unijeli ste netočan broj argumenata!");
				return;
				
			} else {
				try {
					width = Double.parseDouble(args[0]);
					height = Double.parseDouble(args[1]);
				} catch (NumberFormatException ex) {
					System.out.println("Potrebno je upisivati samo brojeve!");
					return;
				}
				
				if (width <= 0 || height <= 0) {
					System.out.println("Brojevi trebaju biti pozitivni!");
					return;
				}
			}
			
		} else {
			Scanner sc = new Scanner(System.in);
			width = inputLength(sc, "Unesite širinu > ");
			height = inputLength(sc, "Unesite visinu > ");
			sc.close();	
		}
		
		calculateAndPrint(width, height);
	}

	/**
	 * Računa i ispisuje površinu i opseg pravokutnika s danim parametrima.
	 * @param width duljina širine pravokutnika
	 * @param height duljina visine pravokutnika
	 */
	private static void calculateAndPrint(double width, double height) {
		double area = width * height;
		double circumference = 2 * (width + height);
		
		System.out.printf("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.%n"
				, width, height, area, circumference);
	}
	
	/**
	 * Calculates the circumference of a rectangle with given parameters as side lengths.
	 * @param width double value of the width of the rectangle
	 * @param height double value of the height of the rectangle
	 * @return a double value of the result of the calculation
	 */
	private static double calculateCircumference(double width, double height) {
		return 2 * width * height;
	}
	
	/**
	 * Metoda koja trazi ispravan unos za duljinu stranice i onda taj isti iznos vraca.
	 * @param sc reference to the scanner that will scan for side lengths
	 * @param side String koji se ispisuje pri traženju unosa duljine stranice te služi kao razlikovni parametar.
	 * @return duljina stranice tipa double
	 */
	private static double inputLength(Scanner sc, String side) {	
		while (true) {
			System.out.print(side);
			if (sc.hasNext()) {
				String token = sc.next();
				try {
					double length = Double.parseDouble(token);
					if (length < 0) {		// dobili smo neki decimalni broj, ako je on negativan, nije ispravan i ispisujemo poruku
						System.out.println("Unijeli ste negativnu vrijednost.");
					} else if (length == 0) { // trebamo pozitivnu, ne-nul vrijednost
						System.out.println("Unijeli ste nulu.");
					} else {
						return length;
					}			
				} catch (Exception ex) {
					System.out.format("'%s' se ne može protumačiti kao broj.%n", token);	//ako nije double, ispisujeme poruku
				}
			}
		}
	}
}

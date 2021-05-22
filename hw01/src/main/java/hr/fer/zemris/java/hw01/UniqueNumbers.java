package hr.fer.zemris.java.hw01;

import java.util.Scanner;
/**
 * Java program koji dodaje elemente u binarno stablo, ali bez ponavljanja elemenata.
 * Konstruirano je sortirano binarno stablo gdje su elementi lijeve grane manji od elemenata desne grane.
 * 
 * @author stipe
 *
 */
public class UniqueNumbers {

	/**
	 * Glavna metoda, izvodi se pokretanjem programa.
	 * @param args Ne primaju se argumenti preko naredbenog retka, vec ih korisnik upisuje preko tipkovnice.
	 * 			   Argumenti predstavljaju brojeve koji se dodaju u binarno stablo sve dok korisnik ne unese niz koji oznacava kraj dodavanja.
	 */
	public static void main(String[] args) {
		
		TreeNode root = null;
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("Unesite broj > ");
			if (sc.hasNextInt()) {
				int token = sc.nextInt();
				if (containsValue(root, token)) {
					System.out.println("Broj već postoji. Preskačem.");
					continue;
				} else {
					root = addNode(root, token);
					System.out.println("Dodano.");
				}
			
			} else {
				String token = sc.next();
				if (token.equalsIgnoreCase("kraj")) {
					break;
				} else {
					System.out.println("'" + token + "' nije cijeli broj.");
				}
			}
		}
			
		sc.close();
		
		System.out.print("Ispis od najmanjeg: ");
		printTreeAscending(root);
		System.out.println();
		System.out.print("Ispis od najvećeg: ");
		printTreeDescending(root);
		System.out.println();
	}
	
	/**
	 * Metoda koja dodaje novi cvor s zeljenom vrijednosti u binarno stablo.
	 * @param root korijenski cvor 
	 * @param broj vrijednost koju treba ubaciti, tj. vrijednost uskoro dodanog cvora.
	 * @return korijenski cvor, tj. glavu
	 */
	public static TreeNode addNode(TreeNode root, int broj) {
//		if (containsValue(root, broj)) System.out.println("Broj već postoji. Preskačem.");?	
		if (root == null) {
			root = new TreeNode();	//dosli smo do mjesta gdje treba dodati cvor, dodamo ga i ispisemo poruku
			root.left = null;		//ovo je rjesivo konstruktorom
			root.right = null;	
			root.value = broj;
			
		} else if (broj < root.value) {
			root.left = addNode(root.left, broj); //nova vrijednost je manja od vrijednosti trenutnog cvora tako da radi sortiranosti mora ici u lijevo podstablo
		
		} else if (broj > root.value) {
			root.right = addNode(root.right, broj);	//analogno gornjem slucaju
		}
		
		return root;		
	}

	/**
	 * Metoda koja prebrojava koliko cvorova sadrzi stablo.
	 * @param root pocetni cvor stabla
	 * @return broj cvoreva stabla
	 */
	public static int treeSize(TreeNode root) {
		if (root == null) {
			return 0;		// ako je root == null, dosli smo do cvora koji ne postoji pa vracamo 0
		}
		return treeSize(root.left) + treeSize(root.right) + 1;	//prvo ode do najlijevijeg cvora pa onda najdesnijeg i rekurzivno zbraja
	}
	
	/**
	 * Metoda koja provjerava nalazi li se odredena vrijednost u binarnom stablu.
	 * @param root Pocetni cvor, tj. korijen ili root.
	 * @param value Vrijednost za koju provjeravamo nalazi li se u stablu.
	 * @return true ako se vrijednost value nalazi u stablu koje pocinje cvorem root, inace false.
	 */
	public static boolean containsValue(TreeNode root, int value) {
		if (root == null) {
			return false;		//dosli smo do kraja, vracamo false
		}
		if (value == root.value) {
			return true;		//nasli smo cvor s trazenom vrijednosti
		}
		if (value < root.value) {
			return containsValue(root.left, value);	//ako se vrijednost nalazi u stablu, onda je u lijevom podstablu jer je manja od vrijednosti trenutnog cvora
		
		} else if (value > root.value) {
			return containsValue(root.right, value);	//analogno gornjem slucaju... moze ? ..left : ..right umjesto zadnja dva ifa
		}
		
		return false;
	}

	/**
	 * Metoda koja ispisuje vrijednosti u stablu od najmanje do najvece.
	 * @param root Pocetni cvor stabla.
	 */
	public static void printTreeAscending(TreeNode root) {
		if (root == null) {
			return;
		}
		printTreeAscending(root.left);		//najlijeviji cvor ima najmanju vrijednost, ona ide prva
		System.out.print(root.value + "  ");
		printTreeAscending(root.right);
	}
	
	/**
	 * Metoda koja ispisuje vrijednosti u stablu od najvece do najmanje.
	 * @param root Pocetni cvor stabla.
	 */
	public static void printTreeDescending(TreeNode root) {
		if (root == null) {
			return;
		}
		printTreeDescending(root.right);
		System.out.print(root.value + "  ");
		printTreeDescending(root.left);
	}
	
	/**
	 * Pomocna struktura koja predstavlja jedan cvor binarnog stabla.
	 * Ima 3 clanske varijable: 2 pokazivaca na podstabla i vrijednost cvora.
	 * 
	 * @author stipe
	 */
	public static class TreeNode {
		TreeNode left;
		TreeNode right;
		int value;
	}
	
}

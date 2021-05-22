package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;

/**
 * Demonstration class for "Slagalica".
 * Shows the functionality of solving a simple puzzle, offering 2 different solving
 * algorithms, bfs and bfsv. Bfsv is an improved algorithm that doesnt get stuck
 * when the problem can't be solved.
 * <p>
 * Puzzle's configuration is pre-set in the program, but can be changed within it.
 * The configuration must be 9 characters long and contain every number from 0 to 9 
 * exactly once.
 * <p>
 * A configuration example is 230146758, but some configurations can't be solved, 
 * e.g. 164502873.
 * 
 * @author stipe
 *
 */
public class SlagalicaDemo {
	
	public static void main(String[] args) {
		
		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(new int[] { 2, 3, 0, 1, 4, 6, 7, 5, 8 })); // { 1, 6, 4, 5, 0, 2, 8, 7, 3 }
																												  // { 2, 3, 0, 1, 4, 6, 7, 5, 8 }
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfs(slagalica, slagalica, slagalica);
//		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);
		
		if (rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
			
		} else {
			System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
			List<KonfiguracijaSlagalice> lista = new ArrayList<>();
			Node<KonfiguracijaSlagalice> trenutni = rješenje;
			
			while (trenutni != null) {
				lista.add(trenutni.getState());
				trenutni = trenutni.getParent();
			}
			
			Collections.reverse(lista);
			
			lista.stream().forEach(k -> {
				System.out.println(k);
				System.out.println();
			});
		}

	}
	
}
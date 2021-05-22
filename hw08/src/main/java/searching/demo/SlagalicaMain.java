package searching.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

/**
 * Demonstration class for "Slagalica".
 * Shows the functionality of solving a simple puzzle.
 * <p>
 * User must input a single argument which will be used to create the starting 
 * configuration of the puzzle. The configuration must be 9 characters long and 
 * contain every number from 0 to 9 exactly once.
 * <p>
 * A configuration example is 230146758, but some configurations can't be solved, 
 * e.g. 164502873.
 * 
 * @author stipe
 *
 */
public class SlagalicaMain {
	
	private static final int CONFIG_LENGTH = 9;
	
	// { 1, 6, 4, 5, 0, 2, 8, 7, 3 }		{ 2, 3, 0, 1, 4, 6, 7, 5, 8 }
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("One command line argument must be given!");
			System.exit(1);
		}
		
		if (args[0].length() != CONFIG_LENGTH) {
			System.out.println("Configuration must be given!");
			System.exit(1);	
		}
		
		for (int i = 0; i < CONFIG_LENGTH; i++) {
			if (!args[0].contains(String.valueOf(i))) {
				System.out.println("Configuration must contain every numebr from 0 to 9 exactly once!");
				System.exit(1);	
			}
		}

		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(
				Arrays.stream(args[0].split("")).mapToInt(Integer::parseInt).toArray())); 
		
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);
		
		if (rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
			
		} else {
			System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
			SlagalicaViewer.display(rješenje);
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
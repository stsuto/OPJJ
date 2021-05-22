package hr.fer.zemris.java.hw17.trazilica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents one document.
 * 
 * @author stipe
 *
 */
public class Document {

	/**
	 * Document path.
	 */
	private Path path;
	/**
	 * Map containing the number of times a certain word has been read.
	 */
	private Map<String, Integer> docWords;
	/**
	 * TF-ID vector.
	 */
	private Vector tfIdfVector;
	
	/**
	 * Constructor.
	 * 
	 * @param path {@link #path}
	 * @param docWords {@link #docWords}
	 */
	public Document(Path path, Map<String, Integer> docWords) {
		this.path = path;
		this.docWords = docWords;
		this.tfIdfVector = new Vector();
	}
	
	/**
	 * Initializes the {@link #tfIdfVector}.
	 * 
	 * @param vocabulary vocabular
	 * @param idfVector vocabular's IDF vector
	 */
	public void initializeVectors(Map<String, Integer> vocabulary, Vector idfVector) {		
		Iterator<Double> idfIterator = idfVector.iterator();
		for (String word : vocabulary.keySet()) {
			double tfValue = docWords.getOrDefault(word, 0);
			double idfValue = idfIterator.next();
			tfIdfVector.addCoord(tfValue * idfValue);
		}
	}

	/**
	 * Calculates the similarity between this document and the given document.
	 * 
	 * @param other other document
	 * @return similarity
	 */
	public double calculateSimilarity(Document other) {
		return this.tfIdfVector.cosAngle(other.tfIdfVector);
	}
	
	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * @return the docWords
	 */
	public Set<String> getDocWords() {
		return docWords.keySet();
	}

	/**
	 * Reads the document and returns the content.
	 * 
	 * @return String of Document text
	 */
	public String getBody() {
		try (Stream<String> stream = Files.lines(path)) {
			return stream.collect(Collectors.joining("\n"));
					
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read document body.");	
		}
	}
	
}

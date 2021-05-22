package hr.fer.zemris.java.hw17.trazilica;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * This class represents a search dictionary containing all documents read,
 * vocabulary created, and the IDF vector.
 * 
 * @author stipe
 *
 */
public class SearchDictionary {

	/**
	 * Document visitor.
	 */
	private DocumentVisitor visitor;
	/**
	 * List of documents.
	 */
	private List<Document> documents;
	/**
	 * Vocabulary.
	 */
	private Map<String, Integer> vocabulary;
	/**
	 * IDF vector.
	 */
	private Vector idfVector;
	/**
	 * Query map.
	 */
	private Map<Document, Double> query; 
	
	/**
	 * Constructor.
	 * 
	 * @param documentFolder folder containing documents
	 */
	public SearchDictionary(String documentFolder) {
		this.visitor = new DocumentVisitor(documentFolder);
		this.documents = visitor.getDocuments();
		this.vocabulary = visitor.getVocabulary();
		this.idfVector = initializeIdfVector();
	}

	/**
	 * Initializes the idf vector.
	 * 
	 * @return {@link #idfVector}
	 */
	private Vector initializeIdfVector() {
		double size = documents.size();
		
		List<Double> idfCoords = vocabulary.values().stream()
				.map(v -> Math.log10(size / v))
				.collect(Collectors.toList());
		
		return new Vector(idfCoords);
	}
	
	/**
	 * Initializes the documents.
	 */
	public void initializeDocuments() {
		documents.forEach(this::initializeDocument);
	}

	/**
	 * Creates a query document.
	 * 
	 * @param text query text
	 * @return query document
	 */
	public Document createQueryDocument(String text) {
		return visitor.loadDocument(null, text, DocumentVisitor.LOAD_QUERY);
	}
	
	/**
	 * Initilializes the given document.
	 * 
	 * @param doc document
	 */
	public void initializeDocument(Document doc) {
		doc.initializeVectors(vocabulary, idfVector);
	}
	
	/**
	 * @return vocabulary size
	 */
	public int getSize() {
		return vocabulary.size();
	}

	/**
	 * Calculates similarities between the given query and all documents.
	 * 
	 * @param queryDoc query document
	 * @return map of similarities
	 */
	public Map<Document, Double> calculateSimilarities(Document queryDoc) {
		query = new TreeMap<>(
			(f, s) -> Double.compare(
				s.calculateSimilarity(queryDoc), 
				f.calculateSimilarity(queryDoc)
		));
			
		for (Document doc : documents) {
			double similarity = doc.calculateSimilarity(queryDoc);
			if (similarity > 0) {
				query.put(doc, similarity);
			}
		}
		
		return query;
	}

	/**
	 * @return query result, map of similarities
	 */
	public Map<Document, Double> getQueryResult() {
		return query;
	}
	
	public Vector getIdf() {
		return idfVector;
	}
	
}

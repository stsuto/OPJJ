package hr.fer.zemris.java.hw17.trazilica;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents an file visitor which for every file visited creates 
 * values for every word repetition, and creates a vocabulary.<p>
 * Has two states: vocabulary loading and query loading.
 * 
 * @author stipe
 *
 */
public class DocumentVisitor extends SimpleFileVisitor<Path> {

	/**
	 * Vocabulary loading state.
	 */
	public static final int LOAD_VOCABULARY = 0;
	/**
	 * Query loading state.
	 */
	public static final int LOAD_QUERY = 1;
	private static final String STOP_WORDS_LOCATION = "src/main/resources/hrvatski_stoprijeci.txt";
	
	/**
	 * List of visited documents.
	 */
	private List<Document> documents;
	/**
	 * Vocabulary.
	 */
	private Map<String, Integer> vocabulary;
	/**
	 * Stop words which are ignored.
	 */
	private Set<String> stopWords;
	
	/**
	 * Constructor.
	 * 
	 * @param documentFolder folder with documents
	 */
	public DocumentVisitor(String documentFolder) {
		Path folderPath = Paths.get(documentFolder);
		
		this.documents = new ArrayList<>();
		this.stopWords = readStopWords(Paths.get(STOP_WORDS_LOCATION));
		this.vocabulary = new LinkedHashMap<>();
		
		startReading(folderPath);
	}

	/**
	 * Reads the stop words from the file.
	 * 
	 * @param file stop word file
	 * @return set of stop words
	 */
	private Set<String> readStopWords(Path file) {
		try (Stream<String> stream = Files.lines(file)) {
			return stream.collect(Collectors.toSet());
			
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read stop words.");
		}
	}
	
	/**
	 * Starts visiting documents from folder path.
	 * 
	 * @param folderPath folder path
	 */
	private void startReading(Path folderPath) {
		try {
			Files.walkFileTree(folderPath, this);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read documents.");
		}	
	}
	
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		buildVocabulary(file);
		return FileVisitResult.CONTINUE;
	}

	/**
	 * Builds the vocabulary.
	 * 
	 * @param file visited file
	 * @throws IOException
	 */
	private void buildVocabulary(Path file) throws IOException {
		String documentText = Files.readString(file);		
		documents.add(loadDocument(file, documentText, DocumentVisitor.LOAD_VOCABULARY));
	}
	
	/**
	 * Loads the document. If state is vocabulary loading then the read words
	 * are added to the vocabulary, otherwise only to the document.
	 * 
	 * @param file visited file
	 * @param text file's text
	 * @param loadOption visitor state
	 * @return read Document
	 */
	public Document loadDocument(Path file, String text, int loadOption) {
		Pattern pattern = Pattern.compile("\\p{IsAlphabetic}+");
		Matcher matcher = pattern.matcher(text);
		
		Map<String, Integer> docWords = new LinkedHashMap<>();
		Set<String> foundWords = new HashSet<>();
		
		while (matcher.find()) {
			String word = matcher.group().strip().toLowerCase();
			
			if (!word.isBlank() && !stopWords.contains(word)) {
				if (loadOption == LOAD_VOCABULARY && !foundWords.contains(word)) {
					foundWords.add(word);
					vocabulary.merge(word, 1, (o, n) -> o + n);					
				}
				docWords.merge(word, 1, (o, n) -> o + n);
			}
		}

		return new Document(file, docWords);
	}
	
	/**
	 * @return the documents
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	/**
	 * @return the vocabulary
	 */
	public Map<String, Integer> getVocabulary() {
		return vocabulary;
	}

	/**
	 * @return the stopWords
	 */
	public Set<String> getStopWords() {
		return stopWords;
	}
	
}

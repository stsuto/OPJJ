package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;

/**
 * This class represents an object capable of parsing text lines containing
 * definition of geometrical objects and then creating those same objects.
 * 
 * @author stipe
 *
 */
public class GeometricalObjectParser {
	
	/**
	 * List of parsed objects.
	 */
	private List<GeometricalObject> objects;
	
	/**
	 * Constructor
	 * 
	 * @param path path to file to be parsed
	 */
	public GeometricalObjectParser(Path path) {
		objects = new ArrayList<>();
		
		for (String definition : splitToLines(path)) {
			objects.add(parse(definition));
		}
	}
	
	/**
	 * Returns parsed objects.
	 * 
	 * @return {@link #objects}
	 */
	public List<GeometricalObject> getParsedObjects() {
		return objects;
	}
	
	/**
	 * Splits the text into lines of text.
	 * 
	 * @param path file path
	 * @return list of lines
	 */
	private List<String> splitToLines(Path path) {
		try {
			return Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't parse object definitions!");
		}
	}

	/**
	 * Parses a line.
	 * 
	 * @param definition line
	 * @return parsed object
	 */
	private GeometricalObject parse(String definition) {
		if (definition.startsWith("LINE")) {
			return parseLine(definition);
		} else if (definition.startsWith("CIRCLE")) {
			return parseCircle(definition);
		} else if (definition.startsWith("FCIRCLE")) {
			return parseFilledCircle(definition);
		} else {
			throw new RuntimeException("Invalid object name definition!");
		}
	}

	/**
	 * Parses a text containing a line object definition.
	 * 
	 * @param definition text 
	 * @return line object
	 */
	public Line parseLine(String definition) {
		int[] p = parseDefinition(definition, 4);
		checkParsedLength(p, 7, "line");
		return new Line(p[0], p[1], p[2], p[3], new Color(p[4], p[5], p[6]));
	}

	/**
	 * Checks if the length is proper.
	 * 
	 * @param p array of parameters
	 * @param length expected length
	 * @param type type of object
	 */
	private void checkParsedLength(int[] p, int length, String type) {
		if (p.length != length) {
			throw new RuntimeException("Invalid number of arguments in " + type + " definition!");
		}
	}

	/**
	 * Parses a text containing a circle object definition.
	 * 
	 * @param definition text
	 * @return circle object
	 */
	public Circle parseCircle(String definition) {
		int[] p = parseDefinition(definition, 3);
		checkParsedLength(p, 6, "circle");
		return new Circle(p[0], p[1], p[2], new Color(p[3], p[4], p[5]));
	}

	/**
	 * Parses a text containing a filled circle object definition.
	 * 
	 * @param definition text
	 * @return filled circle object
	 */
	public FilledCircle parseFilledCircle(String definition) {
		int[] p = parseDefinition(definition, 3);
		return new FilledCircle(
				p[0], p[1], p[2], 
				new Color(p[3], p[4], p[5]), 
				new Color(p[6], p[7], p[8])
			);
	}
	
	/**
	 * parses a line of text
	 * 
	 * @param definition
	 * @param coordNumber
	 * @return
	 */
	private static int[] parseDefinition(String definition, int coordNumber) {
		String[] parts = definition.split("\\s+");
		int numOfArgs = parts.length - 1;
		int[] parsed = new int[numOfArgs];
		
		try {
			for (int i = 0; i < numOfArgs; i++) {
				parsed[i] = Integer.parseInt(parts[i + 1]);
				if (i >= coordNumber - 1 && (parsed[i] < 0 && parsed[i] > 255)) {
					throw new RuntimeException("Color definition values out of bounds!");
				}
			}	
		} catch (NumberFormatException e) {
			throw new RuntimeException("Couldn't parse definition values!");
		}
		
		return parsed;
	}
	
}

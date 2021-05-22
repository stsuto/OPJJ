package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Class <code>LSystemBuilderImpl</code> defines an object which has properties used for 
 * generating production sequences, storing the given productions and commands, and creates
 * objects capable of drawing.
 * 
 * @author stipe
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	/**
	 * Dictionary of commands performed for each character representing a command.
	 */
	private Dictionary<Character, Command> commands;
	/**
	 * Dictionary of productions for each character representing starting point of the production.
	 */
	private Dictionary<Character, String> productions;
	/**
	 * The length of one step.
	 */
	private double unitLength;
	/**
	 * The scaler which scales the unit length differently for each level of drawing.
	 */
	private double unitLengthDegreeScaler;
	/**
	 * The starting position of the turtle.
	 */
	private Vector2D origin;
	/**
	 * The starting angle of the turtle.
	 */
	private double angle;
	/**
	 * The starting sequence from which commands are created after productions.
	 */
	private String axiom;
	
	/**
	 * Constructor which sets all properties to their default values.
	 */
	public LSystemBuilderImpl() {
		commands = new Dictionary<>();
		productions = new Dictionary<>();
		unitLength = 0.1;
		unitLengthDegreeScaler = 1;
		origin = new Vector2D(0, 0);
		angle = 0;
		axiom = "";
	}
	
	/**
	 * Class which represents a fractal drawing object. It has two methods, one which generates
	 * the sequence produced from applying productions from dictionary, and one which creates a
	 * context and the turtle which performs commands resulting in drawing of lines.
	 * 
	 * @author stipe
	 *
	 */
	private class LSystemImpl implements LSystem {

		/**
		 * Draws lines by creating the context and the turtle with which the drawing will be done.
		 * The object is created from values from the outer class {@link LSystemBuilderImpl}
		 * 
		 * @param level depth of production
		 * @param painter object capable of drawing
		 */
		@Override
		public void draw(int level, Painter painter) {
			Context context = new Context();
			TurtleState state = new TurtleState(
					origin, 
					(new Vector2D(1, 0)).rotated(Math.toRadians(angle)), 
					Color.BLACK, 
					unitLength * Math.pow(unitLengthDegreeScaler, level)
				);
			context.pushState(state);
			String generated = generate(level);
			
			for (int i = 0; i < generated.length(); i++) {
				Command command = commands.get(generated.charAt(i));
				if (command != null) {
					command.execute(context, painter);
				}
			}
		}

		/**
		 * Generates the final sequence by applying productions on the axiom and the resulting sequences,
		 * repeating it for the depth given via argument.
		 * 
		 * @param level depth of production applying
		 */
		@Override
		public String generate(int level) {
			String generated = axiom;
			
			for (int currentLevel = 0; currentLevel < level; currentLevel++) {
				StringBuilder produced = new StringBuilder();
			
				for (int i = 0; i < generated.length(); i++) {
					String production = productions.get(generated.charAt(i));
					produced.append(production == null ? generated.charAt(i) : production);
				}
				
				generated = produced.toString();
			}
			
			return generated;
		}
		
	}
	
	/**
	 * Creates a new object capable of drawing using the properties and values of this 
	 * <code>LSystemBuilderImpl</code> class.
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Reads the given Strings and uses them to configure properties, register commands and productions,
	 * and creates directives from the given String.
	 * 
	 * @param lines array of String lines used for configuration
	 * @throws IllegalArgumentException if the command is incorrectly defined
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		for (String line : lines) {
			String[] words = line.split("\\s+");
			
			if (words[0].equals("")) {
				continue;
			}
			switch (words[0]) {
				case "command":
					if (words[1].length() != 1) {
						throw new IllegalArgumentException("Wrong symbol was given as an argument for a command!");
					}
					String action = "";
					// If there are too many words for a proper command, #registerCommand will throw exception.
					for (int i = 2; i < words.length; i++) { 
						action += words[i] + " ";
					}
					registerCommand(words[1].charAt(0), action);
					break;
					
				case "origin":
					if (words.length != 3) {
						throw new IllegalArgumentException("Keyword origin must be followed by exactly two arguments!");
					}
					try {
						setOrigin(Double.parseDouble(words[1]), Double.parseDouble(words[2]));
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("The arguments for origin setup must be type-double numbers!");
					}
					break;
	
				case "angle":
					parseDirective(words, this::setAngle);
					break;

				case "unitLength":
					parseDirective(words, this::setUnitLength);
					break;

				case "unitLengthDegreeScaler":
					if (words.length == 3) {
						if (!words[1].contains("/") && !words[2].contains("/")) {
							throw new IllegalArgumentException("Wrong arguments for unitLengthDegreeScaler setup!");		
						}
					} else if (words.length == 4) {						
						if (!words[2].contains("/")) {
							throw new IllegalArgumentException("Wrong arguments for unitLengthDegreeScaler setup!");
						}
					} else if (words.length != 2) {
						throw new IllegalArgumentException("Wrong arguments for unitLengthDegreeScaler setup!");
					}
					String argument = "";
					for (int i = 1; i < words.length; i++) {
						argument += words[i];
					}
					String[] numbers = argument.split("\\/");
					try {						
						double scaler = 0;
						if (numbers.length == 1) {
							scaler = Double.parseDouble(numbers[0]);
						} else if (numbers.length == 2) {
							scaler = Double.parseDouble(numbers[0]) / Double.parseDouble(numbers[1]);	
						}
						setUnitLengthDegreeScaler(scaler);
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Argument for unitLengthDegreeScaler "
								+ "must either be a decimal number or a division of decimal numbers!");
					}					
					break;

				case "axiom":
					if (words.length != 2) {
						throw new IllegalArgumentException("Keyword axiom must be followed by a single argument!");
					}
					setAxiom(words[1]);
					break;

				case "production":
					if (words.length != 3 || words[1].length() != 1) {
						throw new IllegalArgumentException("Keyword production must be followed by a single "
								+ "character and then a production String for that character!");
					}
					registerProduction(words[1].charAt(0), words[2]);
					break;
					
				default:
					throw new IllegalArgumentException("Unknown directive in given argument!");
				}
		}
		
		return this;
	}

	/**
	 * Parses the String for directives which require only a single argument following them,
	 * such as setting unit length and angle.
	 * 
	 * @param words array of String values, each containing a part of the directive
	 * @param consumer decides which method will be called
	 * @throws IllegalArgumentException if there are too many or not enough arguments for the command
	 */
	private void parseDirective(String[] words, Consumer<Double> consumer) {
		if (words.length != 2) {
			throw new IllegalArgumentException("Keyword " + words[0] + " must be followed by exactly one argument!");
		}
		try {
			consumer.accept(Double.parseDouble(words[1]));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("The argument for " + words[0] + " setup must be a type-double number!");
		}
	}

	/**
	 * Adds a command for this <code>LSystemBuilderImpl</code>.
	 * 
	 * @param symbol the symbol which represents the command
	 * @param action String containing the action to be performed when command is executed
	 */
	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		String[] words = action.split("\\s+");
		
		switch (words[0]) {
			case "draw":
				parseIfSingleArgument(words, symbol, DrawCommand::new);
				break;
	
			case "rotate":
				parseIfSingleArgument(words, symbol, RotateCommand::new);
				break;
				
			case "scale":
				parseIfSingleArgument(words, symbol, ScaleCommand::new);
				break;
				
			case "skip":
				parseIfSingleArgument(words, symbol, SkipCommand::new);
				break;
	
			case "pop":
				parseIfNoArguments(words, symbol, PopCommand::new);
				break;
				
			case "push":
				parseIfNoArguments(words, symbol, PushCommand::new);
				break;
				
			case "color":
				if (words.length != 2) {
					throw new IllegalArgumentException("Command color must be followed by one and only one argument!");
				} else if (words[1].length() != 6) {
					throw new IllegalArgumentException("Color values given as argument are wrong!");
				}
				int red = Integer.parseInt(words[1].substring(0, 2), 16);
				int green = Integer.parseInt(words[1].substring(2, 4), 16);
				int blue = Integer.parseInt(words[1].substring(4), 16);
				Color color = new Color(red, green, blue);
				commands.put(symbol, new ColorCommand(color));
				break;
				
			default:
				throw new IllegalArgumentException("Unknown command name!");
		}
		
		return this;
	}

	/**
	 * Parses the String for commands which don't have an argument following them, 
	 * such as pop and push commands.
	 * 
	 * @param words array of String values, each containing a part of the command
	 * @param symbol the symbol which represents the command
	 * @param supplier defines which command will be created
	 * @throws IllegalArgumentException if there are too many arguments for the command
	 */
	private void parseIfNoArguments(String[] words, char symbol, Supplier<Command> supplier) {
		if (words.length != 1) {
			throw new IllegalArgumentException("Command " + words[0] + " shouldn't be followed by any arguments!");
		}
		commands.put(symbol, supplier.get());
	}

	/**
	 * Parses the String for commands which require only a single argument following them,
	 * such as draw, rotate, scale etc.
	 * 
	 * @param words array of String values, each containing a part of the command
	 * @param symbol the symbol which represents the command
	 * @param function defines which command will be created
	 * @throws IllegalArgumentException if there are too many or not enough arguments for the command
	 */
	private void parseIfSingleArgument(String[] words, char symbol, Function<Double, Command> function) {
		if (words.length != 2) {
			throw new IllegalArgumentException("Command " + words[0] + " must be followed by one and only one argument!");
		}
		try {
			double value = Double.parseDouble(words[1]);
			commands.put(symbol, function.apply(value));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Command " + words[0] + " must be followed by a type-double number!");
		}
	}

	/**
	 * Adds a production for this <code>LSystemBuilderImpl</code>.
	 * 
	 * @param symbol the symbol which represents the production
	 * @param production the sequence which is created from the symbol
	 */
	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		this.productions.put(symbol, production);
		return this;
	}

	/**
	 * Setter for {@link #angle}.
	 * 
	 * @param angle the angle to be set for this object
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	/**
	 * Setter for {@link #axiom}.
	 * 
	 * @param axiom the axiom to be set for this object
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = axiom;
		return this;
	}

	/**
	 * Setter for {@link #origin}.
	 * 
	 * @param origin the origin to be set for this object
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		this.origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Setter for {@link #unitLength}.
	 * 
	 * @param unitLength the unit length to be set for this object
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	/**
	 * Setter for {@link #unitLengthDegreeScaler}.
	 * 
	 * @param unitLengthDegreeScaler the length scaler to be set for this object
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}

}

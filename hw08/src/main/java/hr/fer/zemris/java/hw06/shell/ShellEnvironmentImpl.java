package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.massrename.MassrenameShellCommand;
import hr.fer.zemris.java.util.Util;

public class ShellEnvironmentImpl implements Environment {

	/**
	 * Default value of {@link #PROMPTSYMBOL}.
	 */
	private static char DEFAULTPROMPTSYMBOL = '>';
	/**
	 * Default value of {@link #MORELINESSYMBOL}.
	 */
	private static char DEFAULTMORELINESSYMBOL = '\\';
	/**
	 * Default value of {@link #MULTILINESYMBOL}.
	 */
	private static char DEFAULTMULTILINESYMBOL = '|';
	
	/**
	 * Scanner used for reading user input.
	 */
	private Scanner sc;
	/**
	 * Symbol written by the shell on the beginning of (almost) every line.
	 */
	private char PROMPTSYMBOL;
	/**
	 * Symbol which the user needs to input on the end of the line to inform
	 * the shell that more lines are to be expected.
	 */
	private char MORELINESSYMBOL;
	/**
	 * Symbol written by the shell on the beginning of lines which are
	 * a continuation of previous lines.
	 */
	private char MULTILINESYMBOL;
	
	/**
	 * Current directory of the environment.
	 */
	private Path currentDir;
	private Map<String, Object> data;

	/**
	 * Constructor which initializes the symbol values to their default values.
	 */
	public ShellEnvironmentImpl() {
		sc = new Scanner(System.in);
		PROMPTSYMBOL = DEFAULTPROMPTSYMBOL;
		MORELINESSYMBOL = DEFAULTMORELINESSYMBOL;
		MULTILINESYMBOL = DEFAULTMULTILINESYMBOL;
		currentDir = Paths.get(".").toAbsolutePath().normalize();
		data = new HashMap<>();
	}

	@Override
	public String readLine() throws ShellIOException {
		try {
			return sc.nextLine();
		} catch (NoSuchElementException | IllegalStateException e) {
			throw new  ShellIOException("Error during reading line.");
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		SortedMap<String, ShellCommand> commands = new TreeMap<>();

		commands.put("exit", new ExitShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("pwd", new PwdShellCommand());
		commands.put("cd", new CdShellCommand());
		commands.put("pushd", new PushdShellCommand());
		commands.put("popd", new PopdShellCommand());
		commands.put("listd", new ListdShellCommand());
		commands.put("dropd", new DropdShellCommand());
		commands.put("massrename", new MassrenameShellCommand());
		
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() {
		return MULTILINESYMBOL;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		MULTILINESYMBOL = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		return PROMPTSYMBOL;
	}

	public void setPromptSymbol(Character symbol) {
		PROMPTSYMBOL = symbol;
	}

	@Override
	public Character getMorelinesSymbol() {
		return MORELINESSYMBOL;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		MORELINESSYMBOL = symbol;
	}

	@Override
	public Path getCurrentDirectory() {
		return currentDir;
	}

	/**
	 * {@inheritDoc}
	 * @throws ShellIOException if a directory with the given path doesn't exist
	 */
	@Override
	public void setCurrentDirectory(Path path) {
		if (!Files.isDirectory(path)) {
			throw new ShellIOException("Directory doesn't exist!");
		}
		currentDir = path.toAbsolutePath().normalize();
	}

	@Override
	public Object getSharedData(String key) {
		return data.get(key);
	}

	@Override
	public void setSharedData(String key, Object value) {
		data.put(Util.requireNonNull(key, "Data key"), 
				 Util.requireNonNull(value, "Data value"));
	}

}

package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

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
	 * Constructor which initializes the symbol values to their default values.
	 */
	public ShellEnvironmentImpl() {
		sc = new Scanner(System.in);
		PROMPTSYMBOL = DEFAULTPROMPTSYMBOL;
		MORELINESSYMBOL = DEFAULTMORELINESSYMBOL;
		MULTILINESYMBOL = DEFAULTMULTILINESYMBOL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readLine() throws ShellIOException {
		try {
			return sc.nextLine();
		} catch (NoSuchElementException | IllegalStateException e) {
			throw new  ShellIOException("Error during reading line.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);
	}

	/**
	 * {@inheritDoc}
	 */
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

		return Collections.unmodifiableSortedMap(commands);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character getMultilineSymbol() {
		return MULTILINESYMBOL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMultilineSymbol(Character symbol) {
		MULTILINESYMBOL = symbol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character getPromptSymbol() {
		return PROMPTSYMBOL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPromptSymbol(Character symbol) {
		PROMPTSYMBOL = symbol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character getMorelinesSymbol() {
		return MORELINESSYMBOL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMorelinesSymbol(Character symbol) {
		MORELINESSYMBOL = symbol;
	}

}

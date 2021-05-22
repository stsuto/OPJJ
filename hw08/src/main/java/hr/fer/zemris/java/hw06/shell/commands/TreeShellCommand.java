package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.util.Util;

/**
 * The tree command expects a single argument: directory name and prints a tree
 * in which each directory level is shifted two characters to the right.
 * Directories follow '-' characters while files follow whitespace.
 * 
 * @author stipe
 *
 */
public class TreeShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "tree";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> processedArgs = Util.processArguments(arguments);

			if (processedArgs.size() != 1) {
				env.writeln("Tree command must be followed by exactly one argument!");
				return ShellStatus.CONTINUE;
			}
			
			Path dir = Util.normalizePath(env, processedArgs.get(0));
			Files.walkFileTree(dir, new IspisStabla());
			
		} catch (ShellIOException e) {
			env.writeln(e.getMessage());
		} catch (IOException e) {
			env.writeln("Unexpected error while walking through tree!");
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("The tree command expects a single argument: directory name and prints a tree");
		
		return Collections.unmodifiableList(description); 
	}

	/**
	 * File visitor which is used for printing out the directory tree.
	 * 
	 * @author stipe
	 *
	 */
	private static class IspisStabla extends SimpleFileVisitor<Path> {
		private int razina = 0;

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			System.out.println("-".repeat(razina * 2) + (razina == 0 ? dir.toAbsolutePath() : dir.getFileName()));
			razina++;
			return FileVisitResult.CONTINUE;
		}

		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			razina--;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			System.out.println(" ".repeat(razina * 2) + file.getFileName());
			return FileVisitResult.CONTINUE;
		}
	}

}

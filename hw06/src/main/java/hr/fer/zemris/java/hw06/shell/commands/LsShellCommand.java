package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * Command ls takes a single argument – directory – and writes a directory
 * listing (not recursive). The output consists of 4 columns. First column
 * indicates if current object is directory (d), readable (r), writable (w) and
 * executable (x). Second column contains object size in bytes that is right
 * aligned and occupies 10 characters. Follows file creation date/time and
 * finally file name.
 * 
 * @author stipe
 *
 */
public class LsShellCommand implements ShellCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> processedArgs = Util.processArguments(arguments);

			if (processedArgs.size() != 1) {
				env.writeln("ls command must be followed by exactly one argument!");
				return ShellStatus.CONTINUE;
			}

			Path dir = Paths.get(processedArgs.get(0));

			if (!Files.isDirectory(dir)) {
				env.writeln("ls command requires a directory argument!");
				return ShellStatus.CONTINUE;
			}

			StringBuilder sb = new StringBuilder();
			for (Path child : Files.newDirectoryStream(dir)) {
				sb.setLength(0);
				sb.append(getBasicInfo(child)).append(getFileSize(child)).append(getCreationDateTime(child))
						.append(getFileName(child));

				env.writeln(sb.toString());
			}

		} catch (IOException e) {
			env.writeln("Unexpected error while getting basic info from file!");
		} catch (InvalidPathException e) {
			env.writeln("Illegal path name for your OS!");
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns the name of the file with the given path.
	 * 
	 * @param file path to the file
	 * @return file's name
	 */
	private String getFileName(Path file) {
		return " " + file.getFileName().toString();
	}

	/**
	 * Finds and returns the creation date and time of the given file.
	 * 
	 * @param file whose creation date and time is sought after
	 * @return String containing information about creation date and time
	 * @throws IOException if an error occurred during the reading of files
	 */
	private String getCreationDateTime(Path file) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(file, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes = null;

		attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();

		return sdf.format(new Date(fileTime.toMillis()));
	}

	/**
	 * Returns the size of the file.
	 * 
	 * @param file file whose size is wanted
	 * @return String containing the file's size
	 * @throws IOException if an error occurred during the reading of files
	 */
	private String getFileSize(Path file) throws IOException {
		return String.format("%10d ", Files.size(file));
	}

	/**
	 * Checks if the file is a directory, a file, readable, writable and executable.
	 * 
	 * @param file file to be checked
	 * @return String containing basic information about the files
	 */
	private String getBasicInfo(Path file) {
		StringBuilder sb = new StringBuilder();
		sb.append(Files.isDirectory(file) ? "d" : "-").append(Files.isReadable(file) ? "r" : "-")
				.append(Files.isWritable(file) ? "w" : "-").append(Files.isExecutable(file) ? "x" : "-").append(" ");

		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return "ls";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("Command ls takes a single argument – directory – and writes a directory listing (not recursive).");
		description.add("The output consists of 4 columns. ");
		description.add("First column indicates if current object is directory (d), readable (r), writable (w) and executable (x).");
		description.add("Second column contains object size in bytes that is right aligned and occupies 10 characters.");
		description.add("Follows file creation date/time and finally file name.");

		return description;

	}

}

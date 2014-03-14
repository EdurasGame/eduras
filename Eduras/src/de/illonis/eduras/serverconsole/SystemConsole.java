package de.illonis.eduras.serverconsole;

import java.io.Console;

/**
 * Represents the System Console {@link System#console()}.
 * 
 * @author illonis
 * 
 */
public final class SystemConsole implements ConsoleInterface {
	private final Console console;

	/**
	 * @param triggerer
	 *            event triggerer.
	 * @throws NoConsoleException
	 *             when there is no command prompt available.
	 */
	SystemConsole() throws NoConsoleException {
		console = System.console();
		if (console == null)
			throw new NoConsoleException();
	}

	@Override
	public void println(String line) {
		console.printf(line + System.lineSeparator());
	}

	@Override
	public String readLine() {
		return console.readLine();
	}

	@Override
	public String readLine(String prompt) {
		return console.readLine(prompt);
	}

	@Override
	public void printlnf(String line, Object... args) {
		println(String.format(line, args));
	}
}

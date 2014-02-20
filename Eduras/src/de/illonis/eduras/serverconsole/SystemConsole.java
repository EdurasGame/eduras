package de.illonis.eduras.serverconsole;

import java.io.Console;

import de.illonis.eduras.logic.ConsoleEventTriggerer;

public final class SystemConsole implements ConsoleInterface {
	private final Console console;
	private final ConsoleEventTriggerer triggerer;

	public SystemConsole(ConsoleEventTriggerer triggerer)
			throws NoConsoleException {
		this.triggerer = triggerer;
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

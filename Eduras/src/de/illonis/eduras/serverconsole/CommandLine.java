package de.illonis.eduras.serverconsole;

import java.io.Console;

public class CommandLine implements ConsoleInterface {

	private final Console console;

	public CommandLine() {
		console = System.console();
	}

	@Override
	public void writeLine(String line) {
		console.printf(line + "\n");
	}

	@Override
	public void writef(String str, Object[] args) {
		console.printf(str, args);
	}

	@Override
	public String readLine() {
		return console.readLine();
	}

	@Override
	public String readLine(String prompt) {
		return console.readLine(prompt);
	}

}

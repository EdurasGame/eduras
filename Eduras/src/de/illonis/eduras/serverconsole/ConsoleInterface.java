package de.illonis.eduras.serverconsole;

public interface ConsoleInterface extends ConsolePrinter {

	String readLine();

	String readLine(String prompt);
}

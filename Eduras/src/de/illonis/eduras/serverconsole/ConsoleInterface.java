package de.illonis.eduras.serverconsole;

/**
 * Simulates a console by providing methods for input and output.
 * 
 * @author illonis
 * 
 */
public interface ConsoleInterface extends ConsolePrinter {

	/**
	 * Reads a single line from input.
	 * 
	 * @return the read line.
	 */
	String readLine();

	/**
	 * Reads a single line using a given command prompt.
	 * 
	 * @param prompt
	 *            the prompt.
	 * @return the read line.
	 */
	String readLine(String prompt);
}

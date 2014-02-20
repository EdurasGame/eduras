package de.illonis.eduras.serverconsole;

import java.util.Formatter;
import java.util.IllegalFormatException;

import de.illonis.edulog.EduLog;

/**
 * A printer can handle output print.
 * 
 * @author illonis
 * 
 */
public interface ConsolePrinter {

	/**
	 * Prints given text line to console.
	 * 
	 * @param line
	 *            text to print.
	 */
	void println(String line);

	/**
	 * Prints given (formatted) string to current server console.<br>
	 * <i>Note: This method neither throws an exception nor prints anything if a
	 * command line is available but no server console running. Use
	 * {@link EduLog} for that.</i>
	 * 
	 * @see Formatter
	 * 
	 * @param line
	 *            A (format) string that should be printed.
	 * 
	 * @param args
	 *            Format arguments.
	 * 
	 * @throws IllegalFormatException
	 *             If a format string contains an illegal syntax, a format
	 *             specifier that is incompatible with the given arguments,
	 *             insufficient arguments given the format string, or other
	 *             illegal conditions. For specification of all possible
	 *             formatting errors, see the detail section of
	 *             {@link Formatter} class specification.
	 */
	void printlnf(String line, Object... args);

}

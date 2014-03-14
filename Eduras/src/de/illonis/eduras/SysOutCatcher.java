package de.illonis.eduras;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Catches all {@link System#out} calls, prints a stacktrace and terminates the
 * program.
 * 
 * @author illonis
 * 
 */
public class SysOutCatcher extends PrintStream {

	/**
	 * Starts catching.
	 */
	public static void startCatching() {
		System.setOut(new SysOutCatcher(System.out));
	}

	private SysOutCatcher(OutputStream out) {
		super(out);
	}

	@Override
	public void print(String s) {
		// pretend an exception
		Exception e = new Exception("Found a System.out: " + s);
		e.fillInStackTrace();

		// remove recent two items as it is an internal call to this method.
		StackTraceElement elements[] = e.getStackTrace();
		StackTraceElement newelements[] = new StackTraceElement[elements.length - 2];
		for (int i = 2; i < elements.length; i++) {
			newelements[i - 2] = elements[i];
		}
		e.setStackTrace(newelements);
		
		// print stacktrace and exit.
		e.printStackTrace();
		super.print("Terminating program.");
		System.exit(1);
	}
}

package de.illonis.eduras.serverconsole;

public interface ConsoleInterface {

	void writeLine(String line);
	
	void writef(String str, Object[] args);

	String readLine();
	
	String readLine(String prompt);
}

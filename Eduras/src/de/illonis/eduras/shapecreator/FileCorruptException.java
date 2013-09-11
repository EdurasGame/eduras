package de.illonis.eduras.shapecreator;

public class FileCorruptException extends Exception {

	public FileCorruptException(String corruptFile) {
		super("File could not be parsed: " + corruptFile);
	}

}

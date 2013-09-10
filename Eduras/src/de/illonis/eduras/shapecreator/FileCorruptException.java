package de.illonis.eduras.shapecreator;

import java.io.File;

public class FileCorruptException extends Exception {

	public FileCorruptException(File f) {
		super("File could not be parsed: " + f.getAbsolutePath());
	}

}

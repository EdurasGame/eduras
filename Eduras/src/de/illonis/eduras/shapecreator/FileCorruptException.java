package de.illonis.eduras.shapecreator;

/**
 * Indicates that a file could not be parsed due to syntax errors.
 * 
 * @author illonis
 * 
 */
public class FileCorruptException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link FileCorruptException}.
	 * 
	 * @param corruptFile
	 *            the name of the malicious file.
	 */
	public FileCorruptException(String corruptFile) {
		super("File could not be parsed: " + corruptFile);
	}

}

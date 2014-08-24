package de.illonis.eduras.mapeditor;

/**
 * Indicates an error while performing an editor action.
 * 
 * @author illonis
 * 
 */
public class EditorException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception.
	 * 
	 * @param string
	 *            the error message.
	 */
	public EditorException(String string) {
		super(string);
	}
}

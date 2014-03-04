package de.illonis.eduras.shapecreator;

/**
 * Indicates that an error occured during an operation on a vertice list.
 * 
 * @author illonis
 * 
 */
public class VerticeListException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception.
	 * 
	 * @param string
	 *            error message.
	 */
	public VerticeListException(String string) {
		super(string);
	}

}

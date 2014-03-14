package de.illonis.eduras.exceptions;

/**
 * Indicates that a key has no assigned binding. This is thrown when a binding
 * to a key is tried to receive but key has no binding.
 * 
 * @author illonis
 * 
 */
public class KeyNotBoundException extends Exception {

	private int key;
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link KeyNotBoundException}.
	 * 
	 * @param key
	 *            key that has no binding.
	 */
	public KeyNotBoundException(int key) {
		super();
		this.key = key;
	}

	/**
	 * Returns key.
	 * 
	 * @return key that is not bound.
	 */
	public int getKey() {
		return key;
	}
}

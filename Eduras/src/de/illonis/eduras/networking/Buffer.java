package de.illonis.eduras.networking;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A Buffer is a linked list of Strings. Oldest elements will be popped first.
 * 
 * @author illonis
 * 
 */
public class Buffer extends LinkedList<String> {
	public final static Object SYNCER = new Object();

	private static final long serialVersionUID = 1L;

	/**
	 * Returns the first element of buffer. The first element is that element
	 * that was added before all other elements.
	 * 
	 * @return First element of buffer.
	 * @throws NoSuchElementException
	 *             Thrown when list is empty.
	 */
	public String getNext() throws NoSuchElementException {
		return pop();
	}

	/**
	 * Appends a string at the and of the buffer.
	 * 
	 * @param string
	 *            string to add.
	 */
	public void append(String string) {
		add(string);
	}

	public String[] getAll() {
		return (String[]) toArray();
	}

}

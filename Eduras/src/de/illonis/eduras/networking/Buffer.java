package de.illonis.eduras.networking;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.illonis.eduras.exceptions.BufferIsEmptyException;

/**
 * A Buffer is a linked list of Strings. Elements are returned in same order
 * they where added.
 * 
 * @author illonis
 * 
 */
public class Buffer {

	/**
	 * Use this object to synchronize access.
	 */
	public final static Object SYNCER = new Object();

	private LinkedList<String> list;

	/**
	 * Creates a new buffer.
	 */
	public Buffer() {
		list = new LinkedList<String>();
	}

	/**
	 * Returns and removes the first element of buffer. The first element is
	 * that element that was added before all other elements.<br>
	 * Elements are returned in same order they where added.
	 * 
	 * @return First element of buffer.
	 * @throws BufferIsEmptyException
	 *             Thrown if list is empty.
	 */
	public String getNext() throws BufferIsEmptyException {
		try {
			return list.pop();
		} catch (NoSuchElementException e) {
			throw new BufferIsEmptyException();
		}
	}

	/**
	 * Appends a string at the end of this buffer.
	 * 
	 * @param string
	 *            String to add.
	 */
	public void append(String string) {
		list.add(string);
	}

	/**
	 * Returns all strings that are in this buffer as an array.
	 * 
	 * @return A string array containing all strings.
	 * @throws BufferIsEmptyException
	 *             Thrown if list is empty.
	 */
	public String[] getAll() throws BufferIsEmptyException {
		if (list.size() == 0)
			throw new BufferIsEmptyException();
		return (String[]) list.toArray();
	}
}

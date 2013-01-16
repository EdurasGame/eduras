package de.illonis.eduras.utils;

import java.util.LinkedList;

public abstract class CollectionUtils {

	/**
	 * If the list contains an instance of the class 'c', the first such an
	 * instance is returned. If there is none the function returns null.
	 * 
	 * @param c
	 *            The class
	 * @param list
	 *            The list to be checked for an instance of class 'c'.
	 * @return Returns the first appearance of an instance of class 'c' or null
	 *         if there is none.
	 */
	public static Object getElementOfClass(Class<?> c, LinkedList<?> list) {
		for (Object object : list) {
			if (c.isInstance(object)) {
				return object;
			}
		}
		return null;
	}
}

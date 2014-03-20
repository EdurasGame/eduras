package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Thrown if the map of the given name cannot be found.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NoSuchMapException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger L = EduLog
			.getLoggerFor(NoSuchMapException.class.getName());

	private final String mapName;

	/**
	 * Create a new NoSuchMapException.
	 * 
	 * @param mapThatWasntFound
	 *            The name of the map that wasn't found.
	 */
	public NoSuchMapException(String mapThatWasntFound) {
		mapName = mapThatWasntFound;
	}

	/**
	 * Returns the string for which the corresponding map couldn't be found.
	 * 
	 * @return the name of the map
	 */
	public String getMapName() {
		return mapName;
	}

}

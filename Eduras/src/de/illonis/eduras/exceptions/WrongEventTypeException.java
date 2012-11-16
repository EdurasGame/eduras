/**
 * 
 */
package de.illonis.eduras.exceptions;

import de.illonis.eduras.events.GameEvent;

/**
 * This exception occurs when an event is of the wrong type.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class WrongEventTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GameEvent event;

	/**
	 * Creates a new WrongEventTypeException.
	 * 
	 * @param event
	 *            The event that was of the wrong type.
	 */
	public WrongEventTypeException(GameEvent event) {
		this.event = event;
	}

	public GameEvent getEvent() {
		return this.event;
	}

}

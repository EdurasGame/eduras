/**
 * 
 */
package de.illonis.eduras.events;

import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;

/**
 * Event for any movement to any direction.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MovementEvent extends ObjectEvent {

	private int newXPos = -1;
	private int newYPos = -1;

	/**
	 * Creates a new MovementEvent belonging to the object of the given id.
	 * 
	 * @param id
	 *            The id of the object.
	 * @param direction
	 *            The direction to which the movement shall take place.
	 * @throws GivenParametersDoNotFitToEventException
	 *             Exception takes place if the given GameEventNumber is not a
	 *             MovementEvent.
	 */
	public MovementEvent(GameEventNumber type, int id)
			throws GivenParametersDoNotFitToEventException {

		super(type, id);

	}

	/**
	 * Returns the x position that shall be newly set by this event.
	 * 
	 * @return The new x position.
	 */
	public int getNewXPos() {
		return newXPos;
	}

	/**
	 * Returns the y position that shall be newly set by this event.
	 * 
	 * @return The new y position.
	 */
	public int getNewYPos() {
		return newYPos;
	}

	/**
	 * Sets the x position that is to be set with this event to the given value.
	 * 
	 * @param val
	 *            The new value.
	 */
	public void setNewXPos(int val) {
		newXPos = val;
	}

	/**
	 * Sets the y position that is to be set with this event to the given value.
	 * 
	 * @param val
	 *            The new value.
	 */
	public void setNewYPos(int val) {
		newYPos = val;
	}

}

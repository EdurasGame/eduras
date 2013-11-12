/**
 * 
 */
package de.illonis.eduras.events;

import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.eduras.networking.EventParser;

/**
 * Event for any movement to any direction.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MovementEvent extends ObjectEvent {

	private double newXPos = -1;
	private double newYPos = -1;

	/**
	 * Creates a new MovementEvent belonging to the object of the given id.
	 * 
	 * @param type
	 *            Type of event.
	 * @param id
	 *            The id of the object.
	 */
	public MovementEvent(GameEventNumber type, int id) {
		super(type, id);
	}

	/**
	 * Returns the x position that shall be newly set by this event.
	 * 
	 * @return The new x position.
	 */
	public double getNewXPos() {
		return newXPos;
	}

	/**
	 * Returns the y position that shall be newly set by this event.
	 * 
	 * @return The new y position.
	 */
	public double getNewYPos() {
		return newYPos;
	}

	/**
	 * Sets the x position that is to be set with this event to the given value.
	 * 
	 * @param val
	 *            The new value.
	 */
	public void setNewXPos(double val) {
		newXPos = val;
	}

	/**
	 * Sets the y position that is to be set with this event to the given value.
	 * 
	 * @param val
	 *            The new value.
	 */
	public void setNewYPos(double val) {
		newYPos = val;
	}

	@Override
	public Object getArgument(int i) throws TooFewArgumentsExceptions {
		switch (getEventNumber()) {
		case EventParser.SET_POS_TCP:
		case EventParser.SET_POS_UDP:
			switch (i) {
			case 0:
				return getObjectId();
			case 1:
				return newXPos;
			case 2:
				return newYPos;
			default:
				throw new TooFewArgumentsExceptions(i, 3);
			}
		default:
			if (i == 0) {
				return getObjectId();
			} else {
				throw new TooFewArgumentsExceptions(i, 1);
			}
		}
	}

	@Override
	public int getNumberOfArguments() {
		switch (getType()) {
		case SET_POS_TCP:
		case SET_POS_UDP:
			return 3;
		default:
			return 1;
		}
	}
}

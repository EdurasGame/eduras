package de.illonis.eduras.events;

import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;

/**
 * Super class for all events. Contains an enum for every existing event.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class GameEvent {

	private GameEventNumber type;

	/**
	 * Contains any event that can occur. The number behind is passed via
	 * network.
	 * 
	 * MOVE_LEFT: An id associated with a GameObject will be passed with this
	 * event. The object behind wants to move left. MOVE_UP: An id associated
	 * with a GameObject will be passed with this event. The object behind wants
	 * to move up. MOVE_RIGHT: An id associated with a GameObject will be passed
	 * with this event. The object behind wants to move right. MOVE_DOWN: An id
	 * associated with a GameObject will be passed with this event. The object
	 * behind wants to move down. MOVE_POS: An id associated with a GameObject
	 * will be passed with this event. The object behind is moved to the x and y
	 * position that come along with the event.
	 * 
	 * @author Florian Mai <florian.ren.mai@googlemail.com>
	 */
	public enum GameEventNumber {
		MOVE_LEFT(10), MOVE_RIGHT(11), MOVE_UP(12), MOVE_DOWN(13), MOVE_POS(19), NO_EVENT(99);
		
		private int number;
		
		GameEventNumber(int num) {
			number = num;
		}
		
	}

	/**
	 * Creates a new GameEvent of the given type.
	 * @param type The type of the new GameEvent instance.
	 */
	public GameEvent(GameEventNumber type) {
		this.type = type;
	}

	/**
	 * Returns the type of the GameEvent.
	 * @return The type of the GameEvent.
	 */
	public GameEventNumber getType() {
		return type;
	}

	/** 
	 * Sets the type of the GameEvent.
	 * @param type The new value.
	 */
	public void setType(GameEventNumber type) {
		this.type = type;
	}

	/**
	 * Maps a GameEventNumber to a new instance of the representing
	 * implementation of the event.
	 * 
	 * @param num
	 *            The GameEventNumber to create a new instance of.
	 * @return The GameEvent instance
	 * @throws GivenParametersDoNotFitToEventException
	 */
	public static GameEvent gameEventNumberToGameEvent(GameEventNumber num)
			throws GivenParametersDoNotFitToEventException {

		GameEvent result = null;

		switch (num) {
		case MOVE_DOWN:
			return new MovementEvent(GameEventNumber.MOVE_DOWN, -1);
		case MOVE_LEFT:
			return new MovementEvent(GameEventNumber.MOVE_LEFT, -1);
		case MOVE_POS:
			return new MovementEvent(GameEventNumber.MOVE_POS, -1);
		case MOVE_RIGHT:
			return new MovementEvent(GameEventNumber.MOVE_RIGHT, -1);
		case MOVE_UP:
			return new MovementEvent(GameEventNumber.MOVE_UP, -1);
		default:
			break;
		}

		return result;
	}

	/**
	 * Maps a number to its GameEventNumber representation. Returns NO_EVENT
	 * if the number cannot be mapped to a GameEventNumber.
	 * @param typeInt The number to be mapped to a GameEventNumber.
	 * @return The GameEventNumber.
	 */
	public static GameEventNumber toGameEventNumber(int typeInt) {
		switch(typeInt) {
		case 10: return GameEventNumber.MOVE_LEFT;
		case 11: return GameEventNumber.MOVE_RIGHT;
		case 12: return GameEventNumber.MOVE_UP;
		case 13: return GameEventNumber.MOVE_DOWN;
		case 19: return GameEventNumber.MOVE_POS;
		default:
		}
		return GameEventNumber.NO_EVENT;
	}

}

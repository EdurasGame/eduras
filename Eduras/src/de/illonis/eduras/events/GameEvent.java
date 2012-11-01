package de.illonis.eduras.events;

/**
 * Super class for all events. Contains an enum for every existing event.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class GameEvent extends Event {

	private GameEventNumber type;

	/**
	 * Contains any event that can occur. The number behind is passed via
	 * network.<br>
	 * 
	 * @author Florian Mai <florian.ren.mai@googlemail.com>
	 */
	public enum GameEventNumber {
		MOVE_LEFT_PRESSED(10), MOVE_RIGHT_PRESSED(11), MOVE_UP_PRESSED(12), MOVE_DOWN_PRESSED(
				13), MOVE_LEFT_RELEASED(14), MOVE_RIGHT_RELEASED(15), MOVE_UP_RELEASED(
				16), MOVE_DOWN_RELEASED(17), SETSPEEDVECTOR(18), SETSPEED(19), SET_POS(
				101), OBJECT_CREATE(120), OBJECT_REMOVE(121), SHOOT_PRESSED(30), SHOOT_RELEASED(
				31), DEATH(131), SETHEALTH(132), NO_EVENT(99);

		private int number;

		GameEventNumber(int num) {
			number = num;
		}

		public int getNumber() {
			return number;
		}

	}

	/**
	 * Creates a new GameEvent of the given type.
	 * 
	 * @param type
	 *            The type of the new GameEvent instance.
	 */
	public GameEvent(GameEventNumber type) {
		this.type = type;
	}

	/**
	 * Returns the type of the GameEvent.
	 * 
	 * @return The type of the GameEvent.
	 */
	public GameEventNumber getType() {
		return type;
	}

	/**
	 * Sets the type of the GameEvent.
	 * 
	 * @param type
	 *            The new value.
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

	/*
	 * public static GameEvent gameEventNumberToGameEvent(GameEventNumber num)
	 * throws GivenParametersDoNotFitToEventException {
	 * 
	 * GameEvent result = null;
	 * 
	 * switch (num) { case MOVE_DOWN: return new
	 * MovementEvent(GameEventNumber.MOVE_DOWN, -1); case MOVE_LEFT: return new
	 * MovementEvent(GameEventNumber.MOVE_LEFT, -1); case SET_POS: return new
	 * MovementEvent(GameEventNumber.SET_POS, -1); case MOVE_RIGHT: return new
	 * MovementEvent(GameEventNumber.MOVE_RIGHT, -1); case MOVE_UP: return new
	 * MovementEvent(GameEventNumber.MOVE_UP, -1); default: break; }
	 * 
	 * return result; }
	 */

	/**
	 * Maps a number to its GameEventNumber representation. Returns NO_EVENT if
	 * the number cannot be mapped to a GameEventNumber.<br>
	 * (jme): simplified
	 * 
	 * @param typeInt
	 *            The number to be mapped to a GameEventNumber.
	 * @return The GameEventNumber.
	 */
	public static GameEventNumber toGameEventNumber(int typeInt) {
		for (GameEventNumber evn : GameEventNumber.values())
			if (evn.getNumber() == typeInt)
				return evn;
		return GameEventNumber.NO_EVENT;
	}

}

package de.illonis.eduras.events;

import de.eduras.eventingserver.Event;

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
	@SuppressWarnings("javadoc")
	public enum GameEventNumber {
		INFORMATION_REQUEST(1), CLIENT_SETNAME(2), MOVE_LEFT_PRESSED(10), MOVE_RIGHT_PRESSED(
				11), MOVE_UP_PRESSED(12), MOVE_DOWN_PRESSED(13), MOVE_LEFT_RELEASED(
				14), MOVE_RIGHT_RELEASED(15), MOVE_UP_RELEASED(16), MOVE_DOWN_RELEASED(
				17), SET_SPEEDVECTOR(18), SET_SPEED(19), SWITCH_INTERACTMODE(40), SEND_UNITS(
				41), SET_POS_UDP(101), SET_OWNER(102), SET_VISIBLE(103), SET_COLLIDABLE(
				104), SET_POLYGON_DATA(105), OBJECT_CREATE(120), OBJECT_REMOVE(
				121), SET_ITEM_SLOT(122), ITEM_CD_START(123), ITEM_CD_FINISHED(
				124), ITEM_USE(30), DEATH(131), SET_HEALTH(132), SET_MAXHEALTH(
				133), MATCH_END(190), NO_EVENT(99), MISSILE_LAUNCH(301), LOOT_ITEM_EVENT(
				302), SET_GAMEMODE(140), SET_INTERACTMODE(141), SET_KILLS(150), SET_DEATHS(
				151), SET_REMAININGTIME(180), SET_TEAMS(191), ADD_PLAYER_TO_TEAM(
				192), SET_POS_TCP(106), INIT_INFORMATION(203), GAME_READY(204), SET_ROTATION(
				80);
		private int number;

		GameEventNumber(int num) {
			number = num;
		}

		public int getNumber() {
			return number;
		}

		@Override
		public String toString() {
			return getNumber() + "";
		}

	}

	/**
	 * Creates a new GameEvent of the given type.
	 * 
	 * @param type
	 *            The type of the new GameEvent instance.
	 */
	public GameEvent(GameEventNumber type) {
		super(type.number);
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

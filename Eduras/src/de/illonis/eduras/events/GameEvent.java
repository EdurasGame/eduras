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
		INVALID_NUMBER(-1),
		INFORMATION_REQUEST(1),
		CLIENT_SETNAME(2),
		REQUEST_MAP(3),
		JOIN_TEAM(4),
		MOVE_LEFT_PRESSED(10),
		MOVE_RIGHT_PRESSED(11),
		MOVE_UP_PRESSED(12),
		MOVE_DOWN_PRESSED(13),
		MOVE_LEFT_RELEASED(14),
		MOVE_RIGHT_RELEASED(15),
		MOVE_UP_RELEASED(16),
		MOVE_DOWN_RELEASED(17),
		SET_SPEEDVECTOR(18),
		SET_SPEED(19),
		BLINK(20),
		SWITCH_INTERACTMODE(40),
		SEND_UNITS(41),
		RESURRECT_PLAYER(42),
		SPAWN_ITEM(43),
		HEAL_ACTION(44),
		CREATE_UNIT(45),
		SPEED_SPELL(46),
		INVISIBILITY_SPELL(47),
		BLINK_SPELL(48),
		SET_POS_UDP(101),
		SET_OWNER(102),
		SET_VISIBLE(103),
		SET_COLLIDABLE(104),
		SET_POLYGON_DATA(105),
		SET_RENDER_INFO(112),
		OBJECT_CREATE(120),
		OBJECT_REMOVE(121),
		SET_ITEM_SLOT(122),
		ITEM_CD_START(123),
		ITEM_CD_FINISHED(124),
		ITEM_USE(30),
		INFO_RESPAWN(130),
		DEATH(131),
		SET_HEALTH(132),
		SET_MAXHEALTH(133),
		MATCH_END(190),
		START_ROUND(189),
		NO_EVENT(99),
		MISSILE_LAUNCH(301),
		LOOT_ITEM_EVENT(302),
		SET_GAMEMODE(140),
		SET_INTERACTMODE(141),
		BASE_CONQUERED(142),
		ITEM_USE_FAILED(143),
		AOE_DAMAGE(144),
		SET_KILLS(150),
		SET_DEATHS(151),
		SET_STATS(152),
		SET_REMAININGTIME(180),
		SET_RESPAWNTIME(181),
		SET_TEAMS(191),
		ADD_PLAYER_TO_TEAM(192),
		ADD_OBJECT_TO_TEAM(193),
		SET_POS_TCP(106),
		INIT_INFORMATION(203),
		GAME_READY(204),
		INFO_PLAYER_JOIN(205),
		INFO_PLAYER_LEFT(206),
		PLAYER_JOINED(207),
		PLAYER_LEFT(208),
		SET_SETTINGS(209),
		SET_SETTINGPROPERTY(210),
		SET_ROTATION(80),
		SET_AMMU(125),
		SET_VISION_ANGLE(107),
		SET_VISION_RANGE(108),
		SET_VISION_BLOCKING(109),
		SET_MAP(110),
		SET_SIZE(111),
		SET_TEAM_RESOURCE(134),
		SET_AVAILABLE_BLINKS(135),
		SPELL_SCOUT(401),
		SEND_MAP(501);

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

		public static GameEventNumber getByNumber(int num) {
			for (GameEventNumber gen : GameEventNumber.values()) {
				if (gen.getNumber() == num)
					return gen;
			}
			return INVALID_NUMBER;
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

	@Override
	public String toString() {
		return type.toString();
	}

}

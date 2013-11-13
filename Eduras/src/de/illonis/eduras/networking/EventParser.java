package de.illonis.eduras.networking;

import java.util.logging.Logger;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team.TeamColor;
import de.illonis.eduras.events.AddPlayerToTeamEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.GameReadyEvent;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SendUnitsEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetPolygonDataEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.events.SwitchInteractModeEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * This class serves as a bridge between the network and the logic. Events'
 * arguments are read, put into GameEvents and passed to the game logic.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
@SuppressWarnings("javadoc")
public class EventParser implements EventHandler {

	private final static Logger L = EduLog.getLoggerFor(EventParser.class
			.getName());

	// client events
	public static final int INFORMATION_REQUEST = 1;
	public static final int CLIENT_SETNAME = 2;
	public static final int MOVE_LEFT_PRESSED = 10;
	public static final int MOVE_RIGHT_PRESSED = 11;
	public static final int MOVE_UP_PRESSED = 12;
	public static final int MOVE_DOWN_PRESSED = 13;
	public static final int MOVE_LEFT_RELEASED = 14;
	public static final int MOVE_RIGHT_RELEASED = 15;
	public static final int MOVE_UP_RELEASED = 16;
	public static final int MOVE_DOWN_RELEASED = 17;
	public static final int SET_SPEEDVECTOR = 18;
	public static final int SET_SPEED = 19;
	public static final int ITEM_USE = 30;
	public static final int SWITCH_INTERACTMODE = 40;
	public static final int SEND_UNITS = 41;

	// server events
	public static final int SET_POS_UDP = 101;
	public static final int SET_OWNER = 102;
	public static final int SET_VISIBLE = 103;
	public static final int SET_COLLIDABLE = 104;
	public static final int SET_POLYGON_DATA = 105;
	public static final int SET_POS_TCP = 106;
	public static final int OBJECT_CREATE = 120;
	public static final int OBJECT_REMOVE = 121;
	public static final int SET_ITEM_SLOT = 122;
	public static final int ITEM_CD_START = 123;
	public static final int ITEM_CD_FINISHED = 124;
	public static final int DEATH = 131;
	public static final int SET_HEALTH = 132;
	public static final int SET_GAMEMODE = 140;
	public static final int SET_INTERACTMODE = 141;
	public static final int SET_KILLS = 150;
	public static final int SET_DEATHS = 151;
	public static final int SET_REMAININGTIME = 180;
	public static final int MATCH_END = 190;
	public static final int SET_TEAMS = 191;
	public static final int ADD_PLAYER_TO_TEAM = 192;

	// network events
	public static final int INIT_INFORMATION = 203;
	public static final int GAME_READY = 204;

	private final GameLogicInterface logic;

	/**
	 * Create a new EventParser that passes the GameEvent it parses to the given
	 * logic.
	 * 
	 * @param logic
	 *            The logic to pass the GameEvents to.
	 */
	public EventParser(GameLogicInterface logic) {
		this.logic = logic;
	}

	@Override
	public void handleEvent(Event event) {
		int eventNumber = event.getEventNumber();
		int numberOfArgs = event.getNumberOfArguments();

		try {
			switch (eventNumber) {
			case INFORMATION_REQUEST:

				logic.onGameEventAppeared(new GameInfoRequest((Integer) event
						.getArgument(0)));
				break;
			case CLIENT_SETNAME:
				try {
					logic.onGameEventAppeared(new ClientRenameEvent(
							(Integer) event.getArgument(0), (String) event
									.getArgument(1)));
				} catch (InvalidNameException e) {
					L.warning(e.getMessage());
				}
				break;
			case MOVE_LEFT_PRESSED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_LEFT_PRESSED, (Integer) event
								.getArgument(0)));
				break;
			case MOVE_RIGHT_PRESSED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_RIGHT_PRESSED, (Integer) event
								.getArgument(0)));
				break;
			case MOVE_UP_PRESSED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_UP_PRESSED, (Integer) event
								.getArgument(0)));
				break;
			case MOVE_DOWN_PRESSED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_DOWN_PRESSED, (Integer) event
								.getArgument(0)));
				break;
			case MOVE_LEFT_RELEASED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_LEFT_RELEASED, (Integer) event
								.getArgument(0)));
				break;
			case MOVE_RIGHT_RELEASED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_RIGHT_RELEASED, (Integer) event
								.getArgument(0)));
				break;
			case MOVE_UP_RELEASED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_UP_RELEASED, (Integer) event
								.getArgument(0)));
				break;
			case MOVE_DOWN_RELEASED:
				logic.onGameEventAppeared(new UserMovementEvent(
						GameEventNumber.MOVE_DOWN_RELEASED, (Integer) event
								.getArgument(0)));
				break;
			case SET_SPEED:
				logic.onGameEventAppeared(new SetIntegerGameObjectAttributeEvent(
						GameEventNumber.SETSPEED, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case ITEM_USE:
				ItemEvent itemEvent = new ItemEvent(GameEventNumber.ITEM_USE,
						(Integer) event.getArgument(1),
						(Integer) event.getArgument(0));
				itemEvent.setTargetX((Double) event.getArgument(2));
				itemEvent.setTargetY((Double) event.getArgument(3));
				logic.onGameEventAppeared(itemEvent);
				break;
			case SWITCH_INTERACTMODE:
				InteractMode mode = InteractMode.valueOf((String) event
						.getArgument(1));
				logic.onGameEventAppeared(new SwitchInteractModeEvent(
						(Integer) event.getArgument(0), mode));
				break;
			case SEND_UNITS:
				Vector2D target = new Vector2D((Double) event.getArgument(1),
						(Double) event.getArgument(2));

				int[] unitIds = new int[numberOfArgs - 3];
				for (int i = 3; i < numberOfArgs; i++) {
					unitIds[i - 3] = (Integer) event.getArgument(i);
				}

				logic.onGameEventAppeared(new SendUnitsEvent((Integer) event
						.getArgument(0), target, unitIds));
				break;
			case SET_POS_UDP:
			case SET_POS_TCP:
				// doesnt matter whether this is TCP or UDP event at this point.
				MovementEvent setPosEvent = new MovementEvent(
						GameEventNumber.SET_POS_TCP,
						(Integer) event.getArgument(0));
				setPosEvent.setNewXPos((Double) event.getArgument(1));
				setPosEvent.setNewYPos((Double) event.getArgument(2));
				logic.onGameEventAppeared(setPosEvent);
				break;
			case SET_OWNER:
				logic.onGameEventAppeared(new SetOwnerEvent((Integer) event
						.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case SET_VISIBLE:
				logic.onGameEventAppeared(new SetBooleanGameObjectAttributeEvent(
						GameEventNumber.SET_VISIBLE, (Integer) event
								.getArgument(0), (Boolean) event.getArgument(1)));
				break;
			case SET_COLLIDABLE:
				logic.onGameEventAppeared(new SetBooleanGameObjectAttributeEvent(
						GameEventNumber.SET_COLLIDABLE, (Integer) event
								.getArgument(0), (Boolean) event.getArgument(1)));
				break;
			case SET_POLYGON_DATA:
				Vector2D[] vertices = new Vector2D[numberOfArgs];

				for (int i = 0; i < numberOfArgs; i++) {
					Vector2D vertex = new Vector2D(
							(Double) event.getArgument(2 * i + 1),
							(Double) event.getArgument(2 * i + 2));
					vertices[i] = vertex;
				}
				logic.onGameEventAppeared(new SetPolygonDataEvent(
						(Integer) event.getArgument(0), vertices));
				break;
			case OBJECT_CREATE:
				ObjectFactoryEvent objectCreateEvent = new ObjectFactoryEvent(
						GameEventNumber.OBJECT_CREATE,
						ObjectType.getObjectTypeByNumber((Integer) event
								.getArgument(2)));
				objectCreateEvent.setId((Integer) event.getArgument(0));
				objectCreateEvent.setOwner((Integer) event.getArgument(1));
				logic.onGameEventAppeared(objectCreateEvent);
				break;
			case OBJECT_REMOVE:
				ObjectFactoryEvent objectRemoveEvent = new ObjectFactoryEvent(
						GameEventNumber.OBJECT_REMOVE, ObjectType.NO_OBJECT);
				objectRemoveEvent.setId((Integer) event.getArgument(0));
				logic.onGameEventAppeared(objectRemoveEvent);
				break;
			case SET_ITEM_SLOT:
				logic.onGameEventAppeared(new SetItemSlotEvent((Integer) event
						.getArgument(1), (Integer) event.getArgument(0),
						(Integer) event.getArgument(2)));
				break;
			case ITEM_CD_START:
				logic.onGameEventAppeared(new ItemEvent(
						GameEventNumber.ITEM_CD_START, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case ITEM_CD_FINISHED:
				logic.onGameEventAppeared(new ItemEvent(
						GameEventNumber.ITEM_CD_FINISHED, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case DEATH:
				logic.onGameEventAppeared(new DeathEvent((Integer) event
						.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case SET_HEALTH:
				logic.onGameEventAppeared(new SetIntegerGameObjectAttributeEvent(
						GameEventNumber.SETHEALTH, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case SET_KILLS:
				logic.onGameEventAppeared(new SetIntegerGameObjectAttributeEvent(
						GameEventNumber.SET_KILLS, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case SET_DEATHS:
				logic.onGameEventAppeared(new SetIntegerGameObjectAttributeEvent(
						GameEventNumber.SET_DEATHS, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case SET_REMAININGTIME:
				logic.onGameEventAppeared(new SetRemainingTimeEvent(
						(Long) event.getArgument(0)));
				break;
			case MATCH_END:
				logic.onGameEventAppeared(new MatchEndEvent((Integer) event
						.getArgument(0)));
				break;
			case SET_TEAMS:
				SetTeamsEvent setTeamsEvent = new SetTeamsEvent();
				for (int i = 0; i < numberOfArgs; i = i + 2) {
					setTeamsEvent.addTeam(
							TeamColor.valueOf((String) event.getArgument(i)),
							(String) event.getArgument(i + 1));
				}
				logic.onGameEventAppeared(setTeamsEvent);
				break;
			case ADD_PLAYER_TO_TEAM:
				logic.onGameEventAppeared(new AddPlayerToTeamEvent(
						(Integer) event.getArgument(0), TeamColor
								.valueOf((String) event.getArgument(1))));
			case GAME_READY:
				logic.onGameEventAppeared(new GameReadyEvent());
				break;
			case INIT_INFORMATION:
				logic.onGameEventAppeared(new InitInformationEvent(ClientRole
						.valueOf((String) event.getArgument(0)), (String) event
						.getArgument(1), (Integer) event.getArgument(2)));
				break;
			case SET_GAMEMODE:
				logic.onGameEventAppeared(new SetGameModeEvent((String) event
						.getArgument(0)));
				break;
			case SET_SPEEDVECTOR:
				logic.onGameEventAppeared(new SetIntegerGameObjectAttributeEvent(
						GameEventNumber.SETSPEEDVECTOR, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			default:
				L.warning("Cannot handle event with event number "
						+ eventNumber);
				break;
			}
		} catch (TooFewArgumentsExceptions e) {
			L.warning("EventNumber: " + event.getEventNumber() + ": "
					+ e.getMessage());
		}
	}
}

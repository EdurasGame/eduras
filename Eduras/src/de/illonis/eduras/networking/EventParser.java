package de.illonis.eduras.networking;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.AddPlayerToTeamEvent;
import de.illonis.eduras.events.AreaConqueredEvent;
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
import de.illonis.eduras.events.RespawnPlayerEvent;
import de.illonis.eduras.events.SendUnitsEvent;
import de.illonis.eduras.events.SetAmmunitionEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetFloatGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetMapEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.SetPolygonDataEvent;
import de.illonis.eduras.events.SetRemainingTimeEvent;
import de.illonis.eduras.events.SetResourcesEvent;
import de.illonis.eduras.events.SetStatsEvent;
import de.illonis.eduras.events.SetTeamsEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.events.SwitchInteractModeEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * This class serves as a bridge between the network and the logic. Events'
 * arguments are read, put into GameEvents and passed to the game logic.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EventParser implements EventHandler {

	private final static Logger L = EduLog.getLoggerFor(EventParser.class
			.getName());

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

		GameEventNumber gameEventNumber = GameEventNumber
				.getByNumber(eventNumber);

		try {
			switch (gameEventNumber) {
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
						GameEventNumber.SET_SPEED, (Integer) event
								.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case SET_ROTATION:
				logic.onGameEventAppeared(new SetFloatGameObjectAttributeEvent(
						GameEventNumber.SET_ROTATION, (int) event
								.getArgument(0), (float) event.getArgument(1)));
				break;
			case ITEM_USE:
				ItemEvent itemEvent = new ItemEvent(GameEventNumber.ITEM_USE,
						(Integer) event.getArgument(1),
						(Integer) event.getArgument(0));
				float x = (float) event.getArgument(2);
				float y = (float) event.getArgument(3);
				itemEvent.setTarget(new Vector2df(x, y));
				logic.onGameEventAppeared(itemEvent);
				break;
			case SWITCH_INTERACTMODE:
				InteractMode mode = InteractMode.valueOf((String) event
						.getArgument(1));
				logic.onGameEventAppeared(new SwitchInteractModeEvent(
						(Integer) event.getArgument(0), mode));
				break;
			case SEND_UNITS:
				Vector2df target = new Vector2df((float) event.getArgument(1),
						(float) event.getArgument(2));

				LinkedList<Integer> unitIds = new LinkedList<Integer>();
				for (int i = 3; i < numberOfArgs; i++) {
					unitIds.add((Integer) event.getArgument(i));
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
				setPosEvent.setNewXPos((float) event.getArgument(1));
				setPosEvent.setNewYPos((float) event.getArgument(2));
				logic.onGameEventAppeared(setPosEvent);
				break;
			case SET_OWNER:
				logic.onGameEventAppeared(new SetOwnerEvent((Integer) event
						.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case SET_VISIBLE:
				logic.onGameEventAppeared(new SetVisibilityEvent(
						(Integer) event.getArgument(0), Visibility
								.valueOf((String) event.getArgument(1))));
				break;
			case SET_COLLIDABLE:
				logic.onGameEventAppeared(new SetBooleanGameObjectAttributeEvent(
						GameEventNumber.SET_COLLIDABLE, (Integer) event
								.getArgument(0), (Boolean) event.getArgument(1)));
				break;
			case SET_POLYGON_DATA:
				int numberOfVertices = (numberOfArgs - 1) / 2;
				Vector2f[] vertices = new Vector2f[numberOfVertices];

				for (int i = 0; i < numberOfVertices; i++) {
					Vector2f vertex = new Vector2f(
							(float) event.getArgument(2 * i + 1),
							(float) event.getArgument(2 * i + 2));
					vertices[i] = vertex;
				}
				logic.onGameEventAppeared(new SetPolygonDataEvent(
						(Integer) event.getArgument(0), vertices));
				break;
			case OBJECT_CREATE:
				ObjectFactoryEvent objectCreateEvent = new ObjectFactoryEvent(
						GameEventNumber.OBJECT_CREATE,
						ObjectType.getObjectTypeByNumber((Integer) event
								.getArgument(2)),
						(Integer) event.getArgument(1));
				objectCreateEvent.setId((Integer) event.getArgument(0));
				logic.onGameEventAppeared(objectCreateEvent);
				break;
			case OBJECT_REMOVE:
				ObjectFactoryEvent objectRemoveEvent = new ObjectFactoryEvent(
						GameEventNumber.OBJECT_REMOVE, ObjectType.NO_OBJECT, -1);
				objectRemoveEvent.setId((Integer) event.getArgument(0));
				logic.onGameEventAppeared(objectRemoveEvent);
				break;
			case SET_ITEM_SLOT:
				logic.onGameEventAppeared(new SetItemSlotEvent((Integer) event
						.getArgument(0), (Integer) event.getArgument(1),
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
						GameEventNumber.SET_HEALTH, (Integer) event
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
			case SET_STATS:
				logic.onGameEventAppeared(new SetStatsEvent(StatsProperty
						.valueOf((String) event.getArgument(0)),
						(Integer) event.getArgument(1), (Integer) event
								.getArgument(2)));
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
				for (int i = 0; i < numberOfArgs; i = i + 5) {
					setTeamsEvent.addTeam(new Team((String) event
							.getArgument(i), (int) event.getArgument(i + 1),
							new Color((int) event.getArgument(i + 2),
									(int) event.getArgument(i + 3), (int) event
											.getArgument(i + 4))));
				}
				logic.onGameEventAppeared(setTeamsEvent);
				break;
			case ADD_PLAYER_TO_TEAM:
				logic.onGameEventAppeared(new AddPlayerToTeamEvent(
						(Integer) event.getArgument(0), (int) event
								.getArgument(1)));
				break;
			case GAME_READY:
				logic.onGameEventAppeared(new GameReadyEvent());
				break;
			case INIT_INFORMATION:
				logic.onGameEventAppeared(new InitInformationEvent(ClientRole
						.valueOf((String) event.getArgument(0)), (String) event
						.getArgument(1), (Integer) event.getArgument(2)));
				break;
			case SET_AMMU:
				logic.onGameEventAppeared(new SetAmmunitionEvent((int) event
						.getArgument(0), (int) event.getArgument(1)));
				break;
			case SET_GAMEMODE:
				logic.onGameEventAppeared(new SetGameModeEvent((String) event
						.getArgument(0)));
				break;
			case SET_SPEEDVECTOR:
				MovementEvent movementEvent = new MovementEvent(
						GameEventNumber.SET_SPEEDVECTOR,
						(Integer) event.getArgument(0));
				movementEvent.setNewXPos((float) event.getArgument(1));
				movementEvent.setNewYPos((float) event.getArgument(2));
				logic.onGameEventAppeared(movementEvent);
				break;
			case SET_INTERACTMODE:
				logic.onGameEventAppeared(new SetInteractModeEvent(
						(Integer) event.getArgument(0), InteractMode
								.valueOf((String) event.getArgument(1))));
				break;
			case BASE_CONQUERED:
				logic.onGameEventAppeared(new AreaConqueredEvent(
						(Integer) event.getArgument(0), (Integer) event
								.getArgument(1)));
				break;
			case SET_MAP:
				logic.onGameEventAppeared(new SetMapEvent((String) event
						.getArgument(0)));
				break;
			case SET_RESOURCES:
				logic.onGameEventAppeared(new SetResourcesEvent((Integer) event
						.getArgument(0), (Integer) event.getArgument(1)));
				break;
			case RESPAWN_PLAYER:
				logic.onGameEventAppeared(new RespawnPlayerEvent(
						(Integer) event.getArgument(0), (Integer) event
								.getArgument(1), (Integer) event.getArgument(2)));
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

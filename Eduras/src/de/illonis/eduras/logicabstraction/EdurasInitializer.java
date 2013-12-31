package de.illonis.eduras.logicabstraction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.ClientInterface;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.ClientGameMode;
import de.illonis.eduras.EdurasServer;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.ai.movement.UnitNotControllableException;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetDoubleGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logic.ClientLogic;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.Settings;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;
import de.illonis.eduras.units.Unit;

/**
 * This class provides a main entry point to the game logic and network for GUI
 * developers. This is a singleton class.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EdurasInitializer {

	private final NetworkManager networkManager;
	private final EventSender eventSender;
	private final InformationProvider informationProvider;
	private final Settings settings;
	private final GameLogicInterface logic;
	private static EdurasInitializer instance;

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	private EdurasInitializer() {
		instance = this;
		GameInformation game = new GameInformation();

		// needed because all the game game mode action shall be performed on
		// the server.
		game.getGameSettings().changeGameMode(new ClientGameMode());

		logic = new ClientLogic(game);

		game.setEventTriggerer(new EventTriggerer() {

			@Override
			public void createMissile(ObjectType missileType, int owner,
					Vector2D position, Vector2D speedVector) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeObject(int objectId) {
				// TODO Auto-generated method stub

			}

			@Override
			public void createDynamicPolygonAt(Vector2D[] polygonVertices,
					Vector2D position, int owner) {
				// TODO Auto-generated method stub

			}

			@Override
			public int createObjectAt(ObjectType object, Vector2D position,
					int owner) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setPolygonData(int objectId, Vector2D[] polygonVertices) {
				// TODO Auto-generated method stub

			}

			@Override
			public int createObject(ObjectType object, int owner) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void lootItem(int objectId, int playerId) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changeItemSlot(int slot, int player, Item newItem) {
				// TODO Auto-generated method stub

			}

			@Override
			public void maybeSetPositionOfObject(int objectId,
					Vector2D newPosition) {
				// TODO Auto-generated method stub

			}

			@Override
			public void guaranteeSetPositionOfObject(int objectId,
					Vector2D newPosition) {
				// TODO Auto-generated method stub

			}

			@Override
			public void init() {
				// TODO Auto-generated method stub

			}

			@Override
			public void setHealth(int id, int newHealth) {
				// TODO Auto-generated method stub

			}

			@Override
			public void respawnPlayer(PlayerMainFigure player) {
				// TODO Auto-generated method stub

			}

			@Override
			public void renamePlayer(int ownerId, String newName) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMatchEnd() {
				// TODO Auto-generated method stub

			}

			@Override
			public void restartRound() {
				// TODO Auto-generated method stub

			}

			@Override
			public void setRemainingTime(long remainingTime) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changeMap(Map map) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeath(Unit unit, int killer) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changeGameMode(GameMode newMode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void remaxHealth(Unit unit) {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendUnit(int objectId, Vector2D target)
					throws ObjectNotFoundException,
					UnitNotControllableException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTeams(Team... teams) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addPlayerToTeam(int ownerId, Team team) {
				// TODO Auto-generated method stub

			}

			@Override
			public void kickPlayer(int ownerId) {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendRequestedInfos(ArrayList<GameEvent> infos, int owner) {
				// TODO Auto-generated method stub

			}

			@Override
			public void notifyCooldownStarted(ItemEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void notifyGameObjectStateChanged(
					SetGameObjectAttributeEvent<?> event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void notifyObjectCreated(ObjectFactoryEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void notifyNewObjectPosition(GameObject o) {
				// TODO Auto-generated method stub

			}

			@Override
			public void changeInteractMode(int ownerId, InteractMode newMode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setRotation(GameObject gameObject) {
				// every client only wants to advertise its own rotation
				// TODO: Think about that... do you really wanna advertise you
				// units' rotation? Isn't that managed by the server? Maybe you
				// should check for your playermainfigures objectid.
				if (gameObject.getOwner() != networkManager.getClientId()) {
					return;
				}

				SetDoubleGameObjectAttributeEvent setRotationEvent = new SetDoubleGameObjectAttributeEvent(
						GameEventNumber.SET_ROTATION, gameObject.getId(),
						gameObject.getRotation());
				try {
					eventSender.sendEvent(setRotationEvent);
				} catch (WrongEventTypeException | MessageNotSupportedException e) {
					L.log(Level.SEVERE, "Cannot send setRotationEvent.", e);
				}
			}

		});

		networkManager = new NetworkManager(logic);

		ClientInterface client = networkManager.getClient();

		settings = new Settings();

		eventSender = new EventSender(client, logic);

		informationProvider = new InformationProvider(logic, networkManager);

	}

	/**
	 * Returns the {@link NetworkManager}.
	 * 
	 * @return The NetworkManager
	 */
	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	/**
	 * Returns the unique {@link EdurasInitializer} instance.
	 * 
	 * @return The EdurasInitializer instance
	 */
	public static EdurasInitializer getInstance() {
		if (instance == null) {
			return new EdurasInitializer();
		}
		return instance;
	}

	/**
	 * Returns the {@link EventSender}.
	 * 
	 * @return The EventSender
	 */
	public EventSender getEventSender() {
		return eventSender;
	}

	/**
	 * Returns the {@link InformationProvider}
	 * 
	 * @return The InformationProvider
	 */
	public InformationProvider getInformationProvider() {
		return informationProvider;
	}

	/**
	 * Returns the current {@link Settings} of the current game.
	 * 
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Starts the logic game worker.
	 */
	public void startLogicWorker() {
		logic.startWorker();
	}

	/**
	 * Stops the logic game worker.
	 */
	public void stopLogicWorker() {
		logic.stopWorker();
	}
}

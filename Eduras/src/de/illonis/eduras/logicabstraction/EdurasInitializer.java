package de.illonis.eduras.logicabstraction;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.eduras.eventingserver.ClientInterface;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.ClientGameMode;
import de.illonis.eduras.EdurasServer;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.ai.movement.UnitNotControllableException;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetFloatGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logic.ClientLogic;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.logic.LogicGameWorker;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2df;
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

			Vector2df position, Vector2df speedVector) {

			}

			@Override
			public void removeObject(int objectId) {
			}

			@Override
			public int createObjectAt(ObjectType object, Vector2df position,
					int owner) {
				return -1;
			}

			@Override
			public void setPolygonData(int objectId,
					Vector2df[] polygonVector2dfs) {
			}

			@Override
			public int createObject(ObjectType object, int owner) {
				return -1;
			}

			@Override
			public void lootItem(int objectId, int playerId) {
			}

			@Override
			public void changeItemSlot(int slot, int player, Item newItem) {
			}

			@Override
			public void maybeSetPositionOfObject(int objectId,
					Vector2df newPosition) {
			}

			@Override
			public void guaranteeSetPositionOfObject(int objectId,
					Vector2df newPosition) {
			}

			@Override
			public void init() {
			}

			@Override
			public void setHealth(int id, int newHealth) {
			}

			@Override
			public void respawnPlayer(PlayerMainFigure player) {
			}

			@Override
			public void renamePlayer(int ownerId, String newName) {
			}

			@Override
			public void onMatchEnd() {
			}

			@Override
			public void restartRound() {
			}

			@Override
			public void setRemainingTime(long remainingTime) {
			}

			@Override
			public void changeMap(Map map) {
			}

			@Override
			public void onDeath(Unit unit, int killer) {
			}

			@Override
			public void changeGameMode(GameMode newMode) {
			}

			@Override
			public void remaxHealth(Unit unit) {
			}

			@Override
			public void sendUnit(int objectId, Vector2f target)
					throws ObjectNotFoundException,
					UnitNotControllableException {
			}

			@Override
			public void setTeams(Collection<Team> teams) {
			}

			@Override
			public void addPlayerToTeam(int ownerId, Team team) {
			}

			@Override
			public void kickPlayer(int ownerId) {
			}

			@Override
			public void sendRequestedInfos(ArrayList<GameEvent> infos, int owner) {
			}

			@Override
			public void notifyCooldownStarted(ItemEvent event) {
			}

			@Override
			public void notifyGameObjectStateChanged(
					SetGameObjectAttributeEvent<?> event) {
			}

			@Override
			public void notifyObjectCreated(ObjectFactoryEvent event) {
			}

			@Override
			public void notifyNewObjectPosition(GameObject o) {
			}

			@Override
			public void changeInteractMode(int ownerId, InteractMode newMode) {
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

				SetFloatGameObjectAttributeEvent setRotationEvent = new SetFloatGameObjectAttributeEvent(
						GameEventNumber.SET_ROTATION, gameObject.getId(),
						gameObject.getRotation());
				try {
					eventSender.sendEvent(setRotationEvent);
				} catch (WrongEventTypeException | MessageNotSupportedException e) {
					L.log(Level.SEVERE, "Cannot send setRotationEvent.", e);
				}
			}

			@Override
			public void removePlayer(int ownerId) {
			}

			@Override
			public void setVisibility(int objectId, Visibility newVal) {
			}

			@Override
			public void setCollidability(int objectId, boolean newVal) {
			}

			@Override
			public void createDynamicPolygonObjectAt(ObjectType type,
					Vector2df[] polygonVector2dfs, Vector2df position, int owner) {
			}

			@Override
			public void setStats(StatsProperty property, int ownerId,
					int valueToSet) {
			}

			@Override
			public void changeStatOfPlayerByAmount(StatsProperty prop,
					PlayerMainFigure player, int i) {
			}

			@Override
			public void notifyAreaConquered(NeutralArea neutralBase,
					Team occupyingTeam) {
			}

			@Override
			public void notifyGameObjectVisibilityChanged(
					SetVisibilityEvent event) {
			}

			@Override
			public void changeResourcesOfTeamByAmount(Team team, int amount) {
				// TODO Auto-generated method stub

			}

		});

		networkManager = new NetworkManager(logic);

		ClientInterface client = networkManager.getClient();

		settings = new Settings();
		try {
			settings.load();
		} catch (FileNotFoundException e) {
			L.log(Level.WARNING, "Could not load user preferences.", e);
		}

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
	 * 
	 * @return the worker.
	 */
	public LogicGameWorker startLogicWorker() {
		return logic.startWorker();
	}

	/**
	 * Stops the logic game worker.
	 */
	public void stopLogicWorker() {
		logic.stopWorker();
	}
}

package de.illonis.eduras.logicabstraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ClientGameMode;
import de.illonis.eduras.EdurasServer;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
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
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.gameobjects.NeutralArea;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logic.ClientLogic;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.logic.LogicGameWorker;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.networking.EventParser;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * This class provides a main entry point to the game logic and network for GUI
 * developers. This is a singleton class.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EdurasInitializer {

	final NetworkManager networkManager;
	final EventSender eventSender;
	final InformationProvider informationProvider;
	private final Settings settings;
	GameLogicInterface logic;
	private static EdurasInitializer instance;

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	private EdurasInitializer() {
		instance = this;

		networkManager = new NetworkManager(this);
		settings = new Settings();
		try {

			settings.load();
		} catch (FileNotFoundException e) {
			L.log(Level.WARNING, "Could not load user preferences.", e);
		}
		eventSender = new EventSender(this);
		informationProvider = new InformationProvider(this);

		initGame();

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
	 * @param useInternal
	 *            if using a inbuild thread.
	 * 
	 * @return the worker.
	 */
	public LogicGameWorker startLogicWorker(boolean useInternal) {
		return logic.startWorker(useInternal);
	}

	/**
	 * @return the logic.
	 * 
	 */
	public GameLogicInterface getLogic() {
		return logic;
	}

	public void initGame() {
		GameInformation game = new GameInformation();

		// needed because all the game game mode action shall be performed on
		// the server.
		game.getGameSettings().changeGameMode(new ClientGameMode());

		logic = new ClientLogic(game);

		game.setEventTriggerer(new EventTriggerer() {

			@Override
			public void createMissile(ObjectType missileType, int owner,
					Vector2f position, Vector2f speedVector) {

			}

			@Override
			public void removeObject(int objectId) {
			}

			@Override
			public int createObjectAt(ObjectType object, Vector2f position,
					int owner) {
				return -1;
			}

			@Override
			public void setPolygonData(int objectId, Vector2f[] polygonVector2fs) {
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
			public void respawnPlayerAtRandomSpawnpoint(Player player) {
			}

			@Override
			public void renamePlayer(int ownerId, String newName) {
			}

			@Override
			public void onMatchEnd(int winner) {
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
			public void notifyDeath(Unit unit, int killer) {
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
			public int createDynamicPolygonObjectAt(ObjectType type,
					Vector2f[] polygonVector2fs, Vector2f position, int owner) {
				return -1;
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
			}

			@Override
			public void respawnPlayerAtPosition(Player player, Vector2df pos) {
			}

			@Override
			public void changeHealthByAmount(Unit unitToHeal,
					int spell_heal_amount) {
			}

			@Override
			public void clearInventoryOfPlayer(Player player) {
			}

			@Override
			public void onPlayerJoined(Player newPlayer) {
			}

			@Override
			public void notifyPlayerJoined(int ownerId) {
			}

			@Override
			public void notifyPlayerLeft(int ownerId) {
			}

			@Override
			public void notifyGameReady(int clientId) {
			}

			@Override
			public void loadSettings(File settingsFile) {
			}

			@Override
			public void notifyCooldownFinished(int idOfItem) {
			}

			@Override
			public void giveNewItem(Player player, ObjectType itemType)
					throws WrongObjectTypeException {
			}

			@Override
			public int createObjectWithCenterAt(ObjectType object,
					Vector2f position, int owner) {
				return -1;
			}

			@Override
			public void notifyWeaponAmmoEmpty(int clientId, int slotNum) {
			}

			@Override
			public int createObjectIn(ObjectType object, Shape shape, int owner) {
				return -1;
			}

			@Override
			public void setSetting(String settingName, String settingValue) {
			}

			@Override
			public void notififyRespawnTime(long respawnTime) {
			}

			@Override
			public int createObjectAtBase(ObjectType object, Base base,
					int owner) {
				return -1;
			}

			@Override
			public int respawnPlayerAtBase(Player player, Base base) {
				return -1;
			}

			@Override
			public void changeSpeedBy(MoveableGameObject objectToChangeSpeedOf,
					float amount) {
			}

			@Override
			public void setSpeed(MoveableGameObject object, float newValue) {
			}

			@Override
			public void speedUpObjectForSomeTime(
					MoveableGameObject objectToSpeedUp, long timeInMiliseconds,
					float speedUpValue) {
			}

			@Override
			public void makeInvisibleForSomeTime(
					GameObject objectToMakeInvisible, long timeInMiliseconds) {
			}

		});

		networkManager.getClient().setEventHandler(new EventParser(logic));
	}
}

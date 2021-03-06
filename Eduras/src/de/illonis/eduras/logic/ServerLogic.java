package de.illonis.eduras.logic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory;
import de.illonis.eduras.Player;
import de.illonis.eduras.Spectator;
import de.illonis.eduras.Team;
import de.illonis.eduras.actions.BlinkSpellAction;
import de.illonis.eduras.actions.CreateUnitAction;
import de.illonis.eduras.actions.HealSpellAction;
import de.illonis.eduras.actions.InvisibilitySpellAction;
import de.illonis.eduras.actions.RespawnPlayerAction;
import de.illonis.eduras.actions.ScoutSpellAction;
import de.illonis.eduras.actions.SpawnItemAction;
import de.illonis.eduras.actions.SpeedSpellAction;
import de.illonis.eduras.ai.movement.UnitNotControllableException;
import de.illonis.eduras.events.BlinkEvent;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.CreateUnitEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.PlayerAndTeamEvent;
import de.illonis.eduras.events.RequestResourceEvent;
import de.illonis.eduras.events.ResurrectPlayerEvent;
import de.illonis.eduras.events.ScoutSpellEvent;
import de.illonis.eduras.events.SendUnitsEvent;
import de.illonis.eduras.events.SetFloatGameObjectAttributeEvent;
import de.illonis.eduras.events.SpawnItemEvent;
import de.illonis.eduras.events.SwitchInteractModeEvent;
import de.illonis.eduras.events.UnitSpellActionEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.InsufficientResourceException;
import de.illonis.eduras.exceptions.InvalidNameException;
import de.illonis.eduras.exceptions.NoSpawnAvailableException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.inventory.NoSuchItemException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.maps.persistence.MapParser;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.ResourceManager.ResourceType;

/**
 * Server logic.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerLogic implements GameLogicInterface {
	private final static Logger L = EduLog.getLoggerFor(ServerLogic.class
			.getName());

	private final GameInformation gameInfo;
	private final ObjectFactory objectFactory;
	private LogicGameWorker lgw;
	private final ListenerHolder<GameEventListener> listenerHolder;

	/**
	 * Creates a new logic.
	 * 
	 * @param g
	 *            information to use.
	 */
	public ServerLogic(GameInformation g) {
		listenerHolder = new ListenerHolder<GameEventListener>();
		this.gameInfo = g;
		startWorker(true);
		objectFactory = new ObjectFactory(this, lgw);
	}

	@Override
	public synchronized void onGameEventAppeared(GameEvent event) {
		L.fine(Localization.getStringF("Client.networking.event", event
				.getType().toString()));

		switch (event.getType()) {
		case MOVE_DOWN_PRESSED:
		case MOVE_DOWN_RELEASED:
		case MOVE_UP_PRESSED:
		case MOVE_UP_RELEASED:
		case MOVE_LEFT_PRESSED:
		case MOVE_LEFT_RELEASED:
		case MOVE_RIGHT_PRESSED:
		case MOVE_RIGHT_RELEASED:
			handlePlayerMove((UserMovementEvent) event);
			break;

		case INFORMATION_REQUEST:
			ArrayList<GameEvent> infos = gameInfo.getAllInfosAsEvent();

			gameInfo.getEventTriggerer().sendRequestedInfos(infos,
					((GameInfoRequest) event).getRequester());

			break;
		case CLIENT_SETNAME:
			ClientRenameEvent e = (ClientRenameEvent) event;
			Player p = null;
			try {
				p = gameInfo.getPlayerByOwnerId(e.getOwner());
			} catch (ObjectNotFoundException e1) {
				L.warning("There is no such player with the id"
						+ e1.getObjectId() + "(yet)!");
				return;
			}

			L.info("SETTING player found by owner " + e.getOwner()
					+ " to name: " + e.getName() + " playerowner="
					+ p.getPlayerId());
			p.setName(e.getName());

			getGame().getEventTriggerer().renamePlayer(e.getOwner(),
					e.getName());

			break;
		case SEND_UNITS:
			SendUnitsEvent sendEvent = (SendUnitsEvent) event;
			Vector2f target = sendEvent.getTarget();
			LinkedList<Integer> units = sendEvent.getUnits();
			for (int i = 0; i < units.size(); i++) {
				try {
					getGame().getEventTriggerer()
							.sendUnit(units.get(i), target);
				} catch (ObjectNotFoundException | UnitNotControllableException e1) {
					L.log(Level.SEVERE, "Error sending unit " + units.get(i)
							+ " to position " + target, e1);
				}
			}
			break;
		case SWITCH_INTERACTMODE:
			SwitchInteractModeEvent switchEvent = (SwitchInteractModeEvent) event;
			Player mf;
			try {
				mf = getGame().getPlayerByOwnerId(switchEvent.getOwner());
			} catch (ObjectNotFoundException e1) {
				L.log(Level.SEVERE,
						"Got mode switch request from nonexisting player.", e1);
				break;
			}

			boolean switchAllowed = getGame().getGameSettings().getGameMode()
					.canSwitchMode(mf, switchEvent.getRequestedMode());

			if (switchAllowed
					&& mf.getCurrentMode() != switchEvent.getRequestedMode()) {
				mf.getPlayerMainFigure().stopMoving();
				mf.setMode(switchEvent.getRequestedMode());
				getGame().getEventTriggerer().changeInteractMode(
						switchEvent.getOwner(), switchEvent.getRequestedMode());
			} else {
				L.info("Got an switch request but player is already in that mode or switching is not ready.");
			}
			break;
		case ITEM_USE:
			ItemEvent itemEvent = (ItemEvent) event;
			handleItemEvent(itemEvent);
			break;
		case INIT_INFORMATION:
			InitInformationEvent initInfoEvent = (InitInformationEvent) event;

			// transfer information to client
			GameInfoRequest gameInfos = new GameInfoRequest(
					initInfoEvent.getClientId());
			onGameEventAppeared(gameInfos);

			// extract role and name
			if (initInfoEvent.getRole() == ClientRole.PLAYER) {
				getGame().getGameSettings().getGameMode()
						.onConnect(initInfoEvent.getClientId());

				String playerName = initInfoEvent.getName();
				try {
					onGameEventAppeared(new ClientRenameEvent(
							initInfoEvent.getClientId(), playerName));
				} catch (InvalidNameException e1) {
					L.log(Level.SEVERE, "invalid client name", e1);
				}
				getGame().getEventTriggerer().notifyPlayerJoined(
						initInfoEvent.getClientId());
			} else {
				Spectator spectator = new Spectator(
						initInfoEvent.getClientId(), initInfoEvent.getName());
				gameInfo.addSpectator(spectator);
			}

			getGame().getEventTriggerer().notifyGameReady(
					initInfoEvent.getClientId());
			break;
		case SET_ROTATION:
			if (!(event instanceof SetFloatGameObjectAttributeEvent)) {
				break;
			}
			SetFloatGameObjectAttributeEvent setRotationEvent = (SetFloatGameObjectAttributeEvent) event;
			GameObject gameObject;
			try {
				gameObject = gameInfo.findObjectById(setRotationEvent
						.getObjectId());
			} catch (ObjectNotFoundException e2) {
				L.log(Level.FINE,
						"Cannot find object! Probably because it was removed before and this event was late because of UDP",
						e2);
				break;
			}

			gameObject.setRotation(setRotationEvent.getNewValue());
			break;
		case RESURRECT_PLAYER:
			ResurrectPlayerEvent respawnPlayerEvent = (ResurrectPlayerEvent) event;
			try {
				Player executingPlayer = gameInfo
						.getPlayerByOwnerId(respawnPlayerEvent
								.getExecutingPlayer());
				Player playerToRespawn = gameInfo
						.getPlayerByOwnerId(respawnPlayerEvent
								.getIdOfPlayerToRespawn());
				Base baseToRespawnAt = (Base) gameInfo
						.findObjectById(respawnPlayerEvent
								.getIdOfBaseToRespawnAt());

				RespawnPlayerAction respawnPlayerAction = new RespawnPlayerAction(
						executingPlayer, playerToRespawn, baseToRespawnAt);
				respawnPlayerAction.execute(gameInfo);
			} catch (ObjectNotFoundException e1) {
				L.log(Level.WARNING, "Couldn't find player!", e1);
				return;
			}
			break;
		case SPELL_SCOUT: {
			ScoutSpellEvent scoutEvent = (ScoutSpellEvent) event;
			Player player;
			try {
				player = gameInfo.getPlayerByOwnerId(scoutEvent.getOwner());
				ScoutSpellAction scoutAction = new ScoutSpellAction(player,
						scoutEvent.getTarget());
				scoutAction.execute(gameInfo);
			} catch (ObjectNotFoundException e1) {
				L.log(Level.WARNING,
						"Something went wrong casting scout spell.", e1);
				return;
			}
			break;
		}
		case SPAWN_ITEM: {
			SpawnItemEvent spawnItemEvent = (SpawnItemEvent) event;

			Player executingPlayer;
			try {
				executingPlayer = gameInfo.getPlayerByOwnerId(spawnItemEvent
						.getExecutingPlayer());
				SpawnItemAction spawnItemAction = new SpawnItemAction(
						executingPlayer, spawnItemEvent.getObjectType(),
						spawnItemEvent.getPosition());
				spawnItemAction.execute(gameInfo);
			} catch (ObjectNotFoundException | WrongObjectTypeException e1) {
				L.log(Level.WARNING, "Something went wrong spawning an item.",
						e1);
				return;
			}
			break;
		}
		case HEAL_ACTION: {
			UnitSpellActionEvent healEvent = (UnitSpellActionEvent) event;

			Player executingPlayer;
			try {
				executingPlayer = gameInfo.getPlayerByOwnerId(healEvent
						.getExecutingPlayer());
				HealSpellAction healSpellAction = new HealSpellAction(
						executingPlayer,
						(Unit) gameInfo.findObjectById(healEvent
								.getIdOfUnitToCastSpellOn()));
				healSpellAction.execute(gameInfo);
			} catch (ObjectNotFoundException ex) {
				L.log(Level.WARNING,
						"Cannot find player when receiving heal action.", ex);
				break;
			}
			break;
		}
		case SPEED_SPELL: {
			UnitSpellActionEvent speedEvent = (UnitSpellActionEvent) event;

			Player executingPlayer;
			try {
				executingPlayer = gameInfo.getPlayerByOwnerId(speedEvent
						.getExecutingPlayer());
				SpeedSpellAction speedSpellAction = new SpeedSpellAction(
						executingPlayer,
						(Unit) gameInfo.findObjectById(speedEvent
								.getIdOfUnitToCastSpellOn()));
				speedSpellAction.execute(gameInfo);
			} catch (ObjectNotFoundException ex) {
				L.log(Level.WARNING,
						"Cannot find player when receiving heal action.", ex);
				break;
			}
			break;
		}
		case INVISIBILITY_SPELL: {
			UnitSpellActionEvent invisibilityEvent = (UnitSpellActionEvent) event;

			Player executingPlayer;
			try {
				executingPlayer = gameInfo.getPlayerByOwnerId(invisibilityEvent
						.getExecutingPlayer());
				InvisibilitySpellAction invisibilitySpellAction = new InvisibilitySpellAction(
						executingPlayer,
						(Unit) gameInfo.findObjectById(invisibilityEvent
								.getIdOfUnitToCastSpellOn()));
				invisibilitySpellAction.execute(gameInfo);
			} catch (ObjectNotFoundException ex) {
				L.log(Level.WARNING,
						"Cannot find player when receiving heal action.", ex);
				break;
			}
			break;
		}
		case BLINK_SPELL: {
			UnitSpellActionEvent blinkSpellEvent = (UnitSpellActionEvent) event;

			Player executingPlayer;
			try {
				executingPlayer = gameInfo.getPlayerByOwnerId(blinkSpellEvent
						.getExecutingPlayer());
				BlinkSpellAction blinkSpellAction = new BlinkSpellAction(
						executingPlayer,
						(PlayerMainFigure) gameInfo
								.findObjectById(blinkSpellEvent
										.getIdOfUnitToCastSpellOn()));
				blinkSpellAction.execute(gameInfo);
			} catch (ObjectNotFoundException ex) {
				L.log(Level.WARNING,
						"Cannot find player when receiving heal action.", ex);
				break;
			}
			break;
		}
		case BLINK: {
			BlinkEvent blinkEvent = (BlinkEvent) event;
			try {
				Player blinkingPlayer = gameInfo.getPlayerByOwnerId(blinkEvent
						.getOwner());
				if (blinkingPlayer.getBlinksAvailable() < 1
						|| blinkingPlayer.isDead()
						|| blinkingPlayer.getBlinkCooldown() > 0) {
					L.log(Level.WARNING,
							"Received blink event although there is no blink available or the player is dead or there is still cooldown on blink.");
					break;
				}
				PlayerMainFigure blinkingMainFigure = blinkingPlayer
						.getPlayerMainFigure();

				gameInfo.getEventTriggerer().changeBlinkChargesBy(
						blinkingPlayer, -1);
				blinkingPlayer.useBlink();

				// check if the player tried to blink too far
				try {
					Vector2f actualBlinkTarget = gameInfo
							.findActualTargetForDesiredBlinkTarget(
									blinkingMainFigure,
									blinkEvent.getBlinkTarget());

					// set player to the target
					synchronized (blinkingMainFigure) {
						gameInfo.getEventTriggerer()
								.guaranteeSetPositionOfObjectAtCenter(
										blinkingPlayer.getPlayerMainFigure()
												.getId(), actualBlinkTarget);
						gameInfo.getEventTriggerer().notifyBlinkUsed(
								blinkingPlayer.getPlayerId());
					}
				} catch (NoSpawnAvailableException e1) {
					// TODO: Send a notification to the client, since it didn't
					// figure it out itself that there is no spawn available
					System.out.println("Cannot spawn here.");
				}

			} catch (ObjectNotFoundException e1) {
				L.log(Level.SEVERE,
						"Player not found when receiving blinking event!", e1);
				break;
			}
			break;
		}
		case CREATE_UNIT: {
			CreateUnitEvent createUnitEvent = (CreateUnitEvent) event;

			Player executingPlayer;
			try {
				executingPlayer = gameInfo.getPlayerByOwnerId(createUnitEvent
						.getExecutingPlayer());

				Base baseToCreateAt = (Base) gameInfo
						.findObjectById(createUnitEvent.getIdOfBaseToSpawnAt());

				CreateUnitAction createUnitAction = new CreateUnitAction(
						executingPlayer, createUnitEvent.getTypeOfUnit(),
						baseToCreateAt);
				createUnitAction.execute(gameInfo);
			} catch (ObjectNotFoundException ex) {
				L.log(Level.WARNING, "Cannot find player when creating unit.",
						ex);
				break;
			} catch (WrongObjectTypeException e1) {
				L.log(Level.WARNING, "Trying to spawn a non-unit type!", e1);
				break;
			}
			break;
		}
		case REQUEST_MAP:
			RequestResourceEvent requestResourceEvent = (RequestResourceEvent) event;
			Path file = ResourceManager.resourceToPath(ResourceType.MAP,
					requestResourceEvent.getResourceName()
							+ MapParser.FILE_EXTENSION);
			gameInfo.getEventTriggerer().sendResource(GameEventNumber.SEND_MAP,
					requestResourceEvent.getOwner(),
					requestResourceEvent.getResourceName(), file);
			break;
		case JOIN_TEAM:
			PlayerAndTeamEvent playerAndTeamEvent = (PlayerAndTeamEvent) event;
			Team team = gameInfo.findTeamById(playerAndTeamEvent.getTeam());

			if (team == null) {
				L.severe("Cannot find given team.");
				return;
			}

			gameInfo.getEventTriggerer().addPlayerToTeam(
					playerAndTeamEvent.getOwner(), team);

			Player player;
			try {
				player = gameInfo.getPlayerByOwnerId(playerAndTeamEvent
						.getOwner());
			} catch (ObjectNotFoundException e1) {
				L.log(Level.SEVERE, "Cant find player.", e1);
				break;
			}

			if (!player.isDead()) {
				gameInfo.getEventTriggerer().respawnPlayerAtRandomSpawnpoint(
						player);
			}
			break;
		default:
			L.severe(Localization.getStringF("Server.networking.illegalevent",
					event.getClass()));
			System.exit(1);
			break;
		}
	}

	/**
	 * Handle an item event.
	 * 
	 * @param itemEvent
	 *            event to handle.
	 */
	private void handleItemEvent(ItemEvent itemEvent) {
		Player player;

		try {
			player = gameInfo.getPlayerByOwnerId(itemEvent.getOwner());
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
			return;
		}

		Item item;

		try {
			item = player.getInventory().getItemOfType(itemEvent.getItemType());
		} catch (NoSuchItemException e) {
			L.log(Level.WARNING,
					"Could not handle item event " + itemEvent.getType()
							+ " because no item of type "
							+ itemEvent.getItemType()
							+ " is in inventory of owner "
							+ itemEvent.getOwner(), e);
			return;
		}
		ItemUseInformation useInfo = new ItemUseInformation(
				player.getPlayerMainFigure(), itemEvent.getTarget());

		switch (itemEvent.getType()) {
		case ITEM_USE:
			if (item.isUsable() && !((Usable) item).hasCooldown()) {
				try {
					if (((Usable) item).use(useInfo)) {
						ItemEvent cooldownEvent = new ItemEvent(
								GameEventNumber.ITEM_CD_START,
								itemEvent.getOwner(), itemEvent.getItemType());

						gameInfo.getEventTriggerer().notifyCooldownStarted(
								cooldownEvent);
					}
				} catch (InsufficientResourceException e) {
					getGame().getEventTriggerer().notifyWeaponAmmoEmpty(
							itemEvent.getOwner(), itemEvent.getItemType());
				}
			}
			break;
		default:
			break;
		}

	}

	/**
	 * Handles a player move. These events are only received on serverside.
	 * 
	 * @author illonis
	 * @param event
	 *            event.
	 */
	private void handlePlayerMove(UserMovementEvent event) {

		PlayerMainFigure player = null;
		try {
			player = gameInfo.getPlayerByOwnerId(event.getOwner())
					.getPlayerMainFigure();
		} catch (ObjectNotFoundException e) {
			L.warning(Localization.getStringF("Server.logic.playernotfound",
					e.getObjectId()));
			return;
		}

		switch (event.getType()) {
		case MOVE_DOWN_PRESSED:
			player.startMoving(Direction.BOTTOM);
			break;
		case MOVE_DOWN_RELEASED:
			player.stopMoving(Direction.BOTTOM);
			break;
		case MOVE_UP_PRESSED:
			player.startMoving(Direction.TOP);
			break;
		case MOVE_UP_RELEASED:
			player.stopMoving(Direction.TOP);
			break;
		case MOVE_LEFT_PRESSED:
			player.startMoving(Direction.LEFT);
			break;
		case MOVE_LEFT_RELEASED:
			player.stopMoving(Direction.LEFT);
			break;
		case MOVE_RIGHT_PRESSED:
			player.startMoving(Direction.RIGHT);
			break;
		case MOVE_RIGHT_RELEASED:
			player.stopMoving(Direction.RIGHT);
			break;
		default:
			break;
		}
	}

	@Override
	public void setGameEventListener(GameEventListener listener) {
		listenerHolder.setListener(listener);
	}

	@Override
	public GameInformation getGame() {
		return gameInfo;
	}

	@Override
	public GameEventListener getListener() {
		return listenerHolder.getListener();
	}

	@Override
	public void stopWorker() {
		lgw.stop();
	}

	@Override
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	@Override
	public LogicGameWorker startWorker(boolean useInternal) {
		lgw = new ServerLogicGameWorker(gameInfo, listenerHolder);
		if (useInternal) {
			Thread gameWorker = new Thread(lgw);
			gameWorker.setName("ServerLogicGameWorker");
			gameWorker.start();
		}
		return lgw;
	}

}

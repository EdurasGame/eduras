package de.illonis.eduras.gameclient;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.events.BlinkEvent;
import de.illonis.eduras.events.CreateUnitEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.PlayerAndTeamEvent;
import de.illonis.eduras.events.ResurrectPlayerEvent;
import de.illonis.eduras.events.ScoutSpellEvent;
import de.illonis.eduras.events.SendUnitsEvent;
import de.illonis.eduras.events.SpawnItemEvent;
import de.illonis.eduras.events.SwitchInteractModeEvent;
import de.illonis.eduras.events.UnitSpellActionEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.exceptions.InsufficientResourceException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Handles events from login panels and gui and performs the appropriate action.
 * 
 * @author illonis
 * 
 */
public class GuiInternalEventListener implements GamePanelReactor {

	private final static Logger L = EduLog
			.getLoggerFor(GuiInternalEventListener.class.getName());
	private final GameClient client;
	private final InformationProvider infoPro;

	/**
	 * @param client
	 *            the client that is managed.
	 */
	public GuiInternalEventListener(GameClient client) {
		this.client = client;
		infoPro = EdurasInitializer.getInstance().getInformationProvider();
	}

	@Override
	public void onItemUse(ObjectType type, Vector2f target) {
		ItemEvent event = new ItemEvent(GameEventNumber.ITEM_USE,
				client.getOwnerID(), type);
		event.setTarget(target);
		try {
			client.sendEvent(event);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.WARNING, "error sending item used event for type "
					+ type, e);
		}
	}

	@Override
	public void onStartMovement(Direction direction) {
		UserMovementEvent moveEvent;
		switch (direction) {
		case TOP:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_PRESSED,
					client.getOwnerID());
			break;
		case LEFT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_PRESSED, client.getOwnerID());
			break;
		case RIGHT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_PRESSED, client.getOwnerID());
			break;
		case BOTTOM:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_PRESSED, client.getOwnerID());
			break;
		default:
			return;
		}
		try {
			client.sendEvent(moveEvent);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending move start event for direction "
					+ direction, e);
		}
	}

	@Override
	public void onStopMovement(Direction direction) {
		UserMovementEvent moveEvent;
		switch (direction) {
		case TOP:
			moveEvent = new UserMovementEvent(GameEventNumber.MOVE_UP_RELEASED,
					client.getOwnerID());
			break;
		case BOTTOM:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_DOWN_RELEASED, client.getOwnerID());
			break;
		case LEFT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_LEFT_RELEASED, client.getOwnerID());
			break;
		case RIGHT:
			moveEvent = new UserMovementEvent(
					GameEventNumber.MOVE_RIGHT_RELEASED, client.getOwnerID());
			break;
		default:
			return;
		}
		try {
			client.sendEvent(moveEvent);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending move stop event for direction "
					+ direction, e);
		}

	}

	@Override
	public void onGameQuit() {
		client.tryExit();
	}

	@Override
	public void onModeSwitch() throws NotWithinBaseException {
		try {
			InteractMode nextMode = infoPro.getPlayer().getCurrentMode().next();

			if (infoPro.getGameMode().canSwitchMode(infoPro.getPlayer(),
					nextMode)) {

				client.sendEvent(new SwitchInteractModeEvent(client
						.getOwnerID(), nextMode));
			} else {
				throw new NotWithinBaseException();
			}
		} catch (WrongEventTypeException | MessageNotSupportedException
				| ObjectNotFoundException e) {
			L.log(Level.SEVERE, "mode switch request could not be sent.", e);
		}
	}

	@Override
	public void onUnitsSelected(Rectangle2D.Double area) {
		// FIXME: Use slicks rectangle here
		Rectangle r = new Rectangle((float) area.getX(), (float) area.getY(),
				(float) area.getWidth(), (float) area.getHeight());

		PlayerMainFigure p;
		try {
			p = infoPro.getPlayer().getPlayerMainFigure();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE,
					"No playermainfigure found after units were selected.", e);
			return;
		}

		LinkedList<Integer> ids = new LinkedList<Integer>();
		for (Entry<Integer, GameObject> obj : infoPro.getGameObjects()
				.entrySet()) {
			GameObject o = obj.getValue();
			if (o.isUnit()
					&& o.isVisibleFor(p)
					&& infoPro.getGameMode().getRelation(o, p) == Relation.ALLIED
					&& Geometry.shapeCollides(o.getShape(), r)) {
				ids.add(obj.getKey());
			}
		}
		client.getData().setSelectedUnits(ids);
	}

	@Override
	public void selectOrDeselectAt(Vector2f point) {
		PlayerMainFigure p;
		try {
			p = infoPro.getPlayer().getPlayerMainFigure();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE,
					"No playermainfigure found after a unit was (de)selected.",
					e);
			return;
		}
		for (Entry<Integer, GameObject> obj : infoPro.getGameObjects()
				.entrySet()) {
			GameObject o = obj.getValue();
			if (o.isUnit() && o.isVisibleFor(p)
					&& o.getShape().contains(point.x, point.y)) {
				client.getData().setSelectedUnit(obj.getKey());
				return;
			}
		}
		client.getData().clearSelectedUnits();
	}

	@Override
	public void sendSelectedUnits(Vector2f target) {
		LinkedList<Integer> units = new LinkedList<Integer>(client.getData()
				.getSelectedUnits());
		if (units.isEmpty())
			return;
		SendUnitsEvent sendEvent = new SendUnitsEvent(client.getOwnerID(),
				target, units);
		try {
			client.sendEvent(sendEvent);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending sendunits event", e);
		}
	}

	@Override
	public void onViewingDirectionChanged(Vector2f viewingPoint) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer().getPlayerMainFigure();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Cannot find player main figure :(", e);
			return;
		}

		// fix for rare situation if user moves mouse when player is not there
		// yet.
		if (player == null)
			return;
		Vector2f center = new Vector2f(player.getShape().getCenterX(), player
				.getShape().getCenterY()).sub(player.getPositionVector());
		Vector2df vPoint = new Vector2df(viewingPoint.sub(
				player.getPositionVector()).sub(center));
		try {
			float angle = vPoint.getAngleToXAxis();
			player.setRotation(angle);
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	public void onPlayerRezz(Player player, Base base)
			throws InsufficientResourceException, CantSpawnHereException {

		checkSufficientResources(player,
				S.Server.gm_edura_action_respawnplayer_cost);
		if (!infoPro.fitsObjectInBase(ObjectType.PLAYER, base)) {
			throw new CantSpawnHereException(ObjectType.PLAYER);
		}

		ResurrectPlayerEvent event = new ResurrectPlayerEvent(
				client.getOwnerID(), player.getPlayerId(), base.getId());
		if (player.getPlayerMainFigure() != null
				&& player.getPlayerMainFigure().isDead() == false) {
			client.getLogic().showNotification("Player is not dead");
			return;
		}
		try {
			client.sendEvent(event);
			client.getLogic().showNotification(
					"Resurrecting " + player.getName() + "...");
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending resurrection event", e);
		}
	}

	private void checkSufficientResources(Player player, int neededAmount)
			throws InsufficientResourceException {
		Team playersTeam = null;
		try {
			playersTeam = player.getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.SEVERE, "At this point the player should have a team!",
					e);
		}
		if (playersTeam.getResource() < neededAmount) {
			throw new InsufficientResourceException(neededAmount);
		}
	}

	@Override
	public void onUnitSpell(Unit targetUnit) throws ActionFailedException {

		GameEventNumber spellNumber = infoPro.getClientData()
				.getCurrentSpellSelected();

		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer().getPlayerMainFigure();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found while healing unit.", e);
			return;
		}

		int requiredResources;
		String spellNotification = "";
		switch (spellNumber) {
		case HEAL_ACTION:
			requiredResources = S.Server.spell_heal_costs;
			spellNotification = "Healing unit...";
			break;
		case SPEED_SPELL:
			requiredResources = S.Server.spell_speed_costs;
			spellNotification = "Speeding up unit...";
			break;
		case INVISIBILITY_SPELL:
			requiredResources = S.Server.spell_invisibility_costs;
			spellNotification = "Making unit invisible...";
			break;
		case BLINK_SPELL:
			requiredResources = S.Server.spell_blink_costs;
			spellNotification = "Giving +1 blink charge...";
			break;
		default:
			L.severe("Spell " + spellNumber + " is not supported!!");
			return;
		}
		checkSufficientResources(player.getPlayer(), requiredResources);

		UnitSpellActionEvent spellEvent = new UnitSpellActionEvent(spellNumber,
				client.getOwnerID(), targetUnit.getId());

		if (infoPro.getGameMode().getRelation(targetUnit, player) != Relation.ALLIED) {
			client.getLogic().showNotification("Player is not friendly");
			return;
		}

		String errorMessage = checkAdditionalConditions(targetUnit, spellNumber);
		if (!errorMessage.isEmpty()) {
			throw new ActionFailedException(errorMessage);
		}

		try {
			client.sendEvent(spellEvent);
			client.getLogic().showNotification(spellNotification);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending heal event", e);
		}

	}

	private String checkAdditionalConditions(Unit targetUnit,
			GameEventNumber spell) {
		switch (spell) {
		case HEAL_ACTION:
			if (targetUnit.getHealth() == targetUnit.getMaxHealth()) {
				return "Unit has full health!";
			} else {
				return "";
			}
		case SPEED_SPELL:
			if (targetUnit.getSpeed() == targetUnit.getMaxSpeed()) {
				return "Unit is at maximum speed!";
			} else {
				return "";
			}
		case INVISIBILITY_SPELL:
			if (targetUnit.getVisibility().equals(Visibility.OWNER_TEAM)) {
				return "Unit is invisibile already!";
			} else {
				return "";
			}
		case BLINK_SPELL:
			if (!(targetUnit instanceof PlayerMainFigure)) {
				return "Target is not a player!";
			} else {
				return "";
			}
		default:
			return "";
		}
	}

	@Override
	public void setClickState(ClickState newState) {
		client.getLogic().setClickState(newState);
		switch (newState) {
		case SELECT_TARGET_FOR_HEAL:
			client.getLogic().showTip("Select a unit that should be healed.");
			break;
		case SELECT_BASE_FOR_REZZ:
			client.getLogic().showTip(
					"Select a base where your teammate should be rezzed.");
			break;
		case SELECT_POSITION_FOR_ITEMSPAWN:
			client.getLogic().showTip(
					"Select a position in the world to spawn this item.");
			break;
		case SELECT_POSITION_FOR_OBSERVERUNIT:
			client.getLogic().showTip(
					"Select a base where observer should be spawned.");
			break;
		case SELECT_POSITION_FOR_SCOUT:
			client.getLogic().showTip(
					"Select a position in the world to make vision.");
			break;
		default:
			break;
		}
	}

	@Override
	public void onUnitSpawned(ObjectType type, Base base)
			throws InsufficientResourceException, CantSpawnHereException {

		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found spawning observer.", e);
			return;
		}

		checkSufficientResources(player, type.getCosts());
		if (!infoPro.fitsObjectInBase(type, base)) {
			throw new CantSpawnHereException(type);
		}

		CreateUnitEvent event = new CreateUnitEvent(player.getPlayerId(), type,
				base.getId());
		try {
			client.sendEvent(event);
			client.getLogic().showNotification("Spawning observer...");
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending spawn observer event", e);
		}
	}

	@Override
	public void onSpawnScout(Vector2f target)
			throws InsufficientResourceException {
		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found spawning observer.", e);
			return;
		}

		checkSufficientResources(player, S.Server.spell_scout_costs);

		ScoutSpellEvent event = new ScoutSpellEvent(player.getPlayerId(),
				target);
		try {
			client.sendEvent(event);
			client.getLogic().showNotification("Vision spell...");
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error casting vision spell event", e);
		}

	}

	@Override
	public void onSpawnItem(ObjectType type, Vector2f locationToSpawnAt)
			throws WrongObjectTypeException, InsufficientResourceException {
		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found spawning observer.", e);
			return;
		}

		if (!type.isItem()) {
			throw new WrongObjectTypeException(type);
		}

		checkSufficientResources(player, type.getCosts());

		// TODO: only allow to spawn where you have vision...

		SpawnItemEvent spawnItemEvent = new SpawnItemEvent(
				player.getPlayerId(), type, locationToSpawnAt);

		try {
			client.sendEvent(spawnItemEvent);
			client.getLogic().showNotification("Spawning " + type.toString());
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error spawning item", e);
		}
	}

	@Override
	public void onBlink(Vector2f blinkTarget)
			throws InsufficientChargesException, CantSpawnHereException {
		Player myPlayer;
		try {
			myPlayer = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find player!", e);
			return;
		}
		if (!myPlayer.isDead() && myPlayer.getBlinksAvailable() > 0
				&& myPlayer.getBlinkCooldown() <= 0) {
			try {
				if (!infoPro.canBlinkTo(myPlayer.getPlayerMainFigure(),
						blinkTarget)) {
					throw new CantSpawnHereException(ObjectType.PLAYER);
				}
				myPlayer.useBlink();
				client.sendEvent(new BlinkEvent(myPlayer.getPlayerId(),
						blinkTarget));
			} catch (WrongEventTypeException | MessageNotSupportedException e) {
				L.log(Level.WARNING, "Error sending the blink event.", e);
				return;
			}
		}
	}

	@Override
	public void teamSelected(Team team) {
		try {
			client.sendEvent(new PlayerAndTeamEvent(GameEventNumber.JOIN_TEAM,
					infoPro.getOwnerID(), team.getTeamId()));
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Cannot select team!", e);
			return;
		}
	}
}

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
import de.illonis.eduras.events.CreateUnitEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.HealActionEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.ResurrectPlayerEvent;
import de.illonis.eduras.events.ScoutSpellEvent;
import de.illonis.eduras.events.SendUnitsEvent;
import de.illonis.eduras.events.SpawnItemEvent;
import de.illonis.eduras.events.SwitchInteractModeEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.InsufficientResourceException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.gameobjects.NeutralBase;
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
public class GuiInternalEventListener implements LoginPanelReactor,
		ProgressPanelReactor, GamePanelReactor, LoadingPanelReactor {

	private final static Logger L = EduLog
			.getLoggerFor(GuiInternalEventListener.class.getName());
	private final GameClient client;
	private ConnectionEstablisher establisher;
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
	public void abort() {
		if (null != establisher && establisher.isAlive())
			establisher.interrupt();

		client.getFrame().hideProgress();
		client.getFrame().showLogin();
	}

	@Override
	public void login(LoginData data) {
		client.setClientName(data.getUsername());
		client.setRole(data.getRole());
		establisher = new ConnectionEstablisher(data,
				client.getNetworkManager());
		establisher.start();
		client.getFrame().hideLogin();
		client.getFrame().showProgress(establisher);
	}

	@Override
	public void onItemUse(int slotId, Vector2f target) {
		ItemEvent event = new ItemEvent(GameEventNumber.ITEM_USE,
				client.getOwnerID(), slotId);
		event.setTarget(target);
		try {
			client.sendEvent(event);
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.WARNING, "error sending item used event for slot "
					+ slotId, e);
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
	public void onLoadingFinished() {
		client.getFrame().onDataReady();
	}

	@Override
	public void onUnitsSelected(Rectangle2D.Double area) {

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
		Vector2f center = new Vector2f(player.getShape().getCenter())
				.sub(player.getPositionVector());
		Vector2df vPoint = new Vector2df(viewingPoint.sub(
				player.getPositionVector()).sub(center));
		float angle = vPoint.getAngleToXAxis();
		player.setRotation(angle);
	}

	@Override
	public void onPlayerRezz(Player player, NeutralBase base) {
		try {
			hasSufficientResources(player,
					S.Server.gm_edura_action_respawnplayer_cost);
		} catch (InsufficientResourceException e1) {
			client.getFrame().getGamePanel().onActionFailed(e1);
			return;
		}

		ResurrectPlayerEvent event = new ResurrectPlayerEvent(
				client.getOwnerID(), player.getPlayerId(), base.getId());
		if (player.getPlayerMainFigure() != null
				&& player.getPlayerMainFigure().isDead() == false) {
			client.getFrame().getGamePanel()
					.showNotification("Player is not dead");
			return;
		}
		try {
			client.sendEvent(event);
			client.getFrame()
					.getGamePanel()
					.showNotification(
							"Resurrecting " + player.getName() + "...");
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending resurrection event", e);
		}
	}

	private void hasSufficientResources(Player player, int neededAmount)
			throws InsufficientResourceException {
		if (player.getTeam().getResource() < neededAmount) {
			throw new InsufficientResourceException(neededAmount);
		}
	}

	@Override
	public void onUnitHeal(Unit targetUnit) {

		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer().getPlayerMainFigure();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found while healing unit.", e);
			return;
		}

		try {
			hasSufficientResources(player.getPlayer(),
					S.Server.spell_heal_costs);
		} catch (InsufficientResourceException e1) {
			client.getFrame().getGamePanel().onActionFailed(e1);
			return;
		}

		HealActionEvent healEvent = new HealActionEvent(client.getOwnerID(),
				targetUnit.getId());

		if (infoPro.getGameMode().getRelation(targetUnit, player) != Relation.ALLIED) {
			client.getFrame().getGamePanel()
					.showNotification("Player is not friendly");
			return;
		}
		try {
			client.sendEvent(healEvent);
			client.getFrame().getGamePanel()
					.showNotification("Healing unit...");
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending heal event", e);
		}

	}

	@Override
	public void setClickState(ClickState newState) {
		client.getFrame().getGamePanel().setClickState(newState);
	}

	@Override
	public void onUnitSpawned(ObjectType type, NeutralBase base) {

		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found spawning observer.", e);
			return;
		}

		try {
			hasSufficientResources(player, type.getCosts());
		} catch (InsufficientResourceException e1) {
			client.getFrame().getGamePanel().onActionFailed(e1);
			return;
		}

		CreateUnitEvent event = new CreateUnitEvent(player.getPlayerId(), type,
				base.getId());
		try {
			client.sendEvent(event);
			client.getFrame().getGamePanel()
					.showNotification("Spawning observer...");
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error sending spawn observer event", e);
		}
	}

	@Override
	public void onSpawnScout(Vector2f target) {
		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found spawning observer.", e);
			return;
		}

		try {
			hasSufficientResources(player, S.Server.spell_scout_costs);
		} catch (InsufficientResourceException e1) {
			client.getFrame().getGamePanel().onActionFailed(e1);
			return;
		}

		ScoutSpellEvent event = new ScoutSpellEvent(player.getPlayerId(),
				target);
		try {
			client.sendEvent(event);
			client.getFrame().getGamePanel()
					.showNotification("Vision spell...");
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error casting vision spell event", e);
		}

	}

	@Override
	public void onSpawnItem(ObjectType type, Vector2f locationToSpawnAt)
			throws WrongObjectTypeException {
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

		try {
			hasSufficientResources(player, type.getCosts());
		} catch (InsufficientResourceException e1) {
			client.getFrame().getGamePanel().onActionFailed(e1);
			return;
		}

		// TODO: only allow to spawn where you have vision...

		SpawnItemEvent spawnItemEvent = new SpawnItemEvent(
				player.getPlayerId(), type, locationToSpawnAt);

		try {
			client.sendEvent(spawnItemEvent);
			client.getFrame().getGamePanel()
					.showNotification("Spawning " + type.toString());
		} catch (WrongEventTypeException | MessageNotSupportedException e) {
			L.log(Level.SEVERE, "Error spawning item", e);
		}
	}
}

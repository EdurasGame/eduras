package de.illonis.eduras.gameclient;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.SendUnitsEvent;
import de.illonis.eduras.events.SwitchInteractModeEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

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
		client.stopDiscovery();
		client.setClientName(data.getUsername());
		client.setRole(data.getRole());
		establisher = new ConnectionEstablisher(data,
				client.getNetworkManager());
		establisher.start();
		client.getFrame().hideLogin();
		client.getFrame().showProgress(establisher);
	}

	@Override
	public void onItemUse(int slotId, Vector2D target) {
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
	public void onModeSwitch() {
		try {
			InteractMode nextMode = EdurasInitializer.getInstance()
					.getInformationProvider().getPlayer().getCurrentMode()
					.next();
			client.sendEvent(new SwitchInteractModeEvent(client.getOwnerID(),
					nextMode));
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
		LinkedList<Integer> ids = new LinkedList<Integer>();
		for (Entry<Integer, GameObject> obj : infoPro.getGameObjects()
				.entrySet()) {
			GameObject o = obj.getValue();
			if (o.isUnit() && o.isVisible() && o.getShape().intersects(r)) {
				ids.add(obj.getKey());
			}
		}
		client.getData().setSelectedUnits(ids);

	}

	@Override
	public void selectOrDeselectAt(Vector2D point) {
		for (Entry<Integer, GameObject> obj : infoPro.getGameObjects()
				.entrySet()) {
			GameObject o = obj.getValue();
			Vector2f f = new Vector2f((float) point.getX(),
					(float) point.getY());
			if (o.isUnit() && o.isVisible() && o.getShape().contains(f.x, f.y)) {
				client.getData().setSelectedUnit(obj.getKey());
				return;
			}
		}
		client.getData().clearSelectedUnits();
	}

	@Override
	public void sendSelectedUnits(Vector2D target) {
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

	public void onViewingDirectionChanged(Vector2D viewingPoint) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Cannot find player main figure :(", e);
			return;
		}
		viewingPoint.subtract(player.getPositionVector());
		double angle = viewingPoint.getAngleToXAxis();
		player.setRotation(angle);
	}
}

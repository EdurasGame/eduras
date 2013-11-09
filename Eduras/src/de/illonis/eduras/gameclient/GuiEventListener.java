package de.illonis.eduras.gameclient;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.SwitchInteractModeEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.gameclient.gui.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.LoginPanelReactor;
import de.illonis.eduras.gameclient.gui.ProgressPanelReactor;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

public class GuiEventListener implements LoginPanelReactor,
		ProgressPanelReactor, GamePanelReactor, LoadingPanelReactor {

	private final static Logger L = EduLog.getLoggerFor(GuiEventListener.class
			.getName());
	private final GameClient client;
	private ConnectionEstablisher establisher;

	public GuiEventListener(GameClient client) {
		this.client = client;
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
		client.getNetworkManager().disconnect();
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
				| ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "mode switch request could not be sent.", e1);
		}
	}

	@Override
	public void onLoadingFinished() {
		client.getFrame().onDataReady();
	}
}

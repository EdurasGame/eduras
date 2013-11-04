package de.illonis.eduras.networking;

import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * This class implements {@link GameEventListener}. Basically it generates
 * events to be send to the clients.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerGameEventListener implements GameEventListener {

	private final ServerSender serverSender;

	/**
	 * Creates a new ServerGameEventListener with the given outputBuffer.
	 * 
	 * @param serverSender
	 *            The sender that is used to send messages.
	 */
	public ServerGameEventListener(ServerSender serverSender) {
		this.serverSender = serverSender;
	}

	private void sendEvent(Event event) {
		serverSender.sendEventToAll(event);
	}

	@Override
	public void onNewObjectPosition(GameObject o) {

		MovementEvent moveEvent;

		moveEvent = new MovementEvent(GameEventNumber.SET_POS_UDP, o.getId());
		moveEvent.setNewXPos(o.getXPosition());
		moveEvent.setNewYPos(o.getYPosition());

		sendEvent(moveEvent);
	}

	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos, int owner) {
		for (GameEvent event : infos) {
			serverSender.sendEventToClient(event, owner);
		}
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		sendEvent(event);
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		sendEvent(event);
	}

	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		sendEvent(event);
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		sendEvent(event);
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		sendEvent(event);
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
		sendEvent(event);
	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		sendEvent(event);
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		sendEvent(event);
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
	}

	@Override
	public void onDeath(DeathEvent deathEvent) {
		sendEvent(deathEvent);
	}

	@Override
	public void onCooldownStarted(ItemEvent event) {
		sendEvent(event);
	}

	@Override
	public void onCooldownFinished(ItemEvent event) {
		sendEvent(event);
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		sendEvent(setModeEvent);
	}

}
